package application.server.masterserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import application.enity.*;

public class MasterServer extends Server{
	
	// list
	private List<FileSender> lstFileSender;
	
	
	public MasterServer(InetAddress iP, int pORT) {
		super(iP, pORT);
		lstFileSender = new LinkedList<FileSender>();
	}
	
	@Override
	public void startUp() throws IOException {
		super.startUp();
	}
	
	@Override
	public void terminal() throws IOException {
		serverSocket.close();
	}
	// nhận và xử lí các kết nối từ client 
	// nhận và xử lí các kết nối từ FileServer
	@Override
	public void operation() throws IOException{
		this.startUp();
		while (true) {
			Socket s = null;
			String fSend = null;
			try {
				s = serverSocket.accept();
				System.out.println("Accepting...");
				System.out.println("Listening...");
				
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				
				// nhận dữ liệu đọc
				// nếu là Client thì C_IP_PORT, nếu là fileserver thì FS_IP_PORT
				
				
				fSend = dis.readUTF();
				String[]fs = fSend.split("_");
				if(fs[0] == "C") {
					
					System.out.println("New Client " + fs[1] + " " + fs[2]);
					
					ClientHandler clientHandler = new ClientHandler(s, dis, dos, lstFileSender);
					clientHandler.start();
				} else {
					//tạo một luồng mới để xử lí cho fileserver
					
					System.out.println("New File Server " + fs[1] + " " + fs[2]);
					
//					FileServerHandler fileServerHandler = new FileServerHandler();
//					fileServerHandler.start();				
				}
				
			} catch (Exception e) {
				this.terminal();
			}	
		}
	}
}
