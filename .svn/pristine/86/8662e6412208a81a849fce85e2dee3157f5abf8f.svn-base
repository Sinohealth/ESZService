package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.paser.PersonalHistoryParser;

public class PersonalHistoryDto extends BaseDto {

	private static final long serialVersionUID = -1281565835630426075L;

	private List<HealthArchiveLog> list;

	@Override
	public String toString() {
		String resFormat = "";
		List<String> hisList = new ArrayList<String>();
		try {
			hisList = convertJsonList(list);
			System.out.println("hsiList:" + hisList);
			resFormat = "{\"errCode\":%d,\"body\":{\"personalHis\":%s" + "}}";

		} catch (JSONException e) {
			return "{}";
		}
		return String.format(resFormat, errCode, hisList);
	}

	public List<String> convertJsonList(List<HealthArchiveLog> list)
			throws JSONException {
		List<String> hisList = new ArrayList<String>();
		String pastHistory = "";
		for (int i = 0; i < list.size(); i++) {
			// System.err.println(i+" :"+list.get(i).getAppPastHistory()+"  "+list.get(i).getApplyId());
			HealthArchiveLog healthArchiveLog = list.get(i);

			pastHistory = PersonalHistoryParser.compile(healthArchiveLog, DateUtils
					.formatDate(healthArchiveLog.getPostTime(), "yyyy-MM-dd"));

			hisList.add(pastHistory);

		}
		return hisList;
	}

	public List<HealthArchiveLog> getList() {
		return list;
	}

	public void setList(List<HealthArchiveLog> list) {
		this.list = list;
	}


}
