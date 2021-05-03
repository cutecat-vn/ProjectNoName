package application.server.masterserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Thread.State;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import application.enity.*;

public class MasterServer extends Server{
	
	// list
	private Set<FileSender> lstFileSender;
	
	
	public MasterServer(AddressNet addr) {
		super(addr);
		lstFileSender = new HashSet<FileSender>();
	}
	
	@Override
	public void startUp() throws IOException {
		super.startUp();
	}
	
	public FileSender findSenderFile(String HOSTNAME, int PORT) {
		return lstFileSender.stream()
				  .parallel()
				  .filter(fs -> (fs.getAddr().getIP().getHostName().equals(HOSTNAME) && fs.getAddr().getPORT() == PORT))
				  .findFirst()
				  .orElse(null);
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
				
				if(fs[0].equals("C")) {
					
//					System.out.println("New Client " + fs[1] + " " + fs[2]);
					
					ClientHandler clientHandler = new ClientHandler(s, dis, dos, lstFileSender);
					clientHandler.start();
				} else {
					//tạo một luồng mới để xử lí cho fileserver
					
//					System.out.println("New File Server " + fs[1] + " " + fs[2]);
					
					FileServerHandler fileServerHandler = new FileServerHandler(s, dis, dos, lstFileSender);
					fileServerHandler.start();
							
					fileServerHandler.join();
					lstFileSender = fileServerHandler.getLstFileSender();
								
//					System.out.println("Hello :" + lstFileSender.get(0).getAddr().getIP().getHostAddress());
				}
			} catch (Exception e) {
				this.terminal();
			}	
		}
	}
}
