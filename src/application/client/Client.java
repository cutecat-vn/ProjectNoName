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

public class Client {
	
	@Deprecated
	//hàm này không sài
	private static String findLocation(String filename, String HOSTNAME, int PORT, Set<FileSender> lstFileSender) {
		 FileSender s = lstFileSender.stream().parallel().filter(fs -> (fs.getAddr().getIP().getHostName().equals(HOSTNAME) && fs.getAddr().getPORT() == PORT))
							  .findFirst().orElse(null);
		 String dir = s.getLstFile().stream().parallel().filter(f -> f.getFilename().equals(filename)).findFirst().orElse(null).getDir();
		 return dir;	 
	}
	// tìm Object FileSender trong list fileSender -> tìm thông qua hostName và Port (dùng Stream Java8)
	private static FileSender findSenderFile(String HOSTNAME, int PORT, Set<FileSender> lstFileSender) {
		return lstFileSender.stream().parallel().filter(fs -> (fs.getAddr().getIP().getHostName().equals(HOSTNAME) && fs.getAddr().getPORT() == PORT))
				  .findFirst().orElse(null);
	}
	
	@SuppressWarnings("unchecked")
	// hàm getData - gửi 1 chuổi "LIST" đến master server (yêu cầu nhận List FileSender)
	// tiếp theo rã thông tin ra và biến nó thành LIST
	// cái này là ObjectInputStream có thể tham khảo thêm
	private static Set<FileSender> getData(DataOutputStream dos_server, DataInputStream dis_server) throws IOException, ClassNotFoundException{
		dos_server.writeUTF("LIST");	        	
		ObjectInputStream objectinputstream = new ObjectInputStream(dis_server);
    	return (Set<FileSender>) objectinputstream.readObject();
	}
	// hàm này để in thông tin trong list
	private static void loadListFile(Set<FileSender> lstFileSender) {
    	System.out.println("Danh Sách File Và Máy Chủ:");
    	
    	for(FileSender sender : lstFileSender) {
    		System.out.println("Máy Chủ : " + sender.getAddr().getIP() + ":" + sender.getAddr().getPORT());
    		for(int i = 0; i < sender.getLstFile().size(); i++) {
    			FileEnity e = sender.getLstFile().get(i);    			
    			System.out.println("->" + i + ". " +  e.getFilename() + " - Location: " + e.getDir());
    		}
    	}
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		
		Scanner scanner = new Scanner(System.in);
		Socket sServer = null;
		Set<FileSender> lstFileSender = new HashSet<FileSender>();
		int option;
		
		// đường dẫn thư mục chứa file sau khi nhận từ fileserver
		final String dir_local_storage = "../ProjectNoName/tests/client/";
		FileOutputStream out_save = null;	
		try {
			// đầu tiên kết nối với master server
			//thiết lập thông tin IP
			InetAddress ipMasterServer = InetAddress.getByName("localhost");
			//thiết lập địa chỉ socket thông qua ip và port
			SocketAddress sAddr = new InetSocketAddress(ipMasterServer, 5000);
			// yêu cầu: vì masterserver là server chủ -> ta phải duy trình kết nối cho đến khi client chết
			sServer = new Socket();
			// thiết lập kết nối thông qua socket
			sServer.connect(sAddr);	
			
			DataInputStream dis_server = new DataInputStream(sServer.getInputStream());
	        DataOutputStream dos_server = new DataOutputStream(sServer.getOutputStream());
	        //gửi gói định danh với IP=localhost,Port=0		        
	        dos_server.writeUTF("C_" + InetAddress.getLocalHost().getHostAddress() + "_" + 0);	
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
		        		//in thông tin
		        		loadListFile(lstFileSender);
		        	}catch (Exception e) {
						continue;
					}
		        }
		        else if(option == 2) {
		        	
		        	// Ta phải Yêu cầu lại gói list trong trường hợp có thay đổi
		        	lstFileSender =  getData(dos_server, dis_server);
		        	//in thông tin
		        	loadListFile(lstFileSender);
		        	// nhập các thông tin về fileserver cũng như gói nhận - để số 1,2,3 để chọn cho dễ
		        	System.out.println("Chọn File Cần Chọn (Nhập Số): ");
		        	int _i_f =  Integer.parseInt(scanner.next());
		        	
		        	System.out.println("Nhập Host Name Máy Chủ: ");
		        	String HOSTNAME = scanner.next();
		        	
		        	System.out.println("Nhập cổng Máy Chủ: ");
		        	int PORT = Integer.parseInt(scanner.next());
		        	
		        	// thiết lập IP thông qua hostname đã nhập 
		        	InetAddress addr = InetAddress.getByName(HOSTNAME);
		        	
		        	System.out.println("Connecting to Addr : "+addr.getHostAddress() + "....");
		        	
		        	//nhận fileserver đã tìm ở trên-> đây hoàn toàn là nhập môn lập trình ha :) 
		        	FileSender s = findSenderFile(HOSTNAME, PORT, lstFileSender);
		        	if(s != null ) {
		        		//nếu cố tình nhập sai file cần nhận ở trên
		        		if(_i_f < 0 || _i_f >= s.getLstFile().size() ) {
		        			System.out.println("Index Invalid");
		        		}else {
		        			//lấy thông tin gói sau khi nhập 
		        			FileEnity fileChooser = s.getLstFile().get(_i_f); // -> đây hoàn toàn là pp lập trình hdt ha :) 
				        	
				        	
				        	//khúc này trên mạng có hết :)
				        	//tạo 1 byteArrayOutStream 
				        	ByteArrayOutputStream out = new ByteArrayOutputStream();
				            
				        	//lưu infoSender dưới dạng byte
				        	ObjectOutputStream os = new ObjectOutputStream(out);
				            os.writeObject(fileChooser);
				            
				            // biến nó thành mảng byte
				            byte[] data_info =  out.toByteArray();
				            
				            // tạo gói UDP packet
				            DatagramPacket packet_info = new DatagramPacket(data_info, data_info.length, addr, PORT);
				            DatagramSocket socket = null;
				            
				            try {
				            	socket = new DatagramSocket();
				            	try {
				            		// gửi packet 
				            		socket.send(packet_info);
				            		// nhận file gửi từ client
				            		
				            		byte[] data_file = new byte[2048];
				            		//trước khi nhận cần tạo 1 cái packet rỗng trước
									DatagramPacket packet_file = new DatagramPacket(data_file, 0, data_file.length);
									// nhận thông tin và lưu vào trong packet đó
									socket.receive(packet_file);
			
									//lưu thông tin packet ra file
								
									out_save = new FileOutputStream(dir_local_storage + fileChooser.getFilename());
									out_save.write(packet_file.getData());
									
									System.out.println("Saved, Escaping...");
									
									out_save.close();
									socket.close();
				            		
				            	}catch (Exception e) {
				            		e.printStackTrace();
								}
				            
				            }catch (Exception e) {
				            	e.printStackTrace();
							}
				            socket.close();
		        		}
		        	} else {		        		
		        		System.out.println("Information your input to valid, Exit...");
		        	}
		        	System.out.println("Closed");
		        } else {
		        	//đóng các kết nối
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


