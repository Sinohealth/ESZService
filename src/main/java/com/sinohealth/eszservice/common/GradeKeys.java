package com.sinohealth.eszservice.common;

/**
 * 积分项列表
 * 
 * @author 黄世莲
 * 
 */
public class GradeKeys {

	/**
	 * 推荐好友送积分
	 */
	public static final String doctorInviteFriends = "DOCTOR_INVITE_FRIENDS";

	/**
	 * 医生每日登录行为名
	 */
	public final static String doctorDailyLoginKey = "DOCTOR_DAILY_LOGIN";

	/**
	 * 医生注册行为名
	 */
	public final static String doctorRegisterKey = "DOCTOR_REGISTER";

	/**
	 * 医生接受随访
	 */
	public final static String doctorAcceptVisitKey = "DOCTOR_ACCEPT_VISIT";

	/**
	 * 与1名患者建立随访
	 * 
	 * @see {com.sinohealth.eszservice.common.GradeKeys.doctorAcceptVisitKey =
	 *      "DOCTOR_ACCEPT_VISIT}
	 */
	// public final static String DOCTOR_CREAT_EFU = "DOCTOR_CREAT_EFU";

	/**
	 * 医生每与10位患者建立随访关系
	 */
	public final static String doctorTenVisitRelationship = "DOCTOR_CREAT_EFU_BOUNCE";

	/**
	 * 医生完成随访周期奖励
	 */
	public final static String DOCTOR_DONE_PER_EFU_CYCLE = "DOCTOR_DONE_PER_EFU_CYCLE";

	/**
	 * 获取5星好评
	 */
	public final static String DOCTOR_5SCORE_EFU = "DOCTOR_5SCORE_EFU";

	/**
	 * 参与调查
	 */
	public final static String DOCTOR_SURVEY = "DOCTOR_SURVEY";

	/**
	 * =============== 患者部分 ===================
	 */

	/**
	 * 完善健康档案
	 */
	public final static String updatedHealthArchive = "PATIENT_UpdateHealth_ARCHIVE";

	/**
	 * 意见反馈
	 */
	public final static String PATIENT_FEEDBACK = "PATIENT_FEEDBACK";

	/**
	 * 患者每日登录行为名
	 */
	public final static String sickDailyLoginKey = "SICK_DAILY_LOGIN";

	/**
	 * 患者注册行为名
	 */
	public final static String sickRegisterKey = "PATIENT_REGISTER";

	/**
	 * 提交复诊报告
	 */
	public final static String updatedSubsequentVisitData = "PATIENT_SUBMIT_REPORT";

	/**
	 * 患者完成随访周期奖励
	 */
	public final static String PATIENT_DONE_PER_EFU_CYCLE = "PATIENT_DONE_PER_EFU_CYCLE";

	/**
	 * 按时服药：每个随访周期内的服药依从性&ge;90%获得积分奖励
	 */
	public static final String PATIENT_TAKE_MEDICINE = "PATIENT_TAKE_MEDICINE";

	/**
	 * 每1个随访周期获得医生5星好评
	 */
	public static final String PATIENT_5SCORE_EFU = "PATIENT_5SCORE_EFU";
}
