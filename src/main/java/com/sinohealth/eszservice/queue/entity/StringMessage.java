package com.sinohealth.eszservice.queue.entity;

/**
 * 用于更新医生的积分统计的实体
 * 
 * @author 黄世莲
 * 
 */
public class StringMessage extends BaseMessage {

	private static final long serialVersionUID = 6704525618841954399L;

	private String handlerName = "stringHandler";

	private String text;

	@Override
	public String getHandlerName() {
		return handlerName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
