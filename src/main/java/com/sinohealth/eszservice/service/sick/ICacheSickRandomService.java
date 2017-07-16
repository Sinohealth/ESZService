package com.sinohealth.eszservice.service.sick;

/**
 * 缓存患者随机码service
 * 
 * @author 黄世莲
 * 
 */
public interface ICacheSickRandomService {

	/* 保存随机码到redis上 */
	public void setRandomCode(String userId, String randomCode);

	/* 从redis上获取随机码 */
	public String getRandomCode(String userId);
}
