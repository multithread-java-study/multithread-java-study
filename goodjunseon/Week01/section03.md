## 스레드 생성과 실행

### 📝 Summary

자바의 메모리 구조는 메서드 영역, 스택 영역, 힙 영역으로 나뉜다.  

**`메서드 영역`**: 메서드 영역은 프로그램을 실행하는데 필요한 공통 데이터를 관리한다.  

**`스택 영역`**: 자바 실행 시, 하나의 실행 스택이 생성된다. 스택 영역은 각 스레드가 사용하는 메모리 공간이다. 각 스레드는 독립적인 스택 영역을 가진다.  
스택 영역은 각 스레드별로 하나의 실행 스택이 생성된다. 즉 스레드 수 만큼 스택이 생성된다.

**`힙 영역`**: 힙 영역은 자바 객체가 생성되는 메모리 공간이다. 모든 스레드는 힙 영역을 공유한다. GC가 이루어지는 영역이다.

---
### Thread
아래 코드는 Thread 클래스를 상속받아 스레드를 생성하는 예제이다.  
run() 메서드를 오버라이드하여 스레드가 실행할 코드를 작성한다.  

```java

public class HelloThread extends Thread {
	@Override
    public void run() {
        System.out.println("Hello from " + Thread.currentThread().getName() + ": run()");
    }
}
```
Thread.currentThread() 메서드는 **현재 실행 중인** 스레드를 반환한다.  
Thread.currentThread().getName() 메서드는 현재 실행 중인 **스레드의 이름을** 반환한다.

---
```java
public class HelloThreadMain {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");
        HelloThread helloThread = new HelloThread(); // HelloThread 객체 생성
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");
        helloThread.start(); // 스레드 시작 run()을 직접 호출하지 않고, start() 메서드를 호출해야 한다.
		System.out.println(Thread.currentThread().getName() + ": start() 호출 후");
        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}

```
위의 코드를 실행 시키면 다음과 같은 출력을 볼 수 있다.   
여기서 `run()` 메서드가 아니라 `start()` 메서드를 호출해야 스레드가 실행된다.
```
main: main() start
main: start() 호출 전
main: start() 호출 후
Thread-0: run()
main: main() end
```

프로그램이 실행되면 main() 메서드가 실행된다.  
main() 메서드가 실행되면 main 스레드가 생성된다.  
main 스레드는 HelloThread 객체를 생성하고, start() 메서드를 호출한다.  
start() 메서드는 새로운 스레드를 생성하고, run() 메서드를 호출한다.

만약 start() 메서드가 아닌 run() 메서드를 직접 호출하면, 새로운 스레드가 생성되지 않고, main 스레드에서 run() 메서드가 실행된다.  

### 데몬스레드란?
데몬 스레드는 백그라운드에서 실행되는 스레드이다.
데몬 스레드는 일반 스레드와 달리, 모든 일반 스레드가 종료되면 자동으로 종료된다.
데몬 스레드는 setDaemon(true) 메서드를 호출하여 설정할 수 있다.
데몬 스레드는 주로 백그라운드에서 실행되는 작업을 수행하는데 사용된다.
예를 들어, 가비지 컬렉터(GC) 스레드는 데몬 스레드이다.

```java
public class DaemonThreadMain {
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + ": main() start");
		DaemonThread daemonThread = new DaemonThread();
		daemonThread.setDaemon(true); // 데몬 스레드로 설정
		daemonThread.start();
		System.out.println(Thread.currentThread().getName() + ": main() end");
    }
	
	static class DaemonThread extends Thread {
		@Override
        public void run() {
			System.out.println(Thread.currentThread().getName() + ": run() start");
			try {
				Thread.sleep(10000); // 10초간 실행
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println(Thread.currentThread().getName() + ": run() end");
        }
    }
}

```

---

### Runnable 인터페이스

```java
public class HelloRunnable implements Runnable {
	@Override
	public void run() {
		System.out.println("Hello from " + Thread.currentThread().getName() + ": run()");
	}
}

```

```java

public class  HelloRunnableMain {
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + ": main() start");
        HelloRunnable helloRunnable = new HelloRunnable();
        Thread thread = new Thread(helloRunnable); // Runnable 객체를 Thread 객체에 전달
        thread.start(); // 스레드 시작
        System.out.println(Thread.currentThread().getName() + ": main() end");
	}
}
```

### Thread 클래스 상속 방식 VS Runnable 인터페이스 구현 방식
결론: Runnable 인터페이스 구현 방식을 권장한다.
- 자바는 단일 상속만 지원하기 때문에, Thread 클래스를 상속받으면 다른 클래스를 상속받을 수 없다.
- Runnable 인터페이스를 구현하면, 다른 클래스를 상속받을 수 있다.


### Runnable 인터페이스 구현 방법

1. 정적 중첩 클래스 (static nested class)
```java
public  class InnerRunnableMainV1 {
	public static void main(String[] args) {
        log("main() start");
		
		Runnable runnable = new MyRunnable();
		Thread thread = new Thread(runnable);
        thread.start();
		
		log("main() end");
	}
	
	static class MyRunnable implements Runnable {
        @Override
        public void run() {
            log("run()");
        }
    }
	
}
```
2.익명 클래스(anonymous class)
```java
public class InnerRunnableMainV2 {
	public static void main(String[] args) {
		log("main() start");

		Runnable runnable = new MyRunnable() {
			@Override
			public void run() {
				log("run()");
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();

		log("main() end");
	}
}
```
3. 익명 클래스 변수 없이 직접 전달

```java
public class InnerRunnableMainV3 {
    public static void main(String[] args) {
        log("main() start");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log("run()");
            }
        });
        thread.start();

        log("main() end");
    }
}
```

4. 람다 표현식 (lambda expression)

```java
public class InnerRunnableMainV4 {
	public static void main(String[] args) {
		log("main() start");

		Thread thread = new Thread(() -> log("run()"));
		thread.start();

		log("main() end");
	}
}
```

