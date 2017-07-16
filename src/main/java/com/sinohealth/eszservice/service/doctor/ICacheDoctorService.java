package com.sinohealth.eszservice.service.doctor;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.service.doctor.exception.EntityNotCacheException;

/**
 * 缓存医生信息service
 * 
 * @author 黄世莲
 * 
 */
public interface ICacheDoctorService {

	/**
	 * 缓存医生信息，缓存的结构如下： <br/>
	 * <ul>
	 * <li>$userId --> $doctor</li>
	 * <li>$mobile --> $userId</li>
	 * <li>$email --> $userId</li>
	 * </ul>
	 */
	public void cache(int userId, DoctorEntity doctor);

	/**
	 * 缓存mobile
	 * 
	 * @param mobile
	 * @param userId
	 *            整数或者null
	 */
	public void cacheMobile(String mobile, Integer userId);

	/**
	 * 缓存email
	 * 
	 * @param email
	 * @param userId
	 *            整数或者null
	 */
	public void cacheEmail(String email, Integer userId);

	/**
	 * 根据医生ID，获取医生缓存的信息
	 * 
	 * @param id
	 * @return 返回医生的信息DoctorEntity，或者如果没有缓存返回null
	 * @throws EntityNotCacheException
	 */
	public DoctorEntity get(int id) throws EntityNotCacheException;

	/**
	 * 获取手机号码对应的医生ID
	 * 
	 * @param mobile
	 * @return 返回整数或者null
	 * @throws EntityNotCacheException
	 */
	public Integer getIdByMobile(String mobile) throws EntityNotCacheException;

	/**
	 * 获取email对应的医生ID
	 * 
	 * @param email
	 * @return 返回整数或者null
	 * @throws EntityNotCacheException
	 */
	public Integer getIdByEmail(String email) throws EntityNotCacheException;

	/**
	 * 从缓存中删除医生的缓存数据
	 * 
	 * @param id
	 */
	public void remove(int id);

	/**
	 * 更新已经存在的缓存，如果没有缓存，则不进行更新
	 * 
	 * 
	 * @param doctor
	 */
	public void updateCached(DoctorEntity doctor);

	/**
	 * 是否医生ID已经被缓存
	 * 
	 * @param id
	 * @return
	 */
	public boolean isIdCached(int id);
}
