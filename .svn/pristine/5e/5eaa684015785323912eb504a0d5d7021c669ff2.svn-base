package com.sinohealth.eszservice.dto.sick;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class SickProfileDto extends BaseDto {

	private static final long serialVersionUID = -5924005658375118329L;

	private SickEntity sick;

	public SickEntity getSick() {
		return sick;
	}

	public void setSick(SickEntity sick) {
		this.sick = sick;
	}

	public String toString() {
		JSONObject jo = new JSONObject();

		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

			JSONObject sickObj = new JSONObject();
			if (null != sick) {
				sickObj.put("userId", null != sick.getId() ? sick.getId() : 0);
				sickObj.put(
						"account",
						null != sick.getMobile() ? sick.getMobile() : sick
								.getEmail());
				sickObj.put("name", null != sick.getName() ? sick.getName()
						: "");
				sickObj.put("sex", null != sick.getSex() ? sick.getSex() : 2);
				sickObj.put(
						"birthday",
						null != sick.getBirthday() ? DateUtils.formatDate(sick
								.getBirthday()) : "");
				sickObj.put("provinceId", null != sick.getProvince()
						&& null != sick.getProvince().getId() ? sick
						.getProvince().getId() : 0);
				sickObj.put("cityId", (null != sick.getCity() && null != sick
						.getCity().getId()) ? sick.getCity().getId() : 0);

				String url = null != sick.getHeadShot() ? sick.getHeadShot()
						: "";
				if (!"".equals(url)) {
					url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
				}
				sickObj.put("headshot", url);

				String smallHeadshotUrl = (null != sick.getSmallHeadshot()) ? sick
						.getSmallHeadshot() : "";
				if (!"".equals(smallHeadshotUrl)) {
					smallHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
							smallHeadshotUrl);
				}
				sickObj.put("smallHeadshotUrl", smallHeadshotUrl);

				sickObj.put(
						"lastLoginDate",
						null != sick.getLastLoginDate() ? DateUtils
								.formatDateTime(sick.getLastLoginDate()) : "");
				sickObj.put("totalGrade", null != Integer.valueOf(sick
						.getTotalGrade()) ? sick.getTotalGrade() : 0);
				jo.put("userInfo", sickObj);
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

}
