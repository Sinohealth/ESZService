package com.sinohealth.eszservice.dao.news;

import java.math.BigInteger;
import java.util.List;

import com.sinohealth.eszorm.entity.news.NewsEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;
import com.sinohealth.eszservice.common.persistence.Parameter;

public interface INewsDao extends IGenericDao<NewsEntity, BigInteger> {

	/**
	 * 根据参数查询下一页新闻记录数
	 * 
	 * @param sqlStr
	 *            查询下一页资讯SQL语句
	 * 
	 * @param pageSize
	 *            本页的新闻记录数
	 * 
	 * @param map
	 *            查询参数
	 * 
	 * @return
	 */
	public List<NewsEntity> findNewsByHql(String sqlStr, Integer pageSize,
			Parameter params);
}
