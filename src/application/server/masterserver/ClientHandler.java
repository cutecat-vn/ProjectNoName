package application.server.masterserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Set;

import application.enity.FileSender;

public class ClientHandler extends Thread {
	private Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Set<FileSender> lstFileSender;

	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, Set<FileSender> lstSender) {
		this.s=  s;
		this.dis = dis;
		this.dos = dos;
		this.lstFileSender = lstSender;
	}
	
	@Override
	public void run() {
		String received;
        while (true) 
        {
            try {
            		// hứng yêu cầu từ client
	                received = dis.readUTF(); 
	                if(received.equals("Exit") || received == null || received == "")
	                { 
	                    this.s.close();
	                    break;
	                }
	                switch (received) {
	                	case "LIST":
	                		ObjectOutputStream oos = new ObjectOutputStream(dos);
	                		oos.writeObject(lstFileSender);
	                		break;
	                    default:
	                        dos.writeUTF("Sai Cú Pháp");
	                        break;
	                }
            	
            } catch (IOException e) {
                break;
            }
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
              
        }catch(IOException e){
            e.printStackTrace();
        }
	}
}
