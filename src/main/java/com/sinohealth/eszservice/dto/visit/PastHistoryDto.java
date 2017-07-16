package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.paser.PastHistoryParser;

public class PastHistoryDto extends BaseDto {

	private static final long serialVersionUID = -1281565835630426075L;

	private List<ApplicationEntity> list;

	@Override
	public String toString() {
		String resFormat = "";
		List<String> hisList = new ArrayList<String>();
		try {
			// System.out.println("dto list:"+list.size()+"  : "+list);
			hisList = convertJsonList(list);
			System.out.println("hsiList:" + hisList);
			resFormat = "{\"errCode\":%d,\"pastHistories\":%s" + "}";

		} catch (JSONException e) {
			return "{}";
		}
		return String.format(resFormat, errCode, hisList);
	}

	public List<String> convertJsonList(List<ApplicationEntity> list)
			throws JSONException {
		List<String> hisList = new ArrayList<String>();
		String pastHistory = "";
		for (int i = 0; i < list.size(); i++) {
			// System.err.println(i+" :"+list.get(i).getAppPastHistory()+"  "+list.get(i).getApplyId());
			if (null != list.get(i).getAppPastHistory()) {
				ApplicationEntity application = list.get(i);
				AppPastHistoryComponent pastHis = application
						.getAppPastHistory();
				pastHistory = PastHistoryParser.compile(pastHis, DateUtils
						.formatDate(application.getApplyTime(), "yyyy-MM-dd"));

				if ((!"".equals(pastHis.getMedicalHistories())
						|| (!"".equals(pastHis.getAllergyHistories())) || (!""
							.equals(pastHis.getSurgicalHistories())))) {
					hisList.add(pastHistory);
				}
			}
		}
		return hisList;
	}

	public List<ApplicationEntity> getList() {
		return list;
	}

	public void setList(List<ApplicationEntity> list) {
		this.list = list;
	}

}
