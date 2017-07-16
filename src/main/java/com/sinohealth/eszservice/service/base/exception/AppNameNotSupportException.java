package com.sinohealth.eszservice.service.base.exception;

/**
 * 错误的AppName
 * 
 * @author 黄世莲
 * 
 */
public class AppNameNotSupportException extends RuntimeException {

	private static final long serialVersionUID = 5816563347839220340L;

	private String message;

	private String appName;

	public AppNameNotSupportException(String message, String appName) {
		super();
		this.message = message;
		this.appName = appName;
	}

	public AppNameNotSupportException(String appName) {
		super();
		this.appName = appName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
