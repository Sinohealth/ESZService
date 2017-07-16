package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class HealthCheckDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private List<CheckItemValueEntity> items;

	private List<VisitImgEntity> imgs;
	
	private List<VisitImgEntity> itemImgs;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		JSONObject checks = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			checks.put("checkItems", convertJsonList(items));
			checks.put("inspectionPics", convertImgJsonList(itemImgs));
			checks.put("checkPics", convertImgJsonList(imgs));
			jo.put("checks", checks);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray convertJsonList(List<CheckItemValueEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo = new JSONObject();
				jo.put("resultId", list.get(i).getResultId());
				jo.put("itemId",
						null != Integer.valueOf(list.get(i).getVisitItem()
								.getItemId()) ? list.get(i).getVisitItem()
								.getItemId() : 0);
				jo.put("value", null != list.get(i).getReportValue() ? list
						.get(i).getReportValue() : 0);
				jo.put("warnLevel", null != Integer.valueOf(list.get(i)
						.getReportWarnLevel()) ? list.get(i)
						.getReportWarnLevel() : 0);

				jo.put("postTime",
						null != list.get(i).getReportTime() ? DateUtils
								.formatDate(list.get(i).getReportTime(),
										"yyyy-MM-dd") : "");
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray convertImgJsonList(List<VisitImgEntity> imgList)
			throws JSONException {
		JSONArray ja = new JSONArray();
		if (null != imgList && imgList.size() > 0) {
			for (int i = 0; i < imgList.size(); i++) {
				JSONObject jo = new JSONObject();

				//System.out.println("img : "+i+" :"+imgList.get(i));
				jo.put("imgId", imgList.get(i).getImgId());
				jo.put("itemId", null != Integer.valueOf(imgList.get(i)
						.getItemId()) ? imgList.get(i).getItemId() : 0);

				// 返回头像img，如果没有，则返回空字符串
				String url = (null != imgList.get(i).getImg()) ? imgList.get(i)
						.getImg() : "";
				if (!"".equals(url)) {
					// 带七牛url地址的原图url
					url = QiniuService.getDownloadUrl(Space.RECORD, url);
				}
				jo.put("img", url);

				String samllHeadshotUrl = (null != imgList.get(i).getThumb()) ? imgList
						.get(i).getThumb() : "";
				if (!"".equals(samllHeadshotUrl)) {
					// 带七牛url地址的缩略图url
					samllHeadshotUrl = QiniuService.getDownloadUrl(Space.RECORD,
							samllHeadshotUrl);
				}
				jo.put("thumb", samllHeadshotUrl);

				jo.put("status", null != Integer.valueOf(imgList.get(i)
						.getStatus()) ? imgList.get(i).getStatus() : 0);
				jo.put("remarks", null != imgList.get(i).getRemarks() ? imgList
						.get(i).getRemarks() : "");
				jo.put("postTime",
						null != imgList.get(i).getPostTime() ? DateUtils
								.formatDate(imgList.get(i).getPostTime(),
										"yyyy-MM-dd") : "");
				ja.put(jo);
			}
		}
		return ja;
	}

	public List<CheckItemValueEntity> getItems() {
		return items;
	}

	public void setItems(List<CheckItemValueEntity> items) {
		this.items = items;
	}

	public List<VisitImgEntity> getImgs() {
		return imgs;
	}

	public void setImgs(List<VisitImgEntity> imgs) {
		this.imgs = imgs;
	}

	public List<VisitImgEntity> getItemImgs() {
		return itemImgs;
	}

	public void setItemImgs(List<VisitImgEntity> itemImgs) {
		this.itemImgs = itemImgs;
	}
}
