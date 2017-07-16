package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;

public class FamilyHistoryDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private List<ApplicationEntity> list;

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

	public JSONArray convertJsonList(List<ApplicationEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo = new JSONObject();
				jo.put("updateTime",
						null != list.get(i).getApplyTime() ? DateUtils
								.formatDate(list.get(i).getApplyTime(),
										"yyyy-MM-dd") : "");
				jo.put("data",
						null != list.get(i).getFamilyHistory() ? new JSONArray(
								list.get(i).getFamilyHistory()) : "");

				ja.put(jo);
			}
		}
		return ja;
	}

	public List<ApplicationEntity> getList() {
		return list;
	}

	public void setList(List<ApplicationEntity> list) {
		this.list = list;
	}
	
	public static void main(String[] args) throws JSONException {
		String string ="[{\"disease\":\"结直肠癌\",\"rel\":\"子\",\"relType\":1},{\"disease\":\"乳腺癌\",\"rel\":\"母\",\"relType\":1},{\"disease\":\"肺癌\",\"rel\":\"父\",\"relType\":1}]";
		System.out.println(new JSONArray(string));
	}
}
