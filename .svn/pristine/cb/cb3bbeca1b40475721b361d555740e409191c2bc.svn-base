package com.sinohealth.eszservice.dto.visit.elem;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApplicationElem implements Serializable {

	private static final long serialVersionUID = 7548131088297457334L;

	private int applyId;
	
	private Date applyTime;
	
	private Date expFinishTime;
	
	private Date finishTime;
	
	private int visitStatus;
	
	private String szSubject;

	public int getApplyId() {
		return applyId;
	}

	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getExpFinishTime() {
		return expFinishTime;
	}

	public void setExpFinishTime(Date expFinishTime) {
		this.expFinishTime = expFinishTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public int getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(int visitStatus) {
		this.visitStatus = visitStatus;
	}

	public String getSzSubject() {
		return szSubject;
	}

	public void setSzSubject(String szSubject) {
		this.szSubject = szSubject;
	}
	
}
