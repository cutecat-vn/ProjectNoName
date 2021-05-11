package application.server.fileserver;

import java.io.IOException;
import java.net.InetAddress;

import application.enity.AddressNet;


//Main
public class MainFileServer {
	public static void main(String[] args) throws IOException {
		
		//thông tin ip
		InetAddress ip = InetAddress.getByName("localhost");
		// nhập port vào
		AddressNet addr = new AddressNet(ip, 6002);
		
		FileServer fileServer = new FileServer(addr,"../ProjectNoName/tests/" + addr.getPORT() );
		fileServer.operation();
	}	
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("Hello DM");
	}
	
}
