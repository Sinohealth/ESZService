package com.sinohealth.eszservice.service.visit;

import java.util.List;
import java.util.Set;

import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.common.ConstantVisitItems;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

/**
 * 随访项service
 * 
 * @author 黄世莲
 * 
 */
public interface IVisitItemService {
	/**
	 * 获得itemId项目的值的告警等级
	 * 
	 * @param itemId
	 * @param value
	 * @return
	 * @throws ValueOutOfRangeException
	 */
	int getWarnLevel(Integer itemId, String value, Integer sex)
			throws ValueOutOfRangeException;

	VisitItemEntity get(int itemId);

	/**
	 * 获取项目
	 * 
	 * @param itemId
	 * @param cache
	 * @return
	 */
	VisitItemEntity get(int itemId, boolean cache);

	/**
	 * 获取阶段下的随访项目
	 * 
	 * @param templPhaseId
	 * @return
	 */
	List<VisitItemEntity> getItemsByPhase(Integer templPhaseId);

	boolean isCheckItem(Set<VisitImgEntity> items);

	/**
	 * 检验输入的值范围是否正确
	 * 
	 * @param item
	 * @param value
	 * @throws com.sinohealth.eszservice.service.visit.exception.StringValueLengthException
	 */
	void validate(int itemId, String value);

	/**
	 * 检验输入的值范围是否正确
	 * 
	 * @param item
	 * @param value
	 * @throws com.sinohealth.eszservice.service.visit.exception.StringValueLengthException
	 */
	void validate(VisitItemEntity item, String value);

	/**
	 * @param id
	 * @param visitItemCat
	 *            见常量<code> {@link ConstantVisitItems} .CAT_*</code>
	 * 
	 * @return
	 */
	List<VisitItemEntity> getListByDiseaseId(String id, int... visitItemCat);

	List<VisitItemEntity> getListByTemplAndCat(int templId, int cat);

	/**
	 * 根据itemid数组查询
	 * 
	 * @param items
	 * @return
	 */
	List<VisitItemEntity> getListByItemList(Integer[] items);

	/**
	 * 获取单选级联类型的项目的文字描述
	 * 
	 * @return
	 */
	String getRadioCascadeText(String value, String value2, int itemId);

}
