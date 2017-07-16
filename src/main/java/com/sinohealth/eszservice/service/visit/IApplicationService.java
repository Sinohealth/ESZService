package com.sinohealth.eszservice.service.visit;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.AppInspection;
import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.dto.doctor.DoctorSearchDto;
import com.sinohealth.eszservice.dto.visit.AppDetailsDto;
import com.sinohealth.eszservice.dto.visit.AppReasonDto;
import com.sinohealth.eszservice.dto.visit.ApplicationDto;
import com.sinohealth.eszservice.dto.visit.CaseHistoryRateListDto;
import com.sinohealth.eszservice.dto.visit.DoctorStatusCountDto;
import com.sinohealth.eszservice.dto.visit.FinishedReportDto;
import com.sinohealth.eszservice.dto.visit.SickApplicationDto;
import com.sinohealth.eszservice.dto.visit.SickDetailDto;
import com.sinohealth.eszservice.dto.visit.SickSearchDto;
import com.sinohealth.eszservice.dto.visit.elem.ApplicationElem;
import com.sinohealth.eszservice.dto.visit.sick.UpdateArchiveDto;
import com.sinohealth.eszservice.service.visit.exception.ApplicationDuplicationExecption;
import com.sinohealth.eszservice.service.visit.exception.NoPhaseSelectedExecption;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.UpdatePlanExecption;
import com.sinohealth.eszservice.service.visit.exception.VisitItemNotFoundExecption;
import com.sinohealth.eszservice.vo.visit.TemplContentVo;

public interface IApplicationService {

	/**
	 * 申请随访
	 * 
	 * @param sick
	 * @param szSubject
	 * @param doctorId
	 * @param appCases
	 * @param disease
	 * @return
	 * @throws SystemErrorExecption
	 * @throws ApplicationDuplicationExecption
	 */
	ApplicationEntity saveNewApply(SickEntity sick, SzSubjectEntity szSubject,
			int doctorId, AppCasesComponent appCases, DiseaseEntity disease)
			throws SystemErrorExecption, ApplicationDuplicationExecption;

	/**
	 * 申请随访 V103版本
	 * 
	 * @param sick
	 * @param szSubject
	 * @param doctorId
	 * @param appCases
	 * @param disease
	 * @return
	 * @throws SystemErrorExecption
	 * @throws ApplicationDuplicationExecption
	 */
	ApplicationEntity saveNewApplyV103(SickEntity sick,
			SzSubjectEntity szSubject, int doctorId,
			AppCasesComponent appCases, DiseaseEntity disease,
			Set<CheckItemValueEntity> itemValues) throws SystemErrorExecption,
			ApplicationDuplicationExecption, VisitItemNotFoundExecption;

	/**
	 * 完善随访档案
	 * 
	 * @return
	 */
	UpdateArchiveDto updateArchive(int applyId,
			List<VisitPrescriptionEntity> prescriptions,
			List<VisitImgEntity> checkItems,
			AppPastHistoryComponent pastHistoryObj, String familyHistory);

	ApplicationEntity get(int applyId);

	/**
	 * 3.5.5 获取随访申请详细信息
	 * 
	 * @param applyId
	 * @return
	 */
	AppDetailsDto getDetail(int applyId);

	/**
	 * 获取医生端随访状态统计
	 * 
	 * @param token
	 * @param appName
	 * @return
	 */
	DoctorStatusCountDto getStat(String token, String appName);

	/**
	 * 获取医生端随访患者列表信息 (待同意,随访中,已婉拒,已退出)
	 * 
	 * @param token
	 * @param appName
	 * @param status
	 * @param page
	 * @param pageSize
	 * @return
	 */
	ApplicationDto getSickApplicationDetail(String token, String appName,
			Integer status, Integer page, Integer pageSize, int isPaging);

	/**
	 * 获取医生端“已完成”患者列表
	 * 
	 * @param token
	 * @param appName
	 * @param status
	 * @param page
	 * @param pageSize
	 * @return
	 */
	FinishedReportDto getFinished(String token, String appName, Integer status,
			Integer page, Integer pageSize);

