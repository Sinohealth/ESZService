package com.sinohealth.eszservice.dao.doctor;

import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorCountKey;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

public interface IDoctorCountDao extends
		IGenericDao<DoctorCountEntity, DoctorCountKey> {

	/**
	 * 更新用户积分
	 * 
	 * @param userId
	 */
	public void updateTotalGrade(int userId, String szSubject);

	/**
	 * 更新医生个人模板总数
	 * 
	 * @param userId
	 * @param szSubject
	 */
	public void updateTemplCount(int userId, String szSubject);

	/**
	 * 更新医生随访中状态的随访总数
	 * 
	 * @param szSubject
	 * 
	 */
	void updateVisitCount(int userId, String szSubject);

	public DoctorCountEntity get(int userId, String szSubject);

}
