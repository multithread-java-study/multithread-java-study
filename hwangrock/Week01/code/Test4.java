import static utils.Logger.log;

public class Test4 {
    public static void main(String[] args) {
        Writer w1 = new Writer("A", 1000);
        Writer w2 = new Writer("B", 500);
        Thread a = new Thread(w1, "Thread-A");
        Thread b = new Thread(w2, "Thread-B");
        a.start();
        b.start();
    }

    static class Writer implements Runnable {

        private final String s;
        private final int time;

        public Writer(String s, int time) {
            this.s = s;
            this.time = time;
        }

        public void run() {
            while (true) {
                try {
                    log(s);
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
