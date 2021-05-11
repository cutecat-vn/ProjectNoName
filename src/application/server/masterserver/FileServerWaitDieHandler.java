package application.server.masterserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Set;

import application.enity.FileSender;

@Deprecated
public class FileServerWaitDieHandler extends Thread {
	
	private static FileSender findSenderFile(String HOSTNAME, int PORT, Set<FileSender> lstFileSender) {
		return lstFileSender.stream().parallel().filter(fs -> (fs.getAddr().getIP().getHostName().equals(HOSTNAME) && fs.getAddr().getPORT() == PORT))
				  .findFirst().orElse(null);
	}
	
	private Socket s;
	private DataInputStream dis;
	private Set<FileSender> lstFileSender;
	
	public FileServerWaitDieHandler(Socket s, 
							DataInputStream dis,  
							Set<FileSender> lstSender) {
		this.s=  s;
		this.dis = dis;
		this.lstFileSender = lstSender;
	}
	
	@Override
	public void run() {
		try {
			dis.readUTF();			
		}catch (Exception e) {
    		System.out.println("Deleted");
    		FileSender sender = findSenderFile(s.getInetAddress().getHostName(), s.getPort(), lstFileSender);
    		lstFileSender.remove(sender);
    		return;
		}
		
		System.out.println(this.getState());
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println(this.getState());
	}
	
	@Override
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return super.getUncaughtExceptionHandler();
	}
}
