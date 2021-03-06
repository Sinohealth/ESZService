package com.sinohealth.eszservice.service.visit.exception;

/**
 * 验证密码异常
 * 
 * @author 黄世莲
 * 
 */
public class SystemErrorExecption extends Exception {

	private static final long serialVersionUID = 1167790746474492092L;

	private int errCode;

	private String message;

	public SystemErrorExecption() {
		super();
	}

	public SystemErrorExecption(String message, int errCode) {
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
