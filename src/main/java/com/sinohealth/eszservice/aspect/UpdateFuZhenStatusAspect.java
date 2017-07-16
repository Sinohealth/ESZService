package com.sinohealth.eszservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinohealth.eszorm.entity.visit.AppCurPhaseComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.doctor.IDoctorService;
import com.sinohealth.eszservice.service.visit.IPhaseService;

public class UpdateFuZhenStatusAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IPhaseService phaseService;

	@Autowired
	IDoctorService doctorService;

	@Autowired
	IDoctorCountService doctorCountService;

	/**
	 * 更新复诊值切入
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @throws Throwable
	 */
	public void updateValuesAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {
		Object[] args = pjp.getArgs();
		int phaseId = (int) args[0];

		TemplatePhaseEntity phase = phaseService.get(phaseId);

		if (null == phase) {
			return;
		}

		ApplicationEntity application = phase.getTemplate().getApplication();
		pjp.proceed();
		try {

			// 如果是当前周期，则更新申请单的状态，否则不处理
			AppCurPhaseComponent curPhase = application.getCurPhase();
			if (curPhase.getCurPhaseId() == phase.getTemplPhaseId()) {
				application.getCurPhase().setFuZhenStatus(
						phase.getFuZhenStatus());
				application.getCurPhase().setReportStatus(
						phase.getReportStatus());
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
	}

}
