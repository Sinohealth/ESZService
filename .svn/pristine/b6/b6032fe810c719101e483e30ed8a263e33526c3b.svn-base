package com.sinohealth.eszservice.service.base.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.common.utils.PropertiesLoader;

/**
 * 工作日计算工具类
 * 
 * @author 黄世莲
 * 
 */
@Service
public class HolidayService {

	public HolidayService() {
		// 从resource加载配置文件
		PropertiesLoader propertiesLoader = new PropertiesLoader(
				"holidays.properties");
		String hdays = propertiesLoader.getProperty("holidays", "");
		String workdays = propertiesLoader.getProperty("workdays", "");

		String[] hdaysArr = hdays.split(",");
		for (String s : hdaysArr) {
			Date d = DateUtils.parseDate(s);
			if (null != d) {
				addDate(d, true);
			}
		}

		String[] workdaysArr = workdays.split(",");
		for (String s : workdaysArr) {
			Date d = DateUtils.parseDate(s);
			if (null != d) {
				addDate(d, false);
			}
		}
	}

	/**
	 * 设定的日期，key为日期，value为是是否假期。value=<code>true</code>：假期，value=
	 * <code>false</code> ：工作日
	 */
	private Map<Date, Boolean> holidays = new HashMap<>();

	/**
	 * 是否工作日
	 * 
	 * @return <code>true</code>：假期，<code>false</code>：工作日
	 */
	public boolean isHoliday(Date date) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		dateCal.set(Calendar.HOUR_OF_DAY, 0); // 把当前时间小时变成０
		dateCal.set(Calendar.MINUTE, 0); // 把当前时间分钟变成０
		dateCal.set(Calendar.SECOND, 0); // 把当前时间秒数变成０
		dateCal.set(Calendar.MILLISECOND, 0); // 毫秒也变成0
		if (holidays.containsKey(dateCal.getTime())) {
			Boolean isHoliday = holidays.get(dateCal.getTime());
			// 如果已经有定此日是否不是假期
			return (null != isHoliday) && (isHoliday.booleanValue());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateCal.getTime());
			int weekday = cal.get(Calendar.DAY_OF_WEEK);
			// 是周六周日
			return ((weekday == Calendar.SATURDAY) || (weekday == Calendar.SUNDAY));
		}
	}

	/**
	 * 增加日期
	 * 
	 * @param date
	 *            日期
	 * @param isHoliday
	 *            是否节假日
	 */
	public void addDate(Date date, boolean isHoliday) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // 把当前时间小时变成０
		cal.set(Calendar.MINUTE, 0); // 把当前时间分钟变成０
		cal.set(Calendar.SECOND, 0); // 把当前时间秒数变成０
		cal.set(Calendar.MILLISECOND, 0); // 毫秒也变成0
		holidays.put(date, isHoliday);
	}

	public Map<Date, Boolean> getHolidays() {
		return holidays;
	}

	public void setHolidays(Map<Date, Boolean> holidays) {
		this.holidays = holidays;
	}

	public Calendar getVisitDate(Calendar startDate, int cycleUnit,
			int startTimePoint, int targetTimePoint) {
		return getVisitDate(startDate, cycleUnit, startTimePoint,
				targetTimePoint, true);
	}

	/**
	 * 
	 * 获取阶段随访的时间<br/>
	 * TODO 应该做单元测试
	 * 
	 * @param startDate
	 *            开始时间
	 * @param cycleUnit
	 * @param startTimePoint
	 *            开始时间点
	 * @param targetTimePoint
	 *            结束时间点
	 * @param ignoreHoliday
	 *            是否忽略假期
	 * @return
	 */
	public Calendar getVisitDate(Calendar startDate, int cycleUnit,
			int startTimePoint, int targetTimePoint, boolean ignoreHoliday) {
		int days = 1 == cycleUnit ? 7 : 30; // 1周为7天，1月为30天；
		startDate.add(Calendar.DAY_OF_YEAR, (targetTimePoint - startTimePoint)
				* days);

		if (ignoreHoliday) {
			int lastHolidayCounts = 0; // 之前已经有多少天是假期
			while (isHoliday(startDate.getTime())) {
				startDate.add(Calendar.DAY_OF_YEAR, -1);
				lastHolidayCounts++;
			}
			if (lastHolidayCounts > 0) { // 之前已经有多少天是假期，就向后顺延多少天
				startDate.add(Calendar.DAY_OF_YEAR, lastHolidayCounts * 2);
			}
			// 是假期就跳过
			while (isHoliday(startDate.getTime())) {
				startDate.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return startDate;
	}

}
