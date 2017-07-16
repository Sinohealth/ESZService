package com.sinohealth.eszservice.dto.doctor;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.base.HospitalEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class DoctorHospitalQueryDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -8189973150914042436L;

	private List<HospitalEntity> list;

	private Integer lastPage;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			jo.put("lastPage", lastPage);
			JSONArray listInfo = new JSONArray();
			if (null != list) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject tmp = new JSONObject();
					tmp.put("id", null != list.get(i).getId() ? list.get(i)
							.getId() : 0);
					tmp.put("name",
							null != list.get(i).getHospitalName() ? list.get(i)
									.getHospitalName() : "");
					listInfo.put(tmp);
				}
			}
			jo.put("list", listInfo);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public List<HospitalEntity> getList() {
		return list;
	}

	public void setList(List<HospitalEntity> list) {
		this.list = list;
	}

	public Integer getLastPage() {
		return lastPage;
	}

	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}

}
