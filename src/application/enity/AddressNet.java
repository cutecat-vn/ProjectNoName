package application.enity;

import java.io.Serializable;
import java.net.InetAddress;

public class AddressNet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InetAddress IP;
	private int PORT;
	
	public InetAddress getIP() {
		return IP;
	}
	public void setIP(InetAddress iP) {
		IP = iP;
	}
	public int getPORT() {
		return PORT;
	}
	public void setPORT(int pORT) {
		PORT = pORT;
	}
	public AddressNet(InetAddress iP, int pORT) {
		super();
		IP = iP;
		PORT = pORT;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return IP.hashCode() 
			  + PORT;
	}
	
}
