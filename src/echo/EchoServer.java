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
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
			log("binding " + localhostAddress + ":" + PORT);

			// 3. accept
//			System.out.println("[서버] 연결 기다림");
			while (true) {
				Socket socket = serverSocket.accept();
				Thread thread = new EchoServerReceiveThread(socket);
				thread.start();
			}
			// ----------------------------------------

			// -----------------------------------------------------
		} catch (IOException e) {
			log("error : " + e);

		} finally {
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (Exception e) {
				log("error : " + e);
			}
		}
	}

	public static void log(String log) {
		System.out.println("[Server#" + Thread.currentThread().getId() + "]" + log);
	}
}
