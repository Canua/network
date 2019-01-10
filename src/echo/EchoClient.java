package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "218.39.221.92";
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		Socket socket = null;
		try {
			Scanner s = new Scanner(System.in);

			// 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("connected");
			
			
			// 3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);
			while (true) {
				// 4. 쓰기
				System.out.print(">> ");
				String line = s.nextLine();
				if (line.equals("exit")) {
					break;
				}
//				if("exit".contentEquals(line)) {
//					break;
//				}
				
				pw.println(line);

				// 5. 읽기
				String data = br.readLine();
				if(data == null) {
					log("closed by server");
					break;
				}
				System.out.println("<< " + data);
			}
		} catch (IOException e) {
			log("error : " + e);
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				log("error : " + e);
			}
		}
	}

	public static void log(String log) {
		System.out.println("[client] " + log);
	}
}
