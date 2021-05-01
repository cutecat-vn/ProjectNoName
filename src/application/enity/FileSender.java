package application.enity;

import java.io.Serializable;
import java.util.List;

public class FileSender implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String location; //
	private List<FileEnity> lstFile;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<FileEnity> getLstFile() {
		return lstFile;
	}
	public void setLstFile(List<FileEnity> lstFile) {
		this.lstFile = lstFile;
	}
	
	
}