	/**
	 * 获取患者端的随访列表
	 * 
	 * @param token
	 * @return
	 */
	SickApplicationDto getSickStat(String token, String visitStatus);

	/**
	 * 患者端找医生
	 * 
	 * @param doctorId
	 * @param hospitalId
	 * @param descpId
	 * @param doctorName
	 * @return
	 */
	DoctorSearchDto doctorSearch(String doctorId, String recommendCode,
			int hospitalId);

	/**
	 * 医生端搜索患者
	 * 
	 * @param token
	 * @param sickName
	 * @return
	 */
	SickSearchDto sickSearch(String token, String sickName, String appName);

	/**
	 * v103 医生端搜索患者 返回增加bmi参数
	 * 
	 * @param token
	 * @param sickName
	 * @return
	 */
	SickSearchDto sickSearchv103(String token, String sickName, String appName);

	/**
	 * 某位患者随访详情
	 * 
	 * @param token
	 * @param sickId
	 * @return
	 */
	SickDetailDto getSickRecords(String token, Integer sickId, String appName);

	/**
	 * 某位患者随访详情 v103
	 * 
	 * @param token
	 * @param sickId
	 * @return
	 */
	Set<ApplicationElem> getSickRecordsV103(String token, Integer sickId,
			String appName) throws SystemErrorExecption;

	/**
	 * 保存申请与模板的关系
	 * 
	 * @deprecated v1.3版本以后不再使用
	 * 
	 * @param applyId
	 * @param template
	 * @param visitStartDate
	 */
	void saveAppTemplate(int applyId, TemplateEntity template,
			Date visitStartDate);

	/**
	 * 3.4.9 拒绝随访申请
	 * 
	 * @param applyId
	 * @param reasonId
	 * @param otherReason
	 * @return
	 */
	AppReasonDto saveRejectReason(Integer applyId, Integer reasonId,
			String otherReason);

	/**
	 * 3.5.10设置已退出信息
	 * 
	 * @param applyId
	 * @param reasonId
	 * @param otherReason
	 * @return
	 */
	AppReasonDto saveExitReason(Integer applyId, String reasonIds,
			String otherReason);

	/**
	 * 更新随访状态
	 * 
	 * @see com.sinohealth.eszorm.VisitStatus
	 * @param visitStatus
	 *            状态见 ConstantVisitStatus
	 */
	void updateVisitStatus(int applyId, int visitStatus);

	/**
	 * 将旧的申请单更新为重新提交状态
	 * 
	 * @param applyId
	 *            原申请单ID
	 * @param newApplyId
	 *            新的申请单ID
	 */
	void updateRecommitApply(int applyId, Integer newApplyId);

	/**
	 * 根据患者获取既往史
	 * 
	 * @param sickId
	 * @return
	 */
	List<ApplicationEntity> getHistory(int sickId);

	/**
	 * 保存随访申请的计划模板
	 * 
	 * @return
	 * @throws NoPhaseSelectedExecption
	 */
	void addPlanForApp(int applyId, TemplContentVo templVo, int stdTemplId,
			Date visitStartDate) throws NoPhaseSelectedExecption;

	/**
	 * 更新就诊记录档案
	 * 
	 * @param appCases
	 */
	void updateArchiveCases(int applyId, AppCasesComponent appCases);

	/**
	 * 更新就诊记录档案 V103版本
	 * 
	 * @param appCases
	 */
	void updateArchiveCasesV103(int applyId, AppCasesComponent appCases,
			Set<CheckItemValueEntity> itemValues) throws SystemErrorExecption,
			VisitItemNotFoundExecption;

	List<ApplicationEntity> getRelationApplications(int doctorId,
			String szSubject);

	ApplicationEntity saveApplication(ApplicationEntity application);

	/**
	 * 获取【随访中】的申请，根据阶段的随访时间来获取
	 * 
	 * 
	 * @param time
	 * @param notFuzhen
	 *            是否获取未复诊的随访
	 * @return
	 */
	List<ApplicationEntity> getVisitingListByPhaseVisitTime(Date visitTime,
			boolean notFuzhen);

