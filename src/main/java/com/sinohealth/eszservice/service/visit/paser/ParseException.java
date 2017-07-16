package com.sinohealth.eszservice.service.visit.paser;

/**
 * 模板内容解析异常
 * 
 * @author 黄世莲
 * 
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = -1375628718330115124L;

	private int code;

	public ParseException(int code) {
		super();
		this.code = code;
	}

	public ParseException() {
		super();
	}

	public ParseException(String msg) {
		super(msg);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
