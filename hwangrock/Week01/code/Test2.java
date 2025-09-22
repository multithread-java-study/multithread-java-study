import static utils.Logger.log;

public class Test2 {
    public static void main(String[] args) {
        Thread th = new Thread(new CounterRunnable(), "counter");
        th.start();
    }

    static class CounterRunnable implements Runnable {
        public void run() {
            for (int i = 1; i <= 5; i++) {
                try {
                    log("value: " + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
