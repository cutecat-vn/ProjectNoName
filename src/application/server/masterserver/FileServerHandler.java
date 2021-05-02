package application.server.masterserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Set;

import application.enity.FileSender;

public class FileServerHandler extends Thread{
	private Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Set<FileSender> lstFileSender;
	
	public FileServerHandler(Socket s, 
							DataInputStream dis, 
							DataOutputStream dos, 
							Set<FileSender> lstSender) {
		this.s=  s;
		this.dis = dis;
		this.dos = dos;
		this.lstFileSender = lstSender;
	}
	
	@Override
	public void run() {
		String received;
		FileSender sender;
		ObjectInputStream objectinputstream = null;
            try {
            	objectinputstream = new ObjectInputStream(dis);
            	sender = (FileSender) objectinputstream.readObject();
                lstFileSender.add(sender);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }  
	}

	public Set<FileSender> getLstFileSender() {
		return lstFileSender;
	}

	
	
	
	
}
