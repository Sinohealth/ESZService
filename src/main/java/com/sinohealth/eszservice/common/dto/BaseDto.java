package com.sinohealth.eszservice.common.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BaseDto implements Serializable, IGenericDto {

	private static final long serialVersionUID = -6462577721939297237L;

	/**
	 * errCode: 0 表示成功
	 */
	public static final int ERRCODE_SUCCESS = 0;

	/**
	 * errCode: 1 Token无效
	 */
	public static final int ERRCODE_TOKEN_INVALID = 1;

	/**
	 * errCode: 2 其它失败
	 */
	public static final int ERRCODE_OTHERS = 2;

	/**
	 * errCode: 3 账号在另一台设备上登录，需要重新登录
	 */
	public static final int ERRCODE_LOGINED_OTHERWAY = 3;

	/**
	 * errCode: 4 没有权限访问
	 */
	public static final int ERRCODE_ACCESS_DENIED = 4;

	/**
	 * errCode: 5 系统异常
	 */
	public static final int ERRCODE_SYSTEM_ERROR = 5;

	protected int errCode = 0;

	protected String errMsg = "";

	/**
	 * 错误代码，一般来说，0表示成功执行。默认为0
	 * 
	 * @return
	 */
	public int getErrCode() {
		return errCode;
	}

	/**
	 * 错误代码，一般来说，0表示成功执行。默认为0
	 * 
	 * @return
	 */
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public BaseDto(int errCode, String errMsg) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public BaseDto(String errMsg) {
		super();
		this.errMsg = errMsg;
	}

	public BaseDto() {
		super();
	}

	public BaseDto(int errCode) {
		this.errCode = errCode;
	}

	/**
	 * 
	 * @param picsStr
	 *            picsStr 数据库获取url字符串 url分解并列表转为list集合
	 * @return
	 */
	public List<String> strToList(String picsStr) {
		List<String> list = new ArrayList<String>();
		if (null != picsStr) {
			list = Arrays.asList(picsStr.split(","));
		}
		return list;
	}

	public String dateFormat(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{\"errCode\":");
		buf.append(errCode);

		if (null != errMsg && !"".equals(errMsg)) {
			buf.append(",\"errMsg\":\"");
			errMsg = errMsg.replaceAll("\\\\", "\\\\\\\\"); // 全部斜杠 ”\
															// “替换为双斜杠”\\“
			errMsg = errMsg.replaceAll("\\\"", "\\\\\""); // 全部双引号”"“替换为”\"“
			buf.append(errMsg);
			buf.append("\"");
		}
		buf.append("}");

		return buf.toString();
	}

}
