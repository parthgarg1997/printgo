package com.parthgarg.printgo;

public  class RequestClass {

	 String folder;
	 String file_name;
	 int no_page;
	 String customer_id,mobile_no,email;
	 Boolean colored;
	
	public RequestClass()
	{
	}
	
	public RequestClass(String folder, String file_name, int no_page, String customer_id, String mobile_no, String email,
			Boolean colored) {
		this.folder = folder;
		this.file_name = file_name;
		this.no_page = no_page;
		this.customer_id = customer_id;
		this.mobile_no = mobile_no;
		this.email = email;
		this.colored = colored;
	}

	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public int getNo_page() {
		return no_page;
	}
	public void setNo_page(int no_page) {
		this.no_page = no_page;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getColored() {
		return colored;
	}
	public void setColored(Boolean colored) {
		this.colored = colored;
	}
}
