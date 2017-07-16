package com.sinohealth.eszservice.service.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.common.utils.Identities;
import com.sinohealth.eszservice.service.doctor.IDoctorService;
import com.sinohealth.eszservice.service.doctor.impl.DoctorServiceImpl;
import com.sinohealth.eszservice.service.sick.ISickService;
import com.sinohealth.eszservice.service.sick.impl.SickServiceImpl;

/**
 * 邮件发送
 */
public class MailSender {
	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mail
	 *            待发送的邮件的信息
	 * @throws GeneralSecurityException
	 */
	public static boolean sendTextMail(Email mail) throws GeneralSecurityException {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = mail.getProperties();
		if (mail.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MailAuthenticator(mail.getUserName(),
					mail.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mail.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mail.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mail.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mail.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mail
	 *            待发送的邮件信息
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean sendHtmlMail(Email mail) throws GeneralSecurityException,
			UnsupportedEncodingException {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties props = mail.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mail.isValidate()) {
			authenticator = new MailAuthenticator(mail.getUserName(),
					mail.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);
		sendMailSession.setDebug(false);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mail.getFromAddress(),
					mail.getFromNickName() == null ? ""
							: mail.getFromNickName());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中,可以设置多个收件人，逗号隔开
			// Message.RecipientType.TO属性表示接收者的类型为TO,CC表示抄送,BCC暗送
			mailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mail.getToAddress()));
			// 设置邮件消息的主题
			mailMessage.setSubject(mail.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			MimeBodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mail.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);

			// 设置信件的附件(用本地上的文件作为附件)
			FileDataSource fds = null;
			DataHandler dh = null;
			if (mail.getAttachments() != null) {
				for (File file : mail.getAttachments()) {
					html = new MimeBodyPart();
					fds = new FileDataSource(file);
					dh = new DataHandler(fds);
					html.setFileName(file.getName());
					html.setDataHandler(dh);
					mainPart.addBodyPart(html);
				}
			}

			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			mailMessage.saveChanges();

			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}
    
	/*发送html格式的邮件*/
	public static Boolean updatePassowrd(String email,Integer userType,String uuid) {
		Boolean flag = false;
		String requestTime = DateUtils.getDateTime();
		
		String link = "";
		String domain = Global.getConfig("email.domain");
		if(userType==1){
			link = domain + "doctor/users/changePassword?uuid="+uuid;
		}else if(userType==2){
			link = domain + "sick/users/changePassword?uuid="+uuid;
		}

		/*读取邮件模板*/
//		URL url = ClassLoader.getSystemResource("emailTemplate.html");
//		File file = new File(url.getFile());
//		String content = FileUtil.readFileByLines(file);		

        String content = "";
        try {
            InputStream is = null;
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("emailTemplate.html");
			is = resource.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is, "utf-8"));
			String tempString = "";
			while ((tempString = br.readLine()) != null) {
				content += tempString + "\n";
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}   
		
		content = content.replaceAll("\\[toAddress\\]", email);
		content = content.replaceAll("\\[requestTime\\]", requestTime);
		content = content.replaceAll("\\[link\\]", link);
		
		Email mailInfo = new Email();
		mailInfo.setMailServerHost(SmtpServer.smtp_qq.get("smtp"));
		mailInfo.setMailServerPort(SmtpServer.smtp_qq.get("port"));
		mailInfo.setValidate(true);
		if(userType==1){
			mailInfo.setFromNickName("e随访");
		}else if(userType==2){
			mailInfo.setFromNickName("心康");
		}
		mailInfo.setUserName(Global.getConfig("email.address")); // 实际发送者
		mailInfo.setPassword(Global.getConfig("email.password"));// 您的邮箱密码
		mailInfo.setFromAddress(Global.getConfig("email.address")); // 设置发送人邮箱地址
		mailInfo.setToAddress(email); // 设置接受者邮箱地址
		mailInfo.setSubject(Global.getConfig("email.subject"));
		mailInfo.setContent(content);

		try {
			sendHtmlMail(mailInfo);		
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}   	
	
	@SuppressWarnings("serial")
	public static void main(String[] args) throws Exception {
		
//		(new MailSender()).changePassowrd("2358138635@qq.com",1);

	}
}
