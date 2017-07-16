package com.sinohealth.eszservice.dto.doctor;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

/**
 * 资讯栏目列表dto
 * 
 * @author 黄世莲
 * 
 */
public class DoctorLoginDto extends BaseDto {

	private static final long serialVersionUID = -4911000012335863840L;

	/**
	 * errCode：10001 用户名或者密码错误
	 */
	public static final int ERRCODE_PWD_VILIDATE = 10001;
	/**
	 * errCode：10002 已被禁止登录
	 */
	public static final int ERRCODE_DENIED_LOG = 10002;
	/**
	 * errCode：10003 随机字符串与上次登录相同，应该换一个新的随机字符串
	 */
	public static final int ERRCODE_RADOM_REPEAT = 10003;
	/**
	 * errCode：10004 输入的信息不完成
	 */
	public static final int ERRCODE_NEED_INFO = 10004;

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
			jo.put("errMsg", errMsg);
			JSONObject userInfo = new JSONObject();
			JSONObject serverInfo = new JSONObject();
			userInfo.put("token", token);
			if (null != doctor) {
				userInfo.put("userId", doctor.getId());
				// 登录返回头像headShot，如果没有，则返回空字符串
				String url = (null != doctor.getHeadShot()) ? doctor
						.getHeadShot() : "";

				if (!"".equals(url)) {
					// 带七牛url地址的原图url
					url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
				}
				userInfo.put("headshot", url);
				
				String samllHeadshotUrl = (null != doctor.getSmallHeadshot()) ? doctor
						.getSmallHeadshot() : "";
				if (!"".equals(samllHeadshotUrl)) {
					// 带七牛url地址的缩略图url
					samllHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
							samllHeadshotUrl);
				}			
				userInfo.put("smallHeadshot", samllHeadshotUrl);
				userInfo.put("subjectJoined", subjectJoined ? 1 : 0);
				String account = ((null != doctor.getMobile()) && !""
						.endsWith(doctor.getMobile())) ? doctor.getMobile()
						: doctor.getEmail();
				userInfo.put("account", account);
				userInfo.put("infoCompleted",
						null != doctor.getDiscipline() ? 1 : 0);
				userInfo.put("name",
						(null != doctor.getName()) ? doctor.getName() : "");
				userInfo.put("status", doctor.getStatus());
				userInfo.put("certdStatus", doctor.getCertdStatus());
				userInfo.put("recommendCode", null!=doctor.getRecommendCode()?doctor.getRecommendCode():"");
				userInfo.put("qrCode", null!=doctor.getQrCode()?doctor.getQrCode():"");

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

	public boolean isSubjectJoined() {
		return subjectJoined;
	}

	public void setSubjectJoined(boolean subjectJoined) {
		this.subjectJoined = subjectJoined;
	}

}
