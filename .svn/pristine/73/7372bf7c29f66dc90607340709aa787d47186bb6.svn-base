package com.sinohealth.eszservice.service.visit.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.TakeMedRecordEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.visit.ITakeMedRecordDao;
import com.sinohealth.eszservice.dto.visit.TakeMedicineDto;
import com.sinohealth.eszservice.service.visit.ITakeMedRecordService;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

@Service
public class TakeMedRecordServiceImpl implements ITakeMedRecordService {

	@Autowired
	ITakeMedRecordDao dao;

	@Override
	public int getTakedRecordCount(int sickId, Date startDate, Date endDate) {
		return dao.getTakeRecordCount(sickId, 1, startDate, endDate);
	}

	@Override
	public TakeMedicineDto saveTakeMedicine(int sickId, int taked, String date) {
		TakeMedicineDto dto = new TakeMedicineDto();
		SickEntity sick = new SickEntity();
		// System.out.println("taked: "+taked+" date: "+date+" date flag:"+"".equals(date));
		try {
			if (taked == -1 || "".equals(date)) {
				throw new SystemErrorExecption("参数不能为空", BaseDto.ERRCODE_OTHERS);
			}
			int[] takes = { 0, 1 };
			boolean flag = ArrayUtils.contains(takes, taked);
			if (!flag) {
				throw new SystemErrorExecption("判断'是否已服药的参数'只能输入0或1",
						BaseDto.ERRCODE_OTHERS);
			}
			Date takedDate = DateUtils.parseDate(date);
			if (null == takedDate) {
				throw new SystemErrorExecption("日期格式不合法",
						BaseDto.ERRCODE_OTHERS);
			}

			Date today = Calendar.getInstance().getTime();
			if (takedDate.after(today)) {
				throw new SystemErrorExecption("不能提交未来的数据",
						BaseDto.ERRCODE_OTHERS);
			}

			String hql = "From TakeMedRecordEntity where sick.id =:sickId and curDate =:curDate";
			Parameter params = new Parameter();
			params.put("sickId", sickId);
			params.put("curDate", takedDate);
			List<TakeMedRecordEntity> list = dao.findByHql(hql, params);
			TakeMedRecordEntity entity = null;
			TakeMedRecordEntity returnTaked = null;
			if (0 == list.size() || null == list) {
				entity = new TakeMedRecordEntity();
				// System.out.println("没记录: " + entity);
			} else {
				entity = list.get(0);
				// System.out.println("有记录: " + entity);
			}
			sick.setId(Integer.valueOf(sickId));
			entity.setSick(sick);
			entity.setCurDate(DateUtils.parseDate(date));
			entity.setTaked(taked);
			returnTaked = dao.save(entity);

			if (null == returnTaked) {
				throw new SystemErrorExecption("系统异常",
						TakeMedicineDto.ERRCODE_SYSTEM_ERROR);
			}
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}

		return dto;
	}

	@Override
	public List<TakeMedRecordEntity> getListBySick(int sickId, Date startDate,
			Date endDate) {
		/* 加一天，减一天，方便SQL查询 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.DAY_OF_YEAR, -1); // 减一天
		Date startDate1 = cal.getTime();
		cal.setTime(endDate);
		cal.add(Calendar.DAY_OF_YEAR, +1); // 加一天
		Date endDate1 = cal.getTime();

		return dao
				.findByHql(
						"FROM TakeMedRecordEntity WHERE sick.id=:p1 AND  curDate > :p2 AND curDate < :p3",
						new Parameter(sickId, startDate1, endDate1));
	}

	@Override
	public float getTakedRate(int sickId, Date startDate, Date endDate) {
		if ((null == startDate) || (null == endDate)) {
			return 0;
		}
		int between = DateUtils.getDaysBetween(startDate, endDate); // 两个日期相差多少天
		int takedCount = getTakedRecordCount(sickId, startDate, endDate); // 实际药物多少天
		if (between == 0) {
			return 0;
		}
		return (float) takedCount / (float) between;
	}
}
