package com.sinohealth.eszservice.service.visit;

import java.util.Date;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.pojo.PhaseInspectionPojo;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;
import com.sinohealth.eszservice.service.visit.exception.VisitItemNotFoundExecption;

public interface IPhaseService {

	/**
	 * 获得周期检测项
	 * 
	 * @param templId
	 * @return
	 */
	List<TemplatePhaseEntity> getPhases(int templId);

	TemplatePhaseEntity addNew(int templId, int timePoint, Date visitTime);

	TemplatePhaseEntity addNew(int templId, int timePoint, Date visitTime,
			int[] itemIds, int selected, int isFuzhenItem);

	TemplatePhaseEntity get(int id);

	/**
	 * 更新周期下的复诊项目列表
	 * 
	 * @param phaseId
	 * @param itemIds
	 * @return 正确返回TemplatePhaseEntity对象实例，失败返回null
	 */
	TemplatePhaseEntity updateItems(int phaseId, int[] itemIds);

	/**
	 * 统计该阶段有多少值已经输入
	 * 
	 * @param templId
	 * @return
	 */
	int getItemValuesCount(int phaseId);

	/**
	 * 更新随访阶段检查项
	 * 
	 * @param isFuzhenValue
	 * @param checkValueList
	 * 
	 * @return
	 * 
	 * @throws VisitItemNotFoundExecption
	 * @throws ValueOutOfRangeException
	 */

	TemplatePhaseEntity updateValues(int phaseId,
			List<VisitPrescriptionEntity> list, PhaseInspectionPojo inspection,
			List<VisitImgEntity> checks, int isFuzhenValue,
			List<CheckItemValueEntity> checkValueList)
			throws VisitItemNotFoundExecption, ValueOutOfRangeException,
			SystemErrorExecption;

	/**
	 * 删除此阶段下的检验项结果值
	 * 
	 * @param phaseId
	 */
	void deleteValues(int phaseId);

	/**
	 * 删除此阶段下的检查项结果值
	 * 
	 * @param phaseId
	 */
	void deleteCheckPicValues(int phaseId);

	/**
	 * 删除此阶段下的处方结果值
	 * 
	 * @param phaseId
	 */
	void deletePrescriptionValues(int phaseId);

	/**
	 * 更新阶段
	 * 
	 * @param oldPhase
	 * @param itemIds
	 */
	public void update(TemplatePhaseEntity phase, int[] itemIds);

	/**
	 * 删除阶段
	 * 
	 * @param phase
	 */
	void deletePhase(int id);

	/**
	 * 更新阶段
	 * 
	 * @param phase
	 */
	void update(TemplatePhaseEntity phase);

	List<TemplatePhaseEntity> getSeletedListByVisitTime(Date visitTime,
			int... fuZhenStatus);

	/**
	 * 更新阶段的提交率
	 * 
	 * @param phaseId
	 */
	void updateCommittedCount(int phaseId);

	TemplatePhaseEntity save(TemplatePhaseEntity phase);

	/**
	 * 获取阶段列表
	 * 
	 * @param templId
	 *            模板ID
	 * @param selected
	 *            是否选中的阶段
	 * @return
	 */
	List<TemplatePhaseEntity> getListByTempl(int templId, int selected);

	/**
	 * 统计阶段的应填项目数
	 * 
	 * @param phase
	 * @return
	 */
	int getItemCount(TemplatePhaseEntity phase);

	/**
	 * 获取阶段，按阶段开始时间，及阶段状态
	 * 
	 * @param visitTime
	 * @param fuZhenStatus
	 * @return
	 */
	List<TemplatePhaseEntity> getListByVisitTime(Date visitTime,
			int... fuZhenStatus);

}