	/**
	 * 按预期结束时间，将随访更新为【已完成】状态
	 * 
	 * @param expectedFinishTime
	 *            预期结束时间
	 */
	void updateVisitStatusToCompleted(Date expectedFinishTime);

	/**
	 * 根据随访开始时间获取随访列表
	 * 
	 * @param visitStartDate
	 *            随访开始时间
	 * @param visitStatus
	 *            要获取的随访的状态
	 * @return
	 */
	List<ApplicationEntity> getListByVisitStartDate(Date visitStartDate,
			int visitStatus);

	/**
	 * 根据预期结束的时间，获取相应状态visitStatus的随访申请列表
	 * 
	 * @param expectedFinishTime
	 * @param visitStatus
	 * @return
	 */
	List<ApplicationEntity> getListWithExpectedFinishTime(
			Date expectedFinishTime, int visitStatus);

	/**
	 * 根据申请时间段，获取随访列表
	 * 
	 * @param startApplyTime
	 *            随访开始时间
	 * @param endApplyTime
	 *            随访开始时间
	 * @param visitStatus
	 *            要获取的随访的状态
	 * @return
	 */
	List<ApplicationEntity> getListByApplyTime(Date startApplyTime,
			Date endApplyTime, int visitStatus);

	/**
	 * 随访为已完成状态
	 * 
	 * @param applyId
	 */
	void updateVisitStatusToCompleted(int applyId);

	/**
	 * 更新复诊到诊率
	 * 
	 * @param applyId
	 */
	void updateFuZhenRate(int applyId);

	/**
	 * 更新病历填写完整性分析
	 * 
	 * @param applyId
	 */
	void updateCaseHistoryRate(int applyId);

	/**
	 * 获取患者的随访列表
	 * 
	 * @param sickId
	 * @param visitStatus
	 * @return
	 */
	List<ApplicationEntity> getListBySick(int sickId, int... visitStatus);

	/**
	 * 统计复诊到诊率
	 * 
	 * @param applyId
	 * @return
	 */
	float getFuZhenRate(int applyId);

	/**
	 * 获取病历填写完整性分析<br/>
	 * 病历填写完整性分析 = （已填写必填项目数量/总必填项目数量）*100%
	 */
	float countCaseHistoryRate(int applyId);

	/**
	 * 获取患者相应的随访
	 * 
	 * @param sickId
	 * @param visitStatus
	 * @param szSubject
	 * @param disease
	 * @return
	 */
	List<ApplicationEntity> getListForSick(int sickId, int visitStatus,
			String szSubject, DiseaseEntity disease);

	/**
	 * 将随访当前的病种更改为新的病种
	 * 
	 * @param applyId
	 * @param disease
	 * @throws ApplicationDuplicationExecption
	 *             随访重复异常
	 */
	void updateDisease(int applyId, DiseaseEntity disease)
			throws ApplicationDuplicationExecption;

	/**
	 * 获取健康档案完整性描述列表
	 * 
	 * @param applyId
	 * @return
	 */
	CaseHistoryRateListDto getCaseHistoryRateList(int applyId);

	/**
	 * 更新药物依从性
	 * 
	 * @param applyId
	 */
	void updateTakedMedRate(int applyId);

	List<ApplicationEntity> getFamilyHistoryList(int sickId);

	void updateCaseHistoryRateBySickId(int sickId);

	void updatePlanForApp(int applyId, TemplContentVo templVo,
			Date visitStartDate) throws UpdatePlanExecption;

	/**
	 * 完善健康档案
	 * 
	 * @since 1.03
	 * 
	 * @param applyId
	 * @param prescriptions
	 * @param inspection
	 * @param checks
	 * @param pastHistory
	 * @param familyHistory
	 * @return
	 */
	ApplicationEntity updateArchive(int applyId,
			List<VisitPrescriptionEntity> prescriptions,
			AppInspection inspection, List<VisitImgEntity> checks,
			AppPastHistoryComponent pastHistory, String familyHistory);

	void update(ApplicationEntity application);
	
	/**
	 * 找出上一个月没填体重的患者
	 * 
	 * @param sickId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<ApplicationEntity> getApplications(String szSubject, Date startDate,
			Date endDate);
}
