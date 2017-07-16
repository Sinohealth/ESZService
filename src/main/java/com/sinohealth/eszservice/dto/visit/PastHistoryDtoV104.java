package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.paser.PastHistoryParser;

public class PastHistoryDtoV104 extends BaseDto {

	private static final long serialVersionUID = -1281565835630426075L;

	private List<HealthArchiveLog> list;

	@Override
	public String toString() {
		String resFormat = "";
		List<String> hisList = new ArrayList<String>();
		try {
			hisList = convertJsonList(list);
			resFormat = "{\"errCode\":%d,\"pastHistories\":%s" + "}";

		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
		return String.format(resFormat, errCode, hisList);
	}

	public List<String> convertJsonList(List<HealthArchiveLog> list)
			throws Exception {
		List<String> hisList = new ArrayList<String>();

		String pastHistory = "";
		System.out.println("log size:" + list.size());
		for (int i = 0; i < list.size(); i++) {
			if (null != list.get(i).getValue()) {
				HealthArchiveLog healthArchiveLog = list.get(i);
				AppPastHistoryComponent pastHistoryObj = PastHistoryParser
						.parse(healthArchiveLog.getValue());
				pastHistory = PastHistoryParser.compile(pastHistoryObj,
						DateUtils.formatDate(healthArchiveLog.getPostTime(),
								"yyyy-MM-dd"));

				hisList.add(pastHistory);
			}
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
