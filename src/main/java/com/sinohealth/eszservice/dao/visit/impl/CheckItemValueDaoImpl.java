package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.ICheckItemValueDao;

@Repository
public class CheckItemValueDaoImpl extends
		SimpleBaseDao<CheckItemValueEntity, Integer> implements
		ICheckItemValueDao {

	public CheckItemValueDaoImpl() {
		super(CheckItemValueEntity.class);
	}

}
