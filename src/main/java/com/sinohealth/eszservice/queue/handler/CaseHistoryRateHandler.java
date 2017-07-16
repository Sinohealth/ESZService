package com.sinohealth.eszservice.queue.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.queue.entity.BaseMessage;
import com.sinohealth.eszservice.queue.entity.CaseHistoryRateMessage;
import com.sinohealth.eszservice.service.visit.IApplicationService;

/**
 * 处理医生总积分
 * 
 * @author 黄世莲
 * 
 */
@Component
public class CaseHistoryRateHandler implements IQueueHandler {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IApplicationService applicationService;

	@Override
	public void process(BaseMessage message) {
		logger.info("异步处理统计病历完整性：{}", message);
		if (message instanceof CaseHistoryRateMessage) {
			CaseHistoryRateMessage e = (CaseHistoryRateMessage) message;
			int applyId = e.getApplyId();
			ApplicationEntity application = applicationService.get(applyId);
			if (null == application) {
				System.err.println("CaseHistoryRateHandler:" + applyId);
				return;
			}
			float rate = applicationService.countCaseHistoryRate(applyId);
			application.getRateCount()
					.setCaseHistoryRate((int) Math.rint(rate));
			applicationService.saveApplication(application);
		} else {
			System.out.println("not CaseHistoryRateMessage：" + message);
		}
	}

}
