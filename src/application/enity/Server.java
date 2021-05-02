package application.enity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public abstract class Server {
	protected AddressNet addr;
	protected ServerSocket serverSocket;
	
	
	public Server(AddressNet addr) {
		super();
		this.addr = addr;
	}
	
	public void startUp() throws IOException {
		serverSocket = new ServerSocket(addr.getPORT(), Integer.MAX_VALUE , addr.getIP());
	}
	
	public abstract void terminal() throws IOException;
	public abstract void operation() throws IOException;
}
