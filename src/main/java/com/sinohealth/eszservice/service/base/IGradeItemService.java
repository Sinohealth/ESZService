package com.sinohealth.eszservice.service.base;

import com.sinohealth.eszorm.entity.base.GradeItemEntity;

/**
 * 积分项service
 * 
 * @author 黄世莲
 * 
 */
public interface IGradeItemService {

	/**
	 * 根据变量名，获得积分项
	 * 
	 * @param action
	 * @return
	 */
	public GradeItemEntity getByKey(String key);
}
