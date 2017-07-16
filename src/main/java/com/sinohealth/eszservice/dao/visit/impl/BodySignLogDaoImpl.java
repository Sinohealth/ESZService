package com.sinohealth.eszservice.dao.visit.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.BodySignLogEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IBodySignLogDao;

@Repository
public class BodySignLogDaoImpl extends
		SimpleBaseDao<BodySignLogEntity, Integer> implements IBodySignLogDao {

	public BodySignLogDaoImpl() {
		super(BodySignLogEntity.class);
	}

	@Override
	public BodySignLogEntity getLastLog(int templId, int itemId) {
		String hql = "FROM BodySignLogEntity WHERE templId=:p1 and itemId=:p2 ORDER BY startDate DESC";
		List<BodySignLogEntity> list = findByHql(hql, new Parameter(templId,
				itemId), 1);
		return ((null != list) && (list.size() > 0)) ? list.get(0) : null;
	}

	@Override
	public List<BodySignLogEntity> getList(int templId, int itemId) {
		String hql = "FROM BodySignLogEntity WHERE templId=:p1 and itemId=:p2 ORDER BY startDate ASC";
		return findByHql(hql, new Parameter(templId, itemId));
	}

	@Override
	public List<BodySignLogEntity> getList(int templId) {
		String hql = "FROM BodySignLogEntity WHERE templId=:p1 ORDER BY startDate ASC";
		return findByHql(hql, new Parameter(templId));
	}
}
