package com.sinohealth.eszservice.dao.news;

import java.util.List;

import com.sinohealth.eszorm.entity.news.NewsColsEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;
import com.sinohealth.eszservice.common.persistence.Parameter;

public interface INewscolsDao extends IGenericDao<NewsColsEntity, Integer> {

	/**
	 * 找到NewsColsEntity所有信息
	 * 
	 * @param account
	 */
	public List<NewsColsEntity> findNewsByValidInt(Integer isValid, String appName);

}
