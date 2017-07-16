package com.sinohealth.eszservice.service.base.impl;

import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.base.DisciplineEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.service.base.IDisciplineService;

@Service
public class DisciplineServiceImpl extends SimpleBaseDao<DisciplineEntity, Integer> implements IDisciplineService {

	public DisciplineServiceImpl() {
		super(DisciplineEntity.class);
	}

	@Override
	public DisciplineEntity getById(int disciplineId) {
		return get(disciplineId);
	}

}
