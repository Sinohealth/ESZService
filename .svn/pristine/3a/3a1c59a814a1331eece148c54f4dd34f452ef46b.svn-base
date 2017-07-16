package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.AppCurPhaseComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class ApplicationDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private ApplicationEntity application;

	private List<ApplicationEntity> list;

	private Integer lastPage;

	private int isPaging;

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public Integer getLastPage() {
		return lastPage;
	}

	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			if (isPaging == 1) {
				jo.put("lastPage", lastPage);
			}

			if (null != list) {
				jo.put("waitingSicks", changeJsonList(list));
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
			JSONObject jo = new JSONObject();
			jo.put("applyId", null != list.get(i).getApplyId() ? list.get(i)
					.getApplyId() : 0);

			// 患者
			SickEntity sick = list.get(i).getSick();
			if (null != sick) {
				jo.put("sickId", null != sick.getId() ? sick.getId() : 0);
				jo.put("sex", null != sick.getSex() ? sick.getSex() : 2);
				// 登录返回头像headShot，如果没有，则返回空字符串
				String url = (null != sick.getHeadShot()) ? sick.getHeadShot()
						: "";
				if (!"".equals(url)) {
					// 带七牛url地址的原图url
					url = "".equals(url) ? url : QiniuService.getDownloadUrl(
							Space.PERSONAL, url);
				}
				jo.put("headshot", url);

				String samllHeadshotUrl = (null != sick.getSmallHeadshot()) ? sick
						.getSmallHeadshot() : "";
				if (!"".equals(samllHeadshotUrl)) {
					// 带七牛url地址的缩略图url
					samllHeadshotUrl = "".equals(samllHeadshotUrl) ? samllHeadshotUrl
							: QiniuService.getDownloadUrl(Space.PERSONAL,
									samllHeadshotUrl);
				}
				jo.put("smallHeadshot", samllHeadshotUrl);
			}

			jo.put("applyTime",
					null != list.get(i).getApplyTime() ? DateUtils
							.formatDate(list.get(i).getApplyTime()) : "");
			jo.put("finishTime",
					null != list.get(i).getFinishTime() ? DateUtils
							.formatDate(list.get(i).getFinishTime()) : "");
			jo.put("expFinishTime",
					null != list.get(i).getExpectedFinishTime() ? DateUtils
							.formatDate(list.get(i).getExpectedFinishTime())
							: "");
			jo.put("reportStatus",
					(null != list.get(i).getCurPhase() && null != list.get(i)
							.getCurPhase().getReportStatus()) ? list.get(i)
							.getCurPhase().getReportStatus() : 0);

			if (1 == list.get(i).getVisitStatus()) {
				jo.put("status", null != list.get(i).getVisitStatus() ? list
						.get(i).getVisitStatus() : 0);
			} else if (2 == list.get(i).getVisitStatus()
					|| 5 == list.get(i).getVisitStatus()) {
				AppCurPhaseComponent curPhase = list.get(i).getCurPhase();
				if (null != curPhase) {
					jo.put("timePoint",
							null != curPhase.getCurTimePoint() ? curPhase
									.getCurTimePoint() : 0);
					jo.put("cycleUnit",
							null != curPhase.getCycleUnit() ? curPhase
									.getCycleUnit() : 0);
					jo.put("fuZhenStatus",
							null != curPhase.getFuZhenStatus() ? curPhase
									.getFuZhenStatus() : 0);
				}
				jo.put("caseHistoryRate", list.get(i).getRateCount()
						.getCaseHistoryRate());
			}
			jo.put("sickName", null != list.get(i).getSick().getName() ? list
					.get(i).getSick().getName() : "");

			ja.put(jo);
		}
		return ja;
	}

	public int getIsPaging() {
		return isPaging;
	}

	public void setIsPaging(int isPaging) {
		this.isPaging = isPaging;
	}

	public List<ApplicationEntity> getList() {
		return list;
	}

	public void setList(List<ApplicationEntity> list) {
		this.list = list;
	}

}
