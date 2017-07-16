package com.sinohealth.eszservice.service.doctor.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sinohealth.eszorm.entity.base.DisciplineEntity;
import com.sinohealth.eszorm.entity.base.HospitalEntity;
import com.sinohealth.eszorm.entity.base.ProvinceEntity;
import com.sinohealth.eszorm.entity.base.TitleEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.common.config.ErrorMessage;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.dto.ConstantDoctorUserErrs;
import com.sinohealth.eszservice.common.dto.ConstantSickUserErrs;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.common.utils.EncryptUtil;
import com.sinohealth.eszservice.common.utils.FileUtil;
import com.sinohealth.eszservice.common.utils.Identities;
import com.sinohealth.eszservice.common.utils.QRCode;
import com.sinohealth.eszservice.common.utils.StringUtil;
import com.sinohealth.eszservice.dao.doctor.IDoctorDao;
import com.sinohealth.eszservice.dto.doctor.DoctorExperienceCertDto;
import com.sinohealth.eszservice.dto.doctor.DoctorGradesDto;
import com.sinohealth.eszservice.dto.doctor.DoctorLoginDto;
import com.sinohealth.eszservice.dto.doctor.DoctorLogoutDto;
import com.sinohealth.eszservice.dto.doctor.DoctorModifyDto;
import com.sinohealth.eszservice.dto.doctor.DoctorProfileDto;
import com.sinohealth.eszservice.dto.doctor.DoctorRegisterDto;
import com.sinohealth.eszservice.dto.doctor.DoctorUpdatePwdDto;
import com.sinohealth.eszservice.dto.sick.SaveHeadShotDto;
import com.sinohealth.eszservice.service.base.IAppNameService;
import com.sinohealth.eszservice.service.base.IDisciplineService;
import com.sinohealth.eszservice.service.base.IGradeItemService;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.base.IHospitalService;
import com.sinohealth.eszservice.service.base.IProvinceService;
import com.sinohealth.eszservice.service.base.ITitleService;
import com.sinohealth.eszservice.service.base.exception.AppNameNotSupportException;
import com.sinohealth.eszservice.service.base.exception.NoDisciplineFoundException;
import com.sinohealth.eszservice.service.base.exception.NoHospitalFoundException;
import com.sinohealth.eszservice.service.base.exception.NoProvinceFoundException;
import com.sinohealth.eszservice.service.base.exception.NoTitleFoundException;
import com.sinohealth.eszservice.service.doctor.ICacheDoctorNonceService;
import com.sinohealth.eszservice.service.doctor.ICacheDoctorRandomService;
import com.sinohealth.eszservice.service.doctor.ICacheDoctorService;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.doctor.IDoctorOnlineService;
import com.sinohealth.eszservice.service.doctor.IDoctorService;
import com.sinohealth.eszservice.service.doctor.exception.AccountDuplicateException;
import com.sinohealth.eszservice.service.doctor.exception.AccountValidateException;
import com.sinohealth.eszservice.service.doctor.exception.DoctorTokenValidException;
import com.sinohealth.eszservice.service.doctor.exception.EntityNotCacheException;
import com.sinohealth.eszservice.service.doctor.exception.PasswordValidateException;
import com.sinohealth.eszservice.service.doctor.exception.RegisterException;
import com.sinohealth.eszservice.service.email.MailSender;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.sms.SMS;
import com.sinohealth.eszservice.service.visit.ISzSubjectService;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

