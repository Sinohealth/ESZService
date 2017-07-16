package com.sinohealth.eszservice.dto.sick;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class SaveHeadShotDto extends BaseDto {

	private static final long serialVersionUID = 3914551562840915570L;

	private DoctorEntity doctor;

	private SickEntity sick;

	private String isSick;

	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			String sickHeadshot ="";
			if (null!=sick) {
				sickHeadshot = (null != sick.getHeadShot()) ? sick
						.getHeadShot() : "";
			}
			String doctorHeadshot = "";
			if (null!=doctor) {
				doctorHeadshot = (null != doctor.getHeadShot()) ? doctor
						.getHeadShot() : "";
			}

			String url = "1".equals(isSick) ? sickHeadshot : doctorHeadshot;
			if (!"".equals(url)) {
				url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
			}
			jo.put("headshot", url);
			String sickSmallHeadshot = "";
			if (null!=sick) {
				sickSmallHeadshot = (null != sick.getSmallHeadshot()) ? sick
						.getSmallHeadshot() : "";
			}
			String doctorSmallHeadshot ="";
			if (null !=doctor) {
				doctorSmallHeadshot = (null != doctor.getSmallHeadshot()) ? doctor
						.getSmallHeadshot() : "";
			}

			String samllHeadshotUrl = "1".equals(isSick) ? sickSmallHeadshot
					: doctorSmallHeadshot;
			if (!"".equals(samllHeadshotUrl)) {
				samllHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
						samllHeadshotUrl);
			}
			jo.put("smallHeadshot", samllHeadshotUrl);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	public SickEntity getSick() {
		return sick;
	}

	public void setSick(SickEntity sick) {
		this.sick = sick;
	}

	public String getIsSick() {
		return isSick;
	}

	public void setIsSick(String isSick) {
		this.isSick = isSick;
	}
}
