package com.sinohealth.eszservice.dao.help.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.help.CategoryEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.help.ICategoryDao;

@Repository("categoryDao")
public class CategoryDaoImpl extends SimpleBaseDao<CategoryEntity, Integer> implements ICategoryDao {
	public CategoryDaoImpl() {
		super(CategoryEntity.class);
	}
}
