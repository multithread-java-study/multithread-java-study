# ApiFuture

공모전을 진행하면서 Firestore라는 데이터베이스를 사용했다.

데이터베이스를 사용하면서 ApiFuture, addCallback 메서드를 사용했다.

그러던 와중 자바의 스레드풀에 대해서 공부하며 Future 인터페이스를 배우게 되었다.

공부하면서 Firestore를 쓰면서 사용했던 ApiFuture와 자바의 스레드 풀에서 Future은 무슨 관계일까 하는 생각에 코드를 분석 해보았다.

---

# ApiFuture<V>

Firestore에서 ApiFuture<V>는  비동기 작업의 결과를 나타내는 Google의 인터페이스이다.

데이터베이스 읽기 /쓰기 같은 Firesotre 작업은 네트워크를 통해 이루어지므로 시간이 걸린다.

APiFuture는 이러한 작업이 완료될 때까지 프로그램이 멈추지 않도록(non-blocking) 해주는 역할을 한다.

## Future<V>와의 관계

**`ApiFuture<V>`는 Java의 표준 `Future<V>` 인터페이스를 상속한다.**

즉, `ApiFuture<V>`는 `Future<V>`의 모든 기능을 가지면서 추가적인 편의 기능을 제공한다.

### 1. `Future<V>` (Java 표준)

```java
public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    boolean isDone();

    V get() throws InterruptedException, ExecutionException;

    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}

```

- 비동기 작업의 결과를 나타내는 표준 인터페이스이다.
- 주요 메서드는 `get()`이다.
    - `get()` 메서드는 결과가 준비될 때까지 현재 스레드를 블로킹 시킨다.
    - 작업이 끝날 때까지 프로그램이 그 자리에서 멈춰 기다려야 해서 효율이 떨어질 수 있다.

### 2. `ApiFuture<V>` (Google의 확장)

```java
public interface ApiFuture<V> extends Future<V> {
  void addListener(Runnable listener, Executor executor);
}

```

- `Future<V>`를 상속하므로, `get()` 메서드도 가지고 있다.
- **핵심 기능 (차이점):** `addListener(Runnable listener, Executor executor)` 메서드를 제공한다.
- 이 `addListener`를 사용하면, 스레드를 차단하고 결과를 기다리는 대신 **"작업이 끝나면 이 코드를 실행해줘"**라는 **콜백(callback)**을 등록할 수 있다.
- 작업이 완료되면(성공하든 실패하든), `ApiFuture`가 지정된 `Executor`(스레드)를 사용해 `listener`(콜백 코드)를 자동으로 실행시켜 준다.

# 실제 사용 코드

```java
    //채팅방 생성
    public ConversationRoom createRoom(String userId) {

        // 네트워크 통신을 하지 않는다 (DB에 요청x)
        //Firestore 클라이언트 라이브러리(SDK)가 자체적으로 고유한 20자리 랜덤 ID를 생성한다.
        // 이 ID를 가진 빈 껍데기 주소, 즉 DocumentReference 객체를 만든다.
        DocumentReference docRef = db.collection("ROOMS").document();

        ConversationRoom room = new ConversationRoom();
        room.setTitle("새로운 대화");
        room.setUserId(userId);
        //@ServerTimestamp를 통해서 Firestore가 문서를 쓸 때 자동으로 현재 서버 시간을 해당 필드에 기록
        room.setLastMessageAt(null);
        room.setId(docRef.getId()); //확보된 ID를 객체에 설정

        try {
            // Firestore에 데이터를 쓰는 것은 네트워크를 통해 다른 서버에 요청하는 것
            // ApiFuture은 비동기적으로 작업 요청을 한 후 나중에 완료되면 결과를 담음
            // 실제 DB쓰기는 백그라운드 스레드에서 시작
            ApiFuture<WriteResult> future = docRef.set(room);

            // 쓰기가 완료될 때까지 여기서 대기(Blocking)
            future.get(); //이 시점에 InterruptedException 또는 ExecutionException

            // DB쓰기 성공이 확정된 후에 객체를 반환
            return room;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 복원
            throw new RuntimeException("채팅방 생성 실패(인터럽트)", e);
        } catch (ExecutionException e) {
            // .get() 실행 중 Firestore 쓰기 실패 (권한, 네트워크 등)
            throw new RuntimeException("채팅방 생성 실패", e);
        }

    }

```

## 1. `ApiFuture`와 비동기 작업의 시작

```java
ApiFuture<WriteResult> future = docRef.set(room);
```

