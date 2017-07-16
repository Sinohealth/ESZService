package com.sinohealth.eszservice.dto.news;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.news.NewsEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class NewsIsRecommendDto extends BaseDto {

	private static final long serialVersionUID = -2447711377223178521L;

	private List<NewsEntity> list;

	private List<NewsEntity> adList;

	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

			if (null != list) {
				jo.put("list", changeJsonList(list, adList));
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray changeJsonList(List<NewsEntity> list,
			List<NewsEntity> adList) throws JSONException {
		JSONArray ja = new JSONArray();
		int newsCount = list.size();
		int adCount = adList.size();
//		System.out.println("news.size: " + newsCount + "  adCount: " + adCount);
		if (adCount > 3) {
//			System.out.println("adCount > 3");
			if (newsCount != 0 && newsCount != 0) {
				if ((adCount + newsCount) <= 4) {
					ja = getJsonList(list, adList, newsCount, adCount);
				} else {
					ja = getJsonList(list, adList, Math.abs(adCount - 3), 3);
				}
			} else {
				ja = getJsonList(list, adList, newsCount, adCount);
			}

		} else if (adCount <= 3) {
//			System.out.println("adCount <= 3");
			if (adCount != 0 && newsCount != 0) {
				if ((adCount + newsCount) <= 4) {
					ja = getJsonList(list, adList, newsCount, adCount);
				} else {
					ja = getJsonList(list, adList, Math.abs(4 - adCount),
							adCount);
				}
			} else {
				ja = getJsonList(list, adList, newsCount, adCount);
			}
		}
		return ja;
	}

	public JSONArray getJsonList(List<NewsEntity> list,
			List<NewsEntity> adList, int newsCount, int adCount)
			throws JSONException {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < newsCount; i++) {
			JSONObject jo = new JSONObject();
			jo.put("id", null != list.get(i).getId() ? list.get(i).getId() : "");
			jo.put("titlePic", null != list.get(i).getTitlePic() ? list.get(i)
					.getTitlePic() : "");
			jo.put("title", null != list.get(i).getTitle() ? list.get(i)
					.getTitle() : "");
			jo.put("contentUrl", null != list.get(i).getShareUrl() ? list
					.get(i).getShareUrl() : "");
			jo.put("type", null != list.get(i).getType() ? list.get(i).getType() : "");
			jo.put("adOrNews",  "1");
			ja.put(jo);
		}

		for (int i = 0; i < adCount; i++) {
			JSONObject jo = new JSONObject();
			jo.put("id", null != adList.get(i).getId() ? adList.get(i).getId()
					: "");
			jo.put("titlePic", null != adList.get(i).getTitlePic() ? adList
					.get(i).getTitlePic() : "");
			jo.put("title", null != adList.get(i).getTitle() ? adList.get(i)
					.getTitle() : "");
			jo.put("contentUrl", null != adList.get(i).getShareUrl() ? adList
					.get(i).getShareUrl() : "");
			jo.put("adOrNews",  "2");
			ja.put(jo);
		}
		return ja;
	}

	public List<NewsEntity> getList() {
		return list;
	}

	public void setList(List<NewsEntity> list) {
		this.list = list;
	}

	public List<NewsEntity> getAdList() {
		return adList;
	}

	public void setAdList(List<NewsEntity> adList) {
		this.adList = adList;
	}
}
