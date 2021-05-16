package application.server.fileserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import application.enity.FileEnity;

//xử lí thông tin với 1 client
public class ClientHandler extends Thread {
	
	private DatagramPacket packet;
	private FileEnity ifSender;
	public ClientHandler(DatagramPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		// tách gói
		System.out.println("Hello Client");
		
		int port_packet = packet.getPort();
		InetAddress ip_packet = packet.getAddress();
		
		// phân tích gói từ client->gói FileEnity
		ByteArrayInputStream in = new ByteArrayInputStream(packet.getData());
	    ObjectInputStream is;
		try {
			is = new ObjectInputStream(in);
			ifSender = (FileEnity) is.readObject();
			
			System.out.println(ifSender.getFilename() + " " + ifSender.getDir());
			
			//byte[] bytes_file = Files.readAllBytes(Paths.get(ifSender.getLocation() + "\\" + ifSender.getFilename() ));
			// tạo 1 object file từ filename và path từ client đã gửi
			File f = new File(ifSender.getDir() + "\\" + ifSender.getFilename());
			
//			try (BufferedReader br
//					 = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
//					        String line;
//					  
//					  while ((line = br.readLine()) != null) {
//						  System.out.println(line);  
//					  }
//			}
			
			//nhiệm vụ của khúc này là cho cái file vào trong fileInputStream
			// từ Fis thành mảng bytes
			byte[] bytes_file = new byte[(int) f.length()];
		    FileInputStream fis = null;
		    try {
		          fis = new FileInputStream(f);
		          //read file into bytes[]
		          fis.read(bytes_file);
		    } finally {
		          if (fis != null) {
		              fis.close();
		          }
		    }
			
			// tạo UDP packet
		    // bỏ mảng byte vào trong packet
			DatagramPacket packet_file = new DatagramPacket(bytes_file,
															bytes_file.length,
															ip_packet,
															port_packet);
			try {
				//chuyển packet đi
				DatagramSocket s = new DatagramSocket(7001);
				s.send(packet_file);
				
				System.out.println("already send");
				
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException | ClassNotFoundException e) {
		}
	}
}
