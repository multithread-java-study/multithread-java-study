## 스레드 제어와 생명 주기 1

### 📝 Summary

Thread 클래스의 주요 메서드와 스레드 생명 주기에 대해 알아본다.

Thread 클래스의 주요 메서드는 다음과 같다.

```java

public class ThreadInfoMain {
	public static void main(String[] args) {
		Thread mainThread = Thread.currentThread(); // 현재 실행 중인 스레드를 반환
		log("mainThread.threadId" + mainThread.threadId()); // 스레드 ID 반환
		log("mainThread.getName(): " + mainThread.getName()); // 스레드 이름 반환
		log("mainThread.getPriority(): " + mainThread.getPriority()); // 스레드 우선순위 반환 기본 값 5
		log("mainThread.getThreadGroup(): " + mainThread.getThreadGroup().getName()); // 스레드 그룹 이름 반환
		log("mainThread.getState(): " + mainThread.getState()); // 스레드 상태 반환
	}
}

```

출력 결과

```
09:55:58.709 [ main] mainThread = Thread[#1,main,5,main]
09:55:58.713 [ main] mainThread.threadId() = 1
09:55:58.713 [ main] mainThread.getName() = main
09:55:58.716 [ main] mainThread.getPriority() = 5
09:55:58.716 [ main] mainThread.getThreadGroup() =
java.lang.ThreadGroup[name=main,maxpri=10]
09:55:58.716 [ main] mainThread.getState() = RUNNABLE
```

스레드를 생성할 때는 Runnable의 구현체와 스레드의 이름을 매개변수로 전달할 수 있다.

```java
Thread myThread = new Thread(new MyRunnable(), "myThread");
```

이름을 생략한다면, thread-0, thread-1, ... 과 같이 자동으로 이름이 부여된다.
```java
Thread myThread = new Thread(new MyRunnable());
```


### 스레드 그룹

```thread.getThreadGroup()`` 메서드를 통해 스레드가 속한 스레드 그룹을 반환할 수 있다.  
>  기본적으로 모든 스레드는 부모 스레드와 동일한 스레드 그룹에 속하게 된다.

부모 스레드는 새로운 스레드를 생성하는 스레드를 의미한다. 스레드는 기본적으로 다른 스레드에 의해 생성된다. 예를들어 myThread 스레드는 main 스레드에 의해 생성된다.
```java
Thread myThread = new Thread(new MyRunnable());
```
따라서, myThread 스레드는 main 스레드와 동일한 스레드 그룹에 속하게 된다.   
스레드 그룹은 스레드를 그룹화하여 관리할 수 있는 기능을 제공한다.    
**스레드 그룹을 통해 스레드를 일괄적으로 제어할 수 있다.**  
예를들어, 스레드 그룹에 속한 모든 스레드를 일괄적으로 중지시킬 수 있다.
```java
ThreadGroup threadGroup = myThread.getThreadGroup();
threadGroup.interrupt(); // 스레드 그룹에 속한 모든 스레드를 중지
```

### 스레드 상태

`NEW`: 스레드가 생성되고 아직 시작되지 않은 상태  
`RUNNABLE`: 스레드가 실행 중이거나 실행 준비가 된 상태  
`BLOCKED`: 스레드가 동기화 블록에 진입하려고 대기  
`WAITING`: 스레드가 다른 스레드의 작업이 완료되기를 **무기한** 대기  
`TIMED_WAITING`: 스레드가 지정된 시간 동안 다른 스레드의 작업이 완료되기를 대기  
`TERMINATED`: 스레드가 종료된 상태


### join() 메서드

join() 메서드는 특정 스레드가 종료될 때까지 현재 스레드의 실행을 일시 중지시킨다.  
join() 메서드는 InterruptedException을 발생시킬 수 있으므로, 반드시 예외 처리를 해야 한다.  
join(1000) 과 같이 매개변수로 시간을 지정할 수도 있다.  

join()가 필요한 경우는 다음과 같다.

1~100까지 더하는 작업을 두개의 스레드가 각각 1~50, 51~100을 더하는 작업을 한다고 하자.
1~50을  더하는 스레드 A와 51~100을 더하는 스레드 B가 있다.
main 스레드는 두 스레드 A와 B에서 더하는 작업을 한 값을 출력한다고 하자.
만약 main 스레드가 스레드 A와 B가 작업을 완료하기 전에 값을 출력한다면, 올바른 결과를 얻을 수 없다.
따라서 main 스레드는 스레드 A와 B가 작업을 완료할 때까지 기다려야 한다.
이때 join() 메서드를 사용한다.

