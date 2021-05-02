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

import application.enity.InfoFileSender;

public class ClientHandler extends Thread {
	
	private DatagramPacket packet;
	private InfoFileSender ifSender;
	public ClientHandler(DatagramPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		// tách gói
		
		System.out.println("Hello Client");
		
		int port_packet = packet.getPort();
		InetAddress ip_packet = packet.getAddress();
		
		ByteArrayInputStream in = new ByteArrayInputStream(packet.getData());
	    ObjectInputStream is;
		try {
			is = new ObjectInputStream(in);
			ifSender = (InfoFileSender) is.readObject();
			
			System.out.println(ifSender.getFilename() + " " + ifSender.getLocation());
			
			//byte[] bytes_file = Files.readAllBytes(Paths.get(ifSender.getLocation() + "\\" + ifSender.getFilename() ));
			File f = new File(ifSender.getLocation() + "\\" + ifSender.getFilename());
			try (BufferedReader br
					 = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
					        String line;
					  
					  while ((line = br.readLine()) != null) {
						  System.out.println(line);  
					  }
			}
			
			
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
			
			
			DatagramPacket packet_file = new DatagramPacket(bytes_file,
															bytes_file.length,
															ip_packet,
															port_packet);
			try {
				DatagramSocket s = new DatagramSocket(7001);
				s.send(packet_file);
				
				System.out.println("already send");
				
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
