import static utils.Logger.log;

public class Test3 {
    public static void main(String[] args) {
        Runnable counter=new Runnable() {
            @Override
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
        };

        Thread th=new Thread(counter,"counter");
        th.start();
    }
}
