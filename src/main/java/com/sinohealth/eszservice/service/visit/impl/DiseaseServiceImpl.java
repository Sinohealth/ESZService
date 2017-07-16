package com.sinohealth.eszservice.service.visit.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IDiseaseDao;
import com.sinohealth.eszservice.service.visit.IDiseaseService;

@Service
public class DiseaseServiceImpl implements IDiseaseService {
	@Autowired
	IDiseaseDao dao;

	@Override
	public DiseaseEntity get(String disease) {
		return dao.get(disease);
	}

	@Override
	public List<DiseaseEntity> getListBySzSubject(String szSubject) {
		String hql = "FROM DiseaseEntity WHERE szSubject.id=:p1";
		Parameter params = new Parameter(szSubject);
		return dao.findByHql(hql, params);
	}

}
