package thread;

public class MultithreadEx02 {
	public static void main(String[] args) {
		Thread thread1 = new DigitThread();
		Thread thread2 = new AlphabasicicThread();
		Thread thread3 = new DigitThread();
		// 객체 생성
		
		thread1.start();
		thread2.start();
		thread3.start();
		
	}
}
