package thread.start.test;
import static util.MyLogger.log;

public class StartTestMain {
	
	public static void main(String[] args) {
		CounterThread counterThread = new CounterThread();
		counterThread.start();
		
	}

	
	static class CounterThread extends Thread{
		
		@Override
		public void run() {
			log(1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log(2);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log(3);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log(4);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log(5);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}