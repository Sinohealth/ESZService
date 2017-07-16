package com.sinohealth.eszservice.dto.visit;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class SaveCommentDto extends BaseDto {

	private static final long serialVersionUID = -4494153372643633081L;
	
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
		} catch (JSONException e) {
			{}
		}
		return jo.toString();
	}

}
