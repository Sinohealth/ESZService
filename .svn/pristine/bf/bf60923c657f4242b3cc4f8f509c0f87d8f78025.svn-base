package com.sinohealth.eszservice.queue.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sinohealth.eszorm.entity.base.GradeEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.queue.entity.BaseMessage;
import com.sinohealth.eszservice.queue.entity.GradeMessage;
import com.sinohealth.eszservice.service.base.IGradeService;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.sick.ISickService;

/**
 * 处理积分
 * 
 * @author 黄世莲
 * 
 */
@Component
public class GradeHandler implements IQueueHandler {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDoctorCountService doctorCountService;

	@Autowired
	private IGradeService gradeService;

	@Autowired
	private ISickService sickService;

	@Override
	public void process(BaseMessage message) {
		logger.info("异步处理积分：{}", message);
		if (message instanceof GradeMessage) {
			GradeMessage e = (GradeMessage) message;
			GradeEntity grade = e.getGrade();

			if (null == grade.getUserId()) {
				return;
			}
			int userId = grade.getUserId();

			if (userId > Constants.DOCTOR_USER_ID_START
					&& userId < Constants.DOCTOR_USER_ID_END) { // 医生的ID段
				updateDoctorGrade(grade);
			} else if (userId > Constants.SICK_USER_ID_START) {
				updateSickGrade(grade);
			} else {
				logger.error("用户ID不在设定的区间内");
			}
		} else {
			logger.error("not GradeMessage：{}", message);
		}
	}

	private void updateSickGrade(GradeEntity grade) {
		gradeService.save(grade);
		int userId = grade.getUserId();
		sickService.updateTotalGrade(userId);

	}

	private void updateDoctorGrade(GradeEntity grade) {
		int userId = grade.getUserId();
		String szSubject = grade.getSzSubject();
		gradeService.save(grade);
		DoctorCountEntity count = doctorCountService.get(userId, szSubject);
		if (null == count) {
			doctorCountService.add(userId, szSubject);
		}
		doctorCountService.updateTotalGrade(userId, szSubject);
	}

}
