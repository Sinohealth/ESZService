package com.sinohealth.eszservice.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd",
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHours(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	public static Date getDateStart(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateEnd(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getBeforeMonth(int month, Date date) {
		// 定义日期格式
		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		// 将calendar装换为Date类型
		// Date date = calendar.getTime();
		calendar.setTime(date);

		// 将date类型转换为BigDecimal类型（该类型对应oracle中的number类型）
		// 获取当前时间的前6个月
		calendar.add(Calendar.MONTH, -month);
		String date02 = formatDateTime(calendar.getTime());
		return date02;
	}

	/**
	 * 计算两个日期相应多少天<br/>
	 * 如果相等，则为0；如果cal1大于cal2，返回正整数；如果cal1小于cal2，返回负整数
	 * 
	 * @param startCal
	 * @param endCal
	 * @return
	 */
	public static int getDaysBetween(Calendar startCal, Calendar endCal) {
		// 区间内总共有多少周
		// 每天24小时*60分*60秒 = 86,400秒 = 86,400,000毫秒
		long interval = 86400000;
		long between = endCal.getTimeInMillis() - startCal.getTimeInMillis(); // 相差毫秒

		return (int) (between / interval);
	}

	public static int getDaysBetween(Date startDate, Date endDate) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal2.setTime(endDate);

		return getDaysBetween(cal1, cal2);
	}

	// 从当前时间延迟多少天
	public static String getNewTimeByDay(int day) {// 延迟天数
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, day);
		String newtime = format.format(c.getTime());
		return newtime;
	}

	/**
	 * 获得收益时间(获取当前天+2天，周末不算).
	 * 
	 * @param date
	 * 
	 *            任意日期
	 * 
	 * @return the income date
	 * 
	 * @throws NullPointerException
	 * 
	 *             if null == date
	 */

	public static Date getIncomeDate(Date date) throws NullPointerException {
		if (null == date) {

			throw new NullPointerException("the date is null or empty!");

		}
		// 对日期的操作,我们需要使用 Calendar 对象
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		// +1天
		calendar.add(Calendar.DAY_OF_MONTH, +2);

		// 判断是星期几
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		Date incomeDate = calendar.getTime();

		if (dayOfWeek == 1 || dayOfWeek == 7) {
			// 递归
			return getIncomeDate(incomeDate);
		}

		return incomeDate;
	}

	/**
	 * 获得收益时间(获取当前天+2天，周末不算+节假日不算).
	 * 
	 * @param date
	 * 
	 *            任意日期
	 * 
	 * @return the income date
	 * 
	 * @throws NullPointerException
	 * 
	 *             if null == date
	 */
	public static Date getIncomeDateExceptHoliday(Date date)
			throws NullPointerException {
		if (null == date) {
			throw new NullPointerException("the date is null or empty!");
		}
		// 对日期的操作,我们需要使用 Calendar 对象
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		// +1天
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		Date incomeDate = calendar.getTime();

		if (isWeekend(calendar) || isHoliday(calendar)) {
			// 递归
			return getIncomeDateExceptHoliday(incomeDate);
		}

		return incomeDate;
	}

	/**
	 * 判断一个日历是不是周末.
	 * 
	 * @param calendar
	 *            the calendar
	 * @return true, if checks if is weekend
	 */
	public static boolean isWeekend(Calendar calendar) {
		// 判断是星期几
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1 || dayOfWeek == 7) {

			return true;
		}
		return false;
	}

	/**
	 * 一个日历是不是节假日.
	 * 
	 * @param calendar
	 * 
	 *            the calendar
	 * @return true, if checks if is holiday
	 */
	public static boolean isHoliday(Calendar calendar) {

		String pattern = "yyyy-MM-dd";

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String dateString = simpleDateFormat.format(calendar.getTime());

		// 节假日 这个可能不同地区,不同年份 都有可能不一样,所以需要有个地方配置, 可以放数据库, 配置文件,环境变量 等等地方
		// 这里以配置文件 为例子
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("holidayConfig");
		String holidays = resourceBundle.getString("holiday");
		String[] holidayArray = holidays.split(",");
		// System.out.println("holiday: "+Arrays.asList(holidayArray)+" : "+dateString);
		boolean isHoliday = ArrayUtils.contains(holidayArray, dateString);
		System.out.println("isHoliday: " + isHoliday + " : " + dateString);
		return isHoliday;

	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// System.out.println(formatDate(parseDate("2010/3/6")));
		// System.out.println(getDate("yyyy年MM月dd日 E"));
		// long time = new Date().getTime()-parseDate("2014-11-19").getTime();

		// System.out.println(time/(24*60*60*1000));
		// Interval interval = new IntervalTask(runnable, interval);
		/*
		 * Calendar c = Calendar.getInstance(); Calendar c1 =
		 * Calendar.getInstance(); Calendar c2 = Calendar.getInstance();
		 * c1.setTime(parseDate("2014-11-01 08:30:25")); // int startMon
		 * =c1.get(Calendar.MONTH)+1; // c1.add(c1.MONTH, 3);
		 * c1.add(c1.WEEK_OF_MONTH, 2);
		 * c2.setTime(parseDate("2014-11-14 23:59:25"));
		 * 
		 * // c.setTimeInMillis(inver); System.out.println(c1.getTime() +
		 * "  longInterval:" + c1.get(c1.MONTH)); int result = c2.compareTo(c1);
		 */

		// String date = getBeforeMonth(6, parseDate("2014-11-01 08:30:25"));
		// System.out.println("Date: " + date);

		// System.out.println(DateUtils.pastHours(parseDate("2015-04-16 10:30:00")));

		// String dayStr = getNewTimeByDay(2);
		// System.out.println(dayStr);

		// Date dayStr =
		// getIncomeDate(DateUtils.parseDate("2015-05-15 09:34:02"));
		// System.out.println(DateUtils.formatDateTime(dayStr));

		// double[] array={1.2,1,3,1.4};
		// Boolean flag = ArrayUtils.contains(array, 1.4);
		// System.out.println(flag);

		// Date date =
		// getIncomeDateExceptHoliday(DateUtils.parseDate("2015-05-15 09:34:02"));
		// System.out.println(DateUtils.formatDateTime(date));

		/*
		 * List<Integer> list = new ArrayList<Integer>(); list.add(1);
		 * list.add(2); list.add(4); list.add(1); list.add(2); list.add(5);
		 * list.add(1); List<Integer> tempList = new ArrayList<Integer>(); for
		 * (Integer i : list) { if (!tempList.contains(i)) { tempList.add(i); }
		 * } for (Integer i : tempList) {
		 * 
		 * System.out.println(i); }
		 */
		/*
		 * int sex =3; int[] sexs = new int[]{0,1,2}; boolean flag =
		 * ArrayUtils.contains(sexs, sex); System.out.println("flag: "+flag);
		 */

		Date date1 = parseDate("2015-07-28");
		Date date2 = parseDate("2015-07-29");
		System.out.println(date1.compareTo(date2) >= 0 ? "不合法" : "good");
	}

}
