package com.sinohealth.eszservice.dto.visit.sick;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;

public class TimeLineBody implements Serializable {

	private static final long serialVersionUID = 2958795607926534100L;

	@JsonIgnoreProperties(value = { "doctorId", "stdTemplId", "stdTemplName",
			"beginLimit", "endLimit", "isRecommend", "diseaseId" })
	private TemplateEntity templ;

	private ApplicationEntity apply;

	private List<TemplatePhaseEntity> phases = Collections.emptyList();

	@JsonRawValue
	private String reportTabs = "[ ]";

	public TemplateEntity getTempl() {
		return templ;
	}

	public void setTempl(TemplateEntity templ) {
		this.templ = templ;
	}

	@JsonIgnoreProperties(value = { "pastHistory", "familyHistory",
			"prescription", "inspection", "examine", "personalHistory",
			"rateCount", "checks", "newApplyId" })
	public ApplicationEntity getApply() {
		return apply;
	}

	public void setApply(ApplicationEntity apply) {
		this.apply = apply;
	}

	@JsonIgnoreProperties(value = { "prescription", "itemIds", "checks" })
	public List<TemplatePhaseEntity> getPhases() {
		return phases;
	}

	public void setPhases(List<TemplatePhaseEntity> phases) {
		this.phases = phases;
	}

	public String getReportTabs() {
		return reportTabs;
	}

	public void setReportTabs(String reportTabs) {
		this.reportTabs = reportTabs;
	}

}
