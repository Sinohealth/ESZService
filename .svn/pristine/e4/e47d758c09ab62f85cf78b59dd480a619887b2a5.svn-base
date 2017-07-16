package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sinohealth.eszorm.VisitItemId;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.BodySignEntity;
import com.sinohealth.eszorm.entity.visit.BodySignLogEntity;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.visit.IBodySignDao;
import com.sinohealth.eszservice.dao.visit.IBodySignLogDao;
import com.sinohealth.eszservice.dto.visit.elem.CaseHistoryRateElem;
import com.sinohealth.eszservice.service.visit.IBodySignService;
import com.sinohealth.eszservice.service.visit.IBodySignValueService;
import com.sinohealth.eszservice.service.visit.ITemplateService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;

@Service
public class BodySignServiceImpl implements IBodySignService {
	protected final Logger logger = LoggerFactory
			.getLogger(BodySignServiceImpl.class);

	@Autowired
	private IBodySignDao bodySignDao;

	@Autowired
	private IBodySignLogDao bodySignLogDao;

	@Autowired
	private IBodySignValueService bodySignValueService;

	@Autowired
	private ITemplateService templateService;

	@Autowired
	private IVisitItemService visitItemService;

	@Override
	public BodySignEntity getByItemId(Integer templId, int itemId) {
		List<BodySignEntity> res = bodySignDao
				.findByHql(
						"FROM BodySignEntity WHERE template.id=:p1 and item.itemId=:p2",
						new Parameter(templId, itemId));

		if (null != res && res.size() > 0) {
			return res.get(0);
		}
		return null;
	}

	@Override
	public BodySignEntity save(BodySignEntity bodySign) {

		return bodySignDao.save(bodySign);

	}

	@Override
	public List<BodySignEntity> getBodySignsByTempl(int templId) {
		Parameter params = new Parameter(templId);

		return bodySignDao.findByHql(
				"FROM BodySignEntity  WHERE template.templId=:p1", params);
	}

	@Override
	public void addLog(int templId, BodySignEntity bodySign) {
		Calendar today = Calendar.getInstance();
		int itemId = bodySign.getItem().getItemId();
		int freq = bodySign.getFreq();
		BodySignLogEntity newLog = new BodySignLogEntity();
		newLog.setTemplId(templId);
		newLog.setItemId(itemId);
		newLog.setFreq(freq);
		newLog.setStartDate(today.getTime());
		bodySignLogDao.save(newLog);
	}

	/**
	 * 更新模板的频率修改记录
	 * 
	 */
	@Override
	public void updateLog(int templId, BodySignEntity bodySign) {
		Calendar today = Calendar.getInstance();
		int itemId = bodySign.getItem().getItemId();
		int freq = bodySign.getFreq();
		List<BodySignLogEntity> logs = bodySignLogDao.getList(templId, itemId);
		BodySignLogEntity firstLog = null; // 第一条记录
		BodySignLogEntity lastLog = null; // 最后一条记录
		if ((null != logs) && (logs.size() > 0)) {
			firstLog = logs.get(0);
			lastLog = logs.get(logs.size() - 1);
		}

		if (null == firstLog) { // 如果还没有，则新增一条
			BodySignLogEntity log = new BodySignLogEntity();
			log.setTemplId(templId);
			log.setItemId(itemId);
			log.setFreq(freq);
			log.setStartDate(today.getTime());
			bodySignLogDao.save(log);
			return;
		}

		// 如果已经有日志
		/**
		 * 如果开始时间有做过更改，那就把全部的修改记录清空，保存一条新的
		 */
		Calendar firstCal = Calendar.getInstance();
		firstCal.setTime(firstLog.getStartDate());
		Calendar lastCal = Calendar.getInstance();
		lastCal.setTime(lastLog.getStartDate());
		if (!((firstCal.get(Calendar.YEAR) == today.get(Calendar.YEAR))
				&& (firstCal.get(Calendar.MONTH) == today.get(Calendar.MONTH)) && (firstCal
					.get(Calendar.DAY_OF_YEAR) == today
				.get(Calendar.DAY_OF_YEAR)))) {
			firstLog.setFreq(freq);
			firstLog.setStartDate(today.getTime());
			// 清除全部的记录
			bodySignLogDao
					.deleteByHql(
							"DELETE FROM BodySignLogEntity WHERE templId=:p1 AND itemId=:p2",
							new Parameter(templId, itemId));
			BodySignLogEntity newLog = new BodySignLogEntity();
			newLog.setTemplId(templId);
			newLog.setItemId(itemId);
			newLog.setFreq(freq);
			newLog.setStartDate(today.getTime());
			bodySignLogDao.save(newLog); // 新建
			return;
		}

		/**
		 * 如果日志频率没变，则不更新
		 */
		if (lastLog.getFreq() == freq) {
			return;
		}

		/**
		 * 如果最后一条都是今天修改（即结束日期为昨天），只更新频率，如果不是今天修改，则要新增日志
		 */
		Calendar todayCal = Calendar.getInstance();
		todayCal.set(Calendar.HOUR_OF_DAY, 0);
		todayCal.set(Calendar.MINUTE, 0);
		todayCal.set(Calendar.SECOND, 0);
		todayCal.set(Calendar.MILLISECOND, 0);

		Calendar yestodayCal = Calendar.getInstance();
		yestodayCal.setTime(todayCal.getTime());
		yestodayCal.add(Calendar.DAY_OF_YEAR, -1);

		if (lastCal.compareTo(yestodayCal) == 0) { // 如果最后一次是今天修改
			// 如果跟上一次修改的是同一个频率，那这一条日志就不需要了
			if (logs.size() > 1) {
				BodySignLogEntity prevLog = logs.get(logs.size() - 2);
				if (prevLog.getFreq() == freq) {
					bodySignLogDao.delete(lastLog);
					prevLog.setEndDate(null);
				}
			} else {
				lastLog.setFreq(freq);
				lastLog.setStartDate(todayCal.getTime());
				bodySignLogDao.save(lastLog); // 修改
			}
		} else { // 如果最后一条日志频率不是今天修改
			lastLog.setEndDate(yestodayCal.getTime()); // 结束日期改为昨天
			bodySignLogDao.save(lastLog); // 更新时间
			BodySignLogEntity newLog = new BodySignLogEntity();
			newLog.setTemplId(templId);
			newLog.setItemId(itemId);
			newLog.setFreq(freq);
			newLog.setStartDate(todayCal.getTime());
			bodySignLogDao.save(newLog); // 新建
		}
	}

