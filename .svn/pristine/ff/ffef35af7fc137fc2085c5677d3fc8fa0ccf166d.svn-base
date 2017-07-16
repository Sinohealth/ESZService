package com.sinohealth.eszservice.service.doctor;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.dto.doctor.DoctorExperienceCertDto;
import com.sinohealth.eszservice.dto.doctor.DoctorLoginDto;
import com.sinohealth.eszservice.dto.doctor.DoctorLogoutDto;
import com.sinohealth.eszservice.dto.doctor.DoctorProfileDto;
import com.sinohealth.eszservice.dto.doctor.DoctorRegisterDto;
import com.sinohealth.eszservice.dto.doctor.DoctorUpdatePwdDto;
import com.sinohealth.eszservice.dto.sick.SaveHeadShotDto;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

/**
 * 医生API接口service
 * 
 * @author 黄世莲
 * 
 */
public interface IDoctorService {

	DoctorLoginDto login(String account, String pwdHash, String nOnce,
			String appName);

	DoctorEntity get(Integer id);

	DoctorEntity get(String token);

	/**
	 * 注册账号
	 * 
	 * @param account
	 *            账号
	 * @param pwd
	 *            必填，终端云端按约定密钥约定加密算法进行加密
	 * @param recommended
	 *            选填，推荐人
	 * @param appName
	 *            终端自动带上，APP名称，唯一标识APP，有：<br/>
	 *            eszDoctorGXBForIOS (易随诊冠心病类医生端IOS版本)<br/>
	 *            eszSickGXBForIOS (易随诊冠心病类患者端IOS版本)<br/>
	 *            eszDoctorGXBForAndroid(易随诊冠心病类医生端Andorid版本)<br/>
	 *            eszSickGXBForAndroid(易随诊冠心病类患者端Andorid版本)
	 * 
	 * @return
	 */
	DoctorRegisterDto register(String account, String pwd, String recommended,
			String appName);

	DoctorProfileDto getProfile(String token, String appName);

	DoctorExperienceCertDto getDoctorExperienceCert(String token);

	DoctorLogoutDto logout(String token, String szSubject);

	/**
	 * C级注册
	 * 
	 * @param account
	 *            账号
	 * @param pwd
	 *            必填，终端云端按约定密钥约定加密算法进行加密
	 * @param disciplineId
	 *            必填，专业
	 * @param disciplineName
	 *            必填，专业名称
	 * @param recommended
	 *            选填，推荐人
	 * @param name
	 *            必填，用户姓名
	 * @param province
	 *            必填，省份
	 * @param hospital
	 *            必填，所在医院
	 * @param title
	 *            必填，职称
	 * @param cert
	 *            必填，执业证书编号
	 * @param appName
	 *            终端自动带上，APP名称，唯一标识APP
	 * @return
	 */
	DoctorRegisterDto registerc(String account, String pwd, String recommended,
			String appName,int disciplineId, String disciplineName, String name, int province,
			int hospital, String title, String cert);

	/**
	 * 修改医生个人信息
	 * 
	 * @param disciplineId
	 *            专业ID
	 * @param disciplineName
	 *            专业名称
	 * @param name
	 *            姓名
	 * @param provinceId
	 *            省份ID
	 * @param hospitalId
	 *            医院ID
	 * @param title
	 *            职称
	 * @param cert
	 *            执业证书编号
	 * @return
	 */
	Object update(String token, Integer disciplineId, String disciplineName,
			String name, int provinceId, int hospitalId, String title,
			String cert, String szSubject);

	void update(DoctorEntity doctor);

	Object updateAgreedescp(String token, String appName);

	/**
	 * 设置医生头像
	 * 
	 * @param token
	 * @param headShotUrl
	 * @return
	 */
	SaveHeadShotDto saveHeadShot(String token, String headShotUrl,
			String smallHeadshot);

	/**
	 * 根据mobile找到Doctor的信息
	 * 
	 * @param mobile
	 * @return
	 * @throws SystemErrorExecption 
	 */
	public DoctorEntity findByMobile(String mobile) throws SystemErrorExecption;

	/**
	 * 根据email找到Doctor的信息
	 * 
	 * @param email
	 * @return
	 * @throws SystemErrorExecption 
	 */
	public DoctorEntity findByEmail(String email) throws SystemErrorExecption;

	/**
	 * 根据emailCode找到Doctor的信息
	 * 
	 * @param emailCode
	 * @return
	 */
	public DoctorEntity findByEmailCode(String emailCode);

	/**
	 * 根据email检查医生是否存在
	 * 
	 * @param email
	 * @return
	 */
	public String updateEmailCode(String email);

	/**
	 * 根据mobile检查医生是否存在
	 * 
	 * @param mobile
	 * @return
	 */
	public String checkByMobile(String mobile);

	/**
	 * 根据mobile修改医生的密码
	 * 
	 * @param mobile
	 * @return
	 */
	public String updatePwdByMobile(String mobile, String pwd, String code);

	/**
	 * 3.2.17 修改用户密码
	 * 
	 * @param token
	 * @param OldPwd
	 * @param nonce
	 * @param newPwd
	 * @return
	 */
	DoctorUpdatePwdDto updatePassword(String token, String OldPwd,
			String nonce, String newPwd);

	/**
	 * 创建二维码
	 * 
	 * @param doctor
	 * @return
	 */
	String createQRCode(DoctorEntity doctor);
	

	/**
	 * 3.2.16 检验短信验证码
	 * 
	 * @param token
	 * @param code
	 * @return
	 */
	String checkCode(String mobile, String code);

	boolean isEmail(String recommender);

	boolean isMobile(String recommender);
	
	DoctorEntity findByRecommendCode(String recommendCode);
}
