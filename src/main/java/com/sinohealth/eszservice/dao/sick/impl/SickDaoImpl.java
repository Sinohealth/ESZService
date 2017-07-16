package com.sinohealth.eszservice.dao.sick.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.sick.ISickDao;

@Repository("sickDao")
public class SickDaoImpl extends SimpleBaseDao<SickEntity, Integer> implements
		ISickDao {

	public SickDaoImpl() {
		super(SickEntity.class);
	}

	@Override
	public SickEntity findByMobile(String mobile) {
		SickEntity entity = findByAccount(mobile, "mobile");

		return entity;
	}

	@Override
	public SickEntity findByEmail(String email) {
		SickEntity entity = findByAccount(email, "email");
		return entity;
	}

	@Override
	public SickEntity findByEmailCode(String emailCode) {
		SickEntity entity;
		entity = findUniqueBy("emailCode", emailCode);
		return entity;
	}

	public SickEntity findByAccount(String account, String type) {
		if ("email".equals(type)) {
			return findUniqueBy("email", account);
		} else {
			return findUniqueBy("mobile", account);
		}
	}

	@Override
	public void updateTotalGrade(int userId) {
		String queryString = "UPDATE SickEntity SET totalGrade=IFNULL((SELECT SUM(actionGrade) FROM GradeEntity WHERE userId=:userId "
				+ " ), 0) WHERE id=:userId";
		Query query = getSession().createQuery(queryString);
		query.setParameter("userId", userId);
		query.executeUpdate();

	}

}
