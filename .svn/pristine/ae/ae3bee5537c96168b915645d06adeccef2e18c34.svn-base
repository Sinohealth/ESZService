package com.sinohealth.eszservice.service.base.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.base.ProvinceEntity;
import com.sinohealth.eszservice.dao.base.IProvinceDao;
import com.sinohealth.eszservice.service.base.IProvinceService;

@Service
public class ProvinceServiceImpl implements IProvinceService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IProvinceDao provinceDao;

	@Override
	public ProvinceEntity getByName(String name) {
		return provinceDao.getByName(name);
	}

	@Override
	public ProvinceEntity getById(int provinceId) {
		return provinceDao.get(provinceId);
	}

	public IProvinceDao getProvinceDao() {
		return provinceDao;
	}

	public void setProvinceDao(IProvinceDao provinceDao) {
		this.provinceDao = provinceDao;
	}

}
