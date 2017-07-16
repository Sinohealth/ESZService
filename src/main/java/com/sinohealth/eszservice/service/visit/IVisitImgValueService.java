package com.sinohealth.eszservice.service.visit;

import java.util.List;
import java.util.Set;

import com.sinohealth.eszorm.entity.visit.VisitImgEntity;

/**
 * 随访项service
 * 
 * @author 黄世莲
 * 
 */
public interface IVisitImgValueService {

	/**
	 * 更新阶段的图片值
	 * 
	 * @param phaseId
	 * @param values
	 * @return 返回提交的项目数
	 */
	Set<VisitImgEntity> updateValuesForPhase(int phaseId,
			Set<VisitImgEntity> values);

	/**
	 * 更新申请单的检查检验图片值
	 * 
	 * @param phaseId
	 * @param values
	 * @deprecated v1.03后，不再使用
	 */
	void updateCheckValuesForApply(int applyId, List<VisitImgEntity> values);

	void updateStatus(long imgId, int status, String remarks);

	VisitImgEntity get(long id);

	/**
	 * 获取检查类图片
	 * 
	 * @param sickId
	 * @param itemId
	 * @return
	 */
	List<VisitImgEntity> getImgBySickId(int sickId);

	/**
	 * 获取检查类图片
	 * 
	 * @param sickId
	 * @param itemId
	 * @return
	 */
	List<VisitImgEntity> getImgsBySickId(int sickId);

	/**
	 * 获取检验类图片
	 * 
	 * @param sickId
	 * @param itemId
	 * @return
	 */
	List<VisitImgEntity> getItemImgBySickId(int sickId);

	/**
	 * 获取检验类图片
	 * 
	 * @param sickId
	 * @param itemId
	 * @return
	 */
	List<VisitImgEntity> getItemImgBySickId(int sickId, int applyId);

	/**
	 * 获取出院诊断证明图
	 * 
	 * @return
	 */
	List<VisitImgEntity> getLeaveHosPics(int sickId);

	/**
	 * 获取门诊病历图
	 * 
	 * @param sickId
	 * @return
	 */
	List<VisitImgEntity> getCasesPics(int sickId);

	/**
	 * 获取就诊记录（出院诊断证明与门诊病历图）
	 * 
	 * @param sickId
	 * @return
	 */
	List<VisitImgEntity> getCaseLeaveHosPics(int sickId);

	/**
	 * 更新门诊、住院记录图片
	 * 
	 * @deprecated
	 * @param applyId
	 * @param values
	 */
	void updateCasesValuesForApply(int applyId, List<VisitImgEntity> values);

	/**
	 * 是否没有没处理的指示的图片
	 * 
	 * @param phaseId
	 * @return
	 */
	boolean isDataFormatCorrectWithPhase(int phaseId);

	boolean isDataFormatCorrectWithApply(int applyId);

	/**
	 * 获取此阶段有多少张被标记有误的，并且未重新上传的图片。
	 * 
	 * @param templPhaseId
	 * @return
	 */
	int getCorrectCountByPhase(Integer templPhaseId);
}
