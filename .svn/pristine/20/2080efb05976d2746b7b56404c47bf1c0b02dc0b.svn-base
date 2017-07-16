package com.sinohealth.eszservice.service.base.impl;

import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.base.TitleEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.service.base.ITitleService;

@Service
public class TitleServiceImpl extends SimpleBaseDao<TitleEntity, String> implements
		ITitleService {

	public TitleServiceImpl() {
		super(TitleEntity.class);
	}

	@Override
	public TitleEntity getByTitle(String title) {
		try {
			return findUniqueBy("titleName", title);
		} catch (Exception e) {
			return null;
		}
		
	}

}
