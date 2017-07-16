package com.sinohealth.eszservice.dao.visit.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IApplicationDao;

@Repository("applicationDao")
public class ApplicationDaoImpl extends
		SimpleBaseDao<ApplicationEntity, Integer> implements IApplicationDao {

	public ApplicationDaoImpl() {
		super(ApplicationEntity.class);
	}

	@Override
	public PaginationSupport getApplicationReport(int doctorId, String subject,
			int status, int page, int pageSize) {
		int startIndex = (page - 1) * pageSize;
		String hql = "from ApplicationEntity where doctor.id=:doctorId and szSubject.id="
				+ ":szSubject and visitStatus =:status order by applyTime desc, finishTime desc";
		Parameter params = new Parameter();
		params.put("doctorId", doctorId);
		params.put("szSubject", subject);
		params.put("status", status);
		return findPageByHql(hql, startIndex, pageSize, params);
	}

	@Override
	public List getStatusCountByDoctorId(int doctorId, String szSubject) {
		String hql = "SELECT COUNT(*) c, visit_status s from visit_applications where doctor_id=:doctorId and sz_subject = :szSubject GROUP BY visit_status";

		Query query = getSession().createSQLQuery(hql).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("doctorId", doctorId);
		query.setParameter("szSubject", szSubject);

		List list = query.list();

		return list;
	}

	@Override
	public List<ApplicationEntity> getStatusBySickId(int sickId) {
		Parameter params = new Parameter();
		String hql = "from ApplicationEntity where sick.id=:sickId AND visitStatus < :recommitStatus ORDER BY applyTime DESC";
		params.put("sickId", sickId);
		params.put("recommitStatus", VisitStatus.APPLY_RECOMMITED);
		return findByHql(hql, params);
	}

}
