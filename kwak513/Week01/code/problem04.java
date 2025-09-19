package thread.start.test;

import static util.MyLogger.log;


public class StartTest4Main {
	
	public static void main(String[] args) {
		InfiniteRunnable1 runnable1 =  new InfiniteRunnable1();
		Thread thread1 = new Thread(runnable1);
		thread1.setName("Thread-A");
		thread1.start();
		
		InfiniteRunnable2 runnable2 =  new InfiniteRunnable2();
		Thread thread2 = new Thread(runnable2);
		thread2.setName("Thread-B");
		thread2.start();
	}

	
	static class InfiniteRunnable1 implements Runnable{
		@Override
		public void run() {
			
			while(true) {
				log("A");
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static class InfiniteRunnable2 implements Runnable{
		@Override
		public void run() {
			
			while(true) {
				log("B");
				try {
					Thread.sleep(500);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	

}
