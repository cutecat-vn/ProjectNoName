package application.server.fileserver;

import java.io.IOException;
import java.net.InetAddress;

public class MainServer {
	public static void main(String[] args) throws IOException {
		InetAddress ip = InetAddress.getByName("localhost");
		FileServer fileServer = new FileServer(ip, 6000);
		fileServer.operation();
	}
}