@Service("doctorService")
public class DoctorServiceImpl implements IDoctorService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDoctorDao doctorDao;
	@Autowired
	private IProvinceService provinceService; // 省份
	@Autowired
	private IHospitalService hospitalService; // 医院
	@Autowired
	private IDisciplineService disciplineService;// 专类分类
	@Autowired
	private ITitleService titleService;// 职称分类
	@Autowired
	private IGradeService gradeService;
	@Autowired
	private IGradeItemService gradeItemService;
	@Autowired
	private IDoctorCountService doctorCountService;
	@Autowired
	private ISzSubjectService szSubjectService;
	@Autowired
	private IAppNameService appNameService;
	@Autowired
	private ICacheDoctorNonceService cacheDoctorNonceService;
	@Autowired
	private IDoctorOnlineService doctorOnlineService;
	@Autowired
	private ICacheDoctorRandomService cacheDoctorRandomService;
	@Autowired
	private ICacheDoctorService cacheDoctorService;

	@Override
	public DoctorLoginDto login(String account, String pwdHash, String nOnce,
			String appName) {
		DoctorLoginDto dto = new DoctorLoginDto();

		try {
			if ("".equals(account) || "".equals(pwdHash) || "".equals(nOnce)) {
				throw new Exception("缺少参数");
			}

			validateAccount(account);

			// 随机字符串与上次登录相同，应该换一个新的随机字符串
			validateNonce(account, nOnce);

			DoctorEntity doctor = isMobile(account) ? findByMobile(account)
					: findByEmail(account);
			logger.debug("doctor login(byEmail or by mobile:)" + doctor);
			if (null == doctor) {
				throw new SystemErrorExecption("用户名无效或已删除",
						DoctorLoginDto.ERRCODE_OTHERS);
			}

			if ((doctor.getDelFlag() != null) && (doctor.getDelFlag() == 1)) {
				throw new SystemErrorExecption("此用户已删除", BaseDto.ERRCODE_OTHERS);
			}

			if ((doctor.getStatus() != null) && (doctor.getStatus() == 0)) {
				throw new SystemErrorExecption("此用户已被禁用",
						BaseDto.ERRCODE_OTHERS);
			}
			System.out.println("pwdHash:" + pwdHash);
			System.out.println("doctor.getPwd() + nOnce:"
					+ encryptPassword(doctor.getPwd() + nOnce));
			if (!encryptPassword(doctor.getPwd() + nOnce).equals(pwdHash)) {
				throw new PasswordValidateException(
						ErrorMessage
								.getConfig(DoctorLoginDto.ERRCODE_PWD_VILIDATE),
						DoctorLoginDto.ERRCODE_PWD_VILIDATE);
			}

			// 找到专业
			String szSubject = appNameService.getSzSubjectId(appName);

			String token = finishLogin(doctor.getId(), szSubject);
			doctor.setAppName(appName);

			dto.setToken(token);
			dto.setDoctor(doctor);

			boolean subjectJoined = doctor.containSzSubject(szSubject);
			dto.setSubjectJoined(subjectJoined); // 是否参加了此随访专科

		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountValidateException e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (PasswordValidateException e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("医生登录异常: {}", e.getMessage());
			logger.debug("医生登录异常: {}", e);
			dto.setErrCode(DoctorLoginDto.ERRCODE_OTHERS);
			dto.setErrMsg(e.getMessage());
		}

		return dto;
	}

	public void validateNonce(String account, String nOnce)
			throws PasswordValidateException {
		if (cacheDoctorNonceService.isNonceCached(account, nOnce)) {
			throw new PasswordValidateException(
					ErrorMessage.getConfig(DoctorLoginDto.ERRCODE_RADOM_REPEAT),
					DoctorLoginDto.ERRCODE_RADOM_REPEAT);
		} else {
			cacheDoctorNonceService.cacheNonce(account, nOnce);
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
	 * 密码加密，默认使用hex256加密
	 * 
	 * @param password
	 * @return
	 */
	public String encryptPassword(String password) {
		return DigestUtils.sha256Hex(password).toUpperCase();
	}

	@Override
	public DoctorRegisterDto register(String account, String pwd,
			String recommended, String appName) {
		DoctorRegisterDto dto = new DoctorRegisterDto();

		try {
			if (!"".equals(recommended)) {
				if (!isEmail(recommended) && !isMobile(recommended)) {
					if (!(isNumber(recommended))) {
						throw new SystemErrorExecption(
								"请输入正确格式的推荐人(邮箱，手机或用户ID)",
								BaseDto.ERRCODE_OTHERS);
					}

					if ((recommended.length() != 7)) {
						throw new SystemErrorExecption(
								"请输入正确格式的推荐人(邮箱，手机或用户ID)",
								BaseDto.ERRCODE_OTHERS);
					}
				}
			}
			validateRecommendCode(recommended);

			String szSubjectId = appNameService.getSzSubjectId(appName);

			validateAccount(account);

			DoctorEntity doctor = new DoctorEntity();
			DoctorEntity existsObj = null;

			boolean isMobile = isMobile(account);
			// 先查是否已经被注册
			if (isMobile) {
				existsObj = findByMobile(account);
			} else { // Email
				existsObj = findByEmail(account);
			}
			// System.out.println("existsObj====: "+existsObj);
			if (null != existsObj) {
				throw new AccountDuplicateException("账号： " + account + "已存在",
						DoctorRegisterDto.ERRCODE_OTHERS);
			}

			// 判断账号是电话号码还是email
			if (isMobile) {
				doctor.setMobile(account);
			} else { // Email
				doctor.setEmail(account);
			}

			// 解释并设置密码
			try {
				String password = EncryptUtil.decryptDes(pwd,
						Constants.DOCTOR_REG_ENCRYPT_KEY);
				// logger.debug("解析的密码：{}", password);
				doctor.setPwd(password);
			} catch (Exception e) {
				// 密码解密失败
				throw new RegisterException(
						ErrorMessage
								.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD),
						DoctorRegisterDto.ERRCODE_REG_FAILD);
			}
			// 验证注册专业
			doctor.setRecommended(recommended); // 推荐人
			doctor.setStatus(Constants.DOCTOR_STATUS);
			doctor.setAppName(appName);
			doctor.setRegisterDate(new Date());
			String recommendCode = StringUtil.getRandom();
			DoctorEntity recoDoctor = findByRecommendCode(recommendCode);
			boolean isExist = false;
			if (recoDoctor != null) {
				isExist = true;
			}
			// 判断随机码是否已存在，存在则继续生成
			while (isExist) {
				recommendCode = StringUtil.getRandom();
				recoDoctor = findByRecommendCode(recommendCode);
				if (recoDoctor == null) {
					break;
				}
			}
			doctor.setRecommendCode(recommendCode);
			String qrCode = createQRCode(doctor);
			doctor.setQrCode(qrCode);
			DoctorEntity saved = doctorDao.save(doctor);

			String token = finishLogin(saved.getId(), szSubjectId);

			dto.setToken(token);
			dto.setDoctor(saved);

			// 保存统计记录
			// doctorCountService.add(saved.getId(), szSubject);
		} catch (SystemErrorExecption e) { // 输入错误的appName
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountValidateException e) { // 账号格式
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountDuplicateException e) { // 账号重复
			dto.setErrCode(DoctorRegisterDto.ERRCODE_REG_ACCOUNT_REPEAT);
			dto.setErrMsg(ErrorMessage
					.getConfig(DoctorRegisterDto.ERRCODE_REG_ACCOUNT_REPEAT));
			return dto;
		} catch (RegisterException e) { // 可预见的异常
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
			return dto;
		} catch (Exception e) { // 无法预见的异常，比如保存数据库时数据重复。。。
			logger.warn("Doctor Register Error: {}", e);
			dto.setErrCode(DoctorRegisterDto.ERRCODE_REG_FAILD);
			dto.setErrMsg(ErrorMessage
					.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD));
		}

		return dto;
	}

	@Override
	public DoctorRegisterDto registerc(String account, String pwd,
			String recommended, String appName, int disciplineId,
			String disciplineName, String name, int provinceId, int hospitalId,
			String title, String cert) {
		DoctorRegisterDto dto = new DoctorRegisterDto();
		try {
			if (!"".equals(recommended)) {
				if (!isEmail(recommended) && !isMobile(recommended)) {
					if (!(isNumber(recommended))) {
						System.err.println("aaaa" + recommended);
						throw new SystemErrorExecption(
								"请输入正确格式的推荐人(邮箱，手机或用户ID)",
								BaseDto.ERRCODE_OTHERS);
					}

					if ((recommended.length() != 7)) {
						System.err.println("bbb" + recommended);
						throw new SystemErrorExecption(
								"请输入正确格式的推荐人(邮箱，手机或用户ID)",
								BaseDto.ERRCODE_OTHERS);
					}
				}
			}
			validateRecommendCode(recommended);

			// 找到专业
			String szSubject = appNameService.getSzSubjectId(appName);

			validateAccount(account);

			DoctorEntity doctor = new DoctorEntity();
			DoctorEntity existsObj = null;

			boolean isMobile = isMobile(account);

			// 先查是否已经被注册
			if (isMobile) {
				existsObj = findByMobile(account);
			} else {
				existsObj = findByEmail(account);
			}
			if (null != existsObj) {
				throw new AccountDuplicateException("账号： " + account + "已存在",
						DoctorRegisterDto.ERRCODE_OTHERS);
			}

			if (isMobile) {
				doctor.setMobile(account);
			} else {
				doctor.setEmail(account);
			}

			// 解释并设置密码
			try {
				String password = EncryptUtil.decryptDes(pwd,
						Constants.DOCTOR_REG_ENCRYPT_KEY);
				// logger.debug("解析的密码：{}", password);
				doctor.setPwd(password);
			} catch (Exception e) {
				// 密码解密失败
				throw new RegisterException(
						ErrorMessage
								.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD),
						DoctorRegisterDto.ERRCODE_REG_FAILD);
			}

			// 专业信息
			DisciplineEntity disciplineObj = disciplineService
					.getById(disciplineId);
			if (null == disciplineObj) {
				throw new RegisterException(
						ErrorMessage
								.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD),
						DoctorRegisterDto.ERRCODE_REG_FAILD);
			}
			doctor.setDiscipline(disciplineObj);// 如果是其它专业，处理doctor实体与专业实体之间的关系

			ProvinceEntity provinceObj = provinceService.getById(provinceId);
			if (null == provinceObj) { // 无法找到相应省份信息
				throw new RegisterException(
						ErrorMessage
								.getConfig(ConstantSickUserErrs.ERRCODE_NO_THIS_PROVINCE),
						ConstantSickUserErrs.ERRCODE_NO_THIS_PROVINCE);
			}
			doctor.setProvince(provinceObj);

			HospitalEntity hospitalObj = hospitalService.getById(hospitalId);
			if (null == hospitalObj) { // 无法找到相应医院信息
				throw new RegisterException(
						ErrorMessage
								.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD),
						DoctorRegisterDto.ERRCODE_REG_FAILD);
			}
			hospitalObj
					.setDoctorVisitCount(hospitalObj.getDoctorVisitCount() + 1);
			doctor.setHospital(hospitalObj);

			TitleEntity titleObj = titleService.getByTitle(title);// 找到相应的职称
			if (null == titleObj) {
				throw new RegisterException(
						ErrorMessage
								.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD),
						DoctorRegisterDto.ERRCODE_REG_FAILD);
			}
			doctor.setTitle(titleObj);
			doctor.setCat(Constants.DOCTOR_C_LEVEL_REGISTER);
			doctor.setCert(cert);
			doctor.setName(name);
			doctor.setRecommended(recommended); // 推荐人
			doctor.setStatus(Constants.DOCTOR_STATUS);
			doctor.setAppName(appName);
			doctor.setRegisterDate(new Date());

			Assert.hasLength(szSubject, "错误的参数appName，找不到相应的科室");

			doctor.addSzSubject(szSubject);

			String recommendCode = StringUtil.getRandom();
			DoctorEntity recoDoctor = findByRecommendCode(recommendCode);
			boolean isExist = false;
			if (recoDoctor != null) {
				isExist = true;
			}
			// 判断随机码是否已存在，存在则继续生成
			while (isExist) {
				recommendCode = StringUtil.getRandom();
				recoDoctor = findByRecommendCode(recommendCode);
				if (recoDoctor == null) {
					break;
				}
			}

			doctor.setRecommendCode(recommendCode);
			String qrCode = createQRCode(doctor);
			doctor.setQrCode(qrCode);
			DoctorEntity saved = doctorDao.save(doctor);

			String token = finishLogin(saved.getId(), szSubject);

			dto.setToken(token);

			dto.setDoctor(saved);

			dto.setSubjectJoined(true); // 参加了此专业

			// 保存统计记录
			doctorCountService.add(saved.getId(), szSubject);

		} catch (SystemErrorExecption e) { // 输入错误的appName
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountValidateException e) { // 账号格式
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (AccountDuplicateException e) { // 账号重复
			dto.setErrCode(DoctorRegisterDto.ERRCODE_REG_ACCOUNT_REPEAT);
			dto.setErrMsg(e.getMessage());
			return dto;
		} catch (java.lang.IllegalArgumentException e) { // 数据异常
			dto.setErrCode(DoctorRegisterDto.ERRCODE_REG_FAILD);
			dto.setErrMsg(ErrorMessage
					.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD));
			return dto;
		} catch (RegisterException e) { // 可预见的异常
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
			return dto;
		} catch (Exception e) { // 无法预见的异常，比如保存数据库时数据重复。。。
			logger.warn("Doctor Register Error: {}", e);
			dto.setErrCode(DoctorRegisterDto.ERRCODE_REG_FAILD);
			dto.setErrMsg(ErrorMessage
					.getConfig(DoctorRegisterDto.ERRCODE_REG_FAILD));
		}

		return dto;
	}

	public void validateRecommendCode(String recommender)
			throws SystemErrorExecption {
		if (!"".equals(recommender)) {
			if (isEmail(recommender) || isMobile(recommender)
					|| recommender.length() == 7) {// 推荐 参 数为三个其中之一

				DoctorEntity doctor = new DoctorEntity();
				if (isEmail(recommender)) {
					doctor = findByEmail(recommender);
					if (null == doctor) {
						throw new SystemErrorExecption("推荐码" + recommender
								+ "记录不存在", BaseDto.ERRCODE_OTHERS);
					}
				}
				if (isMobile(recommender)) {
					doctor = findByMobile(recommender);
					if (null == doctor) {
						throw new SystemErrorExecption("推荐码" + recommender
								+ "记录不存在", BaseDto.ERRCODE_OTHERS);
					}
				}
				if (recommender.length() == 7 && !isEmail(recommender)) {
					doctor = findByRecommendCode(recommender);
					if (null == doctor) {
						throw new SystemErrorExecption("推荐码" + recommender
								+ "记录不存在", BaseDto.ERRCODE_OTHERS);
					}
				}
			}
		}
	}

	/**
	 * 实现登录：生成Token，并保存到redis缓存
	 * 
	 * @param userId
	 * @return
	 */
	public String finishLogin(Integer userId, String szSubject) {
		String token = Identities.uuid2();

		DoctorEntity doctor = get(userId);

		Calendar cal = Calendar.getInstance();

		// Date prevLoginDate = doctor.getLastLoginDate(); // 上一次登录的时间

		update(doctor);

		long lt = doctorOnlineService.getLastVisitTime(token);

		Date lastVisitDate = new Date(lt); // 上次访问时间
		Date today = DateUtils.getDateStart(new Date()); // 今天凌晨

		if (lastVisitDate.compareTo(today) < 0) { // 上次访问时间 早于 今天凌晨
			// 为免缓存不正确，需要到数据库去再查一次，确定用户最后登录的时间
			gradeService.addAction(doctor.getId(),
					GradeKeys.doctorDailyLoginKey, szSubject);
		}
		// 缓存最后访问时间
		doctorOnlineService.setLastVisitTime(token, (cal.getTimeInMillis()));

		// 缓存token与doctorId的关系
		doctorOnlineService.cacheToken(token, userId, szSubject,
				(cal.getTimeInMillis()), 1);

		return token;
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

	/**
	 * 验证是否手机号码格式
	 * 
	 * @param s
	 * @return
	 */
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
	public boolean isEmail(String s) {
		String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		return m.matches();
	}

	@Override
	public DoctorEntity get(Integer id) {
		// 如果缓存中已经有用户数据，则从缓存中查找，如果没有，则从数据库中查找
		DoctorEntity o;
		try {
			o = cacheDoctorService.get(id);
		} catch (EntityNotCacheException e) {
			o = doctorDao.get(id);
			cacheDoctorService.cache(id, o);
		}
		return o;
	}

	@Override
	public DoctorEntity get(String token) {
		// 从缓存中找到对应的userId的关系
		Integer userId = doctorOnlineService.getUserId(token);
		return (null != userId) ? get(userId) : null;
	}

	public IDoctorDao getDoctorDao() {
		return doctorDao;
	}

	public void setDoctorDao(IDoctorDao doctorDao) {
		this.doctorDao = doctorDao;
	}

	@Override
	public DoctorProfileDto getProfile(String token, String appName) {
		DoctorProfileDto dto = new DoctorProfileDto();

		String szSubject;
		try {
			szSubject = appNameService.getSzSubjectId(appName);
		} catch (AppNameNotSupportException e) {
			dto.setErrCode(DoctorProfileDto.ERRCODE_OTHERS);
			dto.setErrMsg("错误的appName:" + e.getAppName());
			return dto;
		}

		DoctorEntity doctor = get(token);

		if (null == doctor) { // 无此医生
			dto.setErrCode(DoctorProfileDto.ERRCODE_NO_THIS_DOCTOR);
			dto.setErrMsg("无此医生");
			return dto;
		}

		dto.setDoctor(doctor);

		DoctorCountEntity doctorCount = doctorCountService.get(doctor.getId(),
				szSubject);
		dto.setTotalGrade(doctorCount.getTotalGrade());

		return dto;
	}

	@Override
	public DoctorExperienceCertDto getDoctorExperienceCert(String token) {
		DoctorExperienceCertDto dto = new DoctorExperienceCertDto();
		DoctorEntity doctor = get(token);
		dto.setDoctor(doctor);
		return dto;
	}

	@Override
	public void update(DoctorEntity doctor) {
		doctorDao.update(doctor);
		cacheDoctorService.updateCached(doctor);
	}

	@Override
	public DoctorModifyDto update(String token, Integer disciplineId,
			String disciplineName, String name, int provinceId, int hospitalId,
			String title, String cert, String szSubject) {
		DoctorModifyDto dto = new DoctorModifyDto();
		try {
			// validateToken(token); // 校验token，交由拦截器处理

			DoctorEntity doctor = get(token);

			if (null == doctor) {
				throw new Exception("找不到医生信息，可能是数据库和缓存有错误？");
			}

			DisciplineEntity discipline = disciplineService
					.getById(disciplineId);
			if (null == discipline) { // 找不到相应的专业
				throw new NoDisciplineFoundException();
			}
			doctor.setDiscipline(discipline);

			ProvinceEntity province = provinceService.getById(provinceId);
			if (null == province) {
				throw new NoProvinceFoundException();
			}
			doctor.setProvince(province);

			// 医院
			HospitalEntity hospital = hospitalService.getById(hospitalId);
			if (null == hospital) {
				throw new NoHospitalFoundException();
			}
			hospital.setDoctorVisitCount(hospital.getDoctorVisitCount() + 1);
			doctor.setHospital(hospital);

			// 职称
			TitleEntity titleObj = titleService.getByTitle(title);
			if (null == titleObj) {
				throw new NoTitleFoundException();
			}
			doctor.setTitle(titleObj);

			// 执业证书编号。只有在0-未认证，3-认证失败的状态时，提交才有效
			if (doctor.getCertdStatus() == 0 || doctor.getCertdStatus() == 3) {
				doctor.setCert(cert);
			}

			// TODO 是否应该限制姓名的输入
			doctor.setName(name);

			// 如果是B级用户，则认为是同意了随访
			if (2 != doctor.getCat()) {
				logger.debug("完善个人资料，成为随访用户：szSubject:{},id:{}", szSubject,
						doctor.getId());

				doctor.addSzSubject(szSubject);
				doctor.setCat(Constants.DOCTOR_C_LEVEL_REGISTER);

				// 第一次为修改个人信息为医生加积分
				gradeService.addAction(doctor.getId(),
						GradeKeys.doctorRegisterKey, szSubject);

				// 注册成功发信息通知医生
				SMS.registerSendSms(doctor);

				if (null == doctor.getRecommendCode()) {
					String recommendCode = StringUtil.getRandom();
					DoctorEntity recoDoctor = findByRecommendCode(recommendCode);
					boolean isExist = false;
					if (recoDoctor != null) {
						isExist = true;
					}
					// 判断随机码是否已存在，存在则继续生成
					while (isExist) {
						recommendCode = StringUtil.getRandom();
						recoDoctor = findByRecommendCode(recommendCode);
						if (recoDoctor == null) {
							break;
						}
					}
					doctor.setRecommendCode(recommendCode);
				}
			}

			String qrCode = createQRCode(doctor);
			doctor.setQrCode(qrCode);

			update(doctor);

		} catch (DoctorTokenValidException e) {
			dto.setErrCode(DoctorExperienceCertDto.ERRCODE_TOKEN_INVALID);
			dto.setErrMsg(ErrorMessage
					.getConfig(DoctorExperienceCertDto.ERRCODE_TOKEN_INVALID));
		} catch (NoDisciplineFoundException e) {
			dto.setErrCode(DoctorGradesDto.ERRCODE_OTHERS);
			dto.setErrMsg("找不到相应的专业信息");
		} catch (NoProvinceFoundException e) {
			dto.setErrCode(DoctorGradesDto.ERRCODE_OTHERS);
			dto.setErrMsg("找不到相应的省份信息");
		} catch (NoHospitalFoundException e) {
			dto.setErrCode(DoctorGradesDto.ERRCODE_OTHERS);
			dto.setErrMsg("找不到相应的医院信息");
		} catch (NoTitleFoundException e) {
			dto.setErrCode(DoctorGradesDto.ERRCODE_OTHERS);
			dto.setErrMsg("找不到相应的职称信息");
		} catch (Exception e) {
			logger.warn("修改医生个人信息异常：{}", e);
			dto.setErrCode(DoctorGradesDto.ERRCODE_OTHERS);
			dto.setErrMsg("请求错误");
		}
		return dto;
	}

	@Override
	public DoctorLogoutDto logout(String token, String szSubject) {
		DoctorLogoutDto dto = new DoctorLogoutDto();
		try {
			int doctorId = doctorOnlineService.getUserId(token);
			if (doctorId != 0) {
				dto.setDoctorId(doctorId);
			} else {
				throw new SystemErrorExecption("此用户已退出或token无效",
						DoctorLogoutDto.ERRCODE_SUCCESS);
			}
			doctorOnlineService.deleteToken(token);
		} catch (SystemErrorExecption e) {
			logger.error(e.getMessage());
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public Object updateAgreedescp(String token, String appName) {
		BaseDto dto = new BaseDto();
		String szSubject = appNameService.getSzSubjectId(appName);
		DoctorEntity doctor = get(token);

		if (doctor.containSzSubject(szSubject)) {
			dto.setErrCode(10009);
			dto.setErrMsg("已参加此专科的随访，重复操作");
			return dto;
		}

		if (null == doctor.getName()) {
			dto.setErrCode(ConstantDoctorUserErrs.NOT_COMPLETED_PERSONAL_INFO); // 不是今天首次访问
			dto.setErrMsg(ErrorMessage
					.getConfig(ConstantDoctorUserErrs.NOT_COMPLETED_PERSONAL_INFO));
			return dto;
		}

		doctor.addSzSubject(szSubject);

		doctor.setCat(2);
		update(doctor);

		// 保存统计记录
		doctorCountService.add(doctor.getId(), szSubject);

		// 医生参加另一专科的随访加积分
		gradeService.addAction(doctor.getId(), GradeKeys.doctorRegisterKey,
				szSubject);

		try {
			SMS.registerSendSms(doctor);
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			logger.debug("医生参加另一专科随访发短信失败！");
			return dto;
		}

		return dto;
	}

	public IProvinceService getProvinceService() {
		return provinceService;
	}

	public void setProvinceService(IProvinceService provinceService) {
		this.provinceService = provinceService;
	}

	public IHospitalService getHospitalService() {
		return hospitalService;
	}

	public void setHospitalService(IHospitalService hospitalService) {
		this.hospitalService = hospitalService;
	}

	public IDisciplineService getDisciplineService() {
		return disciplineService;
	}

	public void setDisciplineService(IDisciplineService disciplineService) {
		this.disciplineService = disciplineService;
	}

	public ITitleService getTitleService() {
		return titleService;
	}

	public void setTitleService(ITitleService titleService) {
		this.titleService = titleService;
	}

	@Override
	public SaveHeadShotDto saveHeadShot(String token, String headShotUrl,
			String smallHeadshot) {
		SaveHeadShotDto dto = new SaveHeadShotDto();
		try {
			int userId = doctorOnlineService.getUserId(token);
			DoctorEntity doctor = get(userId);

			if (null != doctor) {
				doctor.setHeadShot(headShotUrl);
				doctor.setSmallHeadshot(smallHeadshot);
				update(doctor);
				dto.setDoctor(doctor);
				dto.setIsSick("0");
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
	public DoctorEntity findByMobile(String mobile) throws SystemErrorExecption {
		Integer userId;
		DoctorEntity entity;
		try {
			userId = cacheDoctorService.getIdByMobile(mobile);

			if (null == userId) { // 已经有mobile的索引，但是没有值，说明是数据库中是没有实体的
				return null; // 直接返回null
			} else {
				entity = get(userId); // 缓存不一定可信，所以还是交给相关方法去缓存或数据库取
			}
		} catch (EntityNotCacheException e) {// 还没有被缓存
			// 如果还没有缓存，就查询数据库，并返回
			entity = doctorDao.findByMobile(mobile);
		}

		// 缓存手机号码与userId的关系
		if (null == entity) {
			// 设置值为null，以便下次不用再找数据库
			cacheDoctorService.cacheMobile(mobile, null);
		} else {
			cacheDoctorService.cacheMobile(mobile, entity.getId());
			cacheDoctorService.cache(entity.getId(), entity);
		}

		return entity;
	}

	@Override
	public DoctorEntity findByEmail(String email) throws SystemErrorExecption {
		DoctorEntity entity;

		Integer userId;
		try {
			userId = cacheDoctorService.getIdByEmail(email);

			if (null == userId) { // 已经有email的索引，但是没有值，说明是数据库中是没有实体的
				return null; // 直接返回null
			} else {
				entity = get(userId); // 缓存不一定可信，所以还是交给相关方法去缓存或数据库取
			}
		} catch (EntityNotCacheException e) {// 还没有被缓存
			// 如果还没有缓存，就查询数据库，并返回
			entity = doctorDao.findByEmail(email);
		}

		// 缓存手机号码与userId的关系
		if (null == entity) {
			// 设置值为null，以便下次不用再找数据库
			cacheDoctorService.cacheEmail(email, null);
		} else {
			cacheDoctorService.cacheEmail(email, entity.getId());
			cacheDoctorService.cache(entity.getId(), entity);
		}

		return entity;
	}

	@Override
	public DoctorEntity findByEmailCode(String emailCode) {
		DoctorEntity entity;
		entity = doctorDao.findByEmailCode(emailCode);
		return entity;
	}

	public String updateEmailCode(String email) {
		String json = "";
		try {
			DoctorEntity doctor = findByEmail(email);

			if (doctor != null) {
				String uuid = Identities.uuid();
				Boolean flag = MailSender.updatePassowrd(doctor.getEmail(), 1,
						uuid);
				if (!flag) {
					json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";

					json = String
							.format(json,
									ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED,
									ErrorMessage
											.getConfig(ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED));
				} else {
					doctor.setEmailCode(uuid);
					doctor.setEmailTime(new Date());
					update(doctor);
					json = "{\"errCode\":0}";
				}
			} else {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED,
								ErrorMessage
										.getConfig(ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED));
			}
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			return new BaseDto(e.getErrCode(), e.getMessage()).toString();
		}

		return json;
	}

	public String checkByMobile(String mobile) {
		String json = "";
		try {
			DoctorEntity doctor = findByMobile(mobile);

			if (doctor != null) {
				Integer randomCode = SMS.sendSms(doctor.getMobile());
				if (randomCode != 0) {
					cacheDoctorRandomService.setRandomCode(
							String.valueOf(doctor.getId()), randomCode + "");
					logger.debug("验证码发送成功：" + randomCode);
					json = "{\"errCode\":0,\"code\":" + randomCode + "}";
				} else {
					json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
					json = String
							.format(json,
									ConstantDoctorUserErrs.FAIL_SEND_CHECKCODE,
									ErrorMessage
											.getConfig(ConstantDoctorUserErrs.FAIL_SEND_CHECKCODE));
				}
			} else {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED,
								ErrorMessage
										.getConfig(ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED));
			}
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			return new BaseDto(e.getErrCode(), e.getMessage()).toString();
		}

		return json;
	}

	public String updatePwdByMobile(String mobile, String pwd, String code) {
		String json = "";
		try {
			DoctorEntity doctor = findByMobile(mobile);
			if (doctor != null) {
				String randomCode = cacheDoctorRandomService
						.getRandomCode(String.valueOf(doctor.getId()));
				if (null == randomCode) {
					json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
					json = String.format(json, BaseDto.ERRCODE_OTHERS,
							"随机验证码错误");
					return json;
				}
				if (randomCode.equals(code)) {
					// 解释并设置密码
					try {
						String password = EncryptUtil.decryptDes(pwd,
								Constants.DOCTOR_REG_ENCRYPT_KEY);
						doctor.setPwd(password);
					} catch (Exception e) {
						e.getStackTrace();
						return new BaseDto(BaseDto.ERRCODE_OTHERS, "密码解释失败")
								.toString();
					}
					update(doctor);
					json = "{\"errCode\":0}";
				} else {
					json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
					json = String
							.format(json,
									ConstantDoctorUserErrs.ERROR_CHECKCODE,
									ErrorMessage
											.getConfig(ConstantDoctorUserErrs.ERROR_CHECKCODE));
				}

			} else {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED,
								ErrorMessage
										.getConfig(ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED));
			}
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			return new BaseDto(e.getErrCode(), e.getMessage()).toString();
		}
		return json;
	}

	@Override
	public DoctorUpdatePwdDto updatePassword(String token, String OldPwd,
			String nonce, String newPwd) {
		DoctorUpdatePwdDto dto = new DoctorUpdatePwdDto();
		try {
			if ("".equals(OldPwd) || "".equals(nonce) || "".equals(newPwd)) {
				throw new Exception("缺少参数");
			}

			int doctorId = doctorOnlineService.getUserId(token);

			DoctorEntity doctor = get(doctorId);
			// System.out.println("doctor: " + doctor);
			if (null == doctor) {
				throw new PasswordValidateException(
						ErrorMessage
								.getConfig(DoctorLoginDto.ERRCODE_PWD_VILIDATE),
						DoctorLoginDto.ERRCODE_PWD_VILIDATE);
			}

			String dbPwd = doctor.getPwd();
			if (!encryptPassword(doctor.getPwd() + nonce).equals(OldPwd)) {
				throw new PasswordValidateException("旧密码错误",
						BaseDto.ERRCODE_OTHERS);
			}
			newPwd = EncryptUtil.decryptDes(newPwd,
					Constants.DOCTOR_REG_ENCRYPT_KEY);
			if (newPwd.equals(dbPwd)) {
				throw new PasswordValidateException(
						ErrorMessage
								.getConfig(DoctorUpdatePwdDto.NEW_PWD_VALIDATION),
						DoctorUpdatePwdDto.NEW_PWD_VALIDATION);
			}
			doctor.setPwd(newPwd);
			update(doctor);
		} catch (PasswordValidateException e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (Exception e) {
			dto.setErrCode(DoctorUpdatePwdDto.ERRCODE_OTHERS);
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public String createQRCode(DoctorEntity doctor) {
		String qrlink = "";
		String qrcode = "";
		InputStream logoIs = null;
		InputStream tplIs = null;
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource logoResource = resourceLoader.getResource("qrLogo.png");
		Resource tplResource = resourceLoader.getResource("qrTemplate.html");

		String uuid = Identities.uuid2();
		String currentPath = this.getClass().getClassLoader().getResource("")
				.getPath();
		String domain = Global.getConfig("share.domain");

		String imgName = "qrcode-" + uuid + ".jpg";
		String imgPath = currentPath + imgName;
		/* 网页内容 */
		String content = "";

		try {
			logoIs = logoResource.getInputStream();
			/* 生成二维码图片 */
			QRCode.createQRCode(doctor.getRecommendCode(), imgPath, logoIs);
			/* 将生成的二维图片上传到七牛 */
			QiniuService.uploadFile(imgName, imgPath);
			/* 删除本地二维码图片 */
			File imgFile = new File(imgPath);
			imgFile.delete();
			/* 二维码在七牛上的访问地址 */
			qrcode = domain + imgName;

			tplIs = tplResource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(tplIs,
					"utf-8"));
			String tempString = "";
			while ((tempString = br.readLine()) != null) {
				content += tempString + "\n";
			}

			String headshot = "http://7xi7xa.com1.z0.glb.clouddn.com/img_qr_doctor.png";
			if (doctor.getHeadShot() != null
					&& !doctor.getHeadShot().equals("")) {
				headshot = QiniuService.zoomOut(doctor.getHeadShot(), 100, 100);
			}

			content = content.replaceAll("\\[headshot\\]", headshot);

			content = content.replaceAll("\\[recommendCode\\]",
					doctor.getRecommendCode());
			if (doctor.getName() != null) {
				content = content.replaceAll("\\[doctorName\\]",
						doctor.getName());
			} else {
				content = content.replaceAll("\\[doctorName\\]", "");
			}
			if (doctor.getHospital() != null) {
				content = content.replaceAll("\\[hospital\\]", doctor
						.getHospital().getHospitalName());
			} else {
				content = content.replaceAll("\\[hospital\\]", "");
			}
			if (doctor.getTitle() != null) {
				content = content.replaceAll("\\[titleName\\]", doctor
						.getTitle().getTitleName());
			} else {
				content = content.replaceAll("\\[titleName\\]", "");
			}
			if (doctor.getDiscipline() != null) {
				content = content.replaceAll("\\[disciplineName\\]", doctor
						.getDiscipline().getDisciplineName());
			} else {
				content = content.replaceAll("\\[disciplineName\\]", "");
			}
			content = content.replaceAll("\\[qrcode\\]", qrcode);

			String registerDate = DateUtils.formatDate(
					doctor.getRegisterDate(), "HHmmss");
			String htmlName = "html-qrcode-" + registerDate
					+ doctor.getRecommendCode() + ".html";
			String htmlPath = currentPath + htmlName;
			/* 生成二维码分享页面 */
			FileUtil.createFile(htmlPath, content);
			QiniuService.uploadFile(htmlName, htmlPath);
			/* 删除本地二维码分享页面 */
			File htmlFile = new File(htmlPath);
			htmlFile.delete();

			/* 二维码页面的访问链接 */
			qrlink = domain + htmlName;
			// System.out.println(qrlink);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return qrlink;
	}

	@Override
	public String checkCode(String mobile, String code) {
		String json = "";
		try {
			DoctorEntity doctor = findByMobile(mobile);
			if (null != doctor) {
				String randomCode = cacheDoctorRandomService
						.getRandomCode(String.valueOf(doctor.getId()));
				// System.out.println("code: " + code + " randomCode: " +
				// randomCode);
				if (null == randomCode) {
					json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
					json = String.format(json, BaseDto.ERRCODE_OTHERS,
							"随机验证码错误");
					return json;
				}

				if (null != code || !"".equals(code)) {
					if (randomCode.equals(code)) {
						json = "{\"errCode\":0}";
					} else {
						json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
						json = String
								.format(json,
										ConstantDoctorUserErrs.ERROR_CHECKCODE,
										ErrorMessage
												.getConfig(ConstantDoctorUserErrs.ERROR_CHECKCODE));
					}
				} else {
					json = "{\"errCode\":2,\"errMsg\":\"请输入验证码\"}";
				}
			} else {
				json = "{\"errCode\":%d,\"errMsg\":\"%s\"}";
				json = String
						.format(json,
								ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED,
								ErrorMessage
										.getConfig(ConstantDoctorUserErrs.DOCTOR_NOT_REGISTERED));
			}
		} catch (SystemErrorExecption e) {
			// TODO: handle exception
		}

		return json;
	}

	@Override
	public DoctorEntity findByRecommendCode(String recommendCode) {
		return doctorDao.findByRecommendCode(recommendCode);
	}
}
