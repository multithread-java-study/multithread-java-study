## 스프링의 @Async 어노테이션과 스레드풀

1. **뜻**: 스프링 프레임워크에서 비동기 처리를 위해 제공하는 어노테이션


2. **실행**:
- 이 어노테이션을 메서드에 붙이면, 스프링은 해당 메서드를 비동기적으로 실행
- 메인 스레드가 아닌, 별도의 스레드에서 메서드 실행
  - 메인 스레드가 블로킹되지 않고, 다른 작업 계속 실행 가능

3. **사용법**: 
- 설정 클래스에 @EnableAsync 어노테이션 추가
  - 스프링이 비동기 처리를 위한, 프록시 객체 생성
  - 스프링은 기본적으로, SimpleAsyncTaskExecutor 사용하지만 ThreadPoolTaskExecutor 사용하여 성능 향상 가능
    - SimpleAsyncTaskExecutor: 매 요청마다 새로운 스레드 생성
    - ThreadPoolTaskExecutor: 스레드풀 이용
```java
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
    }
}
```

4. **내부 동작 원리**:
- 스프링 AOP가 @Async 어노테이션의 비동기 처리 구현
  - @Async 어노테이션이 붙은 메소드를 호출
  - 스프링은 AOP 프록시를 통해 실제 메소드 호출을 가로챔
  - 설정된 ThreadPoolTaskExecutor를 사용하여 메소드를 비동기적으로 실행

5. ** @Async 활용 사례**
- 사용자의 요청 처리하는 동안 긴 시간 소요 작업
  - ex) 이메일 전송, 파일 업로드
```java
@Service
public class EmailService {
    @Async
    public void sendEmail(String to, String subject, String content) {
    // 이메일 전송 로직 구현
    }
}
```
- 대용량 데이터 처리나 복잡한 계산 작업
  
6. **장점**:
- 비동기 처리를 통해 작업을 병렬로 수행
- 일부 작업을 별도의 스레드에서 실행 → 메인 스레드는 사용자에게 빠른 응답
- 작업 처리 시간을 단축할 수 있음
- 시스템 전체 처리 성능 향상 가능