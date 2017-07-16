package com.sinohealth.eszservice.dto.visit.elem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.dto.visit.AppDetailsDto.SimpleApplicationStructs;

public class ApplyDetailsDtoElem implements Serializable {

	private static final long serialVersionUID = 601306879205291140L;

	private ObjectNode profile;

	private AppCasesComponent cases;

	private ObjectNode pastHistory;

	private ArrayNode familyHistory;

	private ObjectNode medicines;

	private Set<VisitImgEntity> items;

	private String reasonDesc;

	private int visitStatus;

	private String szSubjectId;

	private String szSubjectName;

	private String diseaseId;

	private String diseaseName;

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date applyTime;

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date finishTime;

	private List<ApplicationEntity> relations;

	public ObjectNode getProfile() {
		return profile;
	}

	public void setProfile(ObjectNode profile) {
		this.profile = profile;
	}

	public AppCasesComponent getCases() {
		return cases;
	}

	public void setCases(AppCasesComponent cases) {
		this.cases = cases;
	}

	public ObjectNode getPastHistory() {
		return pastHistory;
	}

	public void setPastHistory(ObjectNode pastHistory) {
		this.pastHistory = pastHistory;
	}

	public ObjectNode getMedicines() {
		return medicines;
	}

	public void setMedicines(ObjectNode medicines) {
		this.medicines = medicines;
	}

	public Set<VisitImgEntity> getItems() {
		return items;
	}

	public void setItems(Set<VisitImgEntity> items) {
		this.items = items;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public int getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(int visitStatus) {
		this.visitStatus = visitStatus;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public List<SimpleApplicationStructs> getRelations() {
		List<SimpleApplicationStructs> list = new ArrayList<>();
		for (ApplicationEntity e : relations) {
			list.add(new SimpleApplicationStructs(e));
		}
		return list;
	}

	public void setRelations(List<ApplicationEntity> relations) {
		this.relations = relations;
	}

	public ArrayNode getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(ArrayNode familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getSzSubjectId() {
		return szSubjectId;
	}

	public void setSzSubjectId(String szSubjectId) {
		this.szSubjectId = szSubjectId;
	}

	public String getSzSubjectName() {
		return szSubjectName;
	}

	public void setSzSubjectName(String szSubjectName) {
		this.szSubjectName = szSubjectName;
	}

	public String getDiseaseId() {
		return diseaseId;
	}

	public void setDiseaseId(String diseaseId) {
		this.diseaseId = diseaseId;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

}