	@Override
	public void updateLogToFinished(int templId, int itemId, Date endDate) {
		BodySignLogEntity log = bodySignLogDao.getLastLog(templId, itemId);
		if (null != log) {
			log.setEndDate(endDate);
			bodySignLogDao.save(log); // 更新时间
		}
	}

	@Override
	public BodySignEntity addNew(BodySignEntity o) {
		return bodySignDao.save(o);
	}

	@Override
	public int[] countBodySignReport(int templId) {
		int[] res = new int[2];

		TemplateEntity template;
		ApplicationEntity application;
		List<BodySignEntity> list;
		try {
			template = templateService.get(templId);
			Assert.notNull(template, "找不到template,templId:" + templId);
			application = template.getApplication();
			Assert.notNull(application, "找不到application,templId:" + templId);
			list = template.getBodySigns();
			Assert.notNull(list, "找不到bodySigns,templId:" + templId);
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			return res;
		}
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		Date startDate, endDate;

		List<BodySignLogEntity> logs = bodySignLogDao.getList(templId);

		if ((null == logs) || (logs.size() < 1)) {
			return res;
		}

		for (int i = 0, j = logs.size(); i < j; i++) {
			BodySignLogEntity log = logs.get(i);

			int itemId = log.getItemId();

			// ** 2015-6-10：体征项，高压和低压只算一项 **
			if (VisitItemId.BLOOD_PRESSURE_LOW == itemId) {
				continue;
			}
			// ** 结束-- 2015-6-10:体征项，高压和低压只算一项 **

			startDate = log.getStartDate();

			// 最后一条日志没有结束日期，则选当前的日期
			if (null != log.getEndDate()) {
				endDate = log.getEndDate();
			} else if (null != application.getFinishTime()) { // 已经结束
				endDate = application.getFinishTime();
			} else {
				endDate = application.getExpectedFinishTime(); // 设为预期结束时间
			}

			startCal.setTime(startDate);
			endCal.setTime(endDate);

			List<BodySignValueEntity> values = bodySignValueService.getList(
					application.getSick().getId(), itemId, startDate, endDate);

			startCal.setTime(startDate);

			int[] rate = countReportRate(log.getFreq(), values, startCal,
					endCal);

			res[0] += rate[0];
			res[1] += rate[1];
		}
		return res;
	}

