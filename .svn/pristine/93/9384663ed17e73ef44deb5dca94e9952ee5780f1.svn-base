package com.sinohealth.eszservice.dao.news.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.news.NewsEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.news.INewsDao;

@Repository("newsDao")
public class NewsDaoImpl extends SimpleBaseDao<NewsEntity, BigInteger> implements INewsDao {

	public NewsDaoImpl() {
		super(NewsEntity.class);
	}

	@Override
	public List<NewsEntity> findNewsByHql(String sqlStr,Integer pageSize,
			Parameter map) {
		return findByPagesize(sqlStr, pageSize, map);
	}


}
