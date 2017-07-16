package com.sinohealth.eszservice.service.doctor;

/**
 * 缓存医生找回密码等随机码service
 * 
 * @author 黄世莲
 * 
 */
public interface ICacheDoctorRandomService {

	/* 保存随机码到redis上 */
	public void setRandomCode(String userId, String randomCode);

	/* 从redis上获取随机码 */
	public String getRandomCode(String userId);
}
