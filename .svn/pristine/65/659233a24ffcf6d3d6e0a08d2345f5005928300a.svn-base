package com.sinohealth.eszservice.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.base.DictEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.base.IDictDao;
import com.sinohealth.eszservice.service.base.IDictService;

@Service
public class DictServiceImpl implements IDictService {

	@Autowired
	private IDictDao dao;

	@Override
	public DictEntity getByLabel(String label, String type) {
		Parameter params = new Parameter();
		params.put("label", label);
		params.put("type", type);
		params.put("delFlag", "0");
		List<DictEntity> list = dao
				.findByHql(
						"FROM DictEntity WHERE label=:label AND type=:type AND delFlag=:delFlag",
						params, 1);
		if ((null != list) && (list.size() > 0)) {
			return list.get(0);
		}
		return null;
	}

}
