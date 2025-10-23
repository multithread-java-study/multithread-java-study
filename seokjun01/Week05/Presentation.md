# 5주차 발표자료

## 주제 : Future 객체 ..  더 편리하게 쓸 수 없을까 ?
*아니나 다를까 비동기 실행하는 CompletableFuture가 존재한다 !*

## 목차
1.Future란 무엇인가

2.Future의 한계

3.CompletableFuture의 등장

4.CompletableFuture의 핵심 특징

5.CompletableFuture의 문법



### 1.Future란 무엇인가

Future는 Java 5에서 도입된 **비동기 작업의 결과를 나중에 받을 수 있도록 하는 객체**이다.  
즉, 스레드가 실행하는 작업의 결과를 미래 시점에 받을 수 있게 하는 “약속(promise)” 역할을 한다.

```java
ExecutorService executor = Executors.newFixedThreadPool(1);
Future<Integer> future = executor.submit(() -> {
    Thread.sleep(2000);
    return 10;
});
int result = future.get(); // 결과가 나올 때까지 대기 (blocking)
System.out.println("결과: " + result);
```

Future는 비동기 실행을 가능하게 하지만, 여전히 여러 제약을 가진다.
대표적으로 결과를 기다려야 하며, 외부에서 제어할 수 없고, 여러 비동기 작업을 조합하기 어렵다.


### 2.Future의 한계
(1) 외부에서 직접 완료시킬 수 없다
Future는 Executor 내부에서 실행되어야만 완료된다. 
  즉, 개발자가 “이 작업은 끝났고 결과는 이거야.”라고 지정할 수 없다.
비유하자면, 누군가에게 “2시간 뒤에 결과 줘”라고 부탁했는데 그 사람이 결과를 줄 때까지 나는 아무것도 할 수 없는 상황과 같다.
  Future는 결과를 ‘받는 쪽’이지, 결과를 ‘만드는 쪽’은 될 수 없다.

(2) 결과를 얻기 위해 기다려야 한다 (Blocking)
get() 메서드는 결과가 나올 때까지 현재 스레드를 멈춘다. 즉, 비동기라고 해도 결과를 꺼내려면 결국 동기적으로 기다려야 한다.

(3) 여러 Future를 조합할 수 없다
Future는 하나의 비동기 작업만 처리할 수 있다.
  예를 들어, “회원 정보를 가져온 뒤 그 정보를 기반으로 알림을 발송하는” 연속적인 비동기 작업을 구성할 수 없다.

즉, 하나의 작업이 끝나야 다음 작업을 시작할 수 있다.
비동기의 장점이 사라지는 구조이다.

(4) 예외 처리도 번거롭다  
Future에서 예외가 발생하면 `get()`을 호출할 때 `ExecutionException`이 던져진다.  
즉, 비동기 코드 내부의 예외를 다시 try-catch로 꺼내서 처리해야 한다.  
이로 인해 코드가 복잡해지고, 비동기 작업의 흐름이 끊긴다.

### 3.CompletableFuture의 등장

Java 8에서는 위의 문제를 해결하기 위해  
**CompletableFuture** 클래스가 도입되었다.  

CompletableFuture는 이름 그대로,  
“개발자가 직접 완료(complete)시킬 수 있는 Future”이다.  

또한 **CompletionStage** 인터페이스를 구현하여  
작업이 완료된 후 자동으로 실행할 콜백(callback)을 등록하거나,  
다른 비동기 작업과 조합(Composition)할 수 있게 되었다.

개발자가 직접완료 , 콜백구조, 타 비동기 작업과의 조합 ….. 뭐가 좋은걸까 ? 

차근차근 알아가본다

### 4.CompletableFuture의 핵심 특징
(1) 외부에서 작업을 직접 완료
기존 Future는 결과를 기다리는 입장이었지만,  
CompletableFuture는 직접 결과를 넣어줄 수 있다.

에? 감이안와요   예를 들어
서버 요청이 너무 느릴 때 “응답이 안 오면 그냥 기본값으로 처리해라” 같은 코드가 가능하다

(2) 결과를 기다리지 않고 콜백으로 처리 가능 (Non-blocking)
CompletableFuture는 결과가 도착했을 때 자동으로 실행할 후속 작업(콜백)을 등록할 수 있다.

CompletableFuture.supplyAsync(() -> 10)
    .thenApply(result -> result * 2)
    .thenAccept(finalResult -> System.out.println("결과: " + finalResult));

get()으로 기다릴 필요가 없고,
결과가 준비되는 즉시 다음 단계(thenApply, thenAccept)가 실행된다.

(3) 여러 비동기 작업의 조합이 가능
CompletableFuture는 여러 작업을 동시에 실행하고,  
결과를 합치거나 순차적으로 연결할 수 있다.

CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 20);

CompletableFuture<Integer> result =
        f1.thenCombine(f2, (a, b) -> a + b);

System.out.println(result.join()); 


(4)예외 처리가 간단하다
Future는
결과를 기다렸다가(get) 그때서야 에러를 알 수 있는 구조
즉, 나중에 확인하는 방식.
CompletableFuture는
에러가 나면 바로 콜백(exceptionally)으로 연결되어 자동 처리되는 구조
즉, 즉시 반응하는 방식.

