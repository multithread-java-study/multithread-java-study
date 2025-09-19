package thread.start.test;
// Thread-A, Thread-B 2개만들어라  + A는 1초에 한 번, B는 0.5초에 한 번

public class StartTest4Main {

	public static void main(String[] args) {
		ManyThread a = new ManyThread("A", 1000);
		ManyThread b = new ManyThread("B", 500);

		Thread t1 = new Thread(a, "Thread-A");
		Thread t2 = new Thread(b, "Thread-B");

		t1.start();
		t2.start();



	}
}
