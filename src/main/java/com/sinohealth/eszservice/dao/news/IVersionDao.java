package com.sinohealth.eszservice.dao.news;

import com.sinohealth.eszorm.entity.news.Version;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

public interface IVersionDao extends IGenericDao<Version, String> {

	
	/**
	 * 根据ver找到Version的信息
	 * 
	 * @param account
	 */
	public Version findByVer(String newscols);
}
