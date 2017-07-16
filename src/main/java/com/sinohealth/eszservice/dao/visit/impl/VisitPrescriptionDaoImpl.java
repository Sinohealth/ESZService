package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IVisitPrescriptionDao;

@Repository
public class VisitPrescriptionDaoImpl extends
		SimpleBaseDao<VisitPrescriptionEntity, Integer> implements
		IVisitPrescriptionDao {

	public VisitPrescriptionDaoImpl() {
		super(VisitPrescriptionEntity.class);
	}

}
