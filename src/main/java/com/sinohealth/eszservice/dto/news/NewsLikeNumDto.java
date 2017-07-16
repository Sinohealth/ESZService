package com.sinohealth.eszservice.dto.news;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.news.NewsEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class NewsLikeNumDto extends BaseDto {

	private static final long serialVersionUID = 5757921083936630563L;

	/**
	 * errCode：11001  版本无变化，无需更新
	 */
	public static final int ERRCODE_NOT_Change = 11001;
	
	/**
	 * errCode：11002 服务异常
	 */
	public static final int ERRCODE_SERVICE_ERROR = 11002;
	
	private NewsEntity news;
	
	 public NewsEntity getNews() {
		return news;
	}

	public void setNews(NewsEntity news) {
		this.news = news;
	}

	public String toString() {
			JSONObject jo = new JSONObject();
			try {
				jo.put("errCode", errCode);
				if (null != news) {
					jo.put("likeNum", news.getLikeNum());	
				}
				if (null != errMsg && (!"".equals(errMsg))) {
					jo.put("errMsg", errMsg);
				}
			} catch (JSONException e) {
				return "{}";
			}
			return jo.toString();
		}
}
