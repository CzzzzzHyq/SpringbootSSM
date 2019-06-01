package com.train.shop.tools;

public class CustomerException extends Exception{
	
	private String message;
	public CustomerException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
