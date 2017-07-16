package com.sinohealth.eszservice.service.doctor.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.service.doctor.ICacheDoctorService;
import com.sinohealth.eszservice.service.doctor.exception.EntityNotCacheException;

@Service
public class CacheDoctorServiceImpl implements ICacheDoctorService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 默认缓存时间
	 */
	private final int defaultTime = 5;

	private Integer expireTime = null;

	public ValueOperations<String, Object> ops() {
		return redisTemplate.opsForValue();
	}

	/**
	 * 缓存医生信息，缓存的结构如下： <br/>
	 * <ul>
	 * <li>$userId --> $doctor</li>
	 * <li>$mobile --> $userId</li>
	 * <li>$email --> $userId</li>
	 * </ul>
	 */
	public void cache(int userId, DoctorEntity doctor) {
		// 如果没有的话，就存入null
		ops().set(doctorKey(userId), doctor, getExpire(), TimeUnit.HOURS);

		if ((null == doctor) || (doctor.getId() == null)) {
			return;
		}

		if (null != doctor.getMobile()) {
			cacheMobile(doctor.getMobile(), userId);
		}

		if (null != doctor.getEmail()) {
			cacheEmail(doctor.getEmail(), userId);
		}
	}

	/**
	 * 缓存mobile
	 * 
	 * @param mobile
	 * @param userId
	 *            整数或者null
	 */
	public void cacheMobile(String mobile, Integer userId) {
		String uid = null != userId ? String.valueOf(userId) : null;
		ops().set(userId4MobileKey(mobile), uid, getExpire(), TimeUnit.HOURS);
	}

	/**
	 * 缓存email
	 * 
	 * @param email
	 * @param userId
	 *            整数或者null
	 */
	public void cacheEmail(String email, Integer userId) {
		String uid = null != userId ? String.valueOf(userId) : null;
		ops().set(userId4EmailKey(email), uid, getExpire(), TimeUnit.HOURS);
	}

	/**
	 * 根据医生ID，获取医生缓存的信息
	 * 
	 * @param id
	 * @return 返回医生的信息DoctorEntity，或者如果没有缓存返回null
	 * @throws EntityNotCacheException
	 */
	public DoctorEntity get(int id) throws EntityNotCacheException {
		if (!redisTemplate.hasKey(doctorKey(id))) {
			throw new EntityNotCacheException();
		}
		Object o = ops().get(doctorKey(id));

		if (o instanceof DoctorEntity) {
			return (DoctorEntity) o;
		}

		return null;
	}

	/**
	 * 获取手机号码对应的医生ID
	 * 
	 * @param mobile
	 * @return 返回整数或者null
	 * @throws EntityNotCacheException
	 */
	public Integer getIdByMobile(String mobile) throws EntityNotCacheException {
		if (!redisTemplate.hasKey(userId4MobileKey(mobile))) {
			throw new EntityNotCacheException();
		}
		String id = (String) ops().get(userId4MobileKey(mobile));
		return id != null ? Integer.valueOf(id) : null;
	}

	/**
	 * 获取email对应的医生ID
	 * 
	 * @param email
	 * @return 返回整数或者null
	 * @throws EntityNotCacheException
	 */
	public Integer getIdByEmail(String email) throws EntityNotCacheException {
		if (!redisTemplate.hasKey(userId4EmailKey(email))) {
			throw new EntityNotCacheException();
		}
		String id = (String) ops().get(userId4EmailKey(email));
		return id != null ? Integer.valueOf(id) : null;
	}

	/**
	 * 从缓存中删除医生的缓存数据
	 * 
	 * @param id
	 */
	public void remove(int id) {
		DoctorEntity doctor = null;
		try {
			doctor = get(id);
		} catch (EntityNotCacheException e) {
			System.out.println("医生信息没有被缓存：" + id);
		}

		if (null == doctor) {
			return;
		}

		String mobile = doctor.getMobile();

		String email = doctor.getEmail();

		redisTemplate.delete(doctorKey(id));

		if (null != mobile) {
			redisTemplate.delete(userId4MobileKey(mobile));
		}

		if (null != email) {
			redisTemplate.delete(userId4EmailKey(email));
		}
	}

	/**
	 * 更新已经存在的缓存，如果没有缓存，则不进行更新
	 * 
	 * 
	 * @param doctor
	 */
	public void updateCached(DoctorEntity doctor) {
		Integer id = doctor.getId();
		if (null == id) {
			return;
		}

		try {
			DoctorEntity cached = get(id); // 已经缓存的

			// 处理原有的mobile缓存
			if ((null != cached.getMobile())) {
				if ((null == doctor.getMobile())
						|| (!cached.getMobile().equals(doctor.getMobile()))) { // mobile变没有，或者内容变化了
					redisTemplate.delete(userId4MobileKey(cached.getMobile()));
				}
			}

			// 处理原有的email缓存
			if ((null != cached.getEmail())) {
				if ((null == doctor.getEmail())
						|| (!cached.getEmail().equals(doctor.getEmail()))) { // mobile变没有，或者内容变化了
					redisTemplate.delete(userId4EmailKey(cached.getEmail()));
				}
			}

			// 更新已经存在的缓存
			cache(id, doctor);
		} catch (EntityNotCacheException e) {
			System.out.println("医生信息没有被缓存：" + id);
		}
	}

	public int getExpire() {
		if (null == expireTime) {
			try {
				String t = Global.getConfig("doctor.data.cache.expired");
				Integer tn = Integer.valueOf(t);
				if (null != tn) {
					expireTime = tn.intValue();
				}
			} catch (NumberFormatException e) {
				System.err.println("配置参数doctor.data.cache.expired不是数字，应该是数值类型");
				return defaultTime;
			}
		}
		return expireTime;
	}

	/**
	 * 是否医生ID已经被缓存
	 * 
	 * @param id
	 * @return
	 */
	public boolean isIdCached(int id) {
		return redisTemplate.hasKey(doctorKey(id));
	}

	private String doctorKey(int id) {
		return String.format(doctorKey, id);
	}

	private String userId4MobileKey(String mobile) {
		return String.format(userId4MobileKey, mobile);
	}

	private String userId4EmailKey(String email) {
		return String.format(userId4EmailKey, email);
	}

	private final static String doctorKey = "doctor:data:%s";
	private final static String userId4MobileKey = "doctor:mbl:%s";
	private final static String userId4EmailKey = "doctor:eml:%s";
}
