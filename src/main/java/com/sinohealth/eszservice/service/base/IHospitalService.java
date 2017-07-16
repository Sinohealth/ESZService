package com.sinohealth.eszservice.service.base;

import com.sinohealth.eszorm.entity.base.HospitalEntity;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;

/**
 * 医院service接口
 * 
 * @author 黄世莲
 * @since 2015-2-3
 * 
 */
public interface IHospitalService {

	/**
	 * 根据医院的名称，准确找到医院
	 * 
	 * @param name
	 * @return
	 */
	HospitalEntity getByName(String name);

	/**
	 * 根据医院的ID，准确找到医院
	 * 
	 * @param name
	 * @return
	 */
	HospitalEntity getById(int hospitalId);

	/**
	 * 根据医院名查找医院
	 * 
	 * @param nameLike
	 *            医院名称
	 * @param provinceId
	 *            省份
	 * @param pageNo
	 *            页码，从1开始
	 * @param pageSize
	 *            每页记录数
	 * @return
	 */
	PaginationSupport findByName(String nameLike, int provinceId, int pageNo,
			int pageSize,int hashDoctor);

	/**
	 * 添加医院
	 * 
	 * @param provinceId
	 * @param name
	 * @param address
	 */
	HospitalEntity addNew(int provinceId, String name, String address);

	/**
	 * 添加医院
	 * 
	 * @param provinceId
	 * @param name
	 * @param address
	 * @param isDoctorAdd
	 *            是否医生添加的
	 */
	HospitalEntity addNew(int provinceId, String name, String address,
			boolean isDoctorAdd);

}
