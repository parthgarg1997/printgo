package com.parthgarg.printgo;

public class ResponseClass {

	int rs;
	String Checksum;
	public ResponseClass(int rs,String Checksum) {
		
		this.rs = rs;
		this.Checksum=Checksum;
	}
public ResponseClass() {
		
	}


	public String getChecksum() {
	return Checksum;
}
public void setChecksum(String checksum) {
	Checksum = checksum;
}
	public int getRs() {
		return rs;
	}

	public void setRs(int rs) {
		this.rs = rs;
	}
	
}
