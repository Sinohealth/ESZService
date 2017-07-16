package com.sinohealth.eszservice.dao.base.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.base.ProvinceEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.base.IProvinceDao;

@Repository("provinceDao")
public class ProvinceDaoImpl extends SimpleBaseDao<ProvinceEntity, Integer>
		implements IProvinceDao {
	public ProvinceDaoImpl() {
		super(ProvinceEntity.class);
	}

	@Override
	public ProvinceEntity getByName(String name) {
		return findUniqueBy("provinceName", name);
	}

}
