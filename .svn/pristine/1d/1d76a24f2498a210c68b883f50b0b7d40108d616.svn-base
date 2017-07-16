package com.sinohealth.eszservice.service.sick.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sinohealth.eszorm.entity.base.CityEntity;
import com.sinohealth.eszorm.entity.base.ProvinceEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.common.config.ErrorMessage;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.dto.ConstantSickUserErrs;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.common.utils.EncryptUtil;
import com.sinohealth.eszservice.common.utils.Identities;
import com.sinohealth.eszservice.dao.base.ICityDao;
import com.sinohealth.eszservice.dao.base.IProvinceDao;
import com.sinohealth.eszservice.dao.sick.ISickDao;
import com.sinohealth.eszservice.dto.doctor.DoctorLoginDto;
import com.sinohealth.eszservice.dto.doctor.DoctorRegisterDto;
import com.sinohealth.eszservice.dto.doctor.DoctorUpdatePwdDto;
import com.sinohealth.eszservice.dto.sick.SaveHeadShotDto;
import com.sinohealth.eszservice.dto.sick.SickLoginDto;
import com.sinohealth.eszservice.dto.sick.SickLogoutDto;
import com.sinohealth.eszservice.dto.sick.SickModifyDto;
import com.sinohealth.eszservice.dto.sick.SickProfileDto;
import com.sinohealth.eszservice.dto.sick.SickRegisterDto;
import com.sinohealth.eszservice.dto.sick.SickUpdatePwdDto;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.base.exception.NoCityFoundException;
import com.sinohealth.eszservice.service.base.exception.NoProvinceFoundException;
import com.sinohealth.eszservice.service.doctor.exception.RegisterException;
import com.sinohealth.eszservice.service.email.MailSender;
import com.sinohealth.eszservice.service.sick.ICacheSickNonceService;
import com.sinohealth.eszservice.service.sick.ICacheSickRandomService;
import com.sinohealth.eszservice.service.sick.ICacheSickService;
import com.sinohealth.eszservice.service.sick.ISickOnlineService;
import com.sinohealth.eszservice.service.sick.ISickService;
import com.sinohealth.eszservice.service.sick.exception.AccountDuplicateException;
import com.sinohealth.eszservice.service.sick.exception.AccountValidateException;
import com.sinohealth.eszservice.service.sick.exception.EntityNotCacheException;
import com.sinohealth.eszservice.service.sick.exception.PasswordValidateException;
import com.sinohealth.eszservice.service.sms.SMS;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

