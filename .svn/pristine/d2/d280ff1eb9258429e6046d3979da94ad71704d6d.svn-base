package com.sinohealth.eszservice.dao.visit.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.ITemplateDao;

@Repository
public class TemplateDaoImpl extends SimpleBaseDao<TemplateEntity, Integer>
		implements ITemplateDao {

	public TemplateDaoImpl() {
		super(TemplateEntity.class);
	}

	@Override
	public List<TemplateEntity> findTemplateByParam(int doctorId, String szSubject,
			String templName) {
		String hql = "From TemplateEntity where doctorId =:p1 and szSubject =:p2 and templName =:p3";
		return findByHql(hql, new Parameter(doctorId,szSubject,templName));
	}


}
