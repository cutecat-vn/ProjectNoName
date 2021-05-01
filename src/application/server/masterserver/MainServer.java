package application.server.masterserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainServer {
	public static void main(String[] args) throws IOException {
		InetAddress ip = InetAddress.getByName("localhost");
		MasterServer server = new MasterServer(ip, 5000);
		server.operation();
	}
}
