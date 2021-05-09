package application.enity;

import java.io.Serializable;

//lưu thông tin file
public class FileEnity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String filename;  // like this test.txt
	private String dir;       //location store file
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public FileEnity(String filename, String dir) {
		this.filename = filename;
		this.dir = dir;
	}
	
	
	
}
