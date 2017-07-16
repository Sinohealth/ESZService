package com.sinohealth.eszservice.service.visit;

import java.util.List;

import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;

public interface ISzSubjectService {

	List<SzSubjectEntity> getListByIds(String szSubjects);

	SzSubjectEntity get(String szSubjectId);
}
