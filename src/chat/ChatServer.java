package chat;

import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 5000;

	public static void main(String[] args) {
		// List 선언
		List<Writer> listWriters = new ArrayList<Writer>();

		ServerSocket serverSocket = null;
		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();

			
			// 2. 바인딩
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
			log("연결 기다림 " + localhostAddress + ":" + PORT);

			// 3. 요청 대기
			while (true) {
				Socket socket = serverSocket.accept();
				new ChatServerThread(socket, listWriters).start();
			}
		} catch (Exception e) {
			log("error : " + e);
		}
	}

	public static void log(String log) {
		System.out.println("[Server#" + Thread.currentThread().getId() + "]" + log);
	}
}
