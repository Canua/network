package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClientThread extends Thread {
	private BufferedReader bufferedReader;
	private Socket socket;

	public ChatClientThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			while (true) {
				String request = bufferedReader.readLine();
				if (request == null) {
					ChatClient.log("서버로부터 종료");
					break;
				}
				System.out.println(">> " + request);
			}
		} catch (IOException e) {
//			ChatClient.log("error : " + e);
			System.out.println("채팅 종료");
		}
	}

}
