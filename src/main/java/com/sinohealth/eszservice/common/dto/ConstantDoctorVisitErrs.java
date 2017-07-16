package com.sinohealth.eszservice.common.dto;

/**
 * 医生随访接口错误信息
 * 
 * @author 黄世莲
 * 
 */
public class ConstantDoctorVisitErrs {

	/**
	 * errCode：10205 此医生已参加过随访评价
	 */
	public static final int ERRCODE_DUPLICATE_DOCTOR_PHASEID = 10205;

	/**
	 * errCode：10206 此医生已参加过随访评价
	 */
	public static final int ERRCODE_DUPLICATE_SICK_PHASEID = 10206;

	/**
	 * 申请不是待同意状态##提交失败，稍后再试。
	 */
	public static final int APP_NOT_PENDING_STATUS = 10207;

	/**
	 * 申请已经制定了计划（已经绑定模板）
	 */
	public static final int APP_TEMPLATE_BINED = 10208;

	/**
	 * 解释模板内容失败
	 */
	public static final int TEMPL_PARSE_FAILED = 10209;

	/**
	 * 解释日常结果内容失败
	 */
	public static final int DAILY_RESULT_PARSE_FAILED = 10210;

	/**
	 * 输入错误的模板ID
	 */
	public static final int TEMPL_DELETE_ID_ERROR = 10211;

	/**
	 * 已经批示，不能修改
	 */
	public static final int IMG_HAD_BEEN_MAKRED = 10212;

	/**
	 * 输入的值超出范围（体征或复诊检验项）
	 */
	public static final int VALUE_OUT_OF_RANGE = 10213;

	/**
	 * 必须要选择阶段
	 */
	public static final int PHASE_REQUIRED = 10214;

	/**
	 * 一个阶段至少选择一个检验检查项
	 */
	public static final int VISIT_ITEM_REQUIRED = 10215;

	/**
	 * 解析模板错误
	 */
	public static final int TEMPLATE_CONTENT_INCORRECT = 10216;

	/**
	 * 保存计划时，申请不是待同意/已婉拒状态
	 */
	public static final int APPLY_STATUS_INCORRECT = 10217;

	/**
	 * 输入的申请单ID（applyId）错误
	 */
	public static final int APPLY_ID_INCORRECT = 10218;

	/**
	 * 输入的病种ID错误
	 */
	public static final int DISEASE_ID_INCORRECT = 10219;

	/**
	 * 输入的病种ID与专科ID不一致，也就是说，病种ID不属于专科的病种
	 */
	public static final int DISEASE_AND_SZSUBJECT_ID_INCORRECT = 10220;

	/**
	 * 随访申请病种重复错误
	 */
	public static final int APPLICATION_DISEASE_DUPLICATION = 10221;

	/**
	 * 患者已经提交了复诊报告，所以不能更改随访开始日期。
	 */
	public static final int VISIT_START_DATE_INCORRECT = 10222;
	

}
