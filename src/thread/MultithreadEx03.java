package thread;

public class MultithreadEx03 {
	public static void main(String[] args) {
		Thread thread1 = new AlphabasicicThread();
		Thread thread2 = new DigitThread();
		Thread thread3 = new Thread(new UppercaseAlpahbeticRunnableImpl());
		
		thread1.start();
		thread2.start();
		thread3.start();
	}
}
