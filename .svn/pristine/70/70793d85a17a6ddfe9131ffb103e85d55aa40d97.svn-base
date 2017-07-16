package com.sinohealth.eszservice.dao.visit;

import java.util.Date;

import com.sinohealth.eszorm.entity.visit.TakeMedRecordEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

public interface ITakeMedRecordDao extends
		IGenericDao<TakeMedRecordEntity, Long> {

	/**
	 * 获取患者服药记录数
	 * 
	 * @param sickId
	 * @param tackedFilter
	 *            是否已服药，0=统计全部记录数目，1=只返回已服药记录数目，2=只返回未服药数目
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int getTakeRecordCount(int sickId, int takedFilter, Date startDate,
			Date endDate);

}
