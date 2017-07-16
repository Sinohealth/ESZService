package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.FinishedReportEntity;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IFinishedReportDao;

@Repository("finishedReportDao")
public class FinishedReportDaoImpl extends
		SimpleBaseDao<FinishedReportEntity, Integer> implements
		IFinishedReportDao {

	public FinishedReportDaoImpl() {
		super(FinishedReportEntity.class);
	}

	@Override
	public PaginationSupport getFinishedReport(int doctorId, String subject,
			int status, int page, int pageSize) {
		int startIndex = (page - 1) * pageSize;
		System.out.println("startIndex:"+startIndex+" page:"+page+" pageSize:"+pageSize);
		String hql = "from FinishedReportEntity where doctorId=:doctorId and szSubject="
				+ ":szSubject and visitStatus =:visitStatus order by finishTime desc";
		Parameter params = new Parameter();
		params.put("doctorId", doctorId);
		params.put("szSubject", subject);
		params.put("visitStatus", status);
		return findPageByHql(hql, startIndex, pageSize, params);
	}

}
