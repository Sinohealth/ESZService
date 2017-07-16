package com.sinohealth.eszservice.service.doctor.exception;

/**
 * 账号重复异常
 * 
 * @author 黄世莲
 * 
 */
public class AccountDuplicateException extends Exception {

	private static final long serialVersionUID = 1167790746474492092L;

	private int errCode;

	private String message;
	
	public AccountDuplicateException() {
		super();
	}

	public AccountDuplicateException(String message, int errCode) {
		super(message);
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
