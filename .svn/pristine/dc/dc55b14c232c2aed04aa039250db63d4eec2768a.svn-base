package com.sinohealth.eszservice.service.base.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.service.base.IAppNameService;
import com.sinohealth.eszservice.service.base.IClientIdService;

@Service
public class ClientIdServiceImpl implements IClientIdService {

	private final static String KEY_FOR_USER_ID = "uid";
	private final static String KEY_FOR_CLIENT_ID = "clt";
	private final static String KEY_FOR_APP_NAME = "app";
	private final static String KEY_FOR_SZSUBJECT = "sj";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private IAppNameService appNameService;

	private static Integer expireTime = null;

	public HashOperations<String, String, String> ops() {
		return redisTemplate.opsForHash();
	}

	public void set(String id, String key, String value) {
		ops().put(id, key, value);
		redisTemplate.expire(id, getExpire(), TimeUnit.HOURS);
	}

	public int getExpire() {
		if (null == expireTime) {
			try {
				String t = Global.getConfig("clientId.cache.expired");
				Integer tn = Integer.valueOf(t);
				if (null != tn) {
					expireTime = tn.intValue();
				}
			} catch (NumberFormatException e) {
				System.err.println("配置参数clientId.cache.expired不是数据，应该是数值类型");
				return 12;
			}
		}
		return expireTime;
	}

	/**
	 * 缓存客户端ID<br/>
	 * 结构为：<br/>
	 * <code>
	 * $userId:$szSubject => {appName:$appName, clientId:$clientId} 
	 * 
	 * </code> <br/>
	 * 以及：<br/>
	 * <code>
	 * $clientId => { userId:"$userId", szSubject:"$szSubject"}
	 * </code> <br/>
	 * 在设置缓存时，也同时确保了clientId只同一个userId绑定
	 * 
	 * @param userId
	 *            用户ID，因为医生和患者的ID是不同的区间段，所以不用担心重复的问题
	 * @param clientId
	 */
	@Override
	public void setClientId(int userId, String appName, String clientId) {
		String szSubject = (userId >= Constants.SICK_USER_ID_START) ? ""
				: appNameService.getSzSubjectId(appName);

		// $userId:$szSubject => {appName:$appName, clientId:$clientId}
		set(tableKeyForClientId(userId, szSubject), KEY_FOR_APP_NAME, appName);
		set(tableKeyForClientId(userId, szSubject), KEY_FOR_CLIENT_ID, clientId);

		// $clientId => { userId:"$userId", szSubject:"$szSubject"}
		set(tableKeyForAppName(clientId), KEY_FOR_USER_ID,
				String.valueOf(userId));
		set(tableKeyForAppName(clientId), KEY_FOR_SZSUBJECT, szSubject);
	}

	/**
	 * 清除用户跟clientId绑定的数据
	 * 
	 * @param userId
	 * @param szSubject
	 */
	@Override
	public void deleteClientId(int userId, String szSubject) {
		if (userId >= Constants.SICK_USER_ID_START) {
			szSubject = "";
		}
		String clientId = getClientId(userId, szSubject);

		redisTemplate.delete(tableKeyForClientId(userId, szSubject));
		redisTemplate.delete(tableKeyForClientId(userId, szSubject));
		redisTemplate.delete(tableKeyForAppName(clientId));
	}

	/**
	 * 获取缓存的客户端ID
	 * 
	 * @param userId
	 *            医生或患者的ID
	 * @return
	 */
	@Override
	public String getClientId(int userId, String szSubject) {
		String clientId;
		String strUserId;
		if (userId >= Constants.SICK_USER_ID_START) {
			clientId = ops().get(tableKeyForClientId(userId, ""),
					KEY_FOR_CLIENT_ID);
		} else {
			clientId = ops().get(tableKeyForClientId(userId, szSubject),
					KEY_FOR_CLIENT_ID);
		}
		strUserId = ops().get(tableKeyForAppName(clientId), KEY_FOR_USER_ID);
		if ((null != strUserId) && (Integer.parseInt(strUserId) == userId)) {
			return clientId;
		}
		return null;
	}

	/**
	 * 获取缓存的appName
	 * 
	 * @param userId
	 *            医生或患者的ID
	 * @return
	 */
	@Override
	public String getAppName(int userId, String szSubject) {
		String appName;
		if (userId >= Constants.SICK_USER_ID_START) {
			appName = ops().get(tableKeyForClientId(userId, ""),
					KEY_FOR_APP_NAME);
		} else {
			appName = ops().get(tableKeyForClientId(userId, szSubject),
					KEY_FOR_APP_NAME);
		}
		return appName;
	}

	private static String tableKeyForClientId(int userId, String szSubject) {
		return String.format("clientId:id:%s:%s", userId, szSubject);
	}

	private static String tableKeyForAppName(String clientId) {
		return String.format("clientId:app:%s", clientId);
	}

}
