package com.sinohealth.eszservice.service.visit;

import java.util.Date;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszservice.dto.visit.BodySignValueDto;
import com.sinohealth.eszservice.dto.visit.sick.OtherBodyElem;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

/**
 * 体征结果值Service
 * 
 * @author 黄世莲
 * 
 */
public interface IBodySignValueService {

	/**
	 * 录入日常管理信息
	 * 
	 * @return
	 * @throws ValueOutOfRangeException
	 */
	BodySignValueDto saveBodySignValues(int sickId, Date reportDate,
			List<BodySignValueEntity> values) throws ValueOutOfRangeException;

	/**
	 * 获取抗凝指标信息
	 * 
	 * @param sickId
	 * @param cat
	 * @return
	 */
	List<OtherBodyElem> getBodySignBySickIdCat(int sickId, int cat, int pageNo,
			int pageSize);

	/**
	 * 查出来的结果按reportDate顺序排列
	 * 
	 * @param sickId
	 * @param itemId
	 *            如果是null，则不加itemId限制
	 * @param startDate
	 *            如果是null，则不加开始条件
	 * @param endDate
	 *            如果是null，则不加结束条件
	 * @return
	 */
	List<BodySignValueEntity> getList(int sickId, Integer itemId,
			Date startDate, Date endDate);
}
