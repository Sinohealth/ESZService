package com.sinohealth.eszservice.service.news.exception;

import java.io.Serializable;

public class NoPublishOrDeleteException extends Exception implements Serializable {

	private static final long serialVersionUID = 7216540227427522993L;

	
	private int errCode;
	
	private String message;

	
	public NoPublishOrDeleteException() {
		super();
	}
	
	public NoPublishOrDeleteException(int errCode, String message) {
		super();
		this.errCode = errCode;
		this.message = message;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
