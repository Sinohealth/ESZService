package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.visit.AppCurPhaseComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class SickApplicationDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private ApplicationEntity application;

	private List<ApplicationEntity> list;

	private List<String> statList;

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public List<ApplicationEntity> getList() {
		return list;
	}

	public void setList(List<ApplicationEntity> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

			if (null != list) {
				jo.put("results", changeJsonList(list));
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray changeJsonList(List<ApplicationEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			ApplicationEntity app = list.get(i);
			JSONObject jo = new JSONObject();
			if (null != statList) {
				// System.out.println("i: "+i+" app.getvistitstat: "+app.getVisitStatus()+" flag: "+statList.contains(String.valueOf(app.getVisitStatus())));
				if (statList.contains(String.valueOf(app.getVisitStatus()))) {
					jo = convertJson(app);
					ja.put(jo);
				}
			} else {
				jo = convertJson(app);
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject convertJson(ApplicationEntity app) throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("applyId", null != app.getApplyId() ? app.getApplyId() : 0);
		jo.put("applyTime",
				null != app.getApplyTime() ? DateUtils.formatDate(app
						.getApplyTime()) : 0);
		jo.put("visitStartTime",
				null != app.getVisitStartDate() ? DateUtils.formatDate(app
						.getVisitStartDate()) : "");
		jo.put("expFinishTime",
				null != app.getExpectedFinishTime() ? DateUtils.formatDate(app
						.getExpectedFinishTime()) : "");
		jo.put("finishTime",
				null != app.getFinishTime() ? DateUtils.formatDate(app
						.getFinishTime()) : "");

		DoctorEntity doctor = app.getDoctor();
		jo.put("doctorId", doctor.getId());
		jo.put("doctorName", doctor.getName());
		jo.put("doctorTitle", doctor.getTitle().getTitleName());
		// 返回头像headShot，如果没有，则返回空字符串
		String url = (null != doctor.getHeadShot()) ? doctor.getHeadShot() : "";
		if (!"".equals(url)) {
			// 带七牛url地址的原图url
			url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
		}
		jo.put("doctorHeadshot", url);

		String samllHeadshotUrl = (null != doctor.getSmallHeadshot()) ? doctor
				.getSmallHeadshot() : "";
		if (!"".equals(samllHeadshotUrl)) {
			// 带七牛url地址的缩略图url
			samllHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
					samllHeadshotUrl);
		}
		jo.put("doctorSmallHeadshot", samllHeadshotUrl);

		jo.put("visitStatus", app.getVisitStatus());

		AppCurPhaseComponent curPhase = app.getCurPhase();
		if (null != curPhase) {
			jo.put("timePoint",
					null != curPhase.getCurTimePoint() ? curPhase
							.getCurTimePoint() : 0);
			jo.put("cycleUnit",
					null != curPhase.getCycleUnit() ? curPhase.getCycleUnit()
							: 0);
			jo.put("curVisitTime",
					null != curPhase.getCurVisitTime() ? DateUtils
							.formatDate(curPhase.getCurVisitTime()) : "");
			jo.put("fuZhenStatus",
					null != curPhase.getFuZhenStatus() ? curPhase
							.getFuZhenStatus() : "");
			jo.put("reportStatus",
					null != curPhase.getReportStatus() ? curPhase
							.getReportStatus() : 0);
		}
		jo.put("caseHistoryRate", null != app.getRateCount() ? app
				.getRateCount().getCaseHistoryRate() : 0);
		jo.put("szSubject", null != app.getSzSubject() ? app.getSzSubject()
				.getId() : "");
		jo.put("szSubjectName", null != app.getSzSubject() ? app.getSzSubject()
				.getName() : "");
		jo.put("diseaseId", (null != app.getDisease() && null != app
				.getDisease().getId()) ? app.getDisease().getId() : "");
		jo.put("diseaseName", (null != app.getDisease() && null != app
				.getDisease().getName()) ? app.getDisease().getName() : "");

		return jo;
	}

	public List<String> getStatList() {
		return statList;
	}

	public void setStatList(List<String> statList) {
		this.statList = statList;
	}

}
