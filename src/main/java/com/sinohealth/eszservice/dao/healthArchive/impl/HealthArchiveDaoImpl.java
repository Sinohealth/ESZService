package com.sinohealth.eszservice.dao.healthArchive.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.healthArchive.IHealthArchiveDao;

@Repository("healthArchiveDao")
public class HealthArchiveDaoImpl extends SimpleBaseDao<HealthArchiveLog, Long> implements IHealthArchiveDao {

	public HealthArchiveDaoImpl() {
		super(HealthArchiveLog.class);
	}

}
