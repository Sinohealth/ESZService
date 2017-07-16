package com.sinohealth.eszservice.common.dto;

public class BaseDto2<T> implements IGenericDto {
	private static final long serialVersionUID = -616311346535261537L;

	protected int errCode = 0;

	protected String errMsg = "";

	protected T body;

	/**
	 * 错误代码，一般来说，0表示成功执行。默认为0
	 * 
	 * @return
	 */
	public int getErrCode() {
		return errCode;
	}

	/**
	 * 错误代码，一般来说，0表示成功执行。默认为0
	 * 
	 * @return
	 */
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public BaseDto2(int errCode, String errMsg) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public BaseDto2(String errMsg) {
		super();
		this.errMsg = errMsg;
	}

	public BaseDto2(T body) {
		super();
		this.body = body;
	}

	public BaseDto2() {
		super();
	}

	public BaseDto2(int errCode) {
		this.errCode = errCode;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
