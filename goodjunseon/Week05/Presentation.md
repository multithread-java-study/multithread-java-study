## Executors에서 ThreadPoolExecutor로 변경

### 개요
섹션 13, 14를 들으며 Executors와 ThreadPoolExecutor에 대해 학습한 내용을 바탕으로 실제 프로젝트에서 발생할법한 문제를 해결하는 과정을 소개합니다.

---

주니어 개발자 `박준선`은 Tave 후반기 프로젝트에서 멀티스레딩을 활용한 서버 애플리케이션을 개발하고 있다.
전반기 스터디 멀티스레드-자바를 수강한 뒤, 스레드 관리를 단순화하기 위해 Executors 프레임워크를 사용하여 개발중입니다.

초기에는 다음과 같이 ``` Executors.newFixedThreadPool() ``` 메서드를 사용했습니다.
코드가 단순하고 별다른 설정이 필요하지 않아 빠르게 개발할 수 있었습니다.

```java
ExecutorService executor = Executors.newFixedThreadPool(10);

public void handleRequest(Request request) {
    executor.submit(() -> processRequest(request));
}
```
서비스 초기에는 요청량이 적었고, 위 코드로 충분히 안정적으로 동작했습니다.   
그러나 문제는 트래픽이 급격히 증가하면서 발생합니다.

운영 중이던 서버가 갑자기 응답이 지연되고, 결국 다운되는 현상이 발생했습니다. 

Troubleshooting을 시작한 준선은 다음과 같은 문제들을 발견했습니다.
```Executors.newFixedThreadPool()```은 내부적으로 다음과 같이 동작합니다.
```java
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(
        nThreads,
        nThreads,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>() // ⚠️ 무제한 큐!
    );
}
```
즉, 고정된 스레드 수(nThreads)만큼만 실제 작업을 수행하고, 나머지 요청들은 LinkedBlockingQueue에 계속 쌓이게 됩니다.

문제는 이 큐가 무한히 쌓일 수 있다는 점입니다.  
요청량이 많아지면, 큐가 메모리를 잠식하고 결국 OutOfMemoryError를 일으키게 됩니다.

준선은 이 문제를 해결하기 위해 다음과 같은 조치를 취했다.  
ThreadPoolExecutor 직접 사용하여 큐 용량 제한과 거절 정책을 설정했다.

```java
ExecutorService executor = new ThreadPoolExecutor(
    10,                     // corePoolSize
    20,                     // maximumPoolSize
    60L, TimeUnit.SECONDS,  // idle 스레드 생존 시간
    new ArrayBlockingQueue<>(1000),  // 용량 제한
    Executors.defaultThreadFactory(),
    new ThreadPoolExecutor.CallerRunsPolicy() // 거절 정책
);
```

이제 큐가 가득 차면, 새로운 요청은 예외로 터지지 않고
현재 요청을 처리 중인 스레드가 직접 처리(CallerRunsPolicy)하도록 했다.
이렇게 하면 시스템이 한계 이상으로 과부하되지 않고, 백프레셔 효과를 낼 수 있다.

거절 정책에 대해서 조금 더 설명하자면,  
- AbortPolicy: 기본 정책으로, 큐가 가득 차면 RejectedExecutionException 발생  
- CallerRunsPolicy: 호출한 스레드가 직접 작업 실행 (백프레셔 효과)  
- DiscardPolicy: 새 작업을 조용히 무시  
- DiscardOldestPolicy: 큐에서 가장 오래된 작업을 제거하고 새 작업을 큐에 추가
- 사용자 정의 정책: RejectedExecutionHandler 인터페이스 구현 가능