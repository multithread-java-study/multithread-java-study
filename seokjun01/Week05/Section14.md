# 📚5주차 - 섹션 14  - 스레드 풀과 ExecutorService

## 실무에서 사용하는 중단 방법
Shutdown , shutdownNow
awaitTermination (long timeout, TimeUnit unit) -> 서비스 종료시 모든 작업이 완료될 때까지 대기한다.

### 우아한 종료란?
-> 서비스를 안정적으로 종료하는 방식, 문제없이 종료하는 것을 의미한다.

종료 메서드에는 위에 적어놓은 Shutdown, shutdownNow, awaitTermination 등이 있다.

void shutdown()
: 새로운 작업을 받지 않고, 이미 제출된 작업도 모두 완료한 후에 종료한다. (non-blocking)

List<Runnable> shutdownNow()
: 실행 중인 작업을 중단하고, 대기 중인 작업도 반환하며 즉시 종료한다.
작업을 중단하기 위해 인터럽트를 발생시킨다.

boolean awaitTermination() throws InterruptedException
: 서비스 종료 시 모든 작업이 완료될 때까지 대기한다. 이때 지정된 시간까지만 대기한다. (blocking Method)

강의에서 제안하는 방식:
우아한 종료를 우선 선택하고, 일정 시간 내 완료되지 않으면 강제 종료를 수행하는 방식으로 접근한다.


## Executor는 스레드 풀 관리를 어떻게 할까?

기본적으로 Executor는 기본(core) 스레드 수만큼 관리하지만, 최대 스레드 수까지 확장 가능하다.
- 기본 스레드(core)가 모두 작업 중이면 → Queue에 작업을 쌓는다.
- Queue가 꽉 차면 → 최대 스레드 수(max)까지 초과 스레드를 생성한다.
- 최대 스레드 수도 모두 바쁘다면 → 새로운 작업은 거절된다.
- 초과로 생성된 스레드는 일정 시간 동안 유휴 상태가 되면 자동으로 제거된다.


## 그렇다면 core, blockingQueue, max를 어떻게 활용해야 할까?

Executor 전략의 핵심 요소
- corePoolSize
- maximumPoolSize
- keepAliveTime
- BlockingQueue

자바는 Executors 클래스를 통해 여러 스레드 풀 전략을 제공한다.
예)
newSingleThreadExecutor()
newFixedThreadPool()
newCachedThreadPool()

예시:
new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
→ 단일 스레드 전략

## 스레드 풀 전략 종류
1) 고정 스레드 전략 (FixedThreadPool)
   Executors.newFixedThreadPool(nThreads);

특징:
- 스레드 수가 고정되어 있어 CPU, 메모리 리소스 예측이 용이하다.
- 일반적인 상황에서 가장 안정적으로 서비스를 운영할 수 있다.
  단점:
- BlockingQueue의 크기가 제한이 없기 때문에, 요청이 폭주하면 큐가 무한히 쌓일 수 있다.
- 처리 속도보다 요청 속도가 빠를 경우 OutOfMemoryError 발생 위험이 있다.


2) 캐시 풀 전략 (CachedThreadPool)
   Executors.newCachedThreadPool();

특징:
- 기본(core) 스레드를 사용하지 않고, 60초 생존 주기를 가진 초과(max) 스레드만 사용한다.
- BlockingQueue를 사용하지 않는다.
- 내부적으로 SynchronousQueue를 사용하며, 생산자와 소비자가 직접 연결되는 구조 (스레드 간 직거래).
- 요청이 들어올 때마다 새로운 스레드를 즉시 만들어 처리한다.
- 초과 스레드는 일정 시간 유휴 상태가 되면 자동으로 제거된다.

장점:
- 매우 빠르고 유연하다.
- 대기 큐에 쌓이지 않고 바로 처리된다.

단점:
- 요청이 폭주하면 스레드가 무한히 생성될 수 있다.
- CPU 100%, 메모리 과다 사용으로 서버가 다운될 위험이 있다.


3) 사용자 정의(Custom) 스레드 풀 전략
   상황에 따라 일반 / 긴급 / 거절 등으로 나누어 맞춤형으로 구성할 수 있다.

예시:
ExecutorService es = new ThreadPoolExecutor(
100,                    // corePoolSize
200,                    // maximumPoolSize
60, TimeUnit.SECONDS,   // keepAliveTime
new ArrayBlockingQueue<>(1000) // BlockingQueue
);

설명:
- 기본 스레드 100개
- 최대 스레드 200개
- 생존 주기 60초
- 큐에는 1000개까지 대기 가능

→ 총 1100개(기본 + 큐)까지는 정상 대기,
그 이후(1200개 등)는 긴급 스레드가 추가로 생성되어 빠르게 처리.
초과 스레드는 일정 시간 유휴 시 제거된다.


결국, 처리할 수 없을 정도로 요청이 많을 때를 대비한 “예외 정책”이 필요하다.


Executor 예외(거절) 정책

AbortPolicy:
- 새로운 작업을 제출할 때 RejectedExecutionException 발생 (기본 정책)

DiscardPolicy:
- 새로운 작업을 조용히 버림

CallerRunsPolicy:
- 새로운 작업을 제출한 스레드가 직접 작업을 실행

Custom (RejectedExecutionHandler):
- 개발자가 직접 정의한 거절 정책을 구현 가능


정리:
- 실무에서는 우아한 종료(Graceful Shutdown)를 기본으로 시도하고,
  완료되지 않으면 shutdownNow()로 강제 종료.
- 스레드 풀 설정 시 core, max, queue 크기를 서비스 특성에 맞게 조정.
- FixedThreadPool은 안정적이지만 큐가 무제한이므로 폭주 시 위험.
- CachedThreadPool은 빠르지만, 폭주 시 스레드 폭증으로 시스템 위험.
- 필요에 따라 Custom ThreadPoolExecutor를 구성하여 유연한 전략을 세운다.
