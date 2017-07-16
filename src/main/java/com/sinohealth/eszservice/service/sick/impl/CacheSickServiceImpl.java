package com.sinohealth.eszservice.service.sick.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.service.sick.ICacheSickService;
import com.sinohealth.eszservice.service.sick.exception.EntityNotCacheException;

@Service
public class CacheSickServiceImpl implements ICacheSickService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private final int defaultTime = 5;

	private Integer expireTime = null;

	public ValueOperations<String, Object> ops() {
		return redisTemplate.opsForValue();
	}

	/**
	 * 缓存医生信息，缓存的结构如下： <br/>
	 * <ul>
	 * <li>$userId --> $sick</li>
	 * <li>$mobile --> $userId</li>
	 * <li>$email --> $userId</li>
	 * </ul>
	 */
	public void cache(int userId, SickEntity sick) {
		// 如果没有的话，就存入null
		ops().set(sickKey(userId), sick, getExpire(), TimeUnit.HOURS);

		if ((null == sick) || (sick.getId() == null)) {
			return;
		}

		if (null != sick.getMobile()) {
			cacheMobile(sick.getMobile(), userId);
		}

		if (null != sick.getEmail()) {
			cacheEmail(sick.getEmail(), userId);
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
	 * @param userId
	 * @return 返回医生的信息SickEntity，或者如果没有缓存返回null
	 */
	public SickEntity get(int userId) throws EntityNotCacheException {
		if (!redisTemplate.hasKey(sickKey(userId))) {
			throw new EntityNotCacheException();
		}
		Object o = ops().get(sickKey(userId));
		SickEntity sick = null;

		if (o instanceof SickEntity) {
			sick = (SickEntity) o;
		} else {
			System.err.println("缓存的内容不是SickEntity类型");
		}

		return sick;
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
		String userId = (String) ops().get(userId4MobileKey(mobile));
		return userId != null ? Integer.valueOf(userId) : null;
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
		String userId = (String) ops().get(userId4EmailKey(email));
		return userId != null ? Integer.valueOf(userId) : null;
	}

	/**
	 * 从缓存中删除患者的缓存数据
	 * 
	 * @param userId
	 */
	public void remove(int userId) {
		SickEntity sick = null;
		try {
			sick = get(userId);
		} catch (EntityNotCacheException e) {
			System.out.println("患者对应的ID没有被缓存：" + userId);
		}

		if (null == sick) {
			return;
		}

		String mobile = sick.getMobile();

		String email = sick.getEmail();

		redisTemplate.delete(sickKey(userId));

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
	 * @param sick
	 */
	public void updateCached(SickEntity sick) {
		Integer userId = sick.getId();
		if (null == userId) {
			return;
		}

		try {
			SickEntity cached = get(userId); // 已经缓存的

			// 处理原有的mobile缓存
			if ((null != cached.getMobile())) {
				if ((null == sick.getMobile())
						|| (!cached.getMobile().equals(sick.getMobile()))) { // mobile变没有，或者内容变化了
					redisTemplate.delete(userId4MobileKey(cached.getMobile()));
				}
			}

			// 处理原有的email缓存
			if ((null != cached.getEmail())) {
				if ((null == sick.getEmail())
						|| (!cached.getEmail().equals(sick.getEmail()))) { // mobile变没有，或者内容变化了
					redisTemplate.delete(userId4EmailKey(cached.getEmail()));
				}
			}

			// 更新已经存在的缓存
			cache(userId, sick);
		} catch (EntityNotCacheException e) {
			System.out.println("患者信息没有被缓存：" + userId);
		}
	}

	public int getExpire() {
		if (null == expireTime) {
			try {
				String t = Global.getConfig("sick.data.cache.expired");
				Integer tn = Integer.valueOf(t);
				if (null != tn) {
					expireTime = tn.intValue();
				}
			} catch (NumberFormatException e) {
				System.err.println("配置参数sick.data.cache.expired不是数字，应该是数值类型");
				return defaultTime;
			}
		}
		return expireTime;
	}

	/**
	 * 是否医生ID已经被缓存
	 * 
	 * @param moblie
	 * @return
	 */
	public boolean isIdCached(int userId) {
		return redisTemplate.hasKey(sickKey(userId));
	}

	private String sickKey(int userId) {
		return String.format(sickKey, userId);
	}

	private String userId4MobileKey(String mobile) {
		return String.format(userId4MobileKey, mobile);
	}

	private String userId4EmailKey(String email) {
		return String.format(userId4EmailKey, email);
	}

	private final static String sickKey = "sick:%s";
	private final static String userId4MobileKey = "sick:mbl:%s";
	private final static String userId4EmailKey = "sick:eml:%s";

}