```
ExecutorService executor = Executors.newSingleThreadExecutor();

Future<Integer> future = executor.submit(new Callable<Integer>() {
@Override
public Integer call() throws Exception {
System.out.println("작업 시작");
throw new RuntimeException("에러 발생!");
}
});

try {
// get()을 해야 예외를 알 수 있다
Integer result = future.get();
System.out.println("결과: " + result);
} catch (Exception e) {
System.out.println(" 예외 확인: " + e.getMessage());
}

executor.shutdown();
```
그에 비해 CompletableFuture는 
```
CompletableFuture<Integer> cf = new CompletableFuture<>();

new Thread(new Runnable() {
@Override
public void run() {
try {
System.out.println("작업 시작");
throw new RuntimeException("에러 발생!");
} catch (Exception e) {
cf.completeExceptionally(e); // 예외 전달
}
}
}).start();

cf.exceptionally(e -> { // 예외 발생 시 자동 실행
System.out.println(" 예외 처리: " + e.getMessage());
return 0; // 기본값 반환
}).thenAccept(result -> System.out.println("결과: " + result));
```
예외가 생기면 exceptionally()가 자동으로 실행됨.
get()을 기다릴 필요도 없고, try-catch를 밖에 둘 필요도 없어.
예외 흐름이 비동기 코드 안에서 자연스럽게 이어진다.

### 그래서 어떻게 쓰라고 ? 5.CompletableFuture의 문법

크게
비동기 작업 실행 ,작업 콜백 ,작업 조합 ,예외 처리로 구분할 수 있다.

1. 비동기 작업 실행은 반환 값이 있냐 없냐로 또 구분짓는다 . (ExecutorService.submit()을 더 간결하고 선언적으로 바꾼 형태)
내부적으로 ForkJoinPool.commonPool() 을 자동 사용하기 때문!!!
runAsync, supplyAsync로 구분한다. 
runAsync : 반환값이 없는 경우, 비동기로 작업 실행 콜
supplyAsync : 반환값이 있는 경우, 비동기로 작업 실행 콜
```
// runAsync()는 반환 값이 없다.
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
try {
System.out.println("비동기 작업 시작...");
Thread.sleep(2000);  // 2초 동안 작업 수행
System.out.println("비동기 작업 완료!");
} catch (InterruptedException e) {
e.printStackTrace();
}
});

// supplyAsync()는 반환 값이 있다.
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
try {
System.out.println("비동기 작업 시작 .. ");
Thread.sleep(2000); // 2초 동안 데이터 처리 시뮬레이션
} catch (InterruptedException e) {
e.printStackTrace();
}
return "비동기 작업 완료 !";
});

```
2. thenApply(), thenAccept(), thenRun() : 콜백 처리
CompletableFuture는 다양한 콜백 메서드를 제공하며, 이를 통해 작업이 완료된 후 추가 작업을 처리할 수 있다.
주요 메서드는 다음과 같으며 차이가 있으니 상황에 맞게 잘 사용하자.

thenApply(): 비동기 결과 값을 변환해서 다른 값으로 반환하는 메서드
thenAccept(): 비동기 결과를 반환하지 않고, 단순히 처리하는 메서드
thenRun(): 작업의 결과 값을 사용하지 않고, 그저 후속 작업만 실행하는 메서드

```
import java.util.concurrent.*;

public class FutureExample {
public static void main(String[] args) {
      CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                  Thread.sleep(2000);  // 2초 지연
                 } catch (InterruptedException e) {
                  throw new IllegalStateException(e);
                 }
               return "작업 완료!";
            });

        future.thenAccept(result -> { // 작업 결과를 받아 출력
            System.out.println("비동기 작업 결과: " + result);
        });

    // Thread.sleep(3000);  // 비동기 작업이 끝날 때까지 대기해야 결과 확인 가능
    }
}
```
3. exceptionally(), handle(), whenComplete() : 예외처리
   exceptionally()는 예외가 발생한 경우 기본 값을 반환하거나 예외 처리 로직을 수행하는 데 사용된다.
```
import java.util.concurrent.CompletableFuture;

 public class ExceptionallyExample {
 public static void main(String[] args) {
         CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
         if (true) {
         throw new RuntimeException("Something went wrong!");
         }
         return "Success!";
         }).exceptionally(ex -> {
         System.out.println("Exception occurred: " + ex.getMessage());
         return "Default Value";
         });

        // 결과 출력
        System.out.println(future.join());  // "Default Value"
    }
}
```



handle()는 정상적으로 완료되든 예외가 발생하든 상관없이 결과를 처리할 수 있다. 이 메서드는 두 개의 인자를 받으며, 하나는 결과 값이고, 다른 하나는 예외이다.
```
import java.util.concurrent.*;

 public class FutureExample {
 public static void main(String[] args) {
      CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      if (true) {
      throw new RuntimeException("Something went wrong!");
      }
      return "Success!";
      }).handle((result, ex) -> {
      if (ex != null) {
      System.out.println("Exception occurred: " + ex.getMessage());
      return "Handled Error";
      }
      return result;
      });

        // 결과 출력
        System.out.println(future.join());  // "Handled Error"
    }
}

```


이 외에도 whenComplete()는 비동기 작업이 완료된 후 결과와 예외를 처리할 수 있다. 이 메서드는 결과를 반환하지 않고, 단순히 완료 후 후속 작업을 수행하는 데 사용하고 handle()과 달리 반환 값을 변경할 수는 없지만, 결과나 예외에 대해 후처리 할 수 있다.

또한, completeExceptionally()는 특정 시점에서 의도적으로 예외를 발생시키고 싶은 경우에 사용된다.

이처럼 다양한 예외처리 메서드를 제공하기 때문에, 서비스와 의도에 맞게 선택해서 사용하는 것이 중요하다.

 

 
