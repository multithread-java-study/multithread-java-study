package thread.start.test;

import static util.MyLogger.log;

public class StartTest1Main {
	public static void main(String[] args) {

		CounterThread thread = new CounterThread();
		thread.start();
	}
}
