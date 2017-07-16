package com.sinohealth.eszservice.queue.entity;

import com.sinohealth.eszorm.entity.base.GradeEntity;

/**
 * 用于更新积分统计的实体
 * 
 * @author 黄世莲
 * 
 */
public class GradeMessage extends BaseMessage {

	private static final long serialVersionUID = 2581905592892599205L;

	private GradeEntity grade;

	private String handlerName = "gradeHandler";

	@Override
	public String getHandlerName() {
		return handlerName;
	}

	public GradeEntity getGrade() {
		return grade;
	}

	public void setGrade(GradeEntity grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "GradeMessage [grade=" + grade + ", handlerName=" + handlerName
				+ "]";
	}

}
