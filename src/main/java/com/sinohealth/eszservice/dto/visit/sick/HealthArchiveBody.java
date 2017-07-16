package com.sinohealth.eszservice.dto.visit.sick;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HealthArchiveBody implements Serializable {

	private static final long serialVersionUID = -2378046899077221421L;

	private Date pastHistoryDate;

	private Date casesDate;

	private Date prescribeDate;

	private Date checkItemDate;

	private Date checkPicDate;

	private Date bodySignDate;

	private Date familyHistoryDate;

	private Date personalHistoryDate;

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getPastHistoryDate() {
		return pastHistoryDate;
	}

	public void setPastHistoryDate(Date pastHistoryDate) {
		this.pastHistoryDate = pastHistoryDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getCasesDate() {
		return casesDate;
	}

	public void setCasesDate(Date casesDate) {
		this.casesDate = casesDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getPrescribeDate() {
		return prescribeDate;
	}

	public void setPrescribeDate(Date prescribeDate) {
		this.prescribeDate = prescribeDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getCheckItemDate() {
		return checkItemDate;
	}

	public void setCheckItemDate(Date checkItemDate) {
		this.checkItemDate = checkItemDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getCheckPicDate() {
		return checkPicDate;
	}

	public void setCheckPicDate(Date checkPicDate) {
		this.checkPicDate = checkPicDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getBodySignDate() {
		return bodySignDate;
	}

	public void setBodySignDate(Date bodySignDate) {
		this.bodySignDate = bodySignDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getFamilyHistoryDate() {
		return familyHistoryDate;
	}

	public void setFamilyHistoryDate(Date familyHistoryDate) {
		this.familyHistoryDate = familyHistoryDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getPersonalHistoryDate() {
		return personalHistoryDate;
	}

	public void setPersonalHistoryDate(Date personalHistoryDate) {
		this.personalHistoryDate = personalHistoryDate;
	}

}
