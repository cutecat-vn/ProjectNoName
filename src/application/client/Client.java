package application.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import application.enity.FileEnity;
import application.enity.FileSender;
import application.enity.InfoFileSender;

public class Client {
	
	private static String findLocation(String filename, String HOSTNAME, int PORT, Set<FileSender> lstFileSender) {
		 FileSender s = lstFileSender.stream().parallel().filter(fs -> (fs.getAddr().getIP().getHostName().equals(HOSTNAME) && fs.getAddr().getPORT() == PORT))
							  .findFirst().orElse(null);
		 String dir = s.getLstFile().stream().parallel().filter(f -> f.getFilename().equals(filename)).findFirst().orElse(null).getDir();
		 return dir;
	}
	
	@SuppressWarnings("unchecked")
	private static Set<FileSender> getData(DataOutputStream dos_server, DataInputStream dis_server) throws IOException, ClassNotFoundException{
		dos_server.writeUTF("LIST");	        	
		ObjectInputStream objectinputstream = new ObjectInputStream(dis_server);
    	return (Set<FileSender>) objectinputstream.readObject();
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		Socket sServer = null;
		Set<FileSender> lstFileSender = new HashSet<FileSender>();
		int option;
		
		
		// up file
		
		String dir_local_storage = "../ProjectNoName/tests/client/";
		FileOutputStream out_save = null;	
		try {
			// đầu tiên kết nối với master server
			
			InetAddress ipMasterServer = InetAddress.getByName("localhost");
			SocketAddress sAddr = new InetSocketAddress(ipMasterServer, 5000);
			
			// vì masterserver là server chủ -> ta phải duy trình kết nối cho đến khi client chết
			sServer = new Socket();
			sServer.connect(sAddr);	
			
			DataInputStream dis_server = new DataInputStream(sServer.getInputStream());
	        DataOutputStream dos_server = new DataOutputStream(sServer.getOutputStream());
	        //gửi gói định danh với IP=localhost,Port=0		        
	        dos_server.writeUTF("C_" + InetAddress.getLocalHost().getHostAddress() + "_" + 0);
			
	        lstFileSender =  getData(dos_server, dis_server);
			
			while(true) {
				System.out.println("==========MENU===============");
				System.out.println("|1. Lấy danh sách file      |");
				System.out.println("|2. Tải File                |");
				System.out.println("|3. Thoát                   |");	
				System.out.println("=============================");
				
				System.out.print("Nhap Lua Chon:");
				option = scanner.nextInt();
				scanner.nextLine();
				
		        if(option == 1) {
		        	try {		        	
			        	// yêu cầu gói LIST
			        	
		        		lstFileSender = getData(dos_server, dis_server);
		        		
			        	System.out.println("Danh Sách File Và Máy Chủ:");
			        	
			        	for(FileSender sender : lstFileSender) {
			        		System.out.println("Máy Chủ : " + sender.getAddr().getIP() + ":" + sender.getAddr().getPORT());
			        		for(FileEnity f : sender.getLstFile()) {
			        			System.out.println("->" + f.getFilename());
			        		}
			        	}
		        	}catch (Exception e) {
						continue;
					}
		        }
		        else if(option == 2) {
		        	
		        	System.out.println("Nhập File Cần Nhận: ");
		        	String filename = scanner.next();
		        	
		        	System.out.println("Nhập Host Name Máy Chủ: ");
		        	String HOSTNAME = scanner.next();
		        	
		        	System.out.println("Nhập cổng Máy Chủ: ");
		        	int PORT = Integer.parseInt(scanner.next());
		        	
		        	// tạo kết nối
	
		        	InetAddress addr = InetAddress.getByName(HOSTNAME);
		        	
		        	System.out.println("Addr : "+addr.getHostAddress());
		        	
		        	InfoFileSender infoSender = new InfoFileSender(filename, 
		        									findLocation(filename, 
		        												 HOSTNAME, 
		        											 	 PORT, 
		        												 lstFileSender));
		        	
		        	System.out.println("Info : " + infoSender.getLocation() + " " + infoSender.getFilename() );
		        	
		        	ByteArrayOutputStream out = new ByteArrayOutputStream();
		            ObjectOutputStream os = new ObjectOutputStream(out);
		            os.writeObject(infoSender);
		            byte[] data_info =  out.toByteArray();
		            DatagramPacket packet_info = new DatagramPacket(data_info, data_info.length, addr, PORT);
		            DatagramSocket socket = null;
		            try {
		            	socket = new DatagramSocket();
		            	try {
		            		socket.send(packet_info);
		            		
		            		
		            		// nhận file gửi từ client
		            		
		            		byte[] data_file = new byte[2048];
		            		 
							DatagramPacket packet_file = new DatagramPacket(data_file, 0, data_file.length);
							// Monitor the data of the server
							socket.receive(packet_file);
	
							// Nhận packet dưới dạng bytes và lưu xuống local storage
						
							out_save = new FileOutputStream(dir_local_storage + filename);
							out_save.write(packet_file.getData());
							
							System.out.println("Saved");
							System.out.println(out_save.toString());
							
							out_save.close();
							socket.close();
		            		
		            	}catch (Exception e) {
		            		e.printStackTrace();
						}
		            
		            }catch (Exception e) {
		            	e.printStackTrace();
					}
		            socket.close();
		        } else {
		        	dos_server.writeUTF("EXIT");		        	
		        	dis_server.close();
		        	dos_server.close();
		        	break;
		        }
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.gc();
		}
		finally {
			sServer.close();
			scanner.close();
		}
	}
}


