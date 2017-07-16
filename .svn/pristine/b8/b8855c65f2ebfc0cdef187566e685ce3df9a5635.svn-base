package com.sinohealth.eszservice.dto.visit;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class SickDetailDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 569701207339942513L;

	private SickEntity sick;

	private Integer doctorId;

	private String subject;

	private List<ApplicationEntity> list;

	public SickEntity getSick() {
		return sick;
	}

	public void setSick(SickEntity sick) {
		this.sick = sick;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public List<ApplicationEntity> getList() {
		return list;
	}

	public void setList(List<ApplicationEntity> list) {
		this.list = list;
	}

	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			JSONObject joUser = new JSONObject();
			JSONObject joProfile = new JSONObject();
			if (list != null) {
				if (null != sick) {
					joProfile.put("name",
							null != sick.getName() ? sick.getName() : "");
					// 返回头像headShot，如果没有，则返回空字符串
					String url = (null != sick.getHeadShot()) ? sick
							.getHeadShot() : "";
					if (!"".equals(url)) {
						// 带七牛url地址的原图url
						url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
					}

					joProfile.put("headshot", url);

					String samllHeadshotUrl = (null != sick.getSmallHeadshot()) ? sick
							.getSmallHeadshot() : "";
					if (!"".equals(samllHeadshotUrl)) {
						// 带七牛url地址的缩略图url
						samllHeadshotUrl = QiniuService.getDownloadUrl(
								Space.PERSONAL, samllHeadshotUrl);
					}
					joProfile.put("smallHeadshot", samllHeadshotUrl);
				} else {
					joProfile.put("name", "");
					joProfile.put("headshot", "");
					joProfile.put("smallHeadshot", "");
				}

				joUser.put("sickProfile", joProfile);
				joUser.put("applyList", changeJsonList(list));
				jo.put("visitList", joUser);
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public String getJsonByString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			JSONObject joUser = new JSONObject();
			JSONObject joProfile = new JSONObject();
			if (list != null) {
				if (null != sick) {
					joProfile.put("sickId", null != sick.getId() ? sick.getId()
							: "");

					joProfile.put("sickName",
							null != sick.getName() ? sick.getName() : "");

					joProfile.put("sex", sick.getSex());

					int curYear = Integer.parseInt(DateUtils.getYear());
					int userYear = Integer.parseInt(DateUtils.formatDate(
							sick.getBirthday(), "yyyy"));
					joProfile.put("age", curYear - userYear);

					int height = sick.getHeight();
					float weight = sick.getWeight();
					if (height == 0) {
						joProfile.put("bmi", "0");
					} else if (weight == 0) {
						joProfile.put("bmi", "0");
					} else {
						float bmi_f = (float) weight
								/ (((float) height / 100.00f) * ((float) height / 100.00f));
						float bmi = (float) (Math.round(bmi_f * 100)) / 100;
						joProfile.put("bmi", bmi + "");
					}

					// 返回头像headShot，如果没有，则返回空字符串
					String url = (null != sick.getHeadShot()) ? sick
							.getHeadShot() : "";
					if (!"".equals(url)) {
						// 带七牛url地址的原图url
						url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
					}

					joProfile.put("headshot", url);

					String samllHeadshotUrl = (null != sick.getSmallHeadshot()) ? sick
							.getSmallHeadshot() : "";
					if (!"".equals(samllHeadshotUrl)) {
						// 带七牛url地址的缩略图url
						samllHeadshotUrl = QiniuService.getDownloadUrl(
								Space.PERSONAL, samllHeadshotUrl);
					}
					joProfile.put("smallHeadshot", samllHeadshotUrl);
				} else {
					joProfile.put("sickName", "");
					joProfile.put("sickId", "");
					joProfile.put("sex", "");
					joProfile.put("age", "");
					joProfile.put("bmi", "");
					joProfile.put("headshot", "");
					joProfile.put("smallHeadshot", "");
				}

				joUser.put("sickProfile", joProfile);
				joUser.put("applyList", changeJsonList(list));
				jo.put("visitList", joUser);
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray changeJsonList(List<ApplicationEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				// 随访状态的范围（1，2 ，3，4，5）
				int[] status = new int[] { 1, 2, 3, 4, 5 };
				boolean flag = ArrayUtils.contains(status, null != list.get(i)
						.getVisitStatus() ? list.get(i).getVisitStatus() : 0);
				if (flag) {
					JSONObject jo = new JSONObject();
					jo.put("szSubject",
							null != list.get(i).getSzSubject() ? list.get(i)
									.getSzSubject().getName() : "");
					jo.put("applyTime",
							null != list.get(i).getApplyTime() ? DateUtils
									.formatDate(list.get(i).getApplyTime(),
											"yyyy-MM-dd") : "");
					jo.put("finishTime",
							null != list.get(i).getFinishTime() ? DateUtils
									.formatDate(list.get(i).getFinishTime(),
											"yyyy-MM-dd") : "");
					jo.put("expFinishTime",
							null != list.get(i).getExpectedFinishTime() ? DateUtils
									.formatDate(list.get(i)
											.getExpectedFinishTime(),
											"yyyy-MM-dd") : "");
					jo.put("visitStatus",
							null != list.get(i).getVisitStatus() ? list.get(i)
									.getVisitStatus() : 0);
					String tempSubject = null != list.get(i).getSzSubject()
							.getId() ? list.get(i).getSzSubject().getId() : "";
					int doctorId = (null != list.get(i).getDoctor() && null != list
							.get(i).getDoctor().getId()) ? list.get(i)
							.getDoctor().getId() : 0;
					jo.put("applyId",
							(!subject.equals(tempSubject) || doctorId != this.doctorId
									.intValue()) ? 0 : list.get(i).getApplyId());
					ja.put(jo);
				}

			}
		}
		return ja;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
