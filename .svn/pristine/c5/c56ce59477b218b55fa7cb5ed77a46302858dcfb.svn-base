package com.sinohealth.eszservice.queue.entity;

/**
 * 用于更新积分统计的实体
 * 
 * @author 黄世莲
 * 
 */
public class DoctorVisitCountMessage extends BaseMessage {

	private static final long serialVersionUID = 4246757244718667802L;

	private int doctorId;

	private String szSubject;

	private String handlerName = "doctorVisitCountHandler";

	@Override
	public String getHandlerName() {
		return handlerName;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public String getSzSubject() {
		return szSubject;
	}

	public void setSzSubject(String szSubject) {
		this.szSubject = szSubject;
	}

	@Override
	public String toString() {
		return "DoctorVisitCountMessage [doctorId=" + doctorId + ", szSubject="
				+ szSubject + ", handlerName=" + handlerName + "]";
	}

}
