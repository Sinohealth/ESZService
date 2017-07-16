package com.sinohealth.eszservice.service.doctor;

import java.util.List;

import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;

/**
 * 医生API接口service
 * 
 * @author 黄世莲
 * 
 */
public interface IDoctorCountService {

	/**
	 * 更新医生个人模板总数
	 * 
	 * @param doctorId
	 */
	void updateTemplCount(int doctorId, String szSubject);

	/**
	 * 更新医生随访中状态的随访总数
	 * 
	 */
	void updateVisitCount(Integer id, String szSubject);

	/**
	 * 获取医生此专科的统计
	 * 
	 * @param userId
	 * @param szSbuject
	 * @return
	 */
	DoctorCountEntity get(int userId, String szSbuject);

	/**
	 * 更新医生专科总积分统计
	 * 
	 * @param userId
	 * @param szSubject
	 */
	void updateTotalGrade(int userId, String szSubject);

	// void add(DoctorCountEntity count);

	DoctorCountEntity saveOrUpdate(DoctorCountEntity count);

	List<DoctorCountEntity> getDoctorCount(Integer doctorId);

	void add(int id, String szSubject);

	/**
	 * 是否强行执行
	 * 
	 * @param id
	 * @param szSubject
	 * @param force
	 */
	void updateVisitCount(Integer id, String szSubject, boolean force);
}
