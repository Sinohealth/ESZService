package com.sinohealth.eszservice.service.base.exception;

/**
 * 找不到相关的城市异常
 * 
 * @author 陈学宏
 * 
 */
public class NoCityFoundException extends Exception {

	private static final long serialVersionUID = 4173077323112497046L;
	
	private int errCode;
	
	private String message;

	public NoCityFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NoCityFoundException(int errCode, String message) {
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
