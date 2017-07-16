package com.sinohealth.eszservice.service.base;

import com.sinohealth.eszorm.entity.base.ProvinceEntity;

/**
 * 省份service接口
 * 
 * @author 黄世莲
 * @since 2015-2-3
 * 
 */
public interface IProvinceService {

	/**
	 * 根据省份的名称，准确找到省份
	 * 
	 * @param name
	 * @return
	 */
	ProvinceEntity getByName(String name);

	/**
	 * 根据省份的ID，准确找到省份
	 * 
	 * @param name
	 * @return
	 */
	ProvinceEntity getById(int provinceId);

}
