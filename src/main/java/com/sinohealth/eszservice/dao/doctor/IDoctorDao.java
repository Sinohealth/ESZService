package com.sinohealth.eszservice.dao.doctor;

import java.util.List;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;
import com.sinohealth.eszservice.common.persistence.Parameter;

public interface IDoctorDao extends IGenericDao<DoctorEntity, Integer> {

	/**
	 * 根据mobile找到Doctor的信息
	 * 
	 * @param mobile
	 */
	public DoctorEntity findByMobile(String mobile);

	/**
	 * 根据email找到Doctor的信息
	 * 
	 * @param email
	 */
	public DoctorEntity findByEmail(String email);

	/**
	 * 根据emailCode找到Doctor的信息
	 * 
	 * @param emailCode
	 * @return
	 */
	public DoctorEntity findByEmailCode(String emailCode);

	public List<DoctorEntity> searchDoctor(String hql, Parameter params);

	DoctorEntity findByRecommendCode(String recommendCode);

}
