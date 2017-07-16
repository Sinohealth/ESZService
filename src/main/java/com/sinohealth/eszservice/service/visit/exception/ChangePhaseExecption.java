package com.sinohealth.eszservice.service.visit.exception;

/**
 * 更新阶段异常
 * 
 * @author 黄世莲
 * 
 */
public class ChangePhaseExecption extends Exception {

	private static final long serialVersionUID = 8482457673551580327L;

	private int timePoint;

	private int cycleUnit;

	private String message;

	public ChangePhaseExecption(int timePoint, int cycleUnit, String message) {
		super();
		this.timePoint = timePoint;
		this.cycleUnit = cycleUnit;
		this.message = message;
	}

	public int getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(int timePoint) {
		this.timePoint = timePoint;
	}

	public int getCycleUnit() {
		return cycleUnit;
	}

	public void setCycleUnit(int cycleUnit) {
		this.cycleUnit = cycleUnit;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
