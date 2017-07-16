package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IBodySignValueDao;

@Repository
public class BodySignValueDaoImpl extends
		SimpleBaseDao<BodySignValueEntity, Integer> implements
		IBodySignValueDao {

	public BodySignValueDaoImpl() {
		super(BodySignValueEntity.class);
	}

}
