package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 5000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			// 1. 서버 소켓
			serverSocket = new ServerSocket();

			// 2. 바인딩
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));

			// 3. accept
			System.out.println("[서버] 연결 기다림");
//			while (true) {
			Socket socket = serverSocket.accept();
//				Thread thread = new EchoServerReceiveThread(socket);
//				thread.start();
//			}
			//----------------------------------------

			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remotePort = inetRemoteSocketAddress.getPort();

			System.out.println("[서버] 연결됨 from " + remoteHostAddress + " : " + remotePort + "]");

			try {
				// 4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);
				while (true) {
					// 5. 데이터 읽기
					String rec = br.readLine();
					if (rec == null) {
						System.out.println("[서버] 클라이언트로 부터 연결 끊김");
						break;
					}
					System.out.println("[서버] 데이터 수신 :" + rec);
					// 6. 쓰기
					pw.println(rec);
				}
			} catch (SocketException e) {
				System.out.println("[server] abnormal closed by client");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					// 7. 자원정리(소켓 닫기)
					if (socket != null && socket.isClosed() == false) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//-----------------------------------------------------
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(String log) {
		System.out.println("[Server#" + Thread.currentThread().getId() + "]" + log);
	}
}
