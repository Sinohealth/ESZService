package com.sinohealth.eszservice.service.visit.exception;

/**
 * 
 * 填写的随访项值超出范围的异常
 * 
 * @author 黄世莲
 * 
 */
public class ValueOutOfRangeException extends Exception {

	private static final long serialVersionUID = -5708017146482998224L;

	private int itemId;
	private String value;

	/**
	 * @param itemId
	 *            随访项ID
	 * @param value
	 *            值
	 */
	public ValueOutOfRangeException(int itemId, String value) {
		this.itemId = itemId;
		this.value = value;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
