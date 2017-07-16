package com.sinohealth.eszservice.dto.visit;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class TakeMedicineDto extends BaseDto {

	private static final long serialVersionUID = -1281565835630426075L;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();

		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}


}
