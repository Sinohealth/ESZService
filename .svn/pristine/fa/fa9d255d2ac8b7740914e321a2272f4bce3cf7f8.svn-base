package com.sinohealth.eszservice.dto.doctor;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 医生修改密码
 * 
 * @author 陈学宏
 * 
 */
public class DoctorUpdatePwdDto extends BaseDto {

	private static final long serialVersionUID = -4911000012335863840L;
	
	/**
	 * 新密码与旧密码不能相同
	 */
	public static final int NEW_PWD_VALIDATION = 10011;
	

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
