package com.flow.file;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiMessage {
	
	private String message;
	private String data;
	
	public ApiMessage(String msg){
		this.message 	= msg;
	}
	
	public ApiMessage(String msg,String data){
		this.message 	= msg;
		this.data 		= data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}
}
