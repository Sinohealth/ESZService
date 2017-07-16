package com.sinohealth.eszservice.dto.visit;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class SavePlanDto extends BaseDto {

	private static final long serialVersionUID = 2757068159060044696L;

	private int templId;

	public int getTemplId() {
		return templId;
	}

	public void setTemplId(int templId) {
		this.templId = templId;
	}

}