@Service("sickService")
public class SickServiceImpl implements ISickService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ISickDao sickDao;

	@Autowired
	private IProvinceDao provinceDao;

	@Autowired
	private ICityDao cityDao;

	@Autowired
	private IGradeService gradeService;

	@Autowired
	private ICacheSickNonceService cacheSickNonceService;

	@Autowired
	private ISickOnlineService sickOnlineService;

	@Autowired
	private ICacheSickRandomService cacheSickRandomService;

	@Autowired
	private ICacheSickService cacheSickService;

	@Override
	public SickRegisterDto register(String account, String pwd, String appName) {
		SickRegisterDto dto = new SickRegisterDto();

		try {
			if ("".equals(account)) {
				throw new SystemErrorExecption("账户不能为空", BaseDto.ERRCODE_OTHERS);
			}

			if ("".equals(pwd)) {
				throw new SystemErrorExecption("密码不能为空", BaseDto.ERRCODE_OTHERS);
			}
			validateAccount(account);

			SickEntity sick = new SickEntity();
			SickEntity existsObj = null;
			// 先查是否已经被注册
			boolean isMobile = isMobile(account);
			if (isMobile) {
				existsObj = findByMobile(account);
			} else {// TODO还应该判断是否Email
				existsObj = findByEmail(account);
			}

			if (null != existsObj) {
				throw new AccountDuplicateException(account);
			}

			// 判断账号是电话号码还是email
			if (isMobile) {
				sick.setMobile(account);
			} else { // Email
				sick.setEmail(account);
			}

			// 解释并设置密码
			try {
				String password = EncryptUtil.decryptDes(pwd,
						Constants.DOCTOR_REG_ENCRYPT_KEY);
				logger.debug("解析的密码：{}", password);
				sick.setPwd(password);
			} catch (Exception e) {
				// 密码解密失败
				throw new RegisterException(
						ErrorMessage
								.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD),
						ConstantSickUserErrs.ERRCODE_REG_FAILD);
			}
			sick.setRegisterDate(new Date());
			SickEntity saved = sickDao.save(sick);

			String token = finishLogin(saved.getId());
			dto.setToken(token);
			dto.setSick(saved);
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountValidateException e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountDuplicateException e) {
			dto.setErrCode(ConstantSickUserErrs.ERRCODE_REG_ACCOUNT_REPEAT);
			dto.setErrMsg(ErrorMessage
					.getConfig(ConstantSickUserErrs.ERRCODE_REG_ACCOUNT_REPEAT));
			return dto;
		} catch (RegisterException e) { // 可预见的异常
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
			return dto;
		} catch (Exception e) { // 无法预见的异常，比如保存数据库时数据重复。。。
			logger.warn("Sick Register Error: {}", e);
			dto.setErrCode(ConstantSickUserErrs.ERRCODE_REG_FAILD);
			dto.setErrMsg(ErrorMessage
					.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD));
		}
		return dto;
	}

	@Override
	public SickLoginDto login(String account, String pwdHash, String nOnce,
			String appName) {
		SickLoginDto dto = new SickLoginDto();
		try {

			Assert.hasLength(account, "账号不能为空");
			Assert.hasLength(pwdHash, "密码不能为空");
			Assert.hasLength(nOnce, "随机码不能为空");

			validateAccount(account);

			// 随机字符串与上次登录相同，应该换一个新的随机字符串
			validateNonce(account, nOnce);

			SickEntity sick = isMobile(account) ? findByMobile(account)
					: findByEmail(account);

			if ((null == sick) || (sick.getDelFlag() != 0)) {
				throw new SystemErrorExecption("用户名无效或已删除",
						SickLoginDto.ERRCODE_OTHERS);
			}

			if (sick.getStatus() != 1) {
				throw new SystemErrorExecption("账号被锁定",
						SickLoginDto.ERRCODE_OTHERS);
			}

			if (!encryptPassword(sick.getPwd() + nOnce).equals(pwdHash)) {
				throw new AccountValidateException(
						ErrorMessage
								.getConfig(DoctorLoginDto.ERRCODE_PWD_VILIDATE),
						ConstantSickUserErrs.ERRCODE_PWD_VILIDATE);
			}

			String token = finishLogin(sick.getId());

			dto.setToken(token);
			dto.setSick(sick);
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountValidateException e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (PasswordValidateException e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (Exception e) {
			logger.warn("患者登录异常: {}", e.getMessage());
			logger.debug("医生登录异常: {}", e);
			dto.setErrCode(SickLoginDto.ERRCODE_OTHERS);
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public SickEntity getProfile(String token) throws SystemErrorExecption {
		SickProfileDto dto = new SickProfileDto();

		SickEntity sick = get(token);

		if (null == sick) {
			throw new SystemErrorExecption(
					ErrorMessage
							.getConfig(ConstantSickUserErrs.ERRCODE_NO_THIS_SICK),
					ConstantSickUserErrs.ERRCODE_NO_THIS_SICK);

		}

		dto.setSick(sick);
		return sick;
	}

	@Override
	public SickLogoutDto logout(String token) {
		SickLogoutDto dto = new SickLogoutDto();
		try {
			int userId = sickOnlineService.getUserId(token);
			if (userId != 0) {
				dto.setUserId(Integer.valueOf(userId));
			} else {
				throw new SystemErrorExecption("此用户已退出或token无效",
						SickLogoutDto.ERRCODE_OTHERS);
			}
			sickOnlineService.deleteToken(token);
		} catch (SystemErrorExecption e) {
			logger.error(e.getMessage());
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	/**
	 * 密码加密，默认使用hex256加密
	 * 
	 * @param password
	 * @return
	 */
	public String encryptPassword(String password) {
		return DigestUtils.sha256Hex(password).toUpperCase();
	}

	// 验证输入帐号与随机码是否一样
	public void validateNonce(String account, String nOnce)
			throws PasswordValidateException {
		// System.out.println("验证输入帐号与随机码是否一样: "
		// + sickCacheService.isNonceCached(account, nOnce));
		if (cacheSickNonceService.isNonceCached(account, nOnce)) {
			throw new PasswordValidateException(
					ErrorMessage
							.getConfig(ConstantSickUserErrs.ERRCODE_RADOM_REPEAT),
					ConstantSickUserErrs.ERRCODE_RADOM_REPEAT);
		} else {
			cacheSickNonceService.cacheNonce(account, nOnce);
		}
	}

	/**
	 * 实现登录：生成Token，并保存到redis缓存
	 * 
	 * @param userId
	 * @return
	 */
	public String finishLogin(Integer userId) {
		String token = Identities.uuid2();

		SickEntity sick = get(userId);

		Calendar cal = Calendar.getInstance();

		// Date prevLoginDate = doctor.getLastLoginDate(); // 上一次登录的时间

		update(sick);

		Date lastVisitDate = sick.getLastLoginDate();
		if (null != lastVisitDate) {
			Date today = DateUtils.getDateStart(new Date()); // 今天凌晨
			// System.err.println("whenLogin lastLoginTime:"+lastVisitDate+" :"+today+" lastVisitDate.compareTo(today):"+lastVisitDate.compareTo(today));
			if (lastVisitDate.compareTo(today) < 0) { // 上次访问时间 早于 今天凌晨
				// System.err.println("有进行登录积分！！！");
				gradeService.addAction(userId, GradeKeys.sickDailyLoginKey, "");
				sick.setLastLoginDate(cal.getTime()); // 更新最后一次登录时间
			}
		} else {
			// System.err.println("有进行登录积分！！！");
			gradeService.addAction(userId, GradeKeys.sickDailyLoginKey, "");
			sick.setLastLoginDate(cal.getTime()); // 更新最后一次登录时间
		}

		sickOnlineService.cacheToken(token, userId, new Date().getTime(), 1);

		// 缓存最后访问时间
		sickOnlineService.setLastVisitTime(token, (cal.getTimeInMillis()));

		return token;
	}

	/**
	 * 把token保存到redis缓存
	 * 
	 * @param userId
	 * @return
	 */
	public void finishRegister(Integer userId, String token) {
		SickEntity sick = get(userId);
		update(sick);
		Calendar cal = Calendar.getInstance();
		Date lastVisitDate = sick.getLastLoginDate();
		if (null != lastVisitDate) {
			Date today = DateUtils.getDateStart(new Date()); // 今天凌晨
			// System.err.println("whenLogin lastLoginTime:"+lastVisitDate+" :"+today+" lastVisitDate.compareTo(today):"+lastVisitDate.compareTo(today));
			if (lastVisitDate.compareTo(today) < 0) { // 上次访问时间 早于 今天凌晨
				// System.err.println("有进行登录积分！！！");
				gradeService.addAction(userId, GradeKeys.sickDailyLoginKey, "");
				sick.setLastLoginDate(cal.getTime()); // 更新最后一次登录时间
			}
		} else {
			// System.err.println("有进行登录积分！！！");
			gradeService.addAction(userId, GradeKeys.sickDailyLoginKey, "");
			sick.setLastLoginDate(cal.getTime()); // 更新最后一次登录时间
		}

		sickOnlineService.cacheToken(token, userId, new Date().getTime(), 1);

		// 缓存最后访问时间
		sickOnlineService.setLastVisitTime(token, (cal.getTimeInMillis()));
	}

	@Override
	public SickEntity get(Integer id) {
		try {
			return sickDao.get(id);
		} catch (Exception e) {
			logger.warn("获取Doctor数据异常: {}", e);
			return null;
		}
	}

	/**
	 * 校验账号格式是否正确，如果不正确，抛出AccountValidateException异常
	 * 
	 * @param account
	 * @throws AccountValidateException
	 */
	private void validateAccount(String account)
			throws AccountValidateException {
		// 判断账号是电话号码还是email
		if (isNumber(account)) {
			if (!isMobile(account)) {
				throw new AccountValidateException("不支持此手机号码",
						DoctorLoginDto.ERRCODE_OTHERS);
			}
			// 还应该判断是否Email
		} else if (!isEmail(account)) {
			throw new AccountValidateException("账号格式不正确",
					DoctorLoginDto.ERRCODE_OTHERS);
		}
	}

	/**
	 * 验证是否数字格式
	 * 
	 * @param s
	 * @return
	 */
	public boolean isNumber(String s) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(s);
		return isNum.matches();
	}

	public boolean isMobile(String s) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
		m = p.matcher(s);
		b = m.matches();
		return b;
	}

	/**
	 * 验证是否email格式
	 * 
	 * @param s
	 * @return
	 */
	private boolean isEmail(String s) {
		String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		return m.matches();
	}

	@Override
	public SickEntity get(String token) {
		// 从缓存中找到对应的userId的关系
		Integer userId = sickOnlineService.getUserId(token);
		return (null != userId) ? get(userId) : null;
	}

	@Override
	public SaveHeadShotDto saveHeadShot(String token, String headShotUrl,
			String smallHeadshot) {
		SaveHeadShotDto dto = new SaveHeadShotDto();
		try {
			int userId = sickOnlineService.getUserId(token);
			SickEntity sick = sickDao.get(userId);
			if (null != sick) {
				sick.setHeadShot(headShotUrl);
				sick.setSmallHeadshot(smallHeadshot);
				update(sick);
				dto.setSick(sick);
				dto.setIsSick("1");
			} else {
				throw new SystemErrorExecption("无效用户",
						SaveHeadShotDto.ERRCODE_OTHERS);
			}
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}

		return dto;
	}

	@Override
	public SickModifyDto update(String token, String name, Integer provinceId,
			Integer cityId, int sex, String birthday) {
		SickModifyDto dto = new SickModifyDto();
		try {
			SickEntity sick = get(token);
			if (null == sick) {
				throw new Exception("找不到患信息，系统异常！");
			}

			if ("".equals(name)) {
				throw new SystemErrorExecption("用户名不能为空",
						BaseDto.ERRCODE_OTHERS);
			}

			ProvinceEntity province = provinceDao.get(provinceId);
			if (null == province) {
				throw new NoProvinceFoundException(BaseDto.ERRCODE_OTHERS,
						"请填写相应省份");
			}
			sick.setProvince(province);

			CityEntity city = cityDao.get(cityId);
			if (null == city) {
				throw new NoCityFoundException(
						ConstantSickUserErrs.ERRCODE_NO_THIS_CITY,
						ErrorMessage
								.getConfig(ConstantSickUserErrs.ERRCODE_NO_THIS_CITY));
			}
			// 判断性别范围（0，1，2 中）
			int[] sexs = new int[] { 0, 1, 2 };
			boolean flag = ArrayUtils.contains(sexs, sex);
			if (!flag) {
				throw new SystemErrorExecption(
						ErrorMessage
								.getConfig(ConstantSickUserErrs.SEX_VALUE_ERROR),
						ConstantSickUserErrs.SEX_VALUE_ERROR);
			}
			if ("".equals(birthday)) {
				throw new SystemErrorExecption("出生日期不能为空",
						BaseDto.ERRCODE_OTHERS);
			}
			Date birthdayDate = DateUtils.parseDate(birthday);
			if (null == birthdayDate) {
				throw new SystemErrorExecption(
						ErrorMessage
								.getConfig(ConstantSickUserErrs.BIRTHDAY_FORMAT_ERROR),
						ConstantSickUserErrs.BIRTHDAY_FORMAT_ERROR);
			}
			sick.setName(name);
			sick.setCity(city);
			sick.setSex(sex);
			sick.setBirthday(birthdayDate);
			update(sick);

		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (NoProvinceFoundException e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (NoCityFoundException e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public SickEntity update(String token, String name, Integer provinceId,
			Integer cityId, int sex, String birthday, int height, float weight)
			throws SystemErrorExecption, NoProvinceFoundException,
			NoCityFoundException, Exception {
		SickEntity sick = new SickEntity();
		try {
			sick = get(token);
			if (null == sick) {
				throw new Exception("找不到患信息，系统异常！");
			}

			if ("".equals(name)) {
				throw new SystemErrorExecption("用户名不能为空",
						BaseDto.ERRCODE_OTHERS);
			}

			ProvinceEntity province = provinceDao.get(provinceId);
			if (null == province) {
				throw new NoProvinceFoundException(BaseDto.ERRCODE_OTHERS,
						"请填写相应省份");
			}
			sick.setProvince(province);

			CityEntity city = cityDao.get(cityId);
			if (null == city) {
				throw new NoCityFoundException(
						ConstantSickUserErrs.ERRCODE_NO_THIS_CITY,
						ErrorMessage
								.getConfig(ConstantSickUserErrs.ERRCODE_NO_THIS_CITY));
			}
			// 判断性别范围（0，1，2 中）
			int[] sexs = new int[] { 0, 1, 2 };
			boolean flag = ArrayUtils.contains(sexs, sex);
			if (!flag) {
				throw new SystemErrorExecption(
						ErrorMessage
								.getConfig(ConstantSickUserErrs.SEX_VALUE_ERROR),
						ConstantSickUserErrs.SEX_VALUE_ERROR);
			}
			if ("".equals(birthday)) {
				throw new SystemErrorExecption("出生日期不能为空",
						BaseDto.ERRCODE_OTHERS);
			}
			Date birthdayDate = DateUtils.parseDate(birthday);
			if (null == birthdayDate) {
				throw new SystemErrorExecption(
						ErrorMessage
								.getConfig(ConstantSickUserErrs.BIRTHDAY_FORMAT_ERROR),
						ConstantSickUserErrs.BIRTHDAY_FORMAT_ERROR);
			}
			if (height < 50 || height > 250) {
				throw new SystemErrorExecption("身高录入不合法",
						BaseDto.ERRCODE_OTHERS);
			}
			if (weight < 2 || weight > 300) {
				throw new SystemErrorExecption("体重录入不合法",
						BaseDto.ERRCODE_OTHERS);
			}

			sick.setName(name);
			sick.setCity(city);
			sick.setSex(sex);
			sick.setHeight(height);
			sick.setWeight(weight);
			sick.setBirthday(birthdayDate);
			sickDao.save(sick);
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			throw e;
		} catch (NoProvinceFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (NoCityFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sick;
	}

	//
	// @Override
	// public SickEntity save(SickEntity sick) {
	// return sickDao.save(sick);
	// }

	@Override
	public SickEntity findByMobile(String mobile) {
		SickEntity entity;

		Integer userId;
		try {
			userId = cacheSickService.getIdByMobile(mobile);

			if (null == userId) { // 已经有mobile的索引，但是没有值，说明是数据库中是没有实体的
				return null; // 直接返回null
			} else {
				entity = get(userId); // 缓存不一定可信，所以还是交给相关方法去缓存或数据库取
			}
		} catch (EntityNotCacheException e) {// 还没有被缓存
			// 如果还没有缓存，就查询数据库，并返回
			entity = sickDao.findByMobile(mobile);
		}

		// 缓存手机号码与userId的关系
		if (null == entity) {
			// 设置值为null，以便下次不用再找数据库
			cacheSickService.cacheMobile(mobile, null);
		} else {
			cacheSickService.cacheMobile(mobile, entity.getId());
		}

		return entity;
	}

	@Override
	public SickEntity findByEmail(String email) {
		SickEntity entity;

		Integer userId;
		try {
			userId = cacheSickService.getIdByEmail(email);

			if (null == userId) { // 已经有email的索引，但是没有值，说明是数据库中是没有实体的
				return null; // 直接返回null
			} else {
				entity = get(userId); // 缓存不一定可信，所以还是交给相关方法去缓存或数据库取
			}
		} catch (EntityNotCacheException e) {// 还没有被缓存
			// 如果还没有缓存，就查询数据库，并返回
			entity = sickDao.findByEmail(email);
		}

		// 缓存手机号码与userId的关系
		if (null == entity) {
			// 设置值为null，以便下次不用再找数据库
			cacheSickService.cacheEmail(email, null);
		} else {
			cacheSickService.cacheEmail(email, entity.getId());
		}

		return entity;
	}

	@Override
	public SickEntity findByEmailCode(String emailCode) {
		SickEntity entity;
		entity = findByEmailCode(emailCode);
		return entity;
	}

	public String updateEmailCode(String email) {
		String json = "";
		SickEntity sick = findByEmail(email);

		if (sick != null) {
			String uuid = Identities.uuid();
			Boolean flag = MailSender.updatePassowrd(sick.getEmail(), 2, uuid);
			if (!flag) {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantSickUserErrs.FAIL_TO_SEND_EMAIL,
								ErrorMessage
										.getConfig(ConstantSickUserErrs.FAIL_TO_SEND_EMAIL));

			} else {
				sick.setEmailCode(uuid);
				sick.setEmailTime(new Date());
				update(sick);
				json = "{\"errCode\":0}";
			}
		} else {
			json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
			json = String
					.format(json,
							ConstantSickUserErrs.ERRCODE_NO_THIS_SICK,
							ErrorMessage
									.getConfig(ConstantSickUserErrs.ERRCODE_NO_THIS_SICK));
		}
		return json;
	}

	public String checkByMobile(String mobile) {
		String json = "";
		SickEntity sick = findByMobile(mobile);

		if (sick != null) {
			Integer randomCode = SMS.sendSms(sick.getMobile());
			if (randomCode != 0) {
				cacheSickRandomService.setRandomCode(""
						+ sick.getId().intValue(), randomCode + "");
				logger.debug("验证码发送成功：" + randomCode);
				json = "{\"errCode\":0,\"code\":" + randomCode + "}";
			} else {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantSickUserErrs.FAIL_TO_SEND_MESSAGE,
								ErrorMessage
										.getConfig(ConstantSickUserErrs.FAIL_TO_SEND_MESSAGE));
			}
		} else {
			json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
			json = String
					.format(json,
							ConstantSickUserErrs.ERRCODE_NO_THIS_SICK,
							ErrorMessage
									.getConfig(ConstantSickUserErrs.ERRCODE_NO_THIS_SICK));
		}
		return json;
	}

	public String updatePwdByMobile(String mobile, String pwd, String code) {
		String json = "";
		SickEntity sick = findByMobile(mobile);

		if (sick != null) {
			String randomCode = cacheSickRandomService.getRandomCode(String
					.valueOf(sick.getId()));

			if (null == randomCode) {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String.format(json, BaseDto.ERRCODE_OTHERS, "随机验证码错误");
				return json;
			}

			if (randomCode.equals(code)) {
				// 解释并设置密码
				try {
					String password = EncryptUtil.decryptDes(pwd,
							Constants.DOCTOR_REG_ENCRYPT_KEY);
					sick.setPwd(password);
				} catch (Exception e) {
					e.getStackTrace();
					return new BaseDto(BaseDto.ERRCODE_OTHERS, "密码解释失败")
							.toString();
				}
				update(sick);
				json = "{\"errCode\":0}";
			} else {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantSickUserErrs.CHECKCODE_ERROR,
								ErrorMessage
										.getConfig(ConstantSickUserErrs.CHECKCODE_ERROR));
			}
		} else {
			json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
			json = String
					.format(json,
							ConstantSickUserErrs.ERRCODE_NO_THIS_SICK,
							ErrorMessage
									.getConfig(ConstantSickUserErrs.ERRCODE_NO_THIS_SICK));
		}
		return json;
	}

	@Override
	public void update(SickEntity sick) {
		sickDao.update(sick);
		cacheSickService.updateCached(sick);
	}

	@Override
	public SickUpdatePwdDto updatePassword(String token, String OldPwd,
			String nonce, String newPwd) {
		SickUpdatePwdDto dto = new SickUpdatePwdDto();
		try {
			if ("".equals(OldPwd) || "".equals(nonce) || "".equals(newPwd)) {
				throw new Exception("缺少参数");
			}

			int sickId = sickOnlineService.getUserId(token);
			SickEntity sick = sickDao.get(Integer.valueOf(sickId));
			if (null == sick) {
				throw new PasswordValidateException("此用户不存在",
						BaseDto.ERRCODE_OTHERS);
			}

			String dbPwd = sick.getPwd();
			if (!encryptPassword(sick.getPwd() + nonce).equals(OldPwd)) {
				throw new PasswordValidateException("原密码错误",
						BaseDto.ERRCODE_OTHERS);
			}
			newPwd = EncryptUtil.decryptDes(newPwd,
					Constants.SICK_REG_ENCRYPT_KEY);
			if (newPwd.equals(dbPwd)) {
				throw new PasswordValidateException(
						ErrorMessage
								.getConfig(ConstantSickUserErrs.NEW_PWD_VALIDATION),
						ConstantSickUserErrs.NEW_PWD_VALIDATION);
			}
			sick.setPwd(newPwd);
			update(sick);
		} catch (PasswordValidateException e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrCode(DoctorUpdatePwdDto.ERRCODE_OTHERS);
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public String checkCode(String mobile, String code) {
		String json = "";
		SickEntity sick = findByMobile(mobile);
		String randomCode = cacheSickRandomService.getRandomCode(String
				.valueOf(sick.getId()));
		if (null == randomCode) {
			json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
			json = String.format(json, BaseDto.ERRCODE_OTHERS, "随机验证码错误");
			return json;
		}
		if (null != code || !"".equals(code)) {
			if (randomCode.equals(code)) {
				json = "{\"errCode\":0}";
			} else {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantSickUserErrs.CHECKCODE_ERROR,
								ErrorMessage
										.getConfig(ConstantSickUserErrs.CHECKCODE_ERROR));
			}
		} else {
			json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
			json = String
					.format(json,
							ConstantSickUserErrs.CHECKCODE_NOT_ALLOW_NULL,
							ErrorMessage
									.getConfig(ConstantSickUserErrs.CHECKCODE_NOT_ALLOW_NULL));
		}
		return json;
	}

	@Override
	public void updateTotalGrade(int sickId) {
		sickDao.updateTotalGrade(sickId);

		// 同时更新缓存
		if (cacheSickService.isIdCached(sickId)) {
			SickEntity sick = get(sickId);
			cacheSickService.updateCached(sick);
		}
	}

	@Override
	public SickEntity registerV103(String account, String pwd, String appName,
			String token) throws SystemErrorExecption,
			AccountValidateException, AccountDuplicateException,
			RegisterException, Exception {
		SickEntity saved = new SickEntity();
		try {
			if ("".equals(account)) {
				throw new SystemErrorExecption("账户不能为空", BaseDto.ERRCODE_OTHERS);
			}

			if ("".equals(pwd)) {
				throw new SystemErrorExecption("密码不能为空", BaseDto.ERRCODE_OTHERS);
			}
			validateAccount(account);

			SickEntity sick = new SickEntity();
			SickEntity existsObj = null;
			// 先查是否已经被注册
			boolean isMobile = isMobile(account);
			if (isMobile) {
				existsObj = findByMobile(account);
			} else {// TODO还应该判断是否Email
				existsObj = findByEmail(account);
			}

			if (null != existsObj) {
				throw new AccountDuplicateException(account);
			}

			// 判断账号是电话号码还是email
			if (isMobile) {
				sick.setMobile(account);
			} else { // Email
				sick.setEmail(account);
			}

			// 解释并设置密码
			try {
				String password = EncryptUtil.decryptDes(pwd,
						Constants.DOCTOR_REG_ENCRYPT_KEY);
				logger.debug("解析的密码：{}", password);
				sick.setPwd(password);
			} catch (Exception e) {
				// 密码解密失败
				throw new RegisterException(
						ErrorMessage
								.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD),
						ConstantSickUserErrs.ERRCODE_REG_FAILD);
			}
			sick.setRegisterDate(new Date());
			saved = sickDao.save(sick);
			// String token = finishLogin(saved.getId());
			finishRegister(saved.getId(), token);

		} catch (SystemErrorExecption e) {
			throw e;
		} catch (AccountValidateException e) {
			throw e;
		} catch (AccountDuplicateException e) {
			throw e;
		} catch (RegisterException e) { // 可预见的异常
			throw e;
		} catch (Exception e) { // 无法预见的异常，比如保存数据库时数据重复。。。
			logger.warn("Sick Register Error: {}", e);
			throw e;
		}
		return saved;
	}

	@Override
	public SickEntity loginV103(String account, String pwdHash, String nOnce,
			String appName, String token) throws SystemErrorExecption,
			AccountValidateException, PasswordValidateException, Exception {
		SickEntity sick = new SickEntity();

		Assert.hasLength(account, "账号不能为空");
		Assert.hasLength(pwdHash, "密码不能为空");
		Assert.hasLength(nOnce, "随机码不能为空");

		validateAccount(account);

		// 随机字符串与上次登录相同，应该换一个新的随机字符串
		validateNonce(account, nOnce);

		sick = isMobile(account) ? findByMobile(account) : findByEmail(account);

		if ((null == sick) || (sick.getDelFlag() != 0)) {
			throw new SystemErrorExecption("用户名无效或已删除",
					SickLoginDto.ERRCODE_OTHERS);
		}

		if (sick.getStatus() != 1) {
			throw new SystemErrorExecption("账号被锁定", SickLoginDto.ERRCODE_OTHERS);
		}

		if (!encryptPassword(sick.getPwd() + nOnce).equals(pwdHash)) {
			throw new AccountValidateException(
					ErrorMessage.getConfig(DoctorLoginDto.ERRCODE_PWD_VILIDATE),
					ConstantSickUserErrs.ERRCODE_PWD_VILIDATE);
		}
		// String token = finishLogin(sick.getId());
		finishRegister(sick.getId(), token);

		return sick;
	}

}
