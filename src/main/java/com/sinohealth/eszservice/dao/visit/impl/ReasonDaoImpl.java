package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.ReasonEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IReasonDao;

@Repository("reasonDao")
public class ReasonDaoImpl extends SimpleBaseDao<ReasonEntity, Integer> implements IReasonDao {

	public ReasonDaoImpl() {
		super(ReasonEntity.class);
		// TODO Auto-generated constructor stub
	}
	
}
