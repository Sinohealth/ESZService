package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;

public class FamilyHistoryDtoV104 extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private List<HealthArchiveLog> list;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			System.err.print("errMsg: " + errMsg);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

			jo.put("familyHistories", convertJsonList(list));
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
				jo.put("updateTime",
						null != list.get(i).getPostTime() ? DateUtils
								.formatDate(list.get(i).getPostTime(),
										"yyyy-MM-dd") : "");
				jo.put("data", null != list.get(i).getValue() ? new JSONArray(
						list.get(i).getValue()) : "");

				ja.put(jo);
			}
		}
		return ja;
	}

	public List<HealthArchiveLog> getList() {
		return list;
	}

	public void setList(List<HealthArchiveLog> list) {
		this.list = list;
	}

}
