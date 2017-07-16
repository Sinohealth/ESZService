package com.sinohealth.eszservice.dto.news;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.news.NewsColsEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class NewsColsDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -4602532175500611986L;
	
	private NewsColsDtoElem newsCol;
	
	/**
	 * errCode：11001  版本无变化，无需更新
	 */
	public static final int ERRCODE_NOT_CHANGE = 11001;
	
	/**
	 * errCode：11002 信息不完整
	 */
	public static final int ERRCODE_NEED_INFO = 11002;

	public NewsColsDtoElem getNewsCol() {
		return newsCol;
	}

	public void setNewsCol(NewsColsDtoElem newsCol) {
		this.newsCol = newsCol;
	}
	
	public void setNewsCol(Integer ver, List<NewsColsEntity> list) {
		this.newsCol =new NewsColsDtoElem(ver,list);
	}
	
	 public String toString() {
			JSONObject jo = new JSONObject();

			try {
				jo.put("errCode", errCode);
				if (null != errMsg && (!"".equals(errMsg))) {
					jo.put("errMsg", errMsg);
				}
				JSONObject versionObj = new JSONObject();
				if (null != newsCol) {
					versionObj.put("ver", newsCol.getVer());
					versionObj.put("list", changeJsonList(newsCol.getList()));
					jo.put("newsCols", versionObj);
				}
			} catch (JSONException e) {
				return "{}";
			}
			return jo.toString();
		}

	public JSONArray changeJsonList(List<NewsColsEntity> newsCols)
			throws JSONException {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < newsCols.size(); i++) {
			JSONObject newsObj = new JSONObject();
			newsObj.put("colId", newsCols.get(i).getId());
			newsObj.put("colName",
					null != newsCols.get(i).getColName() ? newsCols.get(i)
							.getColName() : "");
			if (null != newsCols.get(i).getOrd()) {				
				newsObj.put("ord", newsCols.get(i).getOrd());
			} else {
				newsObj.put("ord", "");
			}
			ja.put(newsObj);
		}
		return ja;
	}

}
