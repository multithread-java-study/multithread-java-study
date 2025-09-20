public class problem04 {
	public static void main(String[] args) {

		Thread threadA = new Thread(new AutoRunable("A", 1000), "Thread-A");
		Thread threadB = new Thread(new AutoRunable("B", 500), "Thread-B");
		threadA.start();
		threadB.start();
	}

	static class AutoRunable implements Runnable {

		private String c;
		private int sleepTime;

		public AutoRunable(String c, int sleepTime) {
			this.c = c;
			this.sleepTime = sleepTime;
		}

		@Override
		public void run() {
			while (true) {
				log(c);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}