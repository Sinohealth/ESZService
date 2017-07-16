package com.sinohealth.eszservice.aspect.visit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.entity.visit.AppCurPhaseComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.pojo.PhaseInspectionPojo;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.common.config.VisitPushMsg;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.dto.visit.BodySignValueDto;
import com.sinohealth.eszservice.dto.visit.SaveCommentDto;
import com.sinohealth.eszservice.dto.visit.TakeMedicineDto;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.base.IMessageService;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.doctor.IDoctorService;
import com.sinohealth.eszservice.service.push.PushService;
import com.sinohealth.eszservice.service.push.TransmissionContent;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.ICheckItemValueService;
import com.sinohealth.eszservice.service.visit.IHealthArchiveService;
import com.sinohealth.eszservice.service.visit.IPhaseService;
import com.sinohealth.eszservice.service.visit.ITakeMedRecordService;
import com.sinohealth.eszservice.service.visit.IVisitImgValueService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.IVisitPrescriptionService;

/**
 * 执行复诊切入
 * 
 * @author 黄世莲
 * 
 */
@Component
@Aspect
public class ExeAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IPhaseService phaseService;

	@Autowired
	IDoctorService doctorService;

	@Autowired
	IGradeService gradeService;

	@Autowired
	ICheckItemValueService itemValueService;

	@Autowired
	IApplicationService applicationService;

	@Autowired
	ITakeMedRecordService takeMedRecordService;

	@Autowired
	IDoctorCountService doctorCountService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private IVisitImgValueService visitImgValueService;

	@Autowired
	private IVisitPrescriptionService visitPrescriptionService;

	@Autowired
	private IVisitItemService visitItemService;

	@Autowired
	PushService pushService;

	@Autowired
	IHealthArchiveService healthArchiveService;

	/**
	 * 更新复诊值切入
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "execution(* com.sinohealth.eszservice.service.visit.impl.PhaseServiceImpl.updateValues*(..))")
	public Object updateValuesAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {

		Object[] args = pjp.getArgs();
		int phaseId = (int) args[0];
		TemplatePhaseEntity phase1 = phaseService.get(phaseId);
		ApplicationEntity app = phase1.getTemplate().getApplication();

		PhaseInspectionPojo inspection = (PhaseInspectionPojo) args[2];
		// 做录入复诊信息的日志
		// 处理检验项数值
		logger.debug("处理检验项数值日志======");
		Set<CheckItemValueEntity> newInspectionValues1 = inspection.getValues();
		List<CheckItemValueEntity> newInspectionValues = new ArrayList<CheckItemValueEntity>(
				newInspectionValues1);
		List<CheckItemValueEntity> oldInspectionValues = new ArrayList<>(phase1
				.getInspection().getValues());
		healthArchiveService.updateValues(newInspectionValues,
				oldInspectionValues, app, VisitItemCat.CAT_INSPECTION);

		// 处理检查项数值
		logger.debug("处理检查项数值日志======");
		List<CheckItemValueEntity> newCheckValues = (List<CheckItemValueEntity>) args[5];
		List<CheckItemValueEntity> oldCheckValues = new ArrayList<>(phase1
				.getInspection().getValues());
		healthArchiveService.updateValues(newCheckValues, oldCheckValues, app,
				VisitItemCat.CAT_EXAMINE);

		// 处理处方图片
		logger.debug("处理处方图片日志======");
		List<VisitPrescriptionEntity> newPrecriptionImgs = (List<VisitPrescriptionEntity>) args[1];
		List<VisitPrescriptionEntity> oldPrecriptionImgs = phase1
				.getPrescription().getPics();
		healthArchiveService.updatePrescriptionImgData(newPrecriptionImgs,
				oldPrecriptionImgs, app);

		// 处理检验项图片
		logger.debug("处理检验项图片日志======");

		List<VisitImgEntity> newInspectionImgs = inspection.getPics();
		List<VisitImgEntity> oldInspectionImgs = phase1.getInspection()
				.getPics();
		healthArchiveService.updateImgData(newInspectionImgs,
				oldInspectionImgs, app, VisitItemCat.CAT_INSPECTION);

		// 处理检查项图片
		logger.debug("处理检查项图片日志======");
		List<VisitImgEntity> newCheckImgs = (List<VisitImgEntity>) args[3];
		List<VisitImgEntity> oldCheckImgs = phase1.getChecks();
		healthArchiveService.updateImgData(newCheckImgs, oldCheckImgs, app,
				VisitItemCat.CAT_EXAMINE);

		TemplatePhaseEntity phase = (TemplatePhaseEntity) pjp.proceed();

		if (null == phase) {
			return phase;
		}

		ApplicationEntity application = phase.getTemplate().getApplication();
		try {
			// 如果是当前周期，则更新申请单的状态，否则不处理
			AppCurPhaseComponent curPhase = application.getCurPhase();
			if (curPhase.getCurPhaseId() == phase.getTemplPhaseId()) {
				application.getCurPhase().setFuZhenStatus(
						phase.getFuZhenStatus());
				application.getCurPhase().setReportStatus(
						phase.getReportStatus());
			}

			// 更新复诊到诊率
			applicationService.updateFuZhenRate(application.getApplyId());

			if (phase.getItemCount() != 0) {
				// 要填的项目数等于已提交的项目数，且之前还没积过分情况下才给患者积分
				if ((phase.getItemCount() == phase.getSubmittedCount())
						&& phase.getIsFinishedSubmitted() == 0) {
					Integer userId = application.getSick().getId();
					gradeService.addAction(userId.intValue(),
							GradeKeys.updatedSubsequentVisitData, application
									.getSzSubject().getId());
					phase.setIsFinishedSubmitted(Constants.IS_FINISHED_SUBMITTED);
					phaseService.save(phase);
				}
			}

			// 更新病历填写完整性分析
			applicationService.updateCaseHistoryRate(application.getApplyId());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		try {
			// 提交后就算是完成
			// 是否有医生批注的图片
			int makedCount = visitImgValueService.getCorrectCountByPhase(phase
					.getTemplPhaseId());
			if (makedCount == 0) {
				makedCount = visitPrescriptionService
						.getCorrectCountByPhase(phase.getTemplPhaseId());
			}
			if (makedCount > 0) {
				phase.setFuZhenStatus(VisitStatus.FUZHEN_INCORRECT);
			} else {
				phase.setFuZhenStatus(VisitStatus.FUZHEN_COMPLETED);
			}
			int reportStatus = VisitStatus.DATA_NOMAL;
			int count = itemValueService.getWarnCountByPhase(phase,
					VisitStatus.DATA_URGENT);
			if (count > 0) {
				reportStatus = VisitStatus.DATA_URGENT;
			} else {
				count = itemValueService.getWarnCountByPhase(phase,
						VisitStatus.DATA_WARN);
				if (count > 0) {
					reportStatus = VisitStatus.DATA_WARN;
				}
			}
			phase.setReportStatus(reportStatus); // 复诊数据的状态
			phaseService.update(phase);

			// 如果是当前周期，则更新申请单的状态，否则不处理
			AppCurPhaseComponent curPhase = application.getCurPhase();
			if (null != curPhase
					&& curPhase.getCurPhaseId().equals(phase.getTemplPhaseId())) {
				application.getCurPhase().setFuZhenStatus(
						phase.getFuZhenStatus());
				application.getCurPhase().setReportStatus(
						phase.getReportStatus());
				applicationService.saveApplication(application);
			}
		} catch (Exception e) {
			logger.warn("更新当前周期的申请单复诊状态错误：{}", e);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		try {
			// 更新危急患者总数
			doctorCountService.updateVisitCount(
					application.getDoctor().getId(), application.getSzSubject()
							.getId());
		} catch (Exception e) {
			logger.warn("医生的危急患者总数失败：{}", e);
		}

		// 患者录入、更新复诊检验项数据异常发推送通知
		try {
			if (null != phase) {
				Set<CheckItemValueEntity> itemValues = new HashSet<>();
				if (null != phase.getInspection()) {
					if (phase.getInspection().getValues().size() > 0) {

						// itemValues = dto.getValues();
						itemValues = phase.getInspection().getValues();
						if (null != itemValues && itemValues.size() > 0) {
							// TemplatePhaseEntity phase =
							// phaseService.get(phaseId);

							if (null != phase) {
								// ApplicationEntity application =
								// phase.getTemplate()
								// .getApplication();
								for (CheckItemValueEntity value : itemValues) {
									logger.debug("itemId:"
											+ value.getVisitItem().getItemId()
											+ " warnLevel:"
											+ value.getReportWarnLevel());
									// System.err.println("itemId:"
									// + value.getVisitItem().getItemId()
									// + " warnLevel:" +
									// value.getReportWarnLevel()+" getOp1:"+value.getVisitItem().getOp1());
									VisitItemEntity visitItem = visitItemService
											.get(value.getVisitItem()
													.getItemId());
									if (null != value) {
										if (value.getReportWarnLevel() == 1
												|| value.getReportWarnLevel() == 6) {

											/* 推送消息 */
											String path = VisitPushMsg
													.getConfig("sick.value.warn.path");

											int sickId = application.getSick()
													.getId();
											String titleMsg = VisitPushMsg
													.getConfig("sick.value.warn.title");
											String message = VisitPushMsg
													.getConfig("sick.value.warn");
											message = message.replace(
													":itemName",
													visitItem.getZhName());
											// System.err.println("message: "+message);
											TransmissionContent tc = new TransmissionContent();
											tc.setPath(path);
											tc.addParam("applyId",
													application.getApplyId());
											tc.addParam("title", titleMsg);
											tc.addParam("msg", message);
											tc.addParam("timepoint",
													phase.getTimePoint());
											pushService.push(sickId,
													application.getSzSubject()
															.getId(), titleMsg,
													message, tc, "rp");

											// 推送消息的同时，保存消息
											messageService.addMessage(sickId,
													titleMsg, message,
													application.getSzSubject()
															.getId());
										}

									}
								}
							}

						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("检测检验、检查值是否超标推送失败");
		}
		return phase;
	}

	@Around("execution(* com.sinohealth.eszservice.service.visit.impl.ExecuteServiceImpl.saveComment*(..))")
	public Object saveCommentAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {
		Object[] args = pjp.getArgs();
		Integer phaseId = (Integer) args[0];
		int star = (int) args[1];
		int isSick = (int) args[2];
		SaveCommentDto obj = (SaveCommentDto) pjp.proceed();
		try {
			if (BaseDto.ERRCODE_SUCCESS == obj.getErrCode()) {
				TemplatePhaseEntity phase = phaseService.get(phaseId);
				if (null == phase) {
					return obj;
				}
				ApplicationEntity application = phase.getTemplate()
						.getApplication();

				// 如果给了5星，就给积分
				if (star == 5) {
					int userId;
					String gradeKey;
					// 医生评价给患者积分，如患者评价给医生积分
					if (isSick == 0) {
						userId = application.getSick().getId();
						gradeKey = GradeKeys.PATIENT_5SCORE_EFU;
					} else {
						userId = application.getDoctor().getId();
						gradeKey = GradeKeys.DOCTOR_5SCORE_EFU;
					}

					gradeService.addAction(userId, gradeKey, application
							.getSzSubject().getId());
				}

				// 如果是医生给患者好评，推送消息给患者
				if (isSick == 0) {
					/* 推送消息 */
					String path = VisitPushMsg
							.getConfig("sick.comment.doctorGoodComm.path");

					int sickId = application.getSick().getId();
					String titleMsg = VisitPushMsg
							.getConfig("sick.comment.doctorGoodComm.title");
					String message = VisitPushMsg
							.getConfig("sick.comment.doctorGoodComm");
					message = message.replace(":doctorName", application
							.getDoctor().getName());

					int timePoint = phase.getTimePoint();
					// System.err.println("message: "+message);
					TransmissionContent tc = new TransmissionContent();
					tc.setPath(path);
					tc.addParam("applyId", application.getApplyId());
					tc.addParam("title", titleMsg);
					tc.addParam("msg", message);
					tc.addParam("timepoint", timePoint);
					pushService.push(sickId,
							application.getSzSubject().getId(), titleMsg,
							message, tc, "comment");

					// 推送消息的同时，保存消息
					messageService.addMessage(sickId, titleMsg, message,
							application.getSzSubject().getId());
				}
			}
		} catch (Throwable e) {
			logger.warn("5星评价积分失败：{}", e);
			return null;
		}
		return obj;
	}

	/**
	 * 执行更新服药接口切入
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.sinohealth.eszservice.service.visit.impl.TakeMedRecordServiceImpl.saveTakeMedicine*(..))")
	public TakeMedicineDto saveTakeMedicineAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {
		Object[] args = pjp.getArgs();
		TakeMedicineDto obj = (TakeMedicineDto) pjp.proceed();

		if (BaseDto.ERRCODE_SUCCESS == obj.getErrCode()) {
			int sickId = (int) args[0];
			List<ApplicationEntity> list = applicationService.getListBySick(
					sickId, VisitStatus.APPLY_VISITING);

			for (ApplicationEntity application : list) {
				float rate = takeMedRecordService.getTakedRate(sickId,
						application.getVisitStartDate(),
						application.getExpectedFinishTime());
				int intRate = (int) Math.rint(rate * 100); // 四舍五入
				application.getRateCount().setTakeMedRate(intRate);
				applicationService.saveApplication(application);
			}
		}
		return obj;
	}

	/**
	 * 更新体征值切入,录入值超出正常值将推送
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @throws Throwable
	 * 
	 */
	@Around("execution( * com.sinohealth.eszservice.service.visit.impl.BodySignValueServiceImpl.saveBodySignValues*(..))")
	public BaseDto saveBodySignValuesAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {
		BodySignValueDto dto = (BodySignValueDto) pjp.proceed();
		Object[] args = pjp.getArgs();
		int sickId = (int) args[0];
		if (BaseDto.ERRCODE_SUCCESS == dto.getErrCode()) {
			// 更新病历填写完整性分析
			applicationService.updateCaseHistoryRateBySickId(sickId);

			List<BodySignValueEntity> values = dto.getValues();
			if (null != values && values.size() > 0) {
				for (BodySignValueEntity value : values) {
					if (null != value && null != value.getItem()) {
						if (null != value.getItem().getOp1()
								&& value.getItem().getOp1().length() > 0) {
							if (value.getReportWarnLevel() == 1
									|| value.getReportWarnLevel() == 6) {

								/* 推送消息 */
								String path = VisitPushMsg
										.getConfig("sick.value.warn.bodySign.path");

								String titleMsg = VisitPushMsg
										.getConfig("sick.value.warn.bodySign.title");
								String message = VisitPushMsg
										.getConfig("sick.value.warn.bodySign");
								message = message.replace(":itemName", value
										.getItem().getZhName());
								// System.err.println("message: "+message);
								TransmissionContent tc = new TransmissionContent();
								tc.setPath(path);
								tc.addParam("title", titleMsg);
								tc.addParam("msg", message);
								pushService.push(sickId, "", titleMsg, message,
										tc, "bds");

								// 推送消息的同时，保存消息
								messageService.addMessage(sickId, titleMsg,
										message, "");
							}
						}
					}

				}
			}
		}
		return dto;
	}
}
