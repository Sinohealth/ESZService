package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.FinishedReportEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class FinishedReportDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private FinishedReportEntity finishedReport;

	private PaginationSupport pagination;

	private Integer lastPage;

	public FinishedReportEntity getFinishedReport() {
		return finishedReport;
	}

	public void setFinishedReport(FinishedReportEntity finishedReport) {
		this.finishedReport = finishedReport;
	}

	public PaginationSupport getPagination() {
		return pagination;
	}

	public void setPagination(PaginationSupport pagination) {
		this.pagination = pagination;
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
			jo.put("lastPage", lastPage);
			if (null != pagination && null != pagination.getItems()) {
				jo.put("waitingSicks", changeJsonList(pagination.getItems()));
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray changeJsonList(List<FinishedReportEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject jo = new JSONObject();

			jo.put("sickId", null != list.get(i).getSickId() ? list.get(i)
					.getSickId() : 0);
			jo.put("sex", null != list.get(i).getSex() ? list.get(i).getSex()
					: 2);
			// 登录返回头像headShot，如果没有，则返回空字符串
			String url = (null != list.get(i).getHeadshot()) ? list.get(i)
					.getHeadshot() : "";
			if (!"".equals(url)) {
				// 带七牛url地址的原图url
				url = "".equals(url) ? url : QiniuService.getDownloadUrl(
						Space.PERSONAL, url);
			}
			jo.put("headshot", url);

			String samllHeadshotUrl = (null != list.get(i).getSmallHeadshot()) ? list
					.get(i).getSmallHeadshot() : "";
			if (!"".equals(samllHeadshotUrl)) {
				// 带七牛url地址的缩略图url
				samllHeadshotUrl = "".equals(samllHeadshotUrl) ? samllHeadshotUrl
						: QiniuService.getDownloadUrl(Space.PERSONAL,
								samllHeadshotUrl);
			}
			jo.put("smallHeadshot", samllHeadshotUrl);

			jo.put("applyId", null != list.get(i).getApplyId() ? list.get(i)
					.getApplyId() : 0);
			jo.put("timePoint", null != list.get(i).getTimePoint() ? list
					.get(i).getTimePoint() : 0);
			jo.put("cycleUnit", null != list.get(i).getCycleUnit() ? list
					.get(i).getCycleUnit() : 0);
			jo.put("finishTime",
					null != list.get(i).getFinishTime() ? DateUtils
							.formatDate(list.get(i).getFinishTime()) : "");
			jo.put("applyTime",
					null != list.get(i).getApplyTime() ? DateUtils
							.formatDate(list.get(i).getApplyTime()) : "");
			jo.put("comment", null != list.get(i).getComment() ? list.get(i)
					.getComment() : "");
			jo.put("stars", null != list.get(i).getStars() ? list.get(i)
					.getStars() : 0);
			jo.put("sickName", null != list.get(i).getSickName() ? list.get(i)
					.getSickName() : "");
			ja.put(jo);
		}
		return ja;
	}

}
