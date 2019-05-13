package com.parthgarg.printgo;

public class ResponseClass {

	int rs;
	String Checksum;
	String OrderId;
	public ResponseClass(int rs,String Checksum,String OrderId) {
		this.OrderId=OrderId;
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

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

}