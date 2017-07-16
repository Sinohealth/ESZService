package com.sinohealth.eszservice.service.sick.exception;

/**
 * 注册失败异常
 * 
 * @author 黄世莲
 * 
 */
public class RegisterException extends Exception {

	private static final long serialVersionUID = 1167790746474492092L;

	private int errCode;

	public RegisterException() {
		super();
	}

	public RegisterException(String message) {
		super(message);
	}

	public RegisterException(String message, int errorCode) {
		super(message);
		this.errCode = errorCode;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

}
