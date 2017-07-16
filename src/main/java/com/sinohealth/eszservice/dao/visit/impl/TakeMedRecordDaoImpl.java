package com.sinohealth.eszservice.dao.visit.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.TakeMedRecordEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.ITakeMedRecordDao;

@Repository
public class TakeMedRecordDaoImpl extends
		SimpleBaseDao<TakeMedRecordEntity, Long> implements ITakeMedRecordDao {

	public TakeMedRecordDaoImpl() {
		super(TakeMedRecordEntity.class);
	}

	@Override
	public int getTakeRecordCount(int sickId, int takedFilter, Date startDate,
			Date endDate) {
		StringBuffer buf = new StringBuffer(
				"SELECT COUNT(*) FROM TakeMedRecordEntity WHERE sick.id=:sickId ");

		Parameter params = new Parameter();
		params.put("sickId", sickId);

		if (null != startDate) {
			buf.append(" AND curDate>:startDate ");
			params.put("startDate", startDate);
		}
		if (null != endDate) {
			buf.append(" AND curDate<:endDate ");
			params.put("endDate", endDate);
		}

		if (1 == takedFilter) {
			buf.append(" AND taked=:taked ");
			params.put("taked", 1);
		} else {
			buf.append(" AND taked=:taked ");
			params.put("taked", 0);
		}

		Query query = getSession().createQuery(buf.toString());
		if (null != params) {
			for (String s : params.keySet()) {
				query.setParameter(s, params.get(s));
			}
		}
		Number r = (Number) query.uniqueResult();

//		System.out.println("number:" + r);

		return r != null ? r.intValue() : 0;
	}
}
