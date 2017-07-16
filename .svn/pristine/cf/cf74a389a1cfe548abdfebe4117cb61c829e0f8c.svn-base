package com.sinohealth.eszservice.service.visit;

import java.util.List;

import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.dto.visit.ArchiveListDto;
import com.sinohealth.eszservice.dto.visit.CaseDto;
import com.sinohealth.eszservice.dto.visit.CaseDtov104;
import com.sinohealth.eszservice.dto.visit.FamilyHistoryDto;
import com.sinohealth.eszservice.dto.visit.FamilyHistoryDtoV104;
import com.sinohealth.eszservice.dto.visit.HealthCheckDto;
import com.sinohealth.eszservice.dto.visit.PastHistoryDto;
import com.sinohealth.eszservice.dto.visit.PastHistoryDtoV104;
import com.sinohealth.eszservice.dto.visit.PrescriptionDto;
import com.sinohealth.eszservice.dto.visit.PrescriptionDtoV104;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

public interface IHealthArchiveService {
	/**
	 * 3.4.1 获取获取健康档案列表
	 * 
	 * @param token
	 * @return
	 */
	ArchiveListDto getArchiveList(String token);

	/**
	 * 3.4.2 获取既往史记录
	 * 
	 * @return
	 */
	PastHistoryDto getPastHistories(String token);
	
	/**
	 * 获取既往史记录v104
	 * 
	 * @return
	 */
	PastHistoryDtoV104 getPastHistoriesV104(String token);

	/**
	 * 3.4.3 获取就诊记录
	 * 
	 * @param token
	 * @return
	 */
	CaseDto getCases(String token);

	/**
	 * 3.4.4 获取处方记录
	 */
	PrescriptionDto getPrescriptions(String token);
	
	/**
	 * 获取处方记录V104
	 */
	PrescriptionDtoV104 getPrescriptionsV104(String token);


	/**
	 * 3.4.5 获取检查检验记录
	 * 
	 * @param token
	 * @return
	 */
	HealthCheckDto getHealthCheck(String token);

	/**
	 * 3.4.7 家族史记录
	 * 
	 * @param token
	 * @return
	 */
	FamilyHistoryDto getFamilyHistories(String token);
	
	/**
	 * 家族史记录v104
	 * 
	 * @param token
	 * @return
	 */
	FamilyHistoryDtoV104 getFamilyHistoriesV104(String token);

	/**
	 * 保存healthArchiveLog对象
	 * 
	 * @param healthArchiveLog
	 * @return
	 */
	HealthArchiveLog saveHealthArchiveLog(HealthArchiveLog healthArchiveLog);

	/**
	 * 根据ID找HealthArchiveLog
	 * 
	 * @param id
	 * @return
	 */
	HealthArchiveLog get(Long id);

	/**
	 * 根据img找HealthArchiveLog
	 * 
	 * @param id
	 * @return
	 */
	HealthArchiveLog getByImg(String img);

	/**
	 * 获取就诊记录时间列表
	 * 
	 * @return
	 */
	List<HealthArchiveLog> getCaseList(int sickId);

	/**
	 * 3.4.3 获取就诊记录v104
	 * 
	 * @param token
	 * @return
	 */
	CaseDtov104 getCasesv104(String token);

	/**
	 * 处方图片，新增或删除
	 * 
	 * @param newImgList
	 * @param oldImgList
	 */
	public void updatePrescriptionImgData(
			List<VisitPrescriptionEntity> newImgList,
			List<VisitPrescriptionEntity> oldImgList, ApplicationEntity app);

	/**
	 * 检查, 检验图片，新增或删除
	 * 
	 * @param newImgList
	 * @param oldImgList
	 */
	public void updateImgData(List<VisitImgEntity> newImgList,
			List<VisitImgEntity> oldImgList, ApplicationEntity app, int cat);

	/**
	 * 处理检查，检验项数值
	 * 
	 * @param newValues
	 * @param oldValues
	 */
	public void updateValues(List<CheckItemValueEntity> newValues,
			List<CheckItemValueEntity> oldValues, ApplicationEntity app, int cat)
			throws ValueOutOfRangeException;
	
	/**
	 * 根据用户ID获取过去史,个人，家族史日志记录并时间降序排序
	 */
	public List<HealthArchiveLog> getHealthArchiveLogsBySickId(int sickId, int cat);
	
	/**
	 * 根据用户ID获取处方，检验，检查日志记录并时间降序排序
	 */
	public List<HealthArchiveLog> getHealthArchiveLogImgBySickId(int sickId, int cat);

	/**
	 * 获取检查或检验项的图片日志
	 * @param sickId
	 * @param catInspection
	 * @return
	 */
	List<HealthArchiveLog> getImgsLogBySickId(int sickId, int cat);
	
	/**
	 * 获取检查或检验项数据日志
	 * @param sickId
	 * @param catInspection
	 * @return
	 */
	List<HealthArchiveLog> getDatasLogBySickId(int sickId, int cat);
	
}
