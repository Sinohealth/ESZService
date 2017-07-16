package com.sinohealth.eszservice.dto.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.news.NewsEntity;
import com.sinohealth.eszorm.entity.news.NewspicsEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class NewsBodyDto extends BaseDto {

	private static final long serialVersionUID = -2828087470934255429L;

	 
	 private static ObjectMapper objectMapper = new ObjectMapper();
	 
	 
	/**
	 * errCode：11001  版本无变化，无需更新
	 */
	public static final int ERRCODE_NOT_Change = 11001;
	
	/**
	 * errCode：11002 输入的信息不完整
	 */
	public static final int ERRCODE_NEED_INFO = 11002;
	
	/**
	 * errCode：11003 找不到相应的内容
	 */
	public static final int ERRCODE_NOFOUND_CONTENT = 11003;
	
	/**
	 * errCode：11004资讯未发布否已被删除
	 */
	public static final int ERRCODE_NOPUBLISH_OR_DELETE= 11004;
	
	private NewsEntity news;

	
	 public NewsEntity getNews() {
		return news;
	}


	public void setNews(NewsEntity news) {
		this.news = news;
	}


	public String toString() {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonStr = "";
		try {
			map.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				map.put("errMsg", errMsg);
			}

			Map<String, Object> verMap = new HashMap<String, Object>();
			if (null != news) {
				verMap.put("coldId", null != news.getColIds() ? news
						.getColIds() : "");
				verMap.put("newsId", null != news.getId() ? news
						.getId() : "");
				verMap.put("type", null != news.getType() ? news.getType() : "");
				verMap.put("title", null != news.getTitle() ? news.getTitle()
						: "");
				verMap.put("summary",
						null != news.getSummary() ? news.getSummary() : "");
				verMap.put(
						"publishDate",
						null != news.getPublishDate() ? dateFormat(news
								.getPublishDate()) : "");
				verMap.put("likeNum",
						null != news.getLikeNum() ? news.getLikeNum() : "");
				verMap.put("author",
						null != news.getAuthor() ? news.getAuthor() : "");
				verMap.put("source",
						null != news.getSource() ? news.getSource() : "");
				verMap.put("shareUrl",
						null != news.getShareUrl() ? news.getShareUrl() : "");
				verMap.put(
						"createDate",
						null != news.getCreateDate() ? dateFormat(news
								.getCreateDate()) : "");
				verMap.put(
						"updateDate",
						null != news.getUpdateDate() ? dateFormat(news
								.getUpdateDate()) : "");
				verMap.put("hits", null != news.getHits() ? news.getHits() : "");
				verMap.put("shareNum",
						null != news.getShareNum() ? news.getShareNum() : "");
				verMap.put("pics", null != news.getPics() ? news.getPics() : "");
				verMap.put("titlePic", null != news.getTitlePic() ? news.getTitlePic() : "");
				verMap.put("ver", null != news.getVer() ? news.getVer() : 0);
				verMap.put("flagId", null != news.getFlagId() ? news.getFlagId() : 0);
				verMap.put("flagName", null != news.getFlagName() ? news.getFlagName() : "");
				
				if (2 == news.getType()) {
					verMap.put("imgList", changeJsonList(news.getPicList()));
					
				} else if(1 == news.getType()) {
					verMap.put("videoCover",
							null != news.getVideoCover() ? news.getVideoCover() : "");
					verMap.put("videoUrl",
							null != news.getVideoUrl() ? news.getVideoUrl() : "");
					verMap.put("appContent",
							null != news.getAppContent() ? news.getAppContent()
									: "");
				} else {
					verMap.put("appContent",
							null != news.getAppContent() ? news.getAppContent()
									: "");
				}
				map.put("body", verMap);
			}
			jsonStr = objectMapper.writeValueAsString(map);
		} catch (Exception e) {
			return "{}";
		}
		return jsonStr;
	}
	
	@SuppressWarnings("unchecked")
	public List<LinkedHashMap<String, Object>> changeJsonList(
			List<NewspicsEntity> newspics) throws JsonGenerationException,
			JsonMappingException, IOException {
		List<LinkedHashMap<String, Object>> mapList1 = new ArrayList<LinkedHashMap<String, Object>>();
		List<Object> mapList = new ArrayList<Object>();
		Map<String, Object> map;
		String jsonStr = "";
		if (null != newspics) {
			for (int i = 0; i < newspics.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("img", null != newspics.get(i).getBigPic() ? newspics
						.get(i).getBigPic() : "");
				map.put("note", null != newspics.get(i).getNote() ? newspics
						.get(i).getNote() : "");
				mapList.add(map);
			}
			jsonStr = objectMapper.writeValueAsString(mapList);
			mapList1 = objectMapper.readValue(jsonStr, List.class);
		}
		return mapList1;
	}
	
	public String changeJsonArrayList(List<NewspicsEntity> newspics)
			throws JSONException {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < newspics.size(); i++) {
			JSONObject newsObj = new JSONObject();
			newsObj.put("img",null != newspics.get(i).getBigPic() ? newspics
					.get(i).getBigPic() : "");
			newsObj.put("note",
					null != newspics.get(i).getNote() ? newspics
							.get(i).getNote() : "");
			
			ja.put(newsObj);
		}
		return ja.toString();
	}
	
	public static void main(String[] args) throws JSONException, IOException {
	/*	String string = "“中国   ” </p> <br/>";
		//string = StringEscapeUtils.unescapeHtml4(string);
		string = string2json(string);
		JSONObject jo = new JSONObject();
		jo.put("content", string);
		System.out.println(jo.toString());*/
		String string = "“中国   ” </p> <br/>";
		NewsEntity news =new NewsEntity();
		news.setAuthor(string);
		//jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
		//jsonGenerator.writeObject(news);
/*		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map.put("author", string);
		map.put("content", "welcome to sinoHealth");
		map2.put("img", "url1");
		map2.put("note","这是一张春运照片");
		
		map.put("imgList", map2);
		String string2 = objectMapper.writeValueAsString(map);
		System.out.println("string2 "+string2);
		*/
		List<NewspicsEntity> list =new ArrayList<NewspicsEntity>();
		NewspicsEntity pics =new NewspicsEntity();
		pics.setBigPic("url1");
		pics.setNote("welcome1");
		list.add(pics);
		
		pics =new NewspicsEntity();
		pics.setBigPic("url2");
		pics.setNote("welcome2");
		list.add(pics);
	
		//String string2 = changeJsonArrayList(list).toString();
		//String string2 = objectMapper.writeValueAsString(list);
		//System.out.println("list json: "+string2);
		
	}
	
}
