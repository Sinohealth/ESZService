package com.sinohealth.eszservice.service.doctor;

/**
 * 缓存医生登录随机码service
 * 
 * @author 黄世莲
 * 
 */
public interface ICacheDoctorNonceService {

	/**
	 * 缓存随机数
	 * 
	 * @param account
	 * @param nonce
	 */
	public void cacheNonce(String account, String nonce);

	/**
	 * 获取随机数
	 * 
	 * @param account
	 * @param nonce
	 * @return
	 */
	public boolean isNonceCached(String account, String nonce);
}
