package com.sinohealth.eszservice.dao.base.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.base.CityEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.base.ICityDao;

@Repository("cityDao")
public class CityDaoImpl extends SimpleBaseDao<CityEntity, Integer> implements
		ICityDao {

	public CityDaoImpl() {
		super(CityEntity.class);
		// TODO Auto-generated constructor stub
	}

}
