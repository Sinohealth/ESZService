package com.sinohealth.eszservice.dao.visit;

import java.util.List;

import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

public interface ITemplateDao extends IGenericDao<TemplateEntity, Integer> {
	
	List<TemplateEntity> findTemplateByParam(int doctorId, String szSubject,String templName);
}
