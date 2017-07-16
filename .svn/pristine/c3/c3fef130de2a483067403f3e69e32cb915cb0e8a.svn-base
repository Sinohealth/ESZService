package com.sinohealth.eszservice.service.visit.exception;

/**
 * 找不到随访项的异常
 * 
 * @author 黄世莲
 * 
 */
public class VisitItemNotFoundExecption extends Exception {

	private static final long serialVersionUID = 1167790746474492092L;

	private int errCode;

	private String message;

	private int itemId;

	public VisitItemNotFoundExecption() {
		super();
	}

	public VisitItemNotFoundExecption(int itemId, String message, int errCode) {
		super(message);
		this.errCode = errCode;
		this.message = message;
		this.itemId = itemId;
	}

	public VisitItemNotFoundExecption(int itemId, String message) {
		super(message);
		this.message = message;
		this.itemId = itemId;
	}

	public VisitItemNotFoundExecption(int itemId) {
		this.itemId = itemId;
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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

}
