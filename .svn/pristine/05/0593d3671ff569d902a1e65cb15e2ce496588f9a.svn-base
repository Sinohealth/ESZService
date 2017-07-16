package com.sinohealth.eszservice.dto.visit;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class CaseDtov104 extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private List<HealthArchiveLog> list;

	private Map<String, List<HealthArchiveLog>> map;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		JSONObject cases = new JSONObject();
		try {
			jo.put("errCode", errCode);
			System.err.print("errMsg: "+errMsg);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

			//System.out.println("getValue:leaveHosPics: "+getValue("leaveHosPics"));
			cases.put("leaveHosPics",
					convertJsonList(getValue("leaveHosPics")));
			cases.put("casesPics", convertJsonList(getValue("casesPics")));

			jo.put("cases", cases);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray convertJsonList(List<HealthArchiveLog> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo = new JSONObject();
				jo.put("imgId", list.get(i).getImgId());
				jo.put("itemId", null != Integer.valueOf(list.get(i)
						.getItemId()) ? list.get(i).getItemId() : 0);
				
				// 返回头像img，如果没有，则返回空字符串
				String url = (null != list.get(i).getImg()) ? list.get(i).getImg() : "";
				String samllHeadshotUrl = (null != list.get(i).getThumb()) ? list.get(i).getThumb() : "";
				// 带七牛url地址的原图url
				String img = QiniuService.getDownloadUrl(Space.RECORD, url);
				// 带七牛url地址的缩略图url
				String thumb = QiniuService.getDownloadUrl(Space.RECORD,
						samllHeadshotUrl);
				
				jo.put("img", img);
				jo.put("thumb", thumb);
							
				jo.put("status", null != Integer.valueOf(list.get(i)
						.getStatus()) ? list.get(i).getStatus() : "");
				jo.put("postTime", null != list.get(i).getPostTime() ? DateUtils.formatDate(list
						.get(i).getPostTime(), "yyyy-MM-dd") : "");

				ja.put(jo);
			}
		}

		return ja;
	}

	public List<HealthArchiveLog> getValue(String key) {
		List<HealthArchiveLog> list = map.get(key);
		return list;
	}

	public List<HealthArchiveLog> getList() {
		return list;
	}

	public void setList(List<HealthArchiveLog> list) {
		this.list = list;
	}

	public Map<String, List<HealthArchiveLog>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<HealthArchiveLog>> map) {
		this.map = map;
	}

}
