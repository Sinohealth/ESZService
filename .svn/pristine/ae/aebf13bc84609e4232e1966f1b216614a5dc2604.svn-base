package com.sinohealth.eszservice.service.visit.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.ISzSubjectDao;
import com.sinohealth.eszservice.service.visit.ISzSubjectService;

@Service
public class SzSubjectServiceImpl implements ISzSubjectService {

	@Autowired
	ISzSubjectDao dao;

	@Override
	public List<SzSubjectEntity> getListByIds(String szSubjects) {
		if ((null == szSubjects) || ("".equals(szSubjects))) {
			return null;
		}

		return getListByIds(szSubjects.split(","));
	}

	public List<SzSubjectEntity> getListByIds(String... szSubjects) {
		StringBuffer hql = new StringBuffer("FROM SzSubjectEntity ");
		Parameter params = new Parameter();

		if ((null != szSubjects) && (szSubjects.length > 0)) {
			hql.append("WHERE id in (");
			for (int i = 0; i < szSubjects.length; i++) {
				if (i > 0) {
					hql.append(",");
				}
				hql.append(":p" + i);
				params.put("p" + i, szSubjects[i]);
			}
			hql.append(")");
		}

		return dao.findByHql(hql.toString(), params);
	}

	@Override
	public SzSubjectEntity get(String szSubjectId) {
		return dao.get(szSubjectId);
	}
}
