package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.TakeMedRecordEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.paser.BodySignValueParser;
import com.sinohealth.eszservice.service.visit.paser.ParseException;

public class DailyResultDto extends BaseDto {

	private static final long serialVersionUID = 587513667017630286L;

	private List<BodySignValueEntity> list;

	private List<TakeMedRecordEntity> takeMedRecords;

	public List<BodySignValueEntity> getList() {
		return list;
	}

	public void setList(List<BodySignValueEntity> list) {
		this.list = list;
	}

	public String toString() {

		// 转换体征项，转为：[{d: "2015-1-10",v: [[123,"80"],…]}, {d:,…}]
		String bodySignNodes = "[]";
		StringBuffer buf = new StringBuffer("[]");

		if (null != list) {
			try {
				bodySignNodes = BodySignValueParser.compile(list);
			} catch (ParseException e) {
				return String.format("{\"errCode\":%d,\"errMsg\":\"%s\"}",
						BaseDto.ERRCODE_SYSTEM_ERROR, e.getMessage());
			}
		}
		if (null != takeMedRecords) {
			buf = new StringBuffer();
			buf.append("[");
			if (null != takeMedRecords) {
				boolean notfirst = false;
				for (TakeMedRecordEntity e : takeMedRecords) {
					if (notfirst) {
						buf.append(",");
					} else {
						notfirst = true;
					}
					buf.append("[");
					buf.append("\"");
					buf.append(DateUtils.formatDate(e.getCurDate()));
					buf.append("\",");
					buf.append(e.getTaked());
					buf.append("]");
				}
			}
			buf.append("]");
		}

		// 返回结果
		return String
				.format("{\"errCode\":%d,\"errMsg\":\"%s\",\"result\":%s,\"takeMeds\":%s}",
						errCode, errMsg, bodySignNodes, buf.toString());
	}

	public void setTakeMedRecords(List<TakeMedRecordEntity> takeMedRecords) {
		this.takeMedRecords = takeMedRecords;

	}
}
