package com.sinohealth.eszservice.common.dto;

public class ConstantSickVisitErrs {

	/**
	 * errCode：10301 请已提交或待同意状态下拒绝
	 */
	public static final int ERRCODE_REJECT_STATUS_ERROR = 10301;

	/**
	 * errCode：10302 请输入正确的REASONID入
	 */
	public static final int ERRCODE_REASONID_ERROR = 10302;

	/**
	 * errCode：10303 找不到默认的原因
	 */
	public static final int ERRCODE_NOT_FOUND_DEFAULT_REASON = 10303;

	/**
	 * errCode：10304 保存其它原因失败
	 */
	public static final int ERRCODE_FAIL_SAVE_OTHER_REASON = 10304;

	/**
	 * errCode：10305评价未开放
	 */
	public static final int COMMENT_NOT_OPENED = 10305;

	/**
	 * errCode：10306服药数据已提交
	 */
	public static final int DUPLICATE_SUBMIT_TAKED = 10306;

	/**
	 * errCode：10307 找不到阶段信息
	 */
	public static final int PHASE_NOT_FOUND = 10307;

	/**
	 * errCode：10308 时间未到，不需要复诊
	 */
	public static final int PHASE_TIME_NOT_YET = 10308;

	/**
	 * errCode：10309 处方参数medicines解析错误
	 */
	public static final int PHASE_PARAMS_ERR_MEDICINES = 10309;

	/**
	 * errCode：10310 检验项参数checkItems解析错误
	 */
	public static final int PHASE_PARAMS_ERR_CHECKITEMS = 10310;

	/**
	 * errCode：10311 检查项参数checkPics解析错误
	 */
	public static final int PHASE_PARAMS_ERR_CHECKPICS = 10311;

	/**
	 * errCode：10312中止退出时至少录入一个有效的原因ID
	 */
	public static final int INVALID_REASONID_OTHERREASON = 10312;

	/**
	 * errCode：10313找不到医生，请重新输入医生的信息
	 */
	public static final int DOCTOR_NOT_EXIST = 10313;

	/**
	 * errCode：10314申请单重复，不能申请相同的病种
	 */
	public static final int APPLICATION_DUPLICATION = 10314;

	/**
	 * errCode：10315 处方、检验、检查至少有一项数据不为空
	 */
	public static final int COMMIT_EMPTY_PHASE_DATA = 10315;

	/**
	 * errCode：10316 检查项参数checkValues解析错误
	 */
	public static final int PHASE_PARAMS_ERR_CHECKVALUES = 10316;

	/**
	 * 数据无更新
	 */
	public static final int DICTIONARY_VERSION_NO_UPDATE = 10501;
}
