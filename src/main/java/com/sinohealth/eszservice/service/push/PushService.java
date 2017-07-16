package com.sinohealth.eszservice.service.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.service.base.IClientIdService;
import com.sinohealth.pushservice.action.PushAction;
import com.sinohealth.pushservice.entity.Application;
import com.sinohealth.pushservice.entity.Message;
import com.sinohealth.pushservice.entity.PhoneSystem;

@Service
public class PushService {
	private static Logger logger = LoggerFactory.getLogger(PushService.class);

	@Autowired
	private IClientIdService clientIdServiceImpl;

	public void push(int userId, String szSubject, String titile,
			String summary, TransmissionContent transmissionContent,
			String groupId) {
		ObjectMapper mapper = new ObjectMapper();

		String s = "";
		try {
			s = mapper.writeValueAsString(transmissionContent);
		} catch (JsonProcessingException e) {
			logger.error("无法转换透传内容格式：{}, {}", e, transmissionContent);
			// e.printStackTrace();
			// throw new Exception("无法转换透传内容");
		}

		push(userId, szSubject, titile, summary, s, groupId);

	}

	/**
	 * 推送消息到指定ID
	 * 
	 * @param userId
	 * @param szSubject
	 * @param titile
	 * @param summary
	 * @param transmissionContent
	 *            透传的消息
	 */
	public void push(int userId, String szSubject, String titile,
			String summary, String transmissionContent, String groupId) {
		String clientId = clientIdServiceImpl.getClientId(userId, szSubject);
		String appName = clientIdServiceImpl.getAppName(userId, szSubject);

		if ((null == clientId) || (null == appName) || ("".equals(clientId))) {
			logger.debug(
					"clientId错误：{}，或appName错误：{}, userId:{}, szSubject:{}",
					clientId, appName, userId, szSubject);
			return;
		}

		Application application = Application.valueOf(appName);

		PhoneSystem phoneSystem = parsePhoneSystem(appName);

		if ((null == application) || (null == phoneSystem)) {
			logger.debug("application错误：{}，或phoneSystem错误：{}", application,
					phoneSystem);
			return;
		}
		logger.debug(
				"推送消息：{application:{},phoneSystem:{},titile:{},summary:{},transmissionContent:{},groupId:{}, clientId:{}}",
				application, phoneSystem, titile, summary, transmissionContent,
				groupId, clientId);
		// summary += ":" + clientId;

		Message message = new Message();
		message.setApplication(application); // 哪一个专科
		message.setPhoneSystem(phoneSystem); // android or ios
		message.setTitle(titile);
		message.setSummary(summary);
		message.setTransmissionContent(transmissionContent);
		message.setClientId(clientId);
		message.setGroupId(groupId);
		PushAction.pushToSingle(message);
	}

	public PhoneSystem parsePhoneSystem(String appName) {
		if (appName.endsWith("IOS")) {
			return PhoneSystem.IOS;
		} else if (appName.endsWith("Android")) {
			return PhoneSystem.ANDROID;
		}
		System.err.println("找不到相应的系统：" + appName);
		return null;

	}

	public void push(int sickId, SzSubjectEntity szSubject,
			String noteTitle, String noteMsg, TransmissionContent tc,
			String groupId) {
		push(sickId, szSubject.getId(), noteTitle, noteMsg, tc, groupId);

	}
}