1. **메인 스레드 (A):**
    - `docRef.set(room)` 메서드를 **호출**
    - Firestore SDK는 이 요청(room 객체 저장)을 즉시 내부 작업 큐에 등록한다.
    - 그리고 실제 네트워크 작업을 수행할 **다른 스레드**에게 위임한다.
    - 위임 직후, `ApiFuture`라는 **'작업 영수증(Promise)'** 객체를 *즉시* 반환한다.
    - **이 시점에서 '메인 스레드'는 멈추지 않고** 바로 다음 줄(`future.get()`)로 이동한다.
2. **Firestore I/O 스레드 (B):**
    - Firestore SDK가 내부적으로 관리하는 **백그라운드 스레드 풀**의 스레드이다.
    - (A) 스레드로부터 위임받은 'room 객체 저장' 작업을 실제로 수행한다.
    - 데이터를 직렬화하고, 네트워크를 통해 Firestore 서버와 통신을 시작한다.
    - 이 작업은 (A) 스레드의 작업과 **동시에(비동기)** 일어난다.

---

## 2. `future.get()` - 스레드의 강제 대기 (Blocking)

```java
future.get(); //이 시점에 InterruptedException 또는 ExecutionException
```

- **메인 스레드 (A):** `future.get()`을 만나는 순간, **실행을 멈추고 '대기(Waiting/Blocked)' 상태가 된다.**
- (A) 스레드는 `future`가 '완료' 상태가 될 때까지 아무것도 하지 않고 기다린다.
- **Firestore I/O 스레드 (B):** 한편, (B) 스레드는 여전히 네트워크 통신을 하고 있다.
    - **성공 시:** (B) 스레드가 서버로부터 "저장 성공!" 응답을 받으면, `future` 객체에 `WriteResult`를 넣어주고 '완료' 상태로 만든다.
    - **실패 시:** (B) 스레드가 서버로부터 에러(예: 권한 없음)를 받으면, `future` 객체에 `Exception` 정보를 넣어주고 '완료(실패)' 상태로 만든다.
- `future`의 상태가 '완료'가 되는 순간, 대기 중이던 **메인 스레드 (A)**가 깨어난다(Unblocked).

이 `get()` 호출 때문에, 비동기 작업이 끝날 때까지 동기 방식처럼 기다리게 된다.

---

## 3. 예외 처리와 스레드

### 1) `ExecutionException` (작업 자체의 실패)

- **발생 주체:** **Firestore I/O 스레드 (B)**
- **상황:** (B) 스레드가 Firestore 서버와 통신하다가 실패했다. (네트워크 오류, 서버 다운, 쓰기 권한 없음 등)
- **동작:** (B) 스레드가 이 실패 정보를 `future` 객체에 등록한다.
- **결과:** 대기 중이던 **메인 스레드 (A)**가 깨어나고, `future.get()`은 `ExecutionException`을 **(A) 스레드에게 던진다.**
- `e.getCause()`를 호출하면 (B) 스레드가 겪었던 실제 원인(예: `FirestoreException`)을 알 수 있다.

### 2) `InterruptedException` (작업 대기 중 방해)

- **발생 주체:** **제 3의 스레드 (C)** (메인 스레드도, I/O 스레드도 아닌)
- **상황:** **메인 스레드 (A)**가 `future.get()`에서 대기하고 있다. 이때 (C) 스레드가 (A) 스레드를 깨우기 위해 `(A스레드).interrupt()` 신호를 보낸다. (예: 웹 서버가 종료될 때 요청 처리 스레드를 강제 종료시킬 경우)
- **동작:** (A) 스레드는 작업을 기다리던 것을 *중단*하고 즉시 깨어난다.
- **결과:** `future.get()`은 `InterruptedException`을 **(A) 스레드에게 던진다.**
- **중요 포인트 1:** 이건 **Firestore 작업 실패와 무관**할 수 있다. 단지 **A의 ‘대기’가 끊겼다**는 뜻이다. 백그라운드 작업(B)은 **여전히 돌고 있을 수 있다.**
- **중요 포인트 2:** `InterruptedException`이 던져지면 A의 **인터럽트 플래그는 클리어된**다. 관례적으로 곧바로 `Thread.currentThread().interrupt()`로 **재설정**하고 상위로 전달하거나 취소 로직을 태워야 한다.

---

# `ApiFutures`

- 유틸리티 클래스
- 이 클래스 안에는 ApiFuture 객체를 더 쉽게 다룰 수 있게 도와주는 static 메서드들이 들어 있다.
- 핵심 기능: `ApiFutures.addCallback(future, callback, executor)`
    - ApiFuture 객체를 받아서 “이 작업이 성공하면 이 코드를 실행하고, 실패하면 저 코드를 실행해 줘”라는 콜백을 등록할 수 있다.

## `ApiFutures.addCallback(future, callback, executor)`

