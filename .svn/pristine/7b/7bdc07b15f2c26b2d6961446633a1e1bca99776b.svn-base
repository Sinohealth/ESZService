package com.sinohealth.eszservice.service.doctor.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.service.doctor.ICacheDoctorRandomService;

@Service
public class CacheDoctorRandomServiceImpl implements ICacheDoctorRandomService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 默认缓存时间
	 */
	private final static int defaultTime = 1;

	private final static String cacheKey = "doctor:rand:code:%s";

	private static Integer expireTime = null;

	public ValueOperations<String, String> ops() {
		return redisTemplate.opsForValue();
	}

	/* 保存随机码到redis上 */
	public void setRandomCode(String userId, String randomCode) {
		ops().set(randomCodeKey(userId), randomCode, getExpire(),
				TimeUnit.MINUTES);
	}

	public int getExpire() {
		if (null == expireTime) {
			try {
				String t = Global.getConfig("doctor.randomCode.cache.expired");
				Integer tn = Integer.valueOf(t);
				if (null != tn) {
					expireTime = tn.intValue();
				}
			} catch (NumberFormatException e) {
				System.err
						.println("配置参数doctor.randomCode.cache.expired不是数字，应该是数值类型");
				return defaultTime;
			}
		}
		return expireTime;
	}

	/* 从redis上获取随机码 */
	public String getRandomCode(String userId) {
		return ops().get(randomCodeKey(userId));
	}

	private static String randomCodeKey(String userId) {
		return String.format(cacheKey, userId);
	}
}
