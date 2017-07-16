package com.sinohealth.eszservice.service.visit;

import java.util.List;
import java.util.Set;

import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszservice.dto.visit.CheckItemValueDto;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;
import com.sinohealth.eszservice.service.visit.exception.VisitItemNotFoundExecption;

/**
 * 随访检查项service
 * 
 * @author 黄世莲
 * 
 */
public interface ICheckItemValueService {

	/**
	 * 更新检验项结果值
	 * 
	 * @param phaseId
	 * @param values
	 * @return
	 * @throws VisitItemNotFoundExecption
	 *             随访ID错误异常
	 * @throws ValueOutOfRangeException
	 */
	Set<CheckItemValueEntity> updateValues(int phaseId,
			Set<CheckItemValueEntity> values)
			throws VisitItemNotFoundExecption, ValueOutOfRangeException,
			SystemErrorExecption;

	void updatePersonalValues(int applyId, Set<CheckItemValueEntity> values)
			throws SystemErrorExecption, VisitItemNotFoundExecption;

	/**
	 * 更新检验项结果值 并返回警报值reportWarnLevel 值到前端
	 * 
	 * @param phaseId
	 * @param values
	 * @return 有多少项目结果被填写了
	 * @throws VisitItemNotFoundExecption
	 *             随访ID错误异常
	 * @throws ValueOutOfRangeException
	 */
	CheckItemValueDto updateCheckItemValues(int phaseId,
			Set<CheckItemValueEntity> values)
			throws VisitItemNotFoundExecption, ValueOutOfRangeException,
			SystemErrorExecption;

	/**
	 * 获取检验项值(健康档案)
	 * 
	 * @param sickId
	 * @return
	 */
	List<CheckItemValueEntity> getitemsBySickId(int sickId);

	/**
	 * 获取检验项值(申请详情)
	 * 
	 * @param sickId
	 * @return
	 */
	List<CheckItemValueEntity> getitemsBySickId(int sickId, int applyId);

	/**
	 * 查询个人史数据
	 * 
	 * @param sickId
	 * @param applyId
	 * @return
	 */
	List<CheckItemValueEntity> getPersonsBySickId(int sickId);

	/**
	 * 根据患者ID获得最新检验记录
	 * 
	 * @param sickId
	 * @return
	 */
	List<CheckItemValueEntity> getCheckItem(int sickId);

	/**
	 * 阶段下，告警等级为{level}的值的数量
	 * 
	 * @param phase
	 * @param level
	 * @return
	 */
	int getWarnCountByPhase(TemplatePhaseEntity phase, int level);

	/**
	 * @param sickId
	 * @param cat
	 *            分类见{@link VisitItemCat}
	 * @return
	 */
	List<CheckItemValueEntity> getListBySick(int sickId, int cat);
}
