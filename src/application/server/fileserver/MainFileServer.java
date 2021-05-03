package application.server.fileserver;

import java.io.IOException;
import java.net.InetAddress;

import application.enity.AddressNet;

public class MainFileServer {
	public static void main(String[] args) throws IOException {
		InetAddress ip = InetAddress.getByName("localhost");
		// nhập port vào
		AddressNet addr = new AddressNet(ip, 6002);
		
		FileServer fileServer = new FileServer(addr
									,"../ProjectNoName/tests/" + addr.getPORT() );
		fileServer.operation();
	}	
	
}
