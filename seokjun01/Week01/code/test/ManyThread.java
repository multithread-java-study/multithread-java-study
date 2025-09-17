package thread.start.test;

import static util.MyLogger.*;

public class ManyThread implements Runnable{

	private String content;
	private int delayMs;

	public ManyThread(String content, int delayMs) {
		this.content = content;
		this.delayMs = delayMs;
	}

	@Override
	public void run() {
		while (true) {
			log(content);
			try {
				Thread.sleep(delayMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
