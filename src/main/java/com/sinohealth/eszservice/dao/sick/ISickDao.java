package com.sinohealth.eszservice.dao.sick;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

/**
 * 患者DAO
 * 
 * @author 陈学宏
 * 
 */
public interface ISickDao extends IGenericDao<SickEntity, Integer> {

	/**
	 * 根据mobile找到Sick的信息
	 * 
	 * @param mobile
	 */
	public SickEntity findByMobile(String mobile);

	/**
	 * 根据email找到Sick的信息
	 * 
	 * @param email
	 */
	public SickEntity findByEmail(String email);

	/**
	 * 根据emailCode找到Sick的信息
	 * 
	 * @param emailCode
	 */
	public SickEntity findByEmailCode(String emailCode);

	/**
	 * 更新患者总积分
	 * 
	 * @param userId
	 */
	void updateTotalGrade(int userId);
}
