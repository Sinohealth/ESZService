package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class SaveDailyResultDto extends BaseDto {

	private static final long serialVersionUID = 587513667017630286L;

	private List<BodySignValueEntity> list;

	public List<BodySignValueEntity> getList() {
		return list;
	}

	public void setList(List<BodySignValueEntity> list) {
		this.list = list;
	}

	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}
}
