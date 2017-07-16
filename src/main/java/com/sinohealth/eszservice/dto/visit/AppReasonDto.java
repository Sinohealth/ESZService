package com.sinohealth.eszservice.dto.visit;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class AppReasonDto extends BaseDto {

	private static final long serialVersionUID = -1641997373264903819L;

	
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			System.out.println("errorMsg: "+errMsg);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}
}
