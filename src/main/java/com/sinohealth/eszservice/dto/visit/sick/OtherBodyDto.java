package com.sinohealth.eszservice.dto.visit.sick;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;

public class OtherBodyDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -2378046899077221421L;

	private int pageNo;

	private int lastPage;

	private List<OtherBodyElem> list;

	private List<String> dateList;// 不重复的时间

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public List<OtherBodyElem> getList() {
		return list;
	}

	public void setList(List<OtherBodyElem> list) {
		this.list = list;
	}

	public String toString() {
		JSONObject jo = new JSONObject();
		JSONObject body = new JSONObject();
		try {
			body.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				body.put("errMsg", errMsg);
			}
			jo.put("pageNo", pageNo);
			jo.put("lastPage", lastPage);
			if (list.size() > 0) {
				jo.put("list", convertJsonList(list));
			}
			body.put("body", jo);

		} catch (JSONException e) {
			return "{}";
		}

		return body.toString();
	}

	public JSONArray convertJsonList(List<OtherBodyElem> elemList)
			throws JSONException {
		JSONArray ja = new JSONArray();

		for (String date : dateList) {
			JSONObject updateTime = new JSONObject();
			updateTime.put("updateTime", date);
			updateTime.put("data", convertJsonDatas(list, date));
			ja.put(updateTime);
		}
		return ja;
	}

	public JSONArray convertJsonDatas(List<OtherBodyElem> elemList, String date)
			throws JSONException {
		JSONArray ja = new JSONArray();
		for (OtherBodyElem elem : elemList) {
			if (DateUtils.formatDate(elem.getUpdateTime(), "yyyy-MM-dd")
					.equals(date)) {
				JSONObject object = new JSONObject();
				object.put("zhName", elem.getZhName());
				object.put("enName", elem.getEnName());
				object.put("text", elem.getText());
				ja.put(object);
			}
		}
		return ja;
	}
}