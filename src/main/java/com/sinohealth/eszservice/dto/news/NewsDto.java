package com.sinohealth.eszservice.dto.news;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.news.NewsEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class NewsDto extends BaseDto {

	private static final long serialVersionUID = 5757921083936630563L;

	/**
	 * errCode：11001  版本无变化，无需更新
	 */
	public static final int ERRCODE_NOT_Change = 11001;
	
	/**
	 * errCode：11002 输入的信息不完整
	 */
	public static final int ERRCODE_NEED_INFO = 11002;
	
	private NewsDtoElem newsDtoElem;
	
	private NewsEntity news;

	public NewsDtoElem getNewsDtoElem() {
		return newsDtoElem;
	}

	public void setNewsDtoElem(NewsDtoElem newsDtoElem) {
		this.newsDtoElem = newsDtoElem;
	}
	
	public void setNewsDtoElem(List<NewsEntity> list) {
		this.newsDtoElem = new NewsDtoElem(list);
	}
	
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
				if (null != errMsg && (!"".equals(errMsg))) {
					jo.put("errMsg", errMsg);	
				}
				JSONObject versionObj = new JSONObject();
				if (null != newsDtoElem) {
					jo.put("lastPage", news.getLastPage());
					versionObj.put("list", changeJsonList(newsDtoElem.getList()));
					jo.put("news", versionObj);
				}
			} catch (JSONException e) {
				return "{}";
			}
			return jo.toString();
		}
	 /**
	  * news 集合转为jSON格式的集合
	  * @param news
	  * @return
	  * @throws JSONException
	  */
	 public JSONArray changeJsonList(List<NewsEntity> newsList) throws JSONException {
		 JSONArray ja = new JSONArray();
		 for(int i=0;i< (news.getLastPage() == 1 ? newsList.size() : (newsList.size()-1));i++) {
			 JSONObject jo = new JSONObject();
			 jo.put("newsId", null != newsList.get(i).getId() ? newsList.get(i).getId() : "");
			 jo.put("type", null !=  newsList.get(i).getType() ? newsList.get(i).getType() : "");	 
			 jo.put("title", null !=  newsList.get(i).getTitle() ? newsList.get(i).getTitle() : "");
			 jo.put("summary", null !=  newsList.get(i).getSummary() ? newsList.get(i).getSummary() : "");
			 jo.put("pics", null != newsList.get(i).getPics() ? newsList.get(i).getPics() : "");	 
			 jo.put("publishDate", null !=  newsList.get(i).getPublishDate() ? dateFormat(newsList.get(i).getPublishDate()) : "");	 
			 jo.put("likeNum", null !=  newsList.get(i).getLikeNum() ? newsList.get(i).getLikeNum() : 0);	 
			 jo.put("author", null !=  newsList.get(i).getAuthor() ? newsList.get(i).getAuthor() : "");	 
			 jo.put("source", null !=  newsList.get(i).getSource() ? newsList.get(i).getSource() : "");	
			 jo.put("shareUrl", null !=  newsList.get(i).getShareUrl() ? newsList.get(i).getShareUrl() : "");
			 jo.put("titlePic", null != newsList.get(i).getTitlePic() ? newsList.get(i).getTitlePic() : "");
			 jo.put("flagId", null != newsList.get(i).getFlagId() ? newsList.get(i).getFlagId() : "");
			 jo.put("flagName", null != newsList.get(i).getFlagName() ? newsList.get(i).getFlagName() : "");		
			 jo.put("ver", null != newsList.get(i).getVer() ? newsList.get(i).getVer() : 0);	
			 ja.put(jo);
		 }
		 return ja;
	 }
}
