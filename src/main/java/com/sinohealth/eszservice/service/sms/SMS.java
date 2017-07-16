package com.sinohealth.eszservice.service.sms;
import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esms.MessageData;
import com.esms.PostMsg;
import com.esms.common.entity.Account;
import com.esms.common.entity.GsmsResponse;
import com.esms.common.entity.MTPack;
import com.esms.common.entity.MTPack.MsgType;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

public class SMS {	
	
	private final static Logger logger = LoggerFactory.getLogger(SMS.class);	
	
	public static Integer sendSms(String telephone){		
		Integer randomCode = 0;
		
		Account account = new Account(Global.getConfig("sms.account"), Global.getConfig("sms.password"));
		PostMsg pm = new PostMsg();
		pm.getCmHost().setHost(Global.getConfig("sms.cmHost"), Integer.parseInt(Global.getConfig("sms.cmPort")));//设置网关的IP和port，用于发送信息
		pm.getWsHost().setHost(Global.getConfig("sms.wsHost"),Integer.parseInt(Global.getConfig("sms.wsPort")));//设置网关的IP和port，用于获取账号信息、上行、状态报告等等
		
		MTPack pack = new MTPack();
		pack.setMsgType(MsgType.SMS); //SMS短信发送，MMS彩信发送
		pack.setBizType(1); //业务类型
		ArrayList<MessageData> msgs = new ArrayList<MessageData>();
		
		Random random = new Random();
		int code = random.nextInt(899999);
		code = code + 100000;
		
		msgs.add(new MessageData(telephone, "您申请的手机验证码是："+code+"，请输入后进行验证，谢谢!"));
		pack.setMsgs(msgs);	
		
		pack.setSendType(MTPack.SendType.GROUP);
		
		try {
			GsmsResponse resp = pm.post(account, pack);
			randomCode = code;
//			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return randomCode;
	}
	
	public static void registerSendSms(DoctorEntity doctor) throws SystemErrorExecption{		
		
		Account account = new Account(Global.getConfig("sms.account"), Global.getConfig("sms.password"));
		PostMsg pm = new PostMsg();
		pm.getCmHost().setHost(Global.getConfig("sms.cmHost"), Integer.parseInt(Global.getConfig("sms.cmPort")));//设置网关的IP和port，用于发送信息
		pm.getWsHost().setHost(Global.getConfig("sms.wsHost"),Integer.parseInt(Global.getConfig("sms.wsPort")));//设置网关的IP和port，用于获取账号信息、上行、状态报告等等
		
		MTPack pack = new MTPack();
		pack.setMsgType(MsgType.SMS); //SMS短信发送，MMS彩信发送
		pack.setBizType(1); //业务类型
		ArrayList<MessageData> msgs = new ArrayList<MessageData>();
		try {
			if (null == doctor) {
				logger.debug("医生对象为空, 不发短信通知！");
				return;
			}
			if (null == doctor.getMobile() || null == doctor.getName()) {
				logger.debug("医生手机号或医生姓名为空,不发短信通知！");
				return;
			}
			String message = Global.getConfig("sms.register.message").replace(":doctorName", doctor.getName());
			msgs.add(new MessageData(doctor.getMobile(), message));
			pack.setMsgs(msgs);	
			
			pack.setSendType(MTPack.SendType.GROUP);

			GsmsResponse resp = pm.post(account, pack);
//			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemErrorExecption("注册 发短信失败!", BaseDto.ERRCODE_OTHERS);
		}	
	
	}
	
	public static void main(String[] args) throws SystemErrorExecption {
		//System.out.println(sendSms("13760661689"));
		DoctorEntity doctor = null;
//		DoctorEntity doctor = new DoctorEntity();
//		doctor.setMobile("13760661689");
//		doctor.setName("homy");
		registerSendSms(doctor);
	}
}