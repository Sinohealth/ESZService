package com.sinohealth.eszservice.dao.base.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.base.HospitalEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.base.IHospitalDao;

@Repository("hospitalDao")
public class HospitalDaoImpl extends SimpleBaseDao<HospitalEntity, Integer>
		implements IHospitalDao {
	public HospitalDaoImpl() {
		super(HospitalEntity.class);
	}

	@Override
	public HospitalEntity getByName(String name) {
		return findUniqueBy("hospitalName", name);
	}

}