```java
  public static <V> void addCallback(
      final ApiFuture<V> future, final ApiFutureCallback<? super V> callback, Executor executor) {
    Futures.addCallback(
        listenableFutureForApiFuture(future),
        new FutureCallback<V>() {
          @Override
          public void onFailure(Throwable t) {
            callback.onFailure(t);
          }

          @Override
          public void onSuccess(V v) {
            callback.onSuccess(v);
          }
        },
        executor);
  }
```

### **1. `listenableFutureForApiFuture(future)`**

- `ApiFuture`는 `ListenableFuture`를 상속(extends)한다.
- 이 메서드는 `ApiFuture`를 Guava의 핵심 로직(`Futures.addCallback`)이 알아들을 수 있는 `ListenableFuture` 타입으로 안전하게 변환(캐스팅)한다.

### **2. `new FutureCallback<V>() { ... }`**

- `Futures.addCallback`이 요구하는 콜백 타입은 `FutureCallback` 인터페이스이다.
- 근데 우리가 파라미터로 받은 건 `ApiFutureCallback` 인터페이스이다.
- 그래서 `FutureCallback` 익명 클래스를 즉석에서 만들어서, 그 내부에서 우리가 받은 `ApiFutureCallback`(변수명 `callback`)을 **그대로 호출**해주는 것이다.
- `onFailure`가 오면 `callback.onFailure`를, `onSuccess`가 오면 `callback.onSuccess`를 부르는 **'전달자'** 역할을 한다.

### 3. `Executor executor`

`Executor`는 콜백(onSuccess/onFailure)을 어떤 스레드에서 실행할지를 결정하는 규칙이다.

1. `MoreExecutors.directExecutor()`
- 동작: 즉시 실행
- 별도의 스레드를 사용하지 않는다. `ApiFuture`의 비동기 작업(예: Firestore I/O)을 완료시킨 바로 그 스레드가, 콜백을 즉시 직접 실행한다.
- 언제 쓰는가?
    - 스레드를 갈아타는 비용이 없어서 성능이 가장 좋다.
    - 콜백 로직이 아주 가볍고, 절대 블로킹 되지 않을 때 쓴다.
1. 스레드 풀 (예: **`Executors.newFixedThreadPool(nThreads)`, `Executors.newCachedThreadPool()` )**
- 동작: 작업 위임
- `ApiFuture` 가 완료되면, 콜백 로직(Runnable)을 이 스레드 풀에 작업으로 제출(submit)한다.
    
    그럼 풀에 대기 중인 워커 스레드 중 하나가 그 콜백을 실행한다.
    
- 언제 쓰는가?
    - 콜백안에서 시간이 걸리는 작업을 해야 할 때 쓴다.
    - 예를 들어, DB에서 읽은 결과로 다시 파일을 쓰거나, 다른 네트워크 요청을 보내는 등 **무겁거나 블로킹되는 작업**을 할 때 쓴다.
    - 이렇게 하면, `ApiFuture`를 완료시켰던 I/O 스레드는 무거운 콜백 처리를 워커 스레드에게 넘기고, 자기는 즉시 다른 I/O 작업을 하러 갈 수 있어서 효율적이다.

---

## 실제 사용 코드

```java

public Mono<ConversationMessage> createMessage(String question, String answer, String roomId) {
    DocumentReference ref = db.collection(ROOMS).document(String.valueOf(roomId)).collection(MESSAGES).document();
    String messageId = ref.getId();
    ConversationMessage message = ConversationMessage.builder()
            .question(question)
            .answer(answer)
            .id(messageId)
            .roomId(roomId)
            .build();

    Mono<WriteResult> setMono = Mono.create(sink -> {
        ApiFutures.addCallback(ref.set(message), new ApiFutureCallback<WriteResult>() {

            @Override
            public void onFailure(Throwable t) {
                sink.error(t);
            }

            @Override
            public void onSuccess(WriteResult wr) {
                sink.success(wr);
            }
        }, MoreExecutors.directExecutor()); 
    });

    Mono<DocumentSnapshot> getMono = Mono.create(sink -> {
        ApiFutures.addCallback(ref.get(), new ApiFutureCallback<DocumentSnapshot>() {

            @Override
            public void onFailure(Throwable t) {
                sink.error(t);
            }

            @Override
            public void onSuccess(DocumentSnapshot snap) {
                sink.success(snap);
            }

        }, MoreExecutors.directExecutor());
    });

    return setMono 
            .then(getMono) 
            .map(snap -> { 
                if (!snap.exists()) return message;

                ConversationMessage cm = snap.toObject(ConversationMessage.class);

                if (cm == null) return message;

                cm.setId(snap.getId());
                cm.setRoomId(roomId); 

                return cm; 
            })
            .timeout(java.time.Duration.ofSeconds(5)) 
            .retry(2)
            .onErrorResume(e ->
            {
                return Mono.just(message);
            }); 
}     
```
