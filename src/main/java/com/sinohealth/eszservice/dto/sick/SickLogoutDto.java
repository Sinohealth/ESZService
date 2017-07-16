package com.sinohealth.eszservice.dto.sick;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class SickLogoutDto extends BaseDto {

	private static final long serialVersionUID = -808641694491722826L;

	private Integer userId;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
