package com.sinohealth.eszservice.aspect.visit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.common.config.VisitPushMsg;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.dto.visit.AppReasonDto;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.base.IMessageService;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.doctor.IDoctorService;
import com.sinohealth.eszservice.service.push.PushService;
import com.sinohealth.eszservice.service.push.TransmissionContent;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IHealthArchiveService;
import com.sinohealth.eszservice.service.visit.ITemplateService;

/**
 * 随访申请切入
 * 
 * @author 黄世莲
 * 
 */
@Component
@Aspect
public class ApplyAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDoctorService doctorService;

	@Autowired
	private IApplicationService applicationService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private IGradeService gradeService;

	@Autowired
	private ITemplateService templateService;

	@Autowired
	IDoctorCountService doctorCountService;

	@Autowired
	PushService pushService;
	
	@Autowired
	IHealthArchiveService healthArchiveService;

	/**
	 * 患者新增随访申请
	 * 
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@AfterReturning(value = "execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.saveNewApply(..)) or execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.saveNewApplyV103(..))", returning = "retVal")
	public void applyNewAspectAfterReturning(JoinPoint joinPoint, Object retVal) {
		Object[] args = joinPoint.getArgs();
		logger.debug("新增随访申请时处理，做发送消息等处理");

		ApplicationEntity application = (ApplicationEntity) retVal;
		AppCasesComponent appCases = (AppCasesComponent) args[3];
		
		//保存个人史日志 
		List<CheckItemValueEntity> values = new ArrayList<>((Set<CheckItemValueEntity>)args[5]);
		for (CheckItemValueEntity itemValue:values) {
			HealthArchiveLog healthLog = new HealthArchiveLog();
			healthLog.setSickId(application.getSick().getId());
			healthLog.setItemId(itemValue.getVisitItem().getItemId());
			healthLog.setPostTime(new Date());
			healthLog.setValue(itemValue.getReportValue());
			healthLog.setReportWarnLevel(itemValue.getReportWarnLevel());
			healthLog.setResultId(itemValue.getResultId());
			healthLog.setCat(VisitItemCat.CAT_PERSONAL_HIS);
			healthLog
					.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
			healthArchiveService.saveHealthArchiveLog(healthLog);
		}
	
		
		//写入出院与诊记录的日志
		if (application != null) {
//			Calendar cal = Calendar.getInstance();
			// =================================
			// 门诊、出院图片
			// =================================

			//保存就诊记录日志
			for (VisitImgEntity img : appCases.getApplyImgList()) {
				if (!StringUtils.hasLength(img.getImg())) { // 如果没有传URL过来，跳过
					continue;
				}
//				appCases.getApplyImgList().contains(o);
				// 如果上传的图片带有imgId，则认为不变或修改
				if ((null != img.getImgId())
						&& (0 != img.getImgId().longValue())) {
					// 如果已经存在，不作改变
					int idx = application.getAppCases().getApplyImgList().indexOf(img);
					// 如果是有这条数据，否则认为是无效数据
					if (-1 != idx) {
						VisitImgEntity old = application.getAppCases()
								.getApplyImgList().get(idx);
						// 原图片不相等，认为图片已更改
						if (!img.getImg().equals(old.getImg())) {
							// 因为更新的七牛图库的功能需求，如果是https开头，不做处理
							if (!StringUtils.startsWithIgnoreCase(img.getImg(),
									"https")) {
								logger.info("修改了门诊图片：{}", img);

								HealthArchiveLog healthLog = new HealthArchiveLog();
								healthLog.setImgId(img.getImgId());
								healthLog.setSickId(application.getSick().getId());
								healthLog.setItemId(old.getItemId());
								healthLog.setPostTime(new Date());
								healthLog.setThumb(old.getThumb());
								healthLog.setImg(old.getImg());
								healthLog.setCat(old.getCat());
								healthLog.setStatus(old.getStatus());
								healthLog
										.setOperationFlag(Constants.HEALTH_ARCHIVE_MODIFY);
								healthArchiveService
										.saveHealthArchiveLog(healthLog);
							}
						}
					} else {
						HealthArchiveLog healthLog = new HealthArchiveLog();
						healthLog.setImgId(img.getImgId());
						healthLog.setSickId(application.getSick().getId());
						healthLog.setItemId(img.getItemId());
						healthLog.setPostTime(new Date());
						healthLog.setThumb(img.getThumb());
						healthLog.setImg(img.getImg());
						healthLog.setCat(img.getCat());
						healthLog.setStatus(img.getStatus());
						healthLog
								.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
						healthArchiveService.saveHealthArchiveLog(healthLog);
						// 如果带了imgId，但是没有找到原值，跳过
						logger.info("数据库中找不到原图片{}，新增：{}", healthLog);
						continue;
					}
				} else {
					HealthArchiveLog healthLog = new HealthArchiveLog();
					healthLog.setImgId(img.getImgId());
					healthLog.setSickId(application.getSick().getId());
					healthLog.setItemId(img.getItemId());
					healthLog.setPostTime(new Date());
					healthLog.setThumb(img.getThumb());
					healthLog.setImg(img.getImg());
					healthLog.setCat(img.getCat());
					healthLog.setStatus(img.getStatus());
					healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
					healthArchiveService.saveHealthArchiveLog(healthLog);
					logger.info("新增了门诊图片：{}", healthLog);
				}

			}
		}
		
		String diseaseName = application.getDisease().getName();

		/* 更新医生的的随访统计 */
		doctorCountService.updateVisitCount(application.getDoctor().getId(),
				application.getSzSubject().getId());

		// 更新病历完整性
		applicationService.updateCaseHistoryRate(application.getApplyId());
		// 更新药物依从性
		applicationService.updateTakedMedRate(application.getApplyId());

		/* 推送消息 */
		String path = VisitPushMsg.getConfig("doctor.apply.commited.path");

		int doctorId = application.getDoctor().getId();
		String titleMsg = VisitPushMsg.getConfig("doctor.apply.commited.title");
		String message = VisitPushMsg.getConfig("doctor.apply.commited");
		message = message.replace(":sickName", application.getSick().getName());
		message = message.replace(":appName", "\""
				+ application.getSzSubject().getName() + "\"");
		message = message.replace(":disease", diseaseName);
		// System.err.println("message: "+message);
		TransmissionContent tc = new TransmissionContent();
		tc.setPath(path);
		tc.addParam("applyId", application.getApplyId());
		tc.addParam("title", titleMsg);
		tc.addParam("msg", message);
		pushService.push(doctorId, application.getSzSubject().getId(),
				titleMsg, message, tc, "apply" + application.getApplyId());

		// 推送消息的同时，保存消息
		messageService.addMessage(doctorId, titleMsg, message, application
				.getSzSubject().getId());
	}

	/**
	 * 医生同意
	 * 
	 * @throws Throwable
	 */
	@AfterReturning(value = "execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.addPlanForApp(..))", returning = "retVal")
	public void acceptedAspectAfterReturning(JoinPoint joinPoint, Object retVal) {

		Object[] args = joinPoint.getArgs();

		int applyId = (int) args[0];

		logger.debug("更新患者待同意随访总数,applyId:{}", applyId);

		ApplicationEntity application = applicationService.get(applyId);

		String path = "";
		String titleMsg = "";
		String message = "";

		// 更新病历完整性
		applicationService.updateCaseHistoryRate(application.getApplyId());
		// 更新药物依从性
		applicationService.updateTakedMedRate(application.getApplyId());

		applicationService.updateFuZhenRate(application.getApplyId());

		DoctorEntity doctor = application.getDoctor();

		// 只要有新保存我计划才有积分
		// 保存同意随访的积分
		int doctorId = doctor.getId().intValue();
		gradeService.addAction(doctorId, GradeKeys.doctorAcceptVisitKey,
				application.getSzSubject().getId());
		doctorCountService.updateVisitCount(doctor.getId(), application
				.getSzSubject().getId());
		logger.debug("同意随访并给医生积分 2分");

		List<ApplicationEntity> applications = applicationService
				.getRelationApplications(doctorId, application.getSzSubject()
						.getId());

		if (null != applications && applications.size() > 0) {

			int count = applications.size() % 10;// 是否10的整数倍
			/*
			 * System.err.println("application.size:" + applications.size() +
			 * " Count: " + count);
			 */

			if (count == 0) {
				gradeService.addAction(doctorId,
						GradeKeys.doctorTenVisitRelationship, application
								.getSzSubject().getId());
			}
		}

		/* 同意随访推送消息 */
		path = VisitPushMsg.getConfig("sick.apply.accepted.path");

		titleMsg = VisitPushMsg.getConfig("sick.apply.accepted.title");
		message = VisitPushMsg.getConfig("sick.apply.accepted");

		int sickId = application.getSick().getId();
		message = message.replace(":doctorName", doctor.getName());
		// System.err.println("message: "+message);
		TransmissionContent tc = new TransmissionContent();
		tc.setPath(path);
		tc.addParam("applyId", application.getApplyId());
		tc.addParam("title", titleMsg);
		tc.addParam("msg", message);
		tc.addParam("doctorName", doctor.getName());
		pushService.push(sickId, application.getSzSubject().getId(), titleMsg,
				message, tc, "apply" + application.getApplyId());

		// 推送消息的同时，保存消息
		messageService.addMessage(sickId, titleMsg, message, application
				.getSzSubject().getId());
	}

	/**
	 * 患者退出
	 * 
	 * @throws Throwable
	 */
	@Around("execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.saveExitReason(..))")
	public Object exitedAspectAround(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();

		int applyId = (int) args[0];

		logger.debug("患者退出");

		AppReasonDto retVal;
		retVal = (AppReasonDto) pjp.proceed();

		if (BaseDto.ERRCODE_SUCCESS == retVal.getErrCode()) {
			ApplicationEntity application = applicationService.get(applyId);

			/* 更新医生的的随访统计 */
			doctorCountService.updateVisitCount(
					application.getDoctor().getId(), application.getSzSubject()
							.getId());
			String diseaseName = application.getDisease().getName();
			/* 推送消息 */
			String path = VisitPushMsg.getConfig("doctor.apply.exit.path");

			int doctorId = application.getDoctor().getId();
			String titleMsg = VisitPushMsg.getConfig("doctor.apply.exit.title");
			String message = VisitPushMsg.getConfig("doctor.apply.exit");
			message = message.replace(":sickName", application.getSick()
					.getName());
			message = message.replace(":appName", "\""
					+ application.getSzSubject().getName() + "\"");
			message = message.replace(":disease", diseaseName);

			TransmissionContent tc = new TransmissionContent();
			tc.setPath(path);
			tc.addParam("applyId", application.getApplyId());
			tc.addParam("title", titleMsg);
			tc.addParam("msg", message);
			pushService.push(doctorId, application.getSzSubject().getId(),
					titleMsg, message, tc, "apply" + application.getApplyId());

			// 推送消息的同时，保存消息
			messageService.addMessage(doctorId, titleMsg, message, application
					.getSzSubject().getId());
		}

		return retVal;
	}

	/**
	 * 医生拒绝后
	 * 
	 * @throws Throwable
	 */
	@AfterReturning(value = "execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.saveRejectReason*(..))", returning = "retVal")
	public void rejectedAspectAfterReturning(JoinPoint joinPoint, Object retVal) {
		Object[] args = joinPoint.getArgs();
		AppReasonDto dto;
		if (retVal instanceof AppReasonDto) {
			dto = (AppReasonDto) retVal;
		} else {
			return;
		}

		if (BaseDto.ERRCODE_SUCCESS == dto.getErrCode()) {
			Object param = args[0];
			if (null != param) {
				int applyId = (int) param;

				ApplicationEntity application = applicationService.get(applyId);

				if (null != application) {
					String szSubjectId = application.getSzSubject().getId();
					DoctorEntity doctor = application.getDoctor();
					int doctorId = application.getDoctor().getId();
					int sickId = application.getSick().getId();
					try {

						/* 推送消息 */
						String path = VisitPushMsg
								.getConfig("sick.apply.rejected.path");

						String titleMsg = VisitPushMsg
								.getConfig("sick.apply.rejected.title");
						String message = VisitPushMsg
								.getConfig("sick.apply.rejected");
						message = message.replace(":doctorName",
								doctor.getName());
						TransmissionContent tc = new TransmissionContent();
						tc.setPath(path);
						tc.addParam("applyId", applyId);
						tc.addParam("doctorId", doctorId);
						tc.addParam("title", titleMsg);
						tc.addParam("msg", message);
						tc.addParam("doctorName", doctor.getName());
						pushService.push(sickId, szSubjectId, titleMsg,
								message, tc, "apply" + applyId);

						// 推送消息的同时，保存消息
						messageService.addMessage(sickId, titleMsg, message);
					} catch (Throwable e) {
						e.printStackTrace();
					} finally {
						doctorCountService.updateVisitCount(doctorId,
								szSubjectId);
					}
				}
			}
		}
	}

	// TODO 已完成，更新已完成的统计

	/**
	 * 医生修改计划
	 * 
	 * @throws Throwable
	 */
	@AfterReturning(value = "execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.updatePlanForApp(..))", returning = "retVal")
	public void updatePlanAspectAfterReturning(JoinPoint joinPoint,
			Object retVal) {
		Object[] args = joinPoint.getArgs();
		int applyId = (int) args[0];
		ApplicationEntity application = applicationService.get(applyId);

		// 更新病历完整性
		applicationService.updateCaseHistoryRate(application.getApplyId());
		// 更新药物依从性
		applicationService.updateTakedMedRate(application.getApplyId());

		applicationService.updateFuZhenRate(application.getApplyId());

		logger.debug("医生修改了计划，发送计划给患者：applyId{}", application.getApplyId());
		int sickId = application.getSick().getId();
		String titleMsg = VisitPushMsg.getConfig("sick.plan.updated.title");
		String message = VisitPushMsg.getConfig("sick.plan.updated");
		message = message.replace(":doctorName", application.getDoctor()
				.getName());

		/* 推送消息 */
		String path = VisitPushMsg.getConfig("sick.plan.updated.path");
		TransmissionContent tc = new TransmissionContent();
		tc.setPath(path);
		tc.addParam("applyId", application.getApplyId());
		tc.addParam("title", titleMsg);
		tc.addParam("msg", message);

		pushService.push(sickId, application.getSzSubject().getId(), titleMsg,
				message, tc, "apply" + application.getApplyId());

		// 推送消息的同时，保存消息
		messageService.addMessage(sickId, titleMsg, message);
	}

	/**
	 * 随访完成时
	 * 
	 * @throws Throwable
	 */
	@Around("execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.updateVisitStatusToCompleted*(..))")
	public Object completedAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {
		Object[] args = pjp.getArgs();
		Object o = pjp.proceed();

		Object param = args[0];
		if (null != param) {
			int applyId = (int) param;

			ApplicationEntity application = applicationService.get(applyId);

			if (null != application) {
				try {
					String szSubjectId = application.getSzSubject().getId();
					DoctorEntity doctor = application.getDoctor();
					doctorCountService.updateVisitCount(doctor.getId(),
							szSubjectId);
					return o;
				} catch (Throwable e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return o;
	}
}
