package com.sinohealth.eszservice.dao.base.impl;

import org.hibernate.NonUniqueResultException;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.base.GradeItemEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.base.IGradeItemDao;

@Repository("gradeItemDao")
public class GradeItemDaoImpl extends SimpleBaseDao<GradeItemEntity, Integer>
		implements IGradeItemDao {

	public GradeItemDaoImpl() {
		super(GradeItemEntity.class);
	}

	@Override
	public GradeItemEntity getByKey(String key) {
		try {
			return findUniqueBy("gradeKey", key);
		} catch (NonUniqueResultException e) { // 找不到此项积分项
			return null;
		}
	}
}
