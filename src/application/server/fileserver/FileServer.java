package application.server.fileserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import application.enity.Server;


// sau khi khoi dong sẽ gửi cho Master Server IP và port

// trước khi tắt phải báo cho master server

public class FileServer extends Server {

	public FileServer(InetAddress iP, int PORT) {
		super(iP, PORT);
	}
	private Socket sServer;
	@Override
	public void startUp() throws IOException {
		super.startUp();
		//connect to master server
		InetAddress ipMasterServer = InetAddress.getByName("localhost");
		
		SocketAddress sAddr = new InetSocketAddress(ipMasterServer, 5000);
		sServer = new Socket();
		sServer.connect(sAddr);	
        DataOutputStream dos = new DataOutputStream(sServer.getOutputStream());
        dos.writeUTF("FS_" + IP.getHostAddress() + "_" + PORT );       
	}

	@Override
	public void terminal() throws IOException {
		serverSocket.close();
		sServer.close();
	}

	@Override
	public void operation() throws IOException {		
		this.startUp();
//		while(true) {
//			Socket s = null;
//			try {
//				
//			}
//			
//			
//		}
		this.terminal();
	}
}
