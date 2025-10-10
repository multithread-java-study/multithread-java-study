package thread.start.test;

import static util.MyLogger.log;

public class StartTest2Main {

	public static void main(String[] args) {

		CounterRunnable runnable = new CounterRunnable();
		Thread thread = new Thread(runnable);

		thread.start();
	}
}
