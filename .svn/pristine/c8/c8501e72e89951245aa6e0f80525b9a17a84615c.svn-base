package com.sinohealth.eszservice.aspect.visit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.AppInspection;
import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.dao.visit.IApplicationDao;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.sick.ISickService;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IHealthArchiveService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.paser.PastHistoryParser;

/**
 * 完善健康档案切入
 * 
 * @author 陈学宏
 * 
 */
@Component
@Aspect
public class UpdatedArchiveAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IApplicationService applicationService;

	@Autowired
	private IApplicationDao applicationDao;

	@Autowired
	private IGradeService gradeService;

	@Autowired
	private IVisitItemService visitItemService;

	@Autowired
	private ISickService sickService;

	@Autowired
	IHealthArchiveService healthArchiveService;

	/**
	 * 完善健康档案
	 * 
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around("execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.updateArchive(..))")
	public Object updatedArchiveAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {

		// 更新检查，检验项日志
		Object[] args = pjp.getArgs();
		int applyId = (int) args[0];

		ApplicationEntity app = applicationService.get(applyId);

		if (null != app) {

			// 处理既往史
			logger.debug("处理既往史数据日志======");
			AppPastHistoryComponent pastHis = (AppPastHistoryComponent) args[4];
			String pastData = PastHistoryParser.compile(pastHis, null);
			updateValueData(pastData, VisitItemCat.CAT_PAST_HIS, app);

			// 处理家族史
			logger.debug("处理家族史数据日志======");
			String familyData = (String) args[5];
			updateValueData(familyData, VisitItemCat.CAT_FAMILY_HIS, app);

			// 处理处方图片
			logger.debug("处理处方图片日志======");
			List<VisitPrescriptionEntity> newPrecriptionImgs = (List<VisitPrescriptionEntity>) args[1];
			List<VisitPrescriptionEntity> oldPrecriptionImgs = app
					.getPrescription().getPics();
			healthArchiveService.updatePrescriptionImgData(newPrecriptionImgs,
					oldPrecriptionImgs, app);

			// 处理检验项图片
			logger.debug("处理检验项图片日志======");
			AppInspection inspection = (AppInspection) args[2];
			List<VisitImgEntity> newInspectionImgs = inspection.getPics();
			List<VisitImgEntity> oldInspectionImgs = app.getInspection()
					.getPics();
			healthArchiveService.updateImgData(newInspectionImgs,
					oldInspectionImgs, app, VisitItemCat.CAT_INSPECTION);

			// 处理检查项图片
			logger.debug("处理检查项图片日志======");
			List<VisitImgEntity> newCheckImgs = (List<VisitImgEntity>) args[3];
			List<VisitImgEntity> oldCheckImgs = app.getChecks();
			healthArchiveService.updateImgData(newCheckImgs, oldCheckImgs, app,
					VisitItemCat.CAT_EXAMINE);
		}

		ApplicationEntity application = (ApplicationEntity) pjp.proceed();
		try {
			if (null != application) {
				int isUpdated = application.getSick().getIsUpdatedArchive();
				if (0 == isUpdated) {
					logger.debug("完善健康档案积10分处理");
					// boolean isCheckItems = visitItemService
					// .isCheckItem(application.getItems());
					// TODO v1.3版本，需要分开来保存检查项和检验项的图片
					boolean isCheckItems = (application.getChecks().size() > 0 || application
							.getInspection().getPics().size() > 0);
					boolean isPrescriptions = (null != application
							.getPrescription().getPics() && application
							.getPrescription().getPics().size() > 0) ? true
							: false;

					boolean isMedicalHis = (null != application
							.getAppPastHistory().getMedicalHistories()) ? true
							: false;
					boolean isAllergyHis = (null != application
							.getAppPastHistory().getAllergyHistories()) ? true
							: false;
					boolean isSurgicalHis = (null != application
							.getAppPastHistory().getSurgicalHistories()) ? true
							: false;
					logger.warn("完善健康档案是否可以积分："
							+ (isCheckItems && isMedicalHis && isAllergyHis
									&& isSurgicalHis && isPrescriptions));
					if (isCheckItems && isMedicalHis && isAllergyHis
							&& isSurgicalHis && isPrescriptions) {
						Integer userId = application.getSick().getId();
						gradeService.addAction(userId.intValue(),
								GradeKeys.updatedHealthArchive, application
										.getSzSubject().getId());
						SickEntity sick = sickService.get(userId);
						sick.setIsUpdatedArchive(1);
						sickService.update(sick);
					}
				}
			}
		} catch (Throwable e) {
			logger.warn("完善健康档案积分失败：{}", e);
			return null;
		}
		return application;
	}

	/**
	 * 更新就诊记录日志
	 * 
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around("execution(* com.sinohealth.eszservice.service.visit.impl.ApplicationServiceImpl.updateArchiveCasesV103(..))")
	public void updateArchiveCasesAround(ProceedingJoinPoint pjp)
			throws Throwable {
		Object[] args = pjp.getArgs();

		int applyId = (int) args[0];
		AppCasesComponent appCases = (AppCasesComponent) args[1];

		ApplicationEntity app = applicationService.get(applyId);

		if (app != null) {
			
			//保存个人史日志 =====
			List<CheckItemValueEntity> values = new ArrayList<>((Set<CheckItemValueEntity>)args[2]);
			for (CheckItemValueEntity itemValue:values) {
				HealthArchiveLog healthLog = new HealthArchiveLog();
				healthLog.setSickId(app.getSick().getId());
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
			
			// 门诊、出院图片====
			int newSize = appCases.getApplyImgList().size();
			int oldSize = app.getAppCases().getApplyImgList().size();

			if (newSize < oldSize) {
				// 遍历旧图片, 前端已被删除的图片从数据库中移除
				for (VisitImgEntity img : app.getAppCases().getApplyImgList()) {
					if (!StringUtils.hasLength(img.getImg())) { // 如果没有传URL过来，跳过
						continue;
					}
					// appCases.getApplyImgList().contains(o);
					System.out.println("oldImgId:" + img.getImg() + "ID:"
							+ img.getItemId());
					// 如果上传的图片带有imgId，则认为不变或修改
					if ((null != img.getImgId())
							&& (0 != img.getImgId().longValue())) {

						// 如果已经存在，不作改变
						int idx = appCases.getApplyImgList().indexOf(img);

						// 如果是有这条数据，否则认为是无效数据
						if (-1 != idx) {
							VisitImgEntity newImg = appCases.getApplyImgList()
									.get(idx);
							// 原图片不相等，认为图片已更改
							if (!img.getImg().equals(newImg.getImg())) {
								// 因为更新的七牛图库的功能需求，如果是https开头，不做处理
								if (!StringUtils.startsWithIgnoreCase(
										newImg.getImg(), "https")) {
									logger.info("修改了门诊图片：{}", img);

									HealthArchiveLog healthLog = new HealthArchiveLog();
									healthLog.setImgId(newImg.getImgId());
									healthLog.setSickId(app.getSick().getId());
									healthLog.setItemId(newImg.getItemId());
									healthLog.setPostTime(new Date());
									healthLog.setThumb(newImg.getThumb());
									healthLog.setImg(newImg.getImg());
									healthLog.setCat(newImg.getCat());
									healthLog.setStatus(newImg.getStatus());
									healthLog
											.setOperationFlag(Constants.HEALTH_ARCHIVE_MODIFY);
									healthArchiveService
											.saveHealthArchiveLog(healthLog);
								}
							}
						} else {
							HealthArchiveLog healthLog = healthArchiveService
									.getByImg(img.getImg());
							System.out.println("删除的IMGID:" + img.getImgId());
							healthLog.setImgId(img.getImgId());
							healthLog.setPostTime(new Date());

							healthLog.setDelFlag(1);
							healthLog
									.setOperationFlag(Constants.HEALTH_ARCHIVE_DELETE);
							healthArchiveService
									.saveHealthArchiveLog(healthLog);
							// 如果带了imgId，但是没有找到原值，跳过
							logger.info("数据库中找不到原图片{}，新增：{}", healthLog);
							continue;
						}
					}
				}
			} else {
				// 遍历新图片
				for (VisitImgEntity img : appCases.getApplyImgList()) {
					if (!StringUtils.hasLength(img.getImg())) { // 如果没有传URL过来，跳过
						continue;
					}
					if (0 == img.getImgId().longValue()) {
						HealthArchiveLog healthLog = new HealthArchiveLog();
						healthLog.setImgId(img.getImgId());
						healthLog.setSickId(app.getSick().getId());
						healthLog.setItemId(img.getItemId());
						healthLog.setPostTime(new Date());
						healthLog.setThumb(img.getThumb());
						healthLog.setImg(img.getImg());
						healthLog.setCat(img.getCat());
						healthLog.setStatus(img.getStatus());
						healthLog
								.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
						healthArchiveService.saveHealthArchiveLog(healthLog);
						logger.info("新增了门诊图片：{}", healthLog);
					}
				}

			}

		}

		pjp.proceed();
	}

	/**
	 * 既往史，家族史数据记录
	 * 
	 * @param newImgList
	 * @param oldImgList
	 */
	public void updateValueData(String inputData, int cat, ApplicationEntity app) {
		if (!"".equals(inputData)) {
			HealthArchiveLog healthLog = new HealthArchiveLog();
			healthLog.setSickId(app.getSick().getId());
			healthLog.setPostTime(new Date());
			healthLog.setValue(inputData);
			healthLog.setCat(cat);
			healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
			healthArchiveService.saveHealthArchiveLog(healthLog);
			logger.info("新增既往史或家族史数据记录日志：{} cat:", cat);
		}
	}

}
