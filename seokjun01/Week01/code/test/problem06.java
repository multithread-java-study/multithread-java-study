package thread.control.test;

import static util.MyLogger.*;
import static util.ThreadUtils.*;

//join 활용 2
//join 사용시 , main 스레드는 해당 스레드가 종료될 때까지 기다린다.
public class JoinTest2Main {
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new MyTask(), "t1");
		Thread t2 = new Thread(new MyTask(), "t2");
		Thread t3 = new Thread(new MyTask(), "t3");

		t1.start();
		t2.start();
		t3.start();

		t1.join();
		t2.join();
		t3.join();

		System.out.println("모든 스레드 실행 완료");
	}

	static class MyTask implements Runnable {

		@Override
		public void run() {
			for (int i=1; i<=3; i++) {
				log(i);
				sleep(1000);
			}
		}
	}
}
