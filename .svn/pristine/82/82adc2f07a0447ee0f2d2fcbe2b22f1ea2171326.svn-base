package com.sinohealth.eszservice.dto.sick;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class SickRegisterDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -1175628491947956594L;

	private String token;

	private SickEntity sick;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public SickEntity getSick() {
		return sick;
	}

	public void setSick(SickEntity sick) {
		this.sick = sick;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

			if (null != token) {
				jo.put("token", token);
			}
			JSONObject userInfo = new JSONObject();
			if (null != sick) {
				userInfo.put("userId", null != sick.getId() ? sick.getId() : 0);
				userInfo.put("headshot",
						null != sick.getHeadShot() ? sick.getHeadShot() : "");

				userInfo.put(
						"smallHeadshot",
						(null != sick.getSmallHeadshot()) ? sick
								.getSmallHeadshot() : "");
			}
			jo.put("userInfo", userInfo);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}
}
