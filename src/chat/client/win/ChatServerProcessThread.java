package chat.client.win;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread {
	private String nickname;
	private Socket socket;

	private List<Writer> listWriters;

	BufferedReader bufferedReader = null;
	PrintWriter printWriter = null;

	public ChatServerProcessThread(Socket socket) {
		this.socket = socket;
	}

	// List 생성자
	public ChatServerProcessThread(Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {
		ChatServer.log(": 연결 완료");
		try {
			// 2. 스트림 얻기
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
					true);

			// 3. 요청 처리
			while (true) {
				String request = bufferedReader.readLine(); // 읽어 들여
				if (request == null) {
					ChatServer.log("클라이언트로 부터 연결 끊김");
					doQuit(printWriter);
					break;
				}
				// 4. 프로토콜 분석
				String[] tokens = request.split(":");
				if ("join".equals(tokens[0])) {
					doJoin(tokens[1], printWriter); // join 메소드
				} else if ("message".equals(tokens[0])) {
					doMessage(tokens[1]); // message 메소드
				} else if ("quit".equals(tokens[0])) {
					doQuit(printWriter); // 종료 메소드
				}
			}
		} catch (Exception e) {
			ChatServer.log(": 연결 종료");
		}
	}

	private void doQuit(Writer writer) {
		removeWriter(writer);
		String data = nickname + "님이 퇴장하였습니다.";
		broadcast(data);
	}

	private void removeWriter(Writer writer) {
		listWriters.remove(writer);
	}

	private void doMessage(String message) {
		broadcast(this.nickname + " : " + message);
	}

	private void doJoin(String nickName, Writer writer) {
		this.nickname = nickName;
		String data = nickName + "님이 참여하였습니다.";
		broadcast(data);

		// write pool 저장
		addWriter(writer);

		// ack
//		printWriter.println("join: ok");
//		printWriter.flush();

	}

	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}

	// 브로드케스트
	private void broadcast(String data) {
		synchronized (listWriters) {
			for (Writer writer : listWriters) {
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				printWriter.flush();
			}
		}
	}
}
