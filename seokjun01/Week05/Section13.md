# 📚5주차 - 섹션 13 스레드 풀과 Executor 프레임워크 1

---

## 1️⃣ 스레드를 직접 사용할 때의 문제

### 문제점
1. **스레드 생성 비용이 큼**
- 스레드를 만들면 OS 수준에서 자원이 할당되고, 약 1MB 이상의 메모리가 필요함.
2. **스레드 관리 어려움**
- 직접 만든 스레드를 일일이 종료, 재사용, 제어해야 함.
3. **Runnable의 한계**
- `run()` 메서드는 반환값이 없고 예외를 던질 수 없음.

---

## 2️⃣ 스레드풀(ExecutorService)로 해결

### 개념
- 스레드를 직접 만들지 않고, **스레드풀(Thread Pool)** 이 미리 만들어둔 스레드를 재사용.
- **ExecutorService**가 스레드를 관리하고, 내부적으로 **BlockingQueue**를 사용해 작업을 큐에 저장.

### 코드 예시
import java.util.concurrent.*;

class ExecutorExample {
public static void main(String[] args) {
ExecutorService es = Executors.newFixedThreadPool(2);

        es.execute(new Runnable() {
            public void run() {
                System.out.println("Task A 실행");
            }
        });
        es.execute(new Runnable() {
            public void run() {
                System.out.println("Task B 실행");
            }
        });

        es.shutdown();
    }
}

---

## 3️⃣ Runnable의 한계

### 문제점
- `run()`은 **void** → 결과 반환 불가
- **예외 던지기 불가**
- 결과를 얻기 위해 `join()`과 공유 변수 사용 필요

### 코드 예시
class RunnableResultProblem {
static class MyTask implements Runnable {
int result;
public void run() {
result = (int) (Math.random() * 10);
}
}

    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        Thread t = new Thread(task);
        t.start();
        t.join(); // 스레드 종료 대기
        System.out.println("결과: " + task.result);
    }
}

---

## 4️⃣ Callable + Future로 해결

### 개념
- `Callable<V>`: 값을 반환할 수 있는 작업 단위
- `Future<V>`: 비동기 작업의 **미래 결과**를 받을 수 있는 객체

### 코드 예시
import java.util.concurrent.*;

class FutureBasicExample {
public static void main(String[] args) throws Exception {
ExecutorService es = Executors.newFixedThreadPool(1);

        Future<Integer> future = es.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                System.out.println("Callable 실행 중...");
                Thread.sleep(2000);
                return 42;
            }
        });

        System.out.println("다른 작업 수행 중...");
        int result = future.get(); // 결과가 나올 때까지 대기
        System.out.println("결과: " + result);

        es.shutdown();
    }
}

---

## 5️⃣ Future 내부 작동 원리

submit() 호출 시
1. Callable 작업을 FutureTask로 감쌈
2. FutureTask를 큐에 넣음
3. FutureTask는 Runnable도 구현하고 있어서 스레드가 실행 가능
4. call()이 끝나면 결과를 Future 내부에 저장
5. get()을 호출하면 결과 반환

---

## 6️⃣ Future를 반환하는 이유

class FutureParallelExample {
static class SumTask implements Callable<Integer> {
int start, end;
SumTask(int start, int end) {
this.start = start;
this.end = end;
}
public Integer call() throws Exception {
Thread.sleep(2000);
int sum = 0;
for (int i = start; i <= end; i++) sum += i;
return sum;
}
}

    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(2);
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);

        Future<Integer> f1 = es.submit(task1);
        Future<Integer> f2 = es.submit(task2);

        int r1 = f1.get();
        int r2 = f2.get();

        System.out.println("합계: " + (r1 + r2));
        es.shutdown();
    }
}

---

## 7️⃣ 잘못된 Future 사용 예

class FutureBadExample {
public static void main(String[] args) throws Exception {
ExecutorService es = Executors.newFixedThreadPool(2);
Future<Integer> f1 = es.submit(new Callable<Integer>() {
public Integer call() throws Exception {
Thread.sleep(2000);
return 100;
}
});

        int r1 = f1.get();

        Future<Integer> f2 = es.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                Thread.sleep(2000);
                return 200;
            }
        });
        int r2 = f2.get();

        System.out.println("총합: " + (r1 + r2));
        es.shutdown();
    }
}

---

## 8️⃣ Future 주요 메서드 정리

Future<V>의 핵심 메서드:
- V get()                         → 완료될 때까지 기다리고 결과 반환
- V get(long, TimeUnit)           → 시간 제한 있음
- boolean isDone()                → 완료 여부 확인 (논블로킹)
- boolean cancel(boolean)         → 작업 취소
- boolean isCancelled()           → 취소 여부 확인

---

## 9️⃣ Future 취소 예시

class FutureCancelExample {
public static void main(String[] args) throws Exception {
ExecutorService es = Executors.newFixedThreadPool(1);

        Future<String> future = es.submit(new Callable<String>() {
            public String call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    System.out.println("작업 중: " + i);
                    Thread.sleep(1000);
                }
                return "완료";
            }
        });

        Thread.sleep(3000);
        System.out.println("작업 취소 시도");
        future.cancel(true);

        System.out.println("isCancelled: " + future.isCancelled());
        es.shutdown();
    }
}

---

## 정리

- Future는 비동기 결과를 관리하는 약속 객체
- Callable은 반환값과 예외 처리가 가능한 작업 단위
- ExecutorService는 스레드풀을 관리하는 컨트롤러
- get() 호출 시점이 중요 (병렬성 확보)
- cancel(), isDone() 등으로 제어 가능
