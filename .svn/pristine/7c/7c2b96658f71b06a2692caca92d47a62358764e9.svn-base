package com.sinohealth.eszservice.dto.sick;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class SickLoginDto extends BaseDto {

	private static final long serialVersionUID = -8543902551998265642L;

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
			jo.put("errMsg", errMsg);
			JSONObject userInfo = new JSONObject();
			JSONObject serverInfo = new JSONObject();
			userInfo.put("token", token);
			if (null != sick) {
				userInfo.put("userId", sick.getId());
				// 登录返回头像headShot，如果没有，则返回空字符串
				String url = null != sick.getHeadShot() ? sick.getHeadShot()
						: "";
				if (!"".equals(url)) {
					url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
				}

				userInfo.put("headshot", url);

				String smallHeadshotUrl = (null != sick.getSmallHeadshot()) ? sick
						.getSmallHeadshot() : "";
				if (!"".equals(smallHeadshotUrl)) {
					smallHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
							smallHeadshotUrl);
				}
				userInfo.put("smallHeadshotUrl", smallHeadshotUrl);

				userInfo.put(
						"account",
						(null != sick.getMobile() ? sick.getMobile() : sick
								.getEmail()));
				userInfo.put("name", (null != sick.getName() ? sick.getName()
						: ""));
				userInfo.put("totalGrade", sick.getTotalGrade());

				// 主备服务器
				serverInfo.put("main", Global.getConfig("server.main"));
				serverInfo.put("backup", Global.getConfig("server.backup"));
			}
			jo.put("userInfo", userInfo);
			jo.put("server", serverInfo);

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

}
