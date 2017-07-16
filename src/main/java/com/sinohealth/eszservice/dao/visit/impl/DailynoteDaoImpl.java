package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IDailynoteDao;

@Repository
public class DailynoteDaoImpl extends SimpleBaseDao<DailynoteEntity, Integer>
		implements IDailynoteDao {

	public DailynoteDaoImpl() {
		super(DailynoteEntity.class);
	}

}
