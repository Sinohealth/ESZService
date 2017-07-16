package com.sinohealth.eszservice.dto.visit.sick;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinohealth.eszorm.entity.visit.personal.PersonalHistory;

public class PersonalHis implements Serializable {

	private static final long serialVersionUID = -8661068284889974456L;

	private Date updateTime;

	private PersonalHistory data = new PersonalHistory();

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public PersonalHistory getData() {
		return data;
	}

	public void setData(PersonalHistory data) {
		this.data = data;
	}
}
