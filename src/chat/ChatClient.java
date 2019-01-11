package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClient {
	private static final String SERVER_IP = "218.39.221.92";
	private static final int PORT = 5000;

	public static void main(String[] args) {
		Scanner scanner = null;
		Socket clientsocket = null;
		try {
			// 1. 키보드 연결
			scanner = new Scanner(System.in);

			// 2. 소켓 생성
			clientsocket = new Socket();

			// 3. 연결
			clientsocket.connect(new InetSocketAddress(SERVER_IP, PORT));
			log("connected");

			// 4. reader/writer 생성
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(clientsocket.getInputStream(), StandardCharsets.UTF_8));
			PrintWriter printWriter = new PrintWriter(
					new OutputStreamWriter(clientsocket.getOutputStream(), StandardCharsets.UTF_8), true);

			// 5. join 프로토콜
			System.out.print("닉네임>>");
			String nickname = scanner.nextLine();
			printWriter.println("join:" + nickname);
//			printWriter.flush();

			// 6. ChatClientReceiveThread 시작
			new ChatClientThread(clientsocket).start();
			// 7. 키보드 입력처리
			while (true) {
				// 쓰기
				String input = scanner.nextLine();
				if ("quit".equals(input) == true) {
					// F 8. quit 프로토콜 처리
					break;
				} else {
					// 9. 메시지 처리
					printWriter.println("message:" + input);
				}
			}
		} catch (IOException e) {
			log("error : " + e);
		} finally {
			try {
				if (clientsocket != null && clientsocket.isClosed() == false) {
					clientsocket.close();
				}
			} catch (IOException e) {
				log("error : " + e);
			}
		}
	}

	public static void log(String log) {
		System.out.println("[client#" + Thread.currentThread().getId() + "]" + log);
	}
}
