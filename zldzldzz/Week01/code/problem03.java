public class problem03 {
	public static void main(String[] args) {

		Runnable runnable = new Runnable() {
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
		};

		Thread thread = new Thread(runnable, "counter");
		thread.start();
	}
}