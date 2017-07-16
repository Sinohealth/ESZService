package com.sinohealth.eszservice.service.sick;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.dto.sick.SaveHeadShotDto;
import com.sinohealth.eszservice.dto.sick.SickLoginDto;
import com.sinohealth.eszservice.dto.sick.SickLogoutDto;
import com.sinohealth.eszservice.dto.sick.SickModifyDto;
import com.sinohealth.eszservice.dto.sick.SickProfileDto;
import com.sinohealth.eszservice.dto.sick.SickRegisterDto;
import com.sinohealth.eszservice.dto.sick.SickRegisterDtoV103;
import com.sinohealth.eszservice.dto.sick.SickUpdatePwdDto;
import com.sinohealth.eszservice.service.base.exception.NoCityFoundException;
import com.sinohealth.eszservice.service.base.exception.NoProvinceFoundException;
import com.sinohealth.eszservice.service.doctor.exception.RegisterException;
import com.sinohealth.eszservice.service.sick.exception.AccountDuplicateException;
import com.sinohealth.eszservice.service.sick.exception.AccountValidateException;
import com.sinohealth.eszservice.service.sick.exception.PasswordValidateException;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

public interface ISickService {

	/**
	 * 注册帐号
	 * 
	 * @param account
	 *            帐号必填
	 * 
	 * @param pwd
	 *            必填，终端云端按约定密钥约定加密算法进行加密 V103返回的用户信息统一完整返回； 增加身高、体重二个参数；
	 * @return
	 */
	SickEntity registerV103(String account, String pwd, String appName,
			String token) throws SystemErrorExecption,
			AccountValidateException, AccountDuplicateException,
			RegisterException, Exception;

	/**
	 * 注册帐号
	 * 
	 * @param account
	 *            帐号必填
	 * 
	 * @param pwd
	 *            必填，终端云端按约定密钥约定加密算法进行加密
	 * 
	 * @return
	 */
	SickRegisterDto register(String account, String pwd, String appName);

	/**
	 * 
	 * @param account
	 *            手机号或邮箱
	 * @param pwdHash
	 *            Hex256(hex256(密码)+nOnce)
	 * @param nOnce
	 *            随机字符串
	 * @return
	 */
	SickLoginDto login(String account, String pwdHash, String nOnce,
			String appName);

	/**
	 * 
	 * @param account
	 *            手机号或邮箱
	 * @param pwdHash
	 *            Hex256(hex256(密码)+nOnce)
	 * @param nOnce
	 *            随机字符串
	 * @return
	 */
	SickEntity loginV103(String account, String pwdHash, String nOnce,
			String appName, String token) throws SystemErrorExecption,
			AccountValidateException, PasswordValidateException, Exception;

	SickLogoutDto logout(String token);

	SickEntity get(Integer id);

	/**
	 * 获取患者信息
	 * 
	 * @param token
	 * @return
	 */
	SickEntity getProfile(String token) throws SystemErrorExecption;

	/**
	 * 用token找患者
	 * 
	 * @param token
	 * @return
	 */
	SickEntity get(String token);

	/**
	 * 修改患者信息
	 * 
	 * @param sick
	 */
	void update(SickEntity sick);

	/**
	 * 设置患者头相
	 * 
	 * @param token
	 * @param headShotUrl
	 * @return
	 */
	SaveHeadShotDto saveHeadShot(String token, String headShotUrl,
			String smallHeadshot);

	/**
	 * 3.2.7 修改患者个人信息
	 * 
	 * @param token
	 * @param name
	 * @param provinceId
	 * @param cityId
	 * @param sex
	 * @param birthday
	 * @return
	 */
	SickModifyDto update(String token, String name, Integer provinceId,
			Integer cityId, int sex, String birthday);

	/**
	 * 3.2.7.2 V1.3版本
	 * 
	 * @param token
	 * @param name
	 * @param provinceId
	 * @param cityId
	 * @param sex
	 * @param birthday
	 * @return
	 */
	SickEntity update(String token, String name, Integer provinceId,
			Integer cityId, int sex, String birthday, int height, float weight)
			throws SystemErrorExecption, NoProvinceFoundException,
			NoCityFoundException, Exception;

	// SickEntity save(SickEntity sick);

	/**
	 * 根据mobile找到Sick的信息
	 * 
	 * @param mobile
	 * @return
	 */
	public SickEntity findByMobile(String mobile);

	/**
	 * 根据email找到Sick的信息
	 * 
	 * @param email
	 * @return
	 */
	public SickEntity findByEmail(String email);

	/**
	 * 根据emailCode找到Sick的信息
	 * 
	 * @param emailCode
	 */
	public SickEntity findByEmailCode(String emailCode);

	/**
	 * 根据email检查患者是否存在
	 * 
	 * @param email
	 * @return
	 */
	public String updateEmailCode(String email);

	/**
	 * 根据mobile检查患者是否存在
	 * 
	 * @param mobile
	 * @return
	 */
	public String checkByMobile(String mobile);

	/**
	 * 根据mobile修改患者的密码
	 * 
	 * @param mobile
	 * @return
	 */
	public String updatePwdByMobile(String mobile, String pwd, String code);

	/**
	 * 3.2.13 修改用户密码
	 * 
	 * @param token
	 * @param OldPwd
	 * @param nonce
	 * @param newPwd
	 * @return
	 */
	SickUpdatePwdDto updatePassword(String token, String OldPwd, String nonce,
			String newPwd);

	String checkCode(String mobile, String code);

	/**
	 * 更新患者总积分
	 * 
	 * @param sickId
	 */
	void updateTotalGrade(int sickId);
}
