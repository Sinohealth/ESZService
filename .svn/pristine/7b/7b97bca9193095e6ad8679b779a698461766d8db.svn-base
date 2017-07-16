package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.ReasonEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.StringUtil;

public class RejectReasonDto extends BaseDto {

	private static final long serialVersionUID = -1641997373264903819L;

	private ApplicationEntity application;

	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			
			if (null!=application&&null!=application.getReasonDesc()) {
				String reasonDesc = StringUtil.replaceEnter(application.getReasonDesc());
				jo.put("reasonDesc",reasonDesc);
			} else {
				jo.put("reasonDesc", "");
			}
			

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}
}
