package application.enity;

import java.io.Serializable;

public class InfoFileSender implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String filename;
	private String location;
	public InfoFileSender(String filename, String location) {
		super();
		this.filename = filename;
		this.location = location;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
