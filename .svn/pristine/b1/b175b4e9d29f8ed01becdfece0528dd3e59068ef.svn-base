package com.sinohealth.eszservice.dto.visit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class SickSearchDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 8930453890086734122L;

	private Integer total;

	private List list;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			jo.put("count", total);
			jo.put("sickList", changeJsonList(list));

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}
	
	public String toJsonString() {
		JSONObject jo = new JSONObject();
		JSONObject body = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			body.put("count", total);
			body.put("sickList", changeJsonList(list));
            jo.put("body", body);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray changeJsonList(List list) throws JSONException {
		JSONArray ja = new JSONArray();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo = new JSONObject();
				Map map = (Map) list.get(i);

				jo.put("sickId", map.get("sickId"));
				jo.put("sickName", null != map.get("name") ? map.get("name")
						: "");
				jo.put("visitCount",
						null != map.get("records") ? map.get("records") : 0);
				jo.put("sex", null != map.get("sex") ? map.get("sex") : 0);
				Date birthday = (null != map.get("birthday")) ? (Date) map
						.get("birthday") : (new Date());
				long age = (System.currentTimeMillis() - birthday.getTime())
						/ (1000L * 60 * 60 * 24 * 365);
				jo.put("age", age);
				
				if(map.containsKey("height") && map.containsKey("weight") ){
					int height=(int)map.get("height");
					float weight=(float)map.get("weight");
					if(height==0){
						jo.put("bmi", "0");
					}else if(weight==0){
						jo.put("bmi", "0");
					}else{
						float bmi_f=(float)weight/(((float)height/100.00f)*((float)height/100.00f));						
						float bmi=(float)(Math.round(bmi_f*100))/100;
						jo.put("bmi", bmi+"");
					}
					
				}
				
				ja.put(jo);
			}
		}
		return ja;
	}
}
