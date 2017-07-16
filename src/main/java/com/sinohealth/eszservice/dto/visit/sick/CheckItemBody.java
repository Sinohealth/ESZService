package com.sinohealth.eszservice.dto.visit.sick;

import java.io.Serializable;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;

public class CheckItemBody implements Serializable {

	private static final long serialVersionUID = -2378046899077221421L;

	private List<CheckItemValueEntity> inspectionValues;

	private List<VisitImgEntity> checkPics;

	private List<VisitImgEntity> inspectionPics;

	private List<CheckItemValueEntity> checkValues;

	public List<CheckItemValueEntity> getInspectionValues() {
		return inspectionValues;
	}

	public void setInspectionValues(List<CheckItemValueEntity> inspectionValues) {
		this.inspectionValues = inspectionValues;
	}

	public List<VisitImgEntity> getCheckPics() {
		return checkPics;
	}

	public void setCheckPics(List<VisitImgEntity> checkPics) {
		this.checkPics = checkPics;
	}

	public List<VisitImgEntity> getInspectionPics() {
		return inspectionPics;
	}

	public void setInspectionPics(List<VisitImgEntity> inspectionPics) {
		this.inspectionPics = inspectionPics;
	}

	public List<CheckItemValueEntity> getCheckValues() {
		return checkValues;
	}

	public void setCheckValues(List<CheckItemValueEntity> checkValues) {
		this.checkValues = checkValues;
	}

}
