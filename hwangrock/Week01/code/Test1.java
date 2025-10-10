import static utils.Logger.log;

public class Test1 {
    public static void main(String[] args) {
        Thread th1 = new CounterThread();
        th1.start();
    }

    static class CounterThread extends Thread {

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 5; i++) {
                    log("value: " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}