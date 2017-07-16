package com.sinohealth.eszservice.service.sick;

/**
 * 患者在线状态service
 * 
 * @author 黄世莲
 *
 */
public interface ISickOnlineService {
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
			int online);

	/**
	 * 校验token是否有校
	 * 
	 * @param token
	 * @return
	 */
	public boolean hasToken(String token);

	/**
	 * 删除token，使token失效
	 * 
	 * @param token
	 */
	public void deleteToken(String token);

	/**
	 * 删除token，使token失效
	 * 
	 * @param token
	 */
	public void deleteToken(int userId);

	/**
	 * 根据用户ID找到对应的token
	 * 
	 * @param userId
	 * @return
	 */
	public String getToken(int userId);

	/**
	 * 根据token，找到对应的用户Id
	 * 
	 * @param token
	 * @return
	 */
	public int getUserId(String token);

	/**
	 * 用户最后访问时间，时间戳
	 * 
	 * @param token
	 * @return
	 */
	public long getLastVisitTime(String token);

	/**
	 * 更新用户最后访问时间，时间戳
	 * 
	 * @param t
	 *            时间戳
	 * @return
	 */
	public void setLastVisitTime(String token, long t);

	/**
	 * 设置token下线
	 * 
	 * @return
	 */
	public void offline(String token);

	/**
	 * 是否在线状态
	 * 
	 * @return
	 */
	public boolean isOnline(String token);
}
