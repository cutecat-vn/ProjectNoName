package application.enity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public abstract class Server {
	protected InetAddress IP;
	protected int PORT;
	protected ServerSocket serverSocket;
	
	public Server(InetAddress iP, int pORT) {
		IP = iP;
		PORT = pORT;
	}
	
	public InetAddress getIP() {
		return IP;
	}

	public void setIP(InetAddress iP) {
		IP = iP;
	}

	public int getPORT() {
		return PORT;
	}

	public void setPORT(int pORT) {
		PORT = pORT;
	}
	public void startUp() throws IOException {
		serverSocket = new ServerSocket(PORT, Integer.MAX_VALUE , IP);
	}
	
	public abstract void terminal() throws IOException;
	public abstract void operation() throws IOException;
}
