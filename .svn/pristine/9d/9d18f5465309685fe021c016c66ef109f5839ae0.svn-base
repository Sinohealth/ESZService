package com.sinohealth.eszservice.dto.visit;

import java.util.Map;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class DoctorStatusCountDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	/**
	 * 医生未参加此专科的随访
	 */
	public static final int ERRCODE_ISJOINED_VISIT = 12001;

	/**
	 * 医生是否参加了此随访专科
	 */
	private boolean subjectJoined = false;

	/**
	 * 存放不同状态随访记录
	 */
	private Map<Integer, Integer> map = null;

	public boolean isSubjectJoined() {
		return subjectJoined;
	}

	public void setSubjectJoined(boolean subjectJoined) {
		this.subjectJoined = subjectJoined;
	}

	public Map<Integer, Integer> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Integer> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		String format = "{\"errCode\":%d,\"errMsg\":\"%s\",\"visitStat\":[{\"status\":1,\"count\":%d},{\"status\":2,\"count\":%d},"
				+ "{\"status\":3,\"count\":%d},{\"status\":4,\"count\":%d},{\"status\":5,\"count\":%d}]}";
		return String.format(format, errCode,errMsg, takeCount(1), takeCount(2),
				takeCount(3), takeCount(4), takeCount(5));
	}

	private int takeCount(int status) {
		if (null!=map) {
			Integer c = map.get(status);
			return null == c ? 0 : c.intValue();
		} 
		return 0;
	}
}
