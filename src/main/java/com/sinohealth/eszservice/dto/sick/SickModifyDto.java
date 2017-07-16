package com.sinohealth.eszservice.dto.sick;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 患者修改个人信息DTO
 * 
 * @author 陈学宏
 * 
 */
public class SickModifyDto extends BaseDto {

	private static final long serialVersionUID = -4911000012335863840L;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			jo.put("errMsg", errMsg);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

}
