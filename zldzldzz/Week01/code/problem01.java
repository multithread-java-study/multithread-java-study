

public class problem01 {
	public static void main(String[] args) {
		CountrtThread thread = new CountrtThread();
		thread.start();
	}
	static class CountrtThread extends Thread {
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