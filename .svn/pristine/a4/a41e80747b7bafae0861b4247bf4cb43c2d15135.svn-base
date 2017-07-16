package com.sinohealth.eszservice.dto.doctor;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class DoctorRegisterDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 2853972894556063905L;

	/**
	 * ErrCode:10004 账号重复注册
	 */
	public static final int ERRCODE_REG_ACCOUNT_REPEAT = 10004;

	/**
	 * ErrCode:10005 注册失败
	 */
	public static final int ERRCODE_REG_FAILD = 10005;

	private String token;

	private DoctorEntity doctor;

	/**
	 * 医生是否参加了此随访专科
	 */
	private boolean subjectJoined = false;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && !"".equals(errMsg)) {
				jo.put("errMsg", errMsg);
			}
			JSONObject userInfo = new JSONObject();
			if (null != token) {
				userInfo.put("token", token);
			}
			if (null != doctor) {
				userInfo.put("userId", doctor.getId());

				userInfo.put("headshot",
						(null != doctor.getHeadShot()) ? doctor.getHeadShot()
								: "");
				userInfo.put("smallHeadshot", (null != doctor
						.getSmallHeadshot()) ? doctor.getSmallHeadshot() : "");
				userInfo.put("subjectJoined", subjectJoined ? 1 : 0);
				String account = ((null != doctor.getMobile()) && !""
						.endsWith(doctor.getMobile())) ? doctor.getMobile()
						: doctor.getEmail();
				userInfo.put("account", account);
				userInfo.put("qrCode",
						null != doctor.getQrCode() ? doctor.getQrCode() : "");
				userInfo.put("infoCompleted",
						null != doctor.getDiscipline() ? 1 : 0);
			}
			jo.put("userInfo", userInfo);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public boolean isSubjectJoined() {
		return subjectJoined;
	}

	public void setSubjectJoined(boolean subjectJoined) {
		this.subjectJoined = subjectJoined;
	}

}
