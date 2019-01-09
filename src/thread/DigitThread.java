package thread;

public class DigitThread extends Thread {

	@Override
	public void run() {
		for (int i = 0; i <= 9; i++) {
			System.out.print(i); // thread Id 호출
			// 싱크 맞추기
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
