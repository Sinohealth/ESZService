package com.sinohealth.eszservice.service.visit;

import java.util.List;
import java.util.Set;

import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;

/**
 * 随访处方service
 * 
 * @author 黄世莲
 * 
 */
public interface IVisitPrescriptionService {

	/**
	 * 更新阶段的处方内容：
	 * 
	 * 
	 * 对处方的处理为：<br/>
	 * <ul>
	 * <li>1. 如果上传的内容包括imgId，则：
	 * <ul>
	 * <li>1.1 在数据库中查找到包含此imgId的，并且img,thumb字段内容相同，则认为是不作改动。</li>
	 * <li>1.2 在数据库中查找到包含此imgId，但是img,thumb字段内容却不相同，则认为是对原imgId的处方进行了改动。</li>
	 * 此时，需要将此imgId的处方标记为重传（reuploaded=1），并将img,thumb插入新的记录。
	 * <li>1.3 如果在数据库找不到imgId，忽略处理；</li>
	 * </ul>
	 * <li>2. 如果在上传的内容不包括imgId，则插入新的记录
	 * </ul>
	 * 
	 * @deprecated v1.03版本以后，不再使用，而改为使用关联更新，见
	 *             {@link com.sinohealth.eszservice.service.visit.impl.PhaseServiceImpl#updateValues}
	 * 
	 * @param medicines
	 * @return
	 */
	Set<VisitPrescriptionEntity> updateValues(int phaseId,
			Set<VisitPrescriptionEntity> medicines);

	/**
	 * 医生标记状态
	 * 
	 * @param id
	 * @param status
	 * @param remarks
	 */
	void updateStatusByDoctorMark(int id, int status, String remarks);

	VisitPrescriptionEntity get(int id);

	VisitPrescriptionEntity addNew(VisitPrescriptionEntity appPrescriptionEntity);

	/**
	 * 更新阶段的处方内容：
	 * 
	 * 
	 * 对处方的处理为：<br/>
	 * <ul>
	 * <li>1. 如果上传的内容包括imgId，则：
	 * <ul>
	 * <li>1.1 在数据库中查找到包含此imgId的，并且img,thumb字段内容相同，则认为是不作改动。</li>
	 * <li>1.2 在数据库中查找到包含此imgId，但是img,thumb字段内容却不相同，则认为是对原imgId的处方进行了改动。</li>
	 * 此时，需要将此imgId的处方标记为重传（reuploaded=1），并将img,thumb插入新的记录。
	 * <li>1.3 如果在数据库找不到imgId，忽略处理；</li>
	 * </ul>
	 * <li>2. 如果在上传的内容不包括imgId，则插入新的记录
	 * </ul>
	 * 
	 * @deprecated v1.03版本以后，不要再使用
	 * 
	 * @param prescriptions
	 */
	void updateValuesForApply(int applyId,
			Set<VisitPrescriptionEntity> prescriptions);

	/**
	 * 根据患者ID获取图片列表
	 * 
	 * @param SickId
	 * @return
	 */
	List<VisitPrescriptionEntity> getBySickId(int sickId);

	/**
	 * 根据患者ID获取处方
	 * 
	 * @param sickId
	 * @return
	 */
	List<VisitPrescriptionEntity> getPrescriptions(int sickId);

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