	/**
	 * TODO 应该做单元测试<br/>
	 * 根据体征项和结果值，统计体征的报告率。 <strong>要求两个数组都以时间由小到大排序</strong>
	 * 
	 * @param logs
	 * @param values
	 * @param finishTime
	 * @return
	 */
	public int[] countReportRate(int freq, List<BodySignValueEntity> values,
			Calendar startCal, Calendar endCal) {
		int actual = 0; // 实际
		int expect = 0; // 预期

		Calendar cal = Calendar.getInstance();
		if (freq == 1) { // 如果是周
			Calendar nextPointCal = Calendar.getInstance(); // 下一日/周的时间
			nextPointCal.setTime(startCal.getTime());
			nextPointCal.add(Calendar.WEEK_OF_YEAR, 1); // 加一周
			boolean added = false; // 是否已经计算了
			while (nextPointCal.compareTo(endCal) <= 0) { // 如果小于或等于统计结束日期，就继续做计算
				added = false;
				expect++; // 预期加1
				Iterator<BodySignValueEntity> valueIt = values.iterator();
				while (valueIt.hasNext()) {
					BodySignValueEntity value = (BodySignValueEntity) valueIt
							.next();
					cal.setTime(value.getReportDate());
					if (cal.compareTo(nextPointCal) > 0) { // 日期超出了，则不再计算
						break;
					}
					valueIt.remove(); // 移除已经处理过的值
					if (!added) { // 如果还未计算
						actual++; // 实际加1
						added = true; // 本周确认
					}
				}
				nextPointCal.add(Calendar.WEEK_OF_YEAR, 1); // 加一周
			}
		} else { // 如果不是周，则按天计算
			int between = DateUtils.getDaysBetween(startCal, endCal);// 相差天数
			expect += between; // 预期加上相差天数
			expect++;// bug#2000 【七天后提醒随访评价】肺血管医生端未收到七天后提醒随访评价的推送。同一天，算是1天
			Iterator<BodySignValueEntity> valueIt = values.iterator();
			while (valueIt.hasNext()) {
				BodySignValueEntity value = (BodySignValueEntity) valueIt
						.next();
				cal.setTime(value.getReportDate());
				if (cal.compareTo(endCal) > 0) {
					break;
				}
				valueIt.remove(); // 移除已经处理过的值
				actual++; // 实际加1
			}
		}

		return new int[] { actual, expect };
	}

	@Override
	public List<CaseHistoryRateElem> getCaseHistoryRateList(int templId) {
		List<CaseHistoryRateElem> res = new ArrayList<>();

		TemplateEntity template;
		ApplicationEntity application;
		List<BodySignEntity> list;
		try {
			template = templateService.get(templId);
			Assert.notNull(template);
			application = template.getApplication();
			Assert.notNull(application);
			list = template.getBodySigns();
			Assert.notNull(list);
		} catch (IllegalArgumentException e) {
			return res;
		}

		List<BodySignLogEntity> logs = bodySignLogDao.getList(templId);
		if ((null == logs) || (logs.size() < 1)) {
			return res;
		}

		Date startDate = null;
		Date endDate = null;

		for (BodySignLogEntity log : logs) {
			startDate = log.getStartDate();

			// 最后一条日志没有结束日期，则选当前的日期
			if (null != log.getEndDate()) {
				endDate = log.getEndDate();
			} else if (null != application.getFinishTime()) { // 已经结束
				endDate = application.getFinishTime();
			} else {
				endDate = application.getExpectedFinishTime(); // 设为预期结束时间
			}

			VisitItemEntity item = visitItemService.get(log.getItemId(), true);

			List<BodySignValueEntity> values = bodySignValueService.getList(
					application.getSick().getId(), item.getItemId(), startDate,
					endDate);

			// ** 2015-6-10：体征项，高压和低压只算一项 **
			int itemId = log.getItemId();
			if (VisitItemId.BLOOD_PRESSURE_LOW == itemId) {
				continue;
			}
			// ** 结束-- 2015-6-10:体征项，高压和低压只算一项 **

			startDate = log.getStartDate(); // 本日志的开发日期
			// 本日志的结束日期
			if (null != log.getEndDate()) {
				endDate = log.getEndDate();
			} else if (null != application.getFinishTime()) {
				endDate = application.getFinishTime();
			} else {
				endDate = application.getExpectedFinishTime();
			}
			if ((null == startDate) || (null == endDate)) { // startDate和endDate有可能为null
				continue;
			}
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			startCal.setTime(startDate);
			endCal.setTime(endDate);
			int[] rate = countReportRate(log.getFreq(), values, startCal,
					endCal);

			res.add(new CaseHistoryRateElem(String.format("体征-%s[%s]-[%s]-按%s",
					item.getZhName(), DateUtils.formatDate(startDate),
					DateUtils.formatDate(endDate), getUnitText(log.getFreq())),
					rate[1], rate[0]));
		}
		return res;
	}

	@Override
	public List<BodySignEntity> getBodySignsByTempl(int templId, int cat) {
		Parameter params = new Parameter(templId, cat);

		return bodySignDao.findByHql(
				"FROM BodySignEntity  WHERE template.templId=:p1 and cat=:p2",
				params);

	}

	public String getUnitText(int unit) {
		switch (unit) {
		case 0:
			return "日";
		case 1:
			return "周";
		case 2:
			return "月";
		}
		return null;
	}
}
