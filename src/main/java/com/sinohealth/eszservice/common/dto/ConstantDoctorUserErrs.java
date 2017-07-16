package com.sinohealth.eszservice.common.dto;

public class ConstantDoctorUserErrs {

	/**
	 * errCode：10001 用户名或者密码错误
	 */
	public static final int ERRCODE_PWD_VILIDATE = 10001;
	/**
	 * errCode：10002 已被禁止登录
	 */
	public static final int ERRCODE_DENIED_LOG = 10002;
	/**
	 * errCode：10003 随机字符串与上次登录相同，应该换一个新的随机字符串
	 */
	public static final int ERRCODE_RADOM_REPEAT = 10003;
	/**
	 * errCode：10004 输入的信息不完成
	 */
	public static final int ERRCODE_NEED_INFO = 10004;


	/**
	 * ErrCode:10005 注册失败
	 */
	public static final int ERRCODE_REG_FAILD = 10005;

	/**
	 * errCode：10006 无此医生
	 */
	public static final int ERRCODE_NO_THIS_DOCTOR = 10006;


	/**
	 * errCode：10009 已参加此专科的随访，重复操作
	 */
	public static final int DUPLICATE_SZSUBJECT_DOCTOR = 10009;

	/**
	 * errCode：10010 个人信息不完整
	 */
	public static final int NOT_COMPLETED_PERSONAL_INFO = 10010;

	/**
	 * 新密码与旧密码不能相同
	 */
	public static final int NEW_PWD_VALIDATION = 10011;

	/**
	 * 用户存在但发送短信验证码失败
	 */
	public static final int FAIL_SEND_CHECKCODE = 10012;

	/**
	 * 验证码不正确
	 */
	public static final int ERROR_CHECKCODE = 10013;
	
	/**
	 * 错误的appName
	 */
	public static final int ERROR_appName = 10014;
	
	/**
	 * 找回密码时此医生未注册
	 */
	public static final int DOCTOR_NOT_REGISTERED = 10015;
}
