package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class SzSubjectsDto extends BaseDto {

	private static final long serialVersionUID = -6063868833577466781L;

	@JsonUnwrapped(prefix = "szSubject")
	private List<SzSubjectEntity> szSubjects = new ArrayList<>();

	private String disables = "";

	public List<SzSubjectEntity> getSzSubjects() {
		return szSubjects;
	}

	public void setSzSubjects(List<SzSubjectEntity> szSubjects) {
		this.szSubjects = szSubjects;
	}

	public String getDisables() {
		return disables;
	}

	public void setDisables(String disables) {
		this.disables = disables;
	}

}
