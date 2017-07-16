package com.sinohealth.eszservice.service.base.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sinohealth.eszorm.entity.base.GradeEntity;
import com.sinohealth.eszorm.entity.base.GradeItemEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.GradeKeys;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.base.IGradeDao;
import com.sinohealth.eszservice.dao.doctor.IDoctorDao;
import com.sinohealth.eszservice.dao.sick.ISickDao;
import com.sinohealth.eszservice.dto.doctor.DoctorGradesDto;
import com.sinohealth.eszservice.dto.sick.SickGradeDto;
import com.sinohealth.eszservice.service.base.IGradeItemService;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.sick.ISickOnlineService;
import com.sinohealth.eszservice.service.sick.ISickService;

@Service("gradeService")
public class GradeServiceImpl implements IGradeService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IGradeDao gradeDao;
	@Autowired
	private IDoctorDao doctorDao;
	@Autowired
	private ISickDao sickDao;
	@Autowired
	private IGradeItemService gradeItemService;
	@Autowired
	private IDoctorCountService doctorCountService;

	@Autowired
	private ISickService sickService;

	@Autowired
	private ISickOnlineService sickOnlineService;

	@Override
	public List<GradeEntity> findAll(Integer userId, String szSubject) {
		Parameter params = new Parameter();
		params.put("userId", userId);
		params.put("szSubject", szSubject);

		return gradeDao
				.findByHql(
						"FROM GradeEntity WHERE userId=:userId and szSubject=:szSubject ORDER BY actionTime DESC",
						params);
	}

	@Override
	public List<GradeEntity> findAll(Integer userId) {
		Parameter params = new Parameter();
		params.put("userId", userId);

		return gradeDao
				.findByHql(
						"FROM GradeEntity WHERE userId=:userId ORDER BY actionTime DESC",
						params);
	}

	@Override
	public DoctorGradesDto getDoctorGrades(int userId, String szSubject) {
		DoctorGradesDto dto = new DoctorGradesDto();

		try {
			DoctorCountEntity doctorCount = doctorCountService.get(userId,
					szSubject);
			dto.setTotalGrade(doctorCount.getTotalGrade()); // 总积分

			List<GradeEntity> list = findAll(userId, szSubject);

			dto.setGrades(list);
		} catch (Exception e) {
			logger.warn("获取积分异常：{}", e);
			dto.setErrCode(DoctorGradesDto.ERRCODE_OTHERS);
			dto.setErrMsg("请求错误");
		}

		return dto;
	}

	@Override
	public void save(GradeEntity grade) {
		gradeDao.save(grade);
	}

	@Override
	public Object getSickGrades(String token) {
		SickGradeDto dto = new SickGradeDto();

		try {
			int userId = sickOnlineService.getUserId(token);

			SickEntity sick = sickDao.get(userId);

			Assert.notNull(sick, "找不到患者信息");

			dto.setTotalGrade(sick.getTotalGrade()); // 总积分

			List<GradeEntity> list = findAll(userId);

			dto.setGrades(list);
		} catch (Exception e) {
			logger.warn("获取积分异常：{}", e);
			dto.setErrCode(SickGradeDto.ERRCODE_OTHERS);
			dto.setErrMsg("请求错误");
		}

		return dto;
	}

	@Override
	public void addAction(int userId, String actionName, String szSubject) {
		logger.info("增加积分，userId:{},actionName:{},szSubject:{}", userId,
				actionName, szSubject);
		GradeItemEntity gradeItem = gradeItemService.getByKey(actionName);

		if (null == gradeItem) {
			logger.info("找不到积分项：{}", actionName);
			return;// gradeItem ==null 积分则不被保存入库
		} else if (gradeItem.getGrade() == 0) {
			logger.info("找到的积分项为0分：{}", actionName);
			return; // 如果积分数为0，则积分不被保存入库
		}

		// 为免缓存不正确，需要到数据库去再查一次，确定用户最后登录的时间
		if (actionName.equals(GradeKeys.doctorDailyLoginKey)
				|| actionName.equals(GradeKeys.sickDailyLoginKey)) {
			Date today = new Date();
			GradeEntity exists = getGrade(userId, actionName, szSubject, today);
			if (null != exists) {
				return;
			}
		}

		GradeEntity grade = new GradeEntity();
		grade.setUserId(userId);
		grade.setActionKey(gradeItem.getGradeKey());
		grade.setActionGrade(gradeItem.getGrade());
		grade.setActionName(gradeItem.getName());
		grade.setSzSubject(szSubject);
		grade.setActionTime(new Date());

		save(grade);
		// 异步处理积分

		// 更新积分总分
		updateTotalGrade(userId, szSubject, gradeItem.getGrade());

		// 异步处理前，先刷新hibernate缓存
		// gradeDao.flush();
		//
		// GradeMessage message = new GradeMessage();
		// message.setGrade(grade);
		//
		// QueueUtils.push(message);
	}

	public void updateTotalGrade(int userId, String szSubject, int grade) {

		if (userId > Constants.DOCTOR_USER_ID_START
				&& userId < Constants.DOCTOR_USER_ID_END) { // 医生的ID段
			DoctorCountEntity count = doctorCountService.get(userId, szSubject);
			if (null == count) {
				doctorCountService.add(userId, szSubject);
			}
			doctorCountService.updateTotalGrade(userId, szSubject);
		} else if (userId > Constants.SICK_USER_ID_START) {
			sickService.updateTotalGrade(userId);
		} else {
			logger.error("用户ID不在设定的区间内");
		}

	}

	@Override
	public GradeEntity getGrade(Integer doctorId, String actionKey,
			String szSubject, Date date) {

		Date sdate = DateUtils.getDateStart(date);
		Date edate = DateUtils.getDateEnd(date);

		List<GradeEntity> list = gradeDao
				.findByHql(
						"FROM GradeEntity WHERE userId=:p1 AND actionKey=:p2 AND szSubject=:p3 AND actionTime > :p4 AND actionTime < :p5 ORDER BY actionTime DESC",
						new Parameter(doctorId, actionKey, szSubject, sdate,
								edate));

		if ((null != list) && (list.size() > 0)) {
			return list.get(0);
		}
		return null;

	}

	@Override
	public void addActionIfNotExistToday(int userId, String actionName,
			String szSubject) {
		Date today = new Date();
		GradeEntity exists = getGrade(userId, actionName, szSubject, today);
		if (null != exists) {
			return;
		}
		addAction(userId, actionName, szSubject);

	}
}
