package com.sinohealth.eszservice.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.dto.doctor.DoctorLoginDto;
import com.sinohealth.eszservice.dto.doctor.DoctorRegisterDto;
import com.sinohealth.eszservice.dto.sick.SickLoginDto;
import com.sinohealth.eszservice.dto.sick.SickRegisterDto;
import com.sinohealth.eszservice.service.base.IAppNameService;
import com.sinohealth.eszservice.service.base.IGradeItemService;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.doctor.IDoctorService;
import com.sinohealth.eszservice.service.sms.SMS;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

/**
 * 注册账号切入程序
 * 
 * @author 黄世莲
 * 
 */
public class RegisterAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IGradeService gradeService;

	@Autowired
	private IGradeItemService gradeItemService;

	@Autowired
	private IDoctorService doctorService;

	@Autowired
	private IDoctorCountService doctorCountService;

	@Autowired
	private IAppNameService appNameService;

	/**
	 * 医生注册
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 */
	public Object doctorRegisterAspectAround(ProceedingJoinPoint pjp) {
		DoctorRegisterDto retVal = new DoctorRegisterDto();
		try {
			Object[] params = pjp.getArgs();
			retVal = (DoctorRegisterDto) pjp.proceed();
			// System.err.println("args length:" + params.length);
			String subject = "";
			String newAppName = (String) params[3];
			String newSubject = appNameService.getSzSubjectId(newAppName);

			// 如果是注册成功
			if (retVal.getErrCode() == DoctorLoginDto.ERRCODE_SUCCESS) {
				String appName = (String) params[3];
				String szSubject = appNameService.getSzSubjectId(appName);
				DoctorEntity doctor = new DoctorEntity();
				// C级注册才送积分
				if (params.length > 4) {
					// System.err.println("注册subject:"+szSubject);
					gradeService.addAction(retVal.getDoctor().getId(),
							GradeKeys.doctorRegisterKey, szSubject);

					gradeService.addAction(retVal.getDoctor().getId(),
							GradeKeys.doctorDailyLoginKey, szSubject);

					DoctorEntity doctorSms = retVal.getDoctor();
					// 注册成功发信息通知医生
					SMS.registerSendSms(doctorSms);

					// C级注册给推荐人送积分
					String recommender = (String) params[2];
					if (!"".equals(recommender)) {
						if (doctorService.isEmail(recommender)
								|| doctorService.isMobile(recommender)
								|| recommender.length() == 7) {// 推荐 参 数为三个其中之一

							if (doctorService.isEmail(recommender)) {
								doctor = doctorService.findByEmail(recommender);
							}
							if (doctorService.isMobile(recommender)) {
								doctor = doctorService
										.findByMobile(recommender);
							}
							if (recommender.length() == 7
									&& !doctorService.isEmail(recommender)) {
								doctor = doctorService
										.findByRecommendCode(recommender);
							}

							if (null != doctor.getSzSubjects()) {
								if (doctor.containSzSubject(newSubject)) {
									subject = newSubject;
								} else {
									subject = doctor.getSzSubjects().split(",")[0];
								}

							} else {
								// 如果推荐者是B级用户，则去查doctorCount表中的专科
								List<DoctorCountEntity> list = doctorCountService
										.getDoctorCount(doctor.getId());
								if (isContainSubject(list, newSubject)) {// DoctorCount包含新注册专科，则按新专科积分
									subject = newSubject;
								} else {
									// DoctorCount不包含新注册专科，则取doctorCount第一个积分
									subject = list.get(0).getSzSubject();
								}
							}
							gradeService.addAction(doctor.getId(),
									GradeKeys.doctorInviteFriends, subject);
						}
					}
				} else {
					gradeService.addAction(retVal.getDoctor().getId(),
							GradeKeys.doctorDailyLoginKey, szSubject);
				}
			}

			return retVal;
		} catch (SystemErrorExecption e) {
			logger.warn("处理医生注册发信息失败：{}", e.getMessage());
			logger.debug("处理医生注册发信息失败：{}", e);
		} catch (Throwable e) {
			logger.warn("处理医生注册积分失败：{}", e.getMessage());
			logger.debug("处理医生注册积分失败：{}", e);
		}
		return null;
	}

	/**
	 * 患者注册
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 */
	public Object sickRegisterAspectAround(ProceedingJoinPoint pjp) {
		try {
			SickRegisterDto retVal = (SickRegisterDto) pjp.proceed();

			// 如果是注册成功
			if (retVal.getErrCode() == SickLoginDto.ERRCODE_SUCCESS) {

				// 注册功能，增加积分
				gradeService.addAction(retVal.getSick().getId(),
						GradeKeys.sickRegisterKey, "");
			}

			return retVal;
		} catch (Throwable e) {
			logger.warn("处理患者注册积分失败：{}", e.getMessage());
			logger.debug("处理患者注册积分失败：{}", e);
		}
		return null;
	}

	/**
	 * 患者注册 V103
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 */
	public void sickRegisterV103AspectAround(JoinPoint pjp, Object retVal) {
		try {
			SickEntity sick = (SickEntity) retVal;

			// 如果是注册成功
			if (null != retVal) {
				// 注册功能，增加积分
				gradeService.addAction(sick.getId(), GradeKeys.sickRegisterKey,
						"");
			}

		} catch (Throwable e) {
			logger.warn("处理患者注册积分失败：{}", e.getMessage());
			logger.debug("处理患者注册积分失败：{}", e);
		}
	}

	public boolean isContainSubject(List<DoctorCountEntity> list,
			String esSubject) {
		boolean flag = false;
		for (DoctorCountEntity count : list) {
			if (count.getSzSubject().equals(esSubject)) {
				flag = true;
			}
		}
		return flag;
	}

}
