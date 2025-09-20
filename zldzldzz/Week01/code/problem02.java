public class problem02 {
	public static void main(String[] args) {
		Thread thread = new Thread(new CountrtRunnable(), "counter");
		thread.start();
	}
	static class CountrtRunnable implements Runnable {
		@Override
		public void run() {
			for (int i = 1; i <= 5; i++) {
				log("value: " + i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
