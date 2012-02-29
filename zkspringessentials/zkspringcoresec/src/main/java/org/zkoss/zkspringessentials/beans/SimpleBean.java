package org.zkoss.zkspringessentials.beans;

public class SimpleBean {

	private String message;
	
	public SimpleBean() {
		
	}
	
	public SimpleBean(String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
