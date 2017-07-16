package com.sinohealth.eszservice.common.dto;

import com.sinohealth.eszservice.common.config.ErrorMessage;

/**
 * 基本的错误问题，会从配置文件中获取相应的错误描述
 * 
 * @author 黄世莲
 * 
 */
public class BaseErrDto extends BaseDto {

	private static final long serialVersionUID = 3269270721906400920L;

	public BaseErrDto() {
		super();
	}

	public BaseErrDto(int errCode) {
		super(errCode);
		String msg = ErrorMessage.getConfig(errCode);
		if (null != msg) {
			super.setErrMsg(msg);
		}
	}

	public BaseErrDto(String errCode) {
		int code = Integer.parseInt(errCode);
		super.setErrCode(code);
		String msg = ErrorMessage.getConfig(code);
		if (null != msg) {
			super.setErrMsg(msg);
		}
	}

}
