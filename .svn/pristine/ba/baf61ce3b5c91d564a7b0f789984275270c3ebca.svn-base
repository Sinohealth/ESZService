package com.sinohealth.eszservice.queue.entity;

public class CaseHistoryRateMessage extends BaseMessage {

	private static final long serialVersionUID = 7356632627918693327L;

	private int applyId;

	private String handlerName = "caseHistoryRateHandler";

	public CaseHistoryRateMessage(int applyId) {
		super();
		this.applyId = applyId;
	}

	@Override
	public String getHandlerName() {
		return handlerName;
	}

	public int getApplyId() {
		return applyId;
	}

	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}

}
