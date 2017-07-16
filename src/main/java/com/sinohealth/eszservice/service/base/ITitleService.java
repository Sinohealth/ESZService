package com.sinohealth.eszservice.service.base;

import com.sinohealth.eszorm.entity.base.TitleEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

/**
 * 业务分类service接口
 * 
 * @author 陈学宏
 * @since 2015-2-4
 * 
 */
public interface ITitleService extends IGenericDao<TitleEntity, String> {

	/**
	 * 根据职称ID，准确找到所属职称
	 * 
	 * @param titleId
	 * @return
	 */
	TitleEntity getByTitle(String title);
}
