package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		try {
			while (true) {
				System.out.print("> ");
				String line = s.nextLine();
				if (line.equals("exit")) {
					s.close();
					break;
				}
				InetAddress[] inetAddress = InetAddress.getAllByName(line);
//				for (int i = 0; i < inetAddress.length; i++) {
//					System.out.println(line + " : " + inetAddress[i].getHostAddress());
//				}
				for (InetAddress inet : inetAddress) {
					System.out.println(inet.getHostName() + " : " + inet.getHostAddress());
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
