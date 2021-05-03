package application.server.fileserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;

import application.enity.AddressNet;
import application.enity.FileEnity;
import application.enity.FileSender;
import application.enity.Server;


// sau khi khoi dong sẽ gửi cho Master Server IP và port

// trước khi tắt phải báo cho master server

public class FileServer extends Server {
	
	private FileSender fileSender;
	private String pathLoc;
	
	
	DataOutputStream dos;
	DataInputStream dis;
	DatagramSocket sUDP;
	
	public FileServer(AddressNet addr, FileSender fileSender) {
		super(addr);
		this.setFileSender(fileSender);
	}
	
	public FileServer(AddressNet addr, String pathLoc) {
		super(addr);
		fileSender = new FileSender(new LinkedList<FileEnity>(), addr);
		this.pathLoc = pathLoc;
	}
	private Socket sServer;
	public void retriveFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	retriveFolder(fileEntry);
	        } else {
	            FileEnity fileEnity = new FileEnity(fileEntry.getName(),
	            									folder.getPath());
	            fileSender.getLstFile().add(fileEnity);
	        }
	    }
	}
	
	@Override
	public void startUp() throws IOException {
		super.startUp();
		//connect to master server
		InetAddress ipMasterServer = InetAddress.getByName("localhost");
		SocketAddress sAddr = new InetSocketAddress(ipMasterServer, 5000);
		
		sServer = new Socket();
		sServer.connect(sAddr);	
        
		dos = new DataOutputStream(sServer.getOutputStream());
        dis = new DataInputStream(sServer.getInputStream());
        
        dos.writeUTF("FS_" + addr.getIP().getHostAddress() + "_" + addr.getPORT() );       
        
        // gửi FileSender
        // cần quét lại 1 lần trước mỗi lần gửi
        // link này sẽ thay sau khi deloy trên máy ảo
        
        this.retriveFolder(new File(pathLoc));       
        // gửi đi 1 list dưới dạng serizable
        ObjectOutputStream out = new ObjectOutputStream(dos);
        out.writeObject(fileSender);
	}

	@Override
	public void terminal() throws IOException {
		System.out.println("File Server is terminal");
		// kêu thằng server		
		dos.writeUTF("K_" + addr.getIP().getHostAddress() + "_" + addr.getPORT());
		serverSocket.close();
		sServer.close();
		sUDP.close();
	}
	
	@Override
	public void operation() throws IOException {		
		this.startUp();	
		// tạo một UDP socket để hứng client
		// nhận lệnh gửi file từ chuổi lệnh TCP client
		
		sUDP = new DatagramSocket(6002);
		byte[] data = new byte[1024];
		// tạo gói packet
		
		DatagramPacket packet = new DatagramPacket(data, data.length);		
		while(true) {
			try {
				// gửi gói 
				// https://www.programmersought.com/article/86915654107/
				// tới đây các gói được xử lí theo UDP
			
				sUDP.receive(packet); //chờ bắt gói				
				ClientHandler handler = new ClientHandler(packet);
				handler.start();				
			} catch(Exception ex) {				
				ex.printStackTrace();
				this.terminal();
			}
		}
	}
	
	public FileSender getFileSender() {
		return fileSender;
	}

	public void setFileSender(FileSender fileSender) {
		this.fileSender = fileSender;
	}
}
