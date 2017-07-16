package com.sinohealth.eszservice.service.visit;

import java.util.Date;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.TakeMedRecordEntity;
import com.sinohealth.eszservice.dto.visit.TakeMedicineDto;

public interface ITakeMedRecordService {

	/**
	 * 获取患者“<strong>已服药</strong>”记录数
	 * 
	 * @param sickId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int getTakedRecordCount(int sickId, Date startDate, Date endDate);

	TakeMedicineDto saveTakeMedicine(int sickId, int taked, String date);

	/**
	 * 获取患者服药记录
	 * 
	 * @param sickId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<TakeMedRecordEntity> getListBySick(int sickId, Date startDate,
			Date endDate);

	/**
	 * 获取患者的一段时间内的服药率
	 * 
	 * @param sickId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	float getTakedRate(int sickId, Date startDate, Date endDate);
}
