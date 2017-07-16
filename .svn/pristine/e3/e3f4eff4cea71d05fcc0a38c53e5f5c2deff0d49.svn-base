package com.sinohealth.eszservice.dao.news.impl;


import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.news.Version;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.news.IVersionDao;

@Repository("versionDao")
public class VersionDaoImpl extends SimpleBaseDao<Version, String> implements IVersionDao {

	public VersionDaoImpl() {
		super(Version.class);
	}

	/**
	 * 查找名字为newscols的版本号
	 */
	public Version findByVer(String newscols) {
		return get(newscols);
	}

}
