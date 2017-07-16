package com.sinohealth.eszservice.dto.visit;

import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 3.4.10 获取随访模板列表
 * 
 * @author 黄世莲
 * 
 */
public class TemplSaveDto extends BaseDto {

	private static final long serialVersionUID = 8231118467522480552L;

	private int templId;

	public int getTemplId() {
		return templId;
	}

	public void setTemplId(int templId) {
		this.templId = templId;
	}

}
