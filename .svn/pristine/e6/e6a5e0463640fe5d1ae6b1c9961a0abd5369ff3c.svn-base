package com.sinohealth.eszservice.dao.doctor.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.doctor.IDoctorDao;

@Repository("doctorDao")
public class DoctorDaoImpl extends SimpleBaseDao<DoctorEntity, Integer>
		implements IDoctorDao {

	public DoctorDaoImpl() {
		super(DoctorEntity.class);
	}

	@Override
	public DoctorEntity findByMobile(String mobile) {
		DoctorEntity entity = findByAccount(mobile, "mobile");

		return entity;
	}

	@Override
	public DoctorEntity findByEmail(String email) {
		DoctorEntity entity = findByAccount(email, "email");

		return entity;
	}

	public DoctorEntity findByAccount(String account, String type) {
		if ("email".equals(type)) {
			return findUniqueBy("email", account);
		} else {
			return findUniqueBy("mobile", account);
		}
	}

	/*
	 * public DoctorEntity findByAccount(String account, String type) { String
	 * hql = ""; if ("email".equals(type)) { hql =
	 * "From DoctorEntity where email=:p1 and delFlag=0 and status=1"; } else {
	 * hql = "From DoctorEntity where mobile=:p1 and delFlag=0 and status=1"; }
	 * DoctorEntity doctor = getByHql(hql, new Parameter(account)); return
	 * doctor; }
	 */

	@Override
	public DoctorEntity findByEmailCode(String emailCode) {
		DoctorEntity entity;
		entity = findUniqueBy("emailCode", emailCode);
		return entity;
	}

	/**
	 * 重写 save方法，如果是更新，同时也更新缓存。如果是添加，也将添加的数据缓存到redis
	 * 
	 * @see com.sinohealth.eszservice.common.persistence.SimpleBaseDao#save(java.lang.Object)
	 * 
	 */
	@Override
	public DoctorEntity save(DoctorEntity o) {
		super.save(o);
		if (null != o.getId()) { // 更新的操作
			return o;
		} else {
			String mobile = o.getMobile();
			String email = o.getEmail();

			DoctorEntity added;
			if (null != mobile) {
				added = findUniqueBy("mobile", mobile);
			} else {
				added = findUniqueBy("email", email);
			}
			return added;
		}
	}

	@Override
	public List<DoctorEntity> searchDoctor(String hql, Parameter params) {
		return findByHql(hql, params);
	}

	@Override
	public DoctorEntity findByRecommendCode(String recommendCode) {
		String hql = "From DoctorEntity where recommendCode=:p1";
		return getByHql(hql, new Parameter(recommendCode));
	}
}
