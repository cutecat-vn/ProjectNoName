package application.enity;

import java.io.Serializable;
import java.util.List;

//lưu thông tin file gửi đi
//đại diện cho 1 fileserver
//bao gồm:
//địa chỉ fileserver
//danh sách file có trong server đó

public class FileSender implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<FileEnity> lstFile;
	private AddressNet addr;
	
	public FileSender(List<FileEnity> lstFile, AddressNet addr) {
		super();
		this.lstFile = lstFile;
		this.addr = addr;
	}

	public List<FileEnity> getLstFile() {
		return lstFile;
	}

	public void setLstFile(List<FileEnity> lstFile) {
		this.lstFile = lstFile;
	}

	public AddressNet getAddr() {
		return addr;
	}

	public void setAddr(AddressNet addr) {
		this.addr = addr;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + addr.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FileSender sender = (FileSender) obj;
		if(this.addr.getIP() == sender.getAddr().getIP())
			if(this.addr.getPORT() != sender.getAddr().getPORT()) return false;
		if(this.addr.getIP() != sender.getAddr().getIP()) return false;
		return true;
            		
	}
	
}
