package com.sinohealth.eszservice.dao.news.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.news.NewsColsEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.news.INewscolsDao;

@Repository("newscolsDao")
public class NewscolsDaoImpl extends SimpleBaseDao<NewsColsEntity, Integer>
		implements INewscolsDao {

	public NewscolsDaoImpl() {
		super(NewsColsEntity.class);
	}

	@Override
	public List<NewsColsEntity> findNewsByValidInt(Integer isValid, String appName) {
		String hql = "from NewsColsEntity where isValid =:isValid and delFlag='0' and parent.appName =:appName order by ord asc ";
		Parameter params = new Parameter();
		params.put("isValid", 1);
		params.put("appName", appName.replace("ForAndroid", "").replace("ForIOS", ""));
		return findByHql(hql, params);
	}

}
