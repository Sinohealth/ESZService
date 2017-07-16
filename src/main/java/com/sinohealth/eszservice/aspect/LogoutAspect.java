package com.sinohealth.eszservice.aspect;

import java.util.Date;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sinohealth.eszorm.entity.doctor.LoginHistoryEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.dto.doctor.DoctorLogoutDto;
import com.sinohealth.eszservice.dto.sick.SickLogoutDto;
import com.sinohealth.eszservice.service.base.IGradeItemService;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.base.ILoginHistoryService;

public class LogoutAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private IGradeService gradeService;

	private IGradeItemService gradeItemService;

	@Autowired
	private ILoginHistoryService loginHistoryService;

	/**
	 * 医生登出切入
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 */
	// 标明此方法需使用事务
	@Transactional
	public Object doctorLogoutAspectAround(ProceedingJoinPoint pjp) {
		try {
			DoctorLogoutDto retVal = (DoctorLogoutDto) pjp.proceed();
			// 如果登出成功，增加退出数据...
			if (BaseDto.ERRCODE_SUCCESS == retVal.getErrCode()) {
				List<LoginHistoryEntity> list = loginHistoryService
						.getLoginHistory(retVal.getDoctorId());
				if (null != list && list.size() > 0) {
					LoginHistoryEntity loginHistory = list.get(0);
					loginHistory.setLogoutTime(new Date());
					loginHistoryService.saveLoginHistory(loginHistory);
				}
			}
			return retVal;
		} catch (Exception e) {
			logger.warn("处理医生登出失败：{}", e.getMessage());

		} catch (Throwable e) {
			logger.warn("处理医生登出失败：{}", e.getMessage());
			logger.debug("处理医生登出失败：{}", e);
		}
		return null;
	}

	/**
	 * 患者登出切入
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 */
	// 标明此方法需使用事务
	@Transactional
	public Object sickLogoutAspectAround(ProceedingJoinPoint pjp) {

		try {
			SickLogoutDto retVal = (SickLogoutDto) pjp.proceed();
			// 如果登出成功，增加退出数据...
			if (BaseDto.ERRCODE_SUCCESS == retVal.getErrCode()) {
				int userId = retVal.getUserId();
				List<LoginHistoryEntity> list = loginHistoryService
						.getLoginHistory(userId);
				if (null != list && list.size() > 0) {
					LoginHistoryEntity loginHistory = list.get(0);
					loginHistory.setLogoutTime(new Date());
					loginHistoryService.saveLoginHistory(loginHistory);
				}
			}
			return retVal;
		} catch (Throwable e) {
			logger.warn("处理患者登出时间失败：{}", e.getMessage());
			logger.debug("处理患者登出时间失败：{}", e);
		}
		return null;
	}

	public IGradeItemService getGradeItemService() {
		return gradeItemService;
	}

	@Autowired
	public void setGradeItemService(IGradeItemService gradeItemService) {
		this.gradeItemService = gradeItemService;
	}

	public IGradeService getGradeService() {
		return gradeService;
	}

	@Autowired
	public void setGradeService(IGradeService gradeService) {
		this.gradeService = gradeService;
	}

}
