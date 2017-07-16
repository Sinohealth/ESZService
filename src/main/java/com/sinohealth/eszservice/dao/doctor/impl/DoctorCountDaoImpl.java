package com.sinohealth.eszservice.dao.doctor.impl;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorCountKey;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.doctor.IDoctorCountDao;

@Repository
public class DoctorCountDaoImpl extends
		SimpleBaseDao<DoctorCountEntity, DoctorCountKey> implements
		IDoctorCountDao {

	public DoctorCountDaoImpl() {
		super(DoctorCountEntity.class);
	}

	@Autowired
	private StringRedisTemplate strRedisTmpl;

	@Override
	public void updateTotalGrade(int userId, String szSubject) {
		String queryString = "UPDATE DoctorCountEntity SET totalGrade= "
				+ " IFNULL((SELECT SUM(actionGrade) FROM GradeEntity WHERE userId=:doctorId "
				+ " AND szSubject=:szSubject), 0) "
				+ " WHERE doctorId=:doctorId AND szSubject=:szSubject";
		Query query = getSession().createQuery(queryString);
		query.setParameter("doctorId", userId);
		query.setParameter("szSubject", szSubject);
		query.executeUpdate();

		DoctorCountKey key = new DoctorCountKey(userId, szSubject);
		DoctorCountEntity count = super.get(key);
		getSession().refresh(count); // 重新查询

		// TODO 同时更新缓存
	}

	@Override
	public void updateVisitCount(int userId, String szSubject) {
		String queryString = "UPDATE DoctorCountEntity dc SET dc.visitingCount= "
				+ " IFNULL((SELECT COUNT(*) FROM ApplicationEntity WHERE doctor.id=dc.doctorId "
				+ " AND visitStatus=:APPLY_VISITING AND szSubject.id=dc.szSubject), 0) "
				+ " , dc.pendingCount = "
				+ "IFNULL((SELECT COUNT(*) FROM ApplicationEntity WHERE doctor.id=dc.doctorId "
				+ " AND visitStatus=:APPLY_PENDING AND szSubject.id=dc.szSubject), 0) "
				+ " , urgentSickCount ="
				+ " IFNULL((SELECT COUNT(DISTINCT sick.id) FROM ApplicationEntity WHERE doctor.id=dc.doctorId "
				+ " AND curPhase.reportStatus=:DATA_URGENT AND szSubject.id=dc.szSubject AND visitStatus=:APPLY_VISITING ) , 0)"
				+ " , exitedCount= "
				+ " IFNULL((SELECT COUNT(*) FROM ApplicationEntity WHERE doctor.id=dc.doctorId "
				+ " AND visitStatus=:APPLY_EXITED AND szSubject.id=dc.szSubject), 0) "
				+ " , rejectedCount= "
				+ " IFNULL((SELECT COUNT(*) FROM ApplicationEntity WHERE doctor.id=dc.doctorId "
				+ " AND visitStatus=:APPLY_REJECTED AND szSubject.id=dc.szSubject), 0) "
				+ " WHERE dc.doctorId=:doctorId AND dc.szSubject=:szSubject";
		Query query = getSession().createQuery(queryString);
		query.setParameter("doctorId", userId);
		query.setParameter("APPLY_VISITING", VisitStatus.APPLY_VISITING);
		query.setParameter("APPLY_PENDING", VisitStatus.APPLY_PENDING);
		query.setParameter("DATA_URGENT", VisitStatus.DATA_URGENT);
		query.setParameter("APPLY_VISITING", VisitStatus.APPLY_VISITING);
		query.setParameter("APPLY_EXITED", VisitStatus.APPLY_EXITED);
		query.setParameter("APPLY_REJECTED", VisitStatus.APPLY_REJECTED);
		query.setParameter("szSubject", szSubject);
		query.executeUpdate();

		DoctorCountKey key = new DoctorCountKey(userId, szSubject);
		DoctorCountEntity count = super.get(key);
		if (null != count) {
			getSession().refresh(count); // 重新查询
		}

		// TODO 同时更新缓存
	}

	@Override
	public void updateTemplCount(int userId, String szSubject) {
		String queryString = "UPDATE DoctorCountEntity SET templCount= "
				+ " IFNULL((SELECT COUNT(*) FROM TemplateEntity WHERE doctorId=:doctorId AND szSubject=:szSubject AND visible=1),0) "
				+ " WHERE doctorId=:doctorId AND szSubject=:szSubject";
		Query query = getSession().createQuery(queryString);
		query.setParameter("doctorId", userId);
		query.setParameter("szSubject", szSubject);
		query.executeUpdate();

		DoctorCountKey key = new DoctorCountKey(userId, szSubject);
		DoctorCountEntity count = super.get(key);
		getSession().refresh(count); // 重新查询

		// TODO 同时更新缓存

	}

	@Override
	public DoctorCountEntity get(int userId, String szSubject) {
		DoctorCountKey key = new DoctorCountKey(userId, szSubject);
		return super.get(key);
	}
}
