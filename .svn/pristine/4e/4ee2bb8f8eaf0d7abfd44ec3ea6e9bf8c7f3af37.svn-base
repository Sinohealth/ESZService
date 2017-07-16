package com.sinohealth.eszservice.dto.doctor;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 医生修改个人信息DTO
 * 
 * @author 黄世莲
 * 
 */
public class DoctorModifyDto extends BaseDto {

	private static final long serialVersionUID = -4911000012335863840L;

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

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null!= errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

}
