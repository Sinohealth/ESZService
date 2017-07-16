package com.sinohealth.eszservice.service.sick.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.service.sick.ISickOnlineService;

@Service
public class SickOnlineServiceImpl implements ISickOnlineService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	private final static String KEY_USERID = "tk";
	private final static String KEY_LAST_VISIT_TIME = "lt";
	private final static String KEY_ONLINE = "ol";

	public final static String STATE_ONLINE = "1";
	public final static String STATE_OFFLINE = "0";

	private final static int defaultTime = 5;

	private static Integer expireTime = null;

	public HashOperations<String, String, String> ops() {
		return redisTemplate.opsForHash();
	}

	public ValueOperations<String, String> opsUid() {
		return redisTemplate.opsForValue();
	}

	public void set(String token, String key, String value) {
		ops().put(tableKey(token), key, value);
		redisTemplate.expire(tableKey(token), getExpire(), TimeUnit.HOURS); // 设置有效期
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public String get(String token, String key) {
		return ops().get(tableKey(token), key);
	}

	public void set(int userId, String value) {
		opsUid().set(tableKey(userId), value);
		redisTemplate.expire(tableKey(userId), getExpire(), TimeUnit.HOURS); // 设置有效期
	}

	public String get(int userId) {
		return opsUid().get(tableKey(userId));
	}

	/**
	 * <p>
	 * 缓存token与用户的关系
	 * </p>
	 * 缓存的结构如下：
	 * <ul>
	 * <li>$token:USER_ID -> $userId</li>
	 * <li>$token:LAST_VISIT_TIME -> $lastVisitTime</li>
	 * <li>$token:ONLINE -> $online</li>
	 * <li>$token:SZ_SUBJECT -> $szSubject</li>
	 * <li>$userId:TOKEN -> $token</li>
	 * </ul>
	 * 
	 * @param token
	 * @param userId
	 *            用户ID
	 * @param lastVisitTime
	 *            最后访问时间戳
	 * @param online
	 *            在线状态，0：不在线（另一个终端上线，此终端下线），1：在线
	 * @param clientId
	 */
	public void cacheToken(String token, int userId, long lastVisitTime,
			int online) {
		// 此用户ID已经有token在缓存中，则将缓存的值设置为0。即：只能有一个客户端能够登录接口，另一个终端提示已经有另一个终端登录
		String cachedUserToken = get(userId); // 已经缓存的用户token
		// System.err.println("cachedUserToken: " + cachedUserToken);
		if (null != cachedUserToken) {
			offline(cachedUserToken); // 设置下线
			// System.out.println("cacheOnline:"+CacheSickOnlineUtils.isOnline(cachedUserToken));
		}

		// userId --> token
		set(userId, token);

		// token --> {userId,lastVisit,online,clientId}
		set(token, KEY_USERID, String.valueOf(userId));
		set(token, KEY_LAST_VISIT_TIME, String.valueOf(lastVisitTime));
		set(token, KEY_ONLINE, String.valueOf(online));
	}

	/**
	 * 校验token是否有校
	 * 
	 * @param token
	 * @return
	 */
	public boolean hasToken(String token) {
		return redisTemplate.hasKey(tableKey(token));
	}

	/**
	 * 删除token，使token失效
	 * 
	 * @param token
	 */
	public void deleteToken(String token) {
		int userId = getUserId(token);
		if (userId > 0) {
			delete(tableKey(userId));
		}
		delete(tableKey(token));
	}

	/**
	 * 删除token，使token失效
	 * 
	 * @param token
	 */
	public void deleteToken(int userId) {
		String token = getToken(userId);
		if (userId > 0) {
			delete(tableKey(userId));
		}
		if (hasToken(token)) {
			deleteToken(token);
		}
	}

	/**
	 * 根据用户ID找到对应的token
	 * 
	 * @param userId
	 * @return
	 */
	public String getToken(int userId) {
		return get(userId);
	}

	/**
	 * 根据token，找到对应的用户Id
	 * 
	 * @param token
	 * @return
	 */
	public int getUserId(String token) {
		String userId = (String) get(token, KEY_USERID);

		return (null == userId || "".equals(userId)) ? 0 : Integer
				.parseInt(userId);
	}

	/**
	 * 用户最后访问时间，时间戳
	 * 
	 * @param token
	 * @return
	 */
	public long getLastVisitTime(String token) {
		Object o = get(token, KEY_LAST_VISIT_TIME);
		return null != o ? Long.parseLong((String) o) : 0;
	}

	/**
	 * 更新用户最后访问时间，时间戳
	 * 
	 * @param t
	 *            时间戳
	 * @return
	 */
	public void setLastVisitTime(String token, long t) {
		set(token, KEY_LAST_VISIT_TIME, String.valueOf(t));
	}

	/**
	 * 设置token下线
	 * 
	 * @return
	 */
	public void offline(String token) {
		set(token, KEY_ONLINE, STATE_OFFLINE);
	}

	/**
	 * 是否在线状态
	 * 
	 * @return
	 */
	public boolean isOnline(String token) {
		Object o = get(token, KEY_ONLINE);
		return STATE_ONLINE.equals(o);
	}

	public int getExpire() {
		if (null == expireTime) {
			try {
				String t = Global.getConfig("login.token_expired");
				Integer tn = Integer.valueOf(t);
				if (null != tn) {
					expireTime = tn.intValue();
				}
			} catch (NumberFormatException e) {
				System.err.println("配置参数login.token_expired不是数字，应该是数值类型");
				return defaultTime;
			}
		}
		return expireTime;
	}

	private String tableKey(String token) {
		return String.format(cacheTokenKey, token);
	}

	private String tableKey(int userId) {
		return String.format(cacheUidKey, userId);
	}

	private final static String cacheTokenKey = "sick:ol:token:%s";
	private final static String cacheUidKey = "sick:ol:uid:%s";
}
