package com.sinohealth.eszservice.service.visit.exception;

import com.sinohealth.eszorm.entity.visit.VisitItemEntity;

/**
 * 
 * 填写的随访项字符长度超出范围
 * 
 * @author 黄世莲
 * 
 */
public class StringValueLengthException extends RuntimeException {

	private static final long serialVersionUID = 2411055520262023863L;

	private VisitItemEntity item;

	private String value;

	private int min;

	private int max;

	public StringValueLengthException(VisitItemEntity item, String value,
			int min, int max) {
		super();
		this.item = item;
		this.value = value;
		this.min = min;
		this.max = max;
	}

	public VisitItemEntity getItem() {
		return item;
	}

	public void setItem(VisitItemEntity item) {
		this.item = item;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

}
