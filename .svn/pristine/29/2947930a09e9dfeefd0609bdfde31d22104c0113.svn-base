package com.sinohealth.eszservice.queue.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sinohealth.eszservice.queue.entity.BaseMessage;
import com.sinohealth.eszservice.queue.entity.DoctorVisitCountMessage;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;

/**
 * 处理积分
 * 
 * @author 黄世莲
 * 
 */
@Component
public class DoctorVisitCountHandler implements IQueueHandler {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDoctorCountService doctorCountService;

	@Override
	public void process(BaseMessage message) {
		logger.info("医生信息统计：{}", message);
		if (message instanceof DoctorVisitCountMessage) {
			DoctorVisitCountMessage e = (DoctorVisitCountMessage) message;
			doctorCountService.updateVisitCount(e.getDoctorId(),
					e.getSzSubject(), true);
		} else {
			logger.error("not DoctorVisitCountMessage：{}", message);
		}
	}

}
