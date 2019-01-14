package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
	private static final int PORT = 5000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();

			// 2. Binding - Socket에 SocketAddress ( IPAddress + port ) 바인딩한다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
			// InetAddress를 이용하여 localhost를 구하고 port 번호를 지정해 준다.
			System.out.println("[server] binding " + localhostAddress + " : " + PORT);
			// 3. accept - 클라이언트로부터 연결요청을 기다린다.
			Socket socket = serverSocket.accept(); // Blocking
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remotePort = inetRemoteSocketAddress.getPort(); // PORT 리턴

			System.out.println("[server] connected by client[" + remoteHostAddress + " : " + remotePort + "]");

			try { // 데이터 소켓 사용
					// 4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				while (true) {

					// 5. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); // block();
					if (readByteCount == -1) {
						// 정상종료 : remote socket이 close()
						// 메소드를 통해서 정상적으로 소켓을 닫은 경우
						System.out.println("[server] closed by client");
						break;
					}
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[server] received :" + data);

					// 5-1 Thread.sleep
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					// 6. 데이터 쓰기
					os.write(data.getBytes("UTF-8"));

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

}
