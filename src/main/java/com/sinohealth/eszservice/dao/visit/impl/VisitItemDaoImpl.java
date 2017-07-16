package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IVisitItemDao;

@Repository
public class VisitItemDaoImpl extends SimpleBaseDao<VisitItemEntity, Integer>
		implements IVisitItemDao {

	public VisitItemDaoImpl() {
		super(VisitItemEntity.class);
	}

}
