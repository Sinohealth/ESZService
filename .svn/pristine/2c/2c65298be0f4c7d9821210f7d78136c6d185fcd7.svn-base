package com.sinohealth.eszservice.aspect;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sinohealth.eszorm.entity.doctor.LoginHistoryEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dto.doctor.DoctorLoginDto;
import com.sinohealth.eszservice.dto.sick.SickLoginDto;
import com.sinohealth.eszservice.service.base.IAppNameService;
import com.sinohealth.eszservice.service.base.IGradeItemService;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.base.ILoginHistoryService;
import com.sinohealth.eszservice.service.doctor.IDoctorOnlineService;
import com.sinohealth.eszservice.service.sick.ISickOnlineService;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

public class LoginAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private IGradeService gradeService;

	private IGradeItemService gradeItemService;

	@Autowired
	private ILoginHistoryService loginHistoryService;

	@Autowired
	private IAppNameService appNameService;

	@Autowired
	private IDoctorOnlineService doctorOnlineService;

	@Autowired
	private ISickOnlineService sickOnlineService;

	HttpServletRequest request = null;

	/**
	 * 医生登录切入
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 * @throws Throwable
	 */
	// 标明此方法需使用事务
	@Transactional
	public Object doctorLoginAspectAround(ProceedingJoinPoint pjp)
			throws Throwable {
		DoctorLoginDto retVal = null;

		retVal = (DoctorLoginDto) pjp.proceed();
		try {
			Object[] args = pjp.getArgs();
			String appName = (String) args[3];

			// 如果是登录成功，增加每天登录积分的处理...
			if (BaseDto.ERRCODE_SUCCESS == retVal.getErrCode()) {
				// 找到专业
				String szSubject = appNameService.getSzSubjectId(appName);

				int userId = doctorOnlineService.getUserId(retVal.getToken());
				if (null == request) {
					request = ((ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes()).getRequest();
				}
				String token = retVal.getToken();
				long lt = doctorOnlineService.getLastVisitTime(token);

				Date lastVisitDate = new Date(lt); // 上次访问时间
				Date today = DateUtils.getDateStart(new Date()); // 今天凌晨

				if (lastVisitDate.compareTo(today) < 0) { // 上次访问时间 早于 今天凌晨
					// 为免缓存不正确，需要到数据库去再查一次，确定用户最后登录的时间
					Integer doctorId = retVal.getDoctor().getId();
					gradeService.addAction(doctorId,
							GradeKeys.doctorDailyLoginKey, szSubject);
				}

				// 插入登录信息(日志 )
				Date loginTime = new Date();
				String loginIp = request.getRemoteAddr();
				LoginHistoryEntity loginHistory = new LoginHistoryEntity();
				loginHistory.setUserId(Integer.valueOf(userId));
				loginHistory.setLoginAppName(appName);
				loginHistory.setLoginIp(loginIp);
				loginHistory.setLoginTime(loginTime);

				LoginHistoryEntity returnLoginHistory = loginHistoryService
						.saveLoginHistory(loginHistory);
				if (null == returnLoginHistory) {
					throw new SystemErrorExecption("处理医生登录日志失败",
							DoctorLoginDto.ERRCODE_OTHERS);
				}
			}

			return retVal;
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			retVal.setErrCode(e.getErrCode());
			retVal.setErrMsg(e.getMessage());
			logger.warn("处理医生登录日志失败：{}", e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().equals("ERROR_APPNAME")) {
				logger.warn("错误的appName");
			} else {
				logger.warn("处理医生登录积分失败：{}", e.getMessage());
			}
		} catch (Throwable e) {
			logger.warn("处理医生登录积分失败：{}", e.getMessage());
			logger.debug("处理医生登录积分失败：{}", e);
		}
		return null;
	}

	/**
	 * 医生登录切入
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 */
	// 标明此方法需使用事务
	@Transactional
	public Object sickLoginAspectAround(ProceedingJoinPoint pjp) {
		try {
			SickLoginDto retVal = (SickLoginDto) pjp.proceed();

			// 如果是登录成功，增加每天登录积分的处理...
			if (BaseDto.ERRCODE_SUCCESS == retVal.getErrCode()) {
				String token = retVal.getToken();

				if (null == request) {
					request = ((ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes()).getRequest();
				}

				// 插入登录信息(日志 )
				Object[] args = pjp.getArgs();
				String appName = (String) args[3];
				int userId = sickOnlineService.getUserId(token);
				Date loginTime = new Date();
				String loginIp = request.getRemoteAddr();
				logger.debug("loginIp:" + loginIp);
				LoginHistoryEntity loginHistory = new LoginHistoryEntity();
				loginHistory.setUserId(Integer.valueOf(userId));
				loginHistory.setLoginTime(loginTime);
				loginHistory.setLoginIp(loginIp);
				loginHistory.setLoginAppName(appName);
				loginHistoryService.saveLoginHistory(loginHistory);
			}

			return retVal;
		} catch (Throwable e) {
			logger.warn("处理患者登录积分失败：{}", e.getMessage());
			logger.debug("处理医患者登录积分失败：{}", e);
		}
		return null;
	}

	/**
	 * 医生登录切入
	 * 
	 * @param point
	 *            获得切面方法参数等属性的对象
	 * @return 返回处理结果
	 */
	// 标明此方法需使用事务
	@Transactional
	public void sickLoginV103AspectAround(JoinPoint pjp, Object retVal) {

		try {

			SickEntity sick = (SickEntity) retVal;
			// 如果是登录成功，增加每天登录积分的处理...
			if (null != sick) {
				Object[] args = pjp.getArgs();
				String token = (String) args[4];

				if (null == request) {
					request = ((ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes()).getRequest();
				}

				// 插入登录信息(日志 )
				String appName = (String) args[3];
				int userId = sickOnlineService.getUserId(token);
				Date loginTime = new Date();
				String loginIp = request.getRemoteAddr();
				logger.debug("loginIp:" + loginIp);
				LoginHistoryEntity loginHistory = new LoginHistoryEntity();
				loginHistory.setUserId(Integer.valueOf(userId));
				loginHistory.setLoginTime(loginTime);
				loginHistory.setLoginIp(loginIp);
				loginHistory.setLoginAppName(appName);
				loginHistoryService.saveLoginHistory(loginHistory);
			}

		} catch (Throwable e) {
			logger.warn("处理患者登录积分失败：{}", e.getMessage());
			logger.debug("处理医患者登录积分失败：{}", e);
		}
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
