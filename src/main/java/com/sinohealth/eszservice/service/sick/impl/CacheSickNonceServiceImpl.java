package com.sinohealth.eszservice.service.sick.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.service.sick.ICacheSickNonceService;

@Service
public class CacheSickNonceServiceImpl implements ICacheSickNonceService {
	private final static int defaultTime = 5;
	private final static String cacheKey = "sick:nonce:%s:%s";

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static Integer expireTime = null;

	public ValueOperations<String, String> ops() {
		return redisTemplate.opsForValue();
	}

	/**
	 * 缓存随机数
	 * 
	 * @param account
	 * @param nonce
	 */
	@Override
	public void cacheNonce(String account, String nonce) {
		ops().set(nonceKey(account, nonce), "1");
		redisTemplate.expire(nonceKey(account, nonce), getExpire(),
				TimeUnit.HOURS);
	}

	/**
	 * 获取随机数
	 * 
	 * @param account
	 * @param nonce
	 * @return
	 */
	@Override
	public boolean isNonceCached(String account, String nonce) {
		return redisTemplate.hasKey(nonceKey(account, nonce));
	}

	public int getExpire() {
		if (null == expireTime) {
			try {
				String t = Global.getConfig("nonce.cache.expired");
				Integer tn = Integer.valueOf(t);
				if (null != tn) {
					expireTime = tn.intValue();
				}
			} catch (NumberFormatException e) {
				System.err.println("配置参数nonce.cache.expired不是数字，应该是数值类型");
				return defaultTime;
			}
		}
		return expireTime;
	}

	private String nonceKey(String account, String nonce) {
		return String.format(cacheKey, account, nonce);
	}
}
