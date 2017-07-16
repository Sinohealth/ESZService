package com.sinohealth.eszservice.service.base;

/**
 * ClientId Service
 * 
 * @author 黄世莲
 *
 */
public interface IClientIdService {
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
	public void setClientId(int userId, String appName, String clientId);

	/**
	 * 清除用户跟clientId绑定的数据
	 * 
	 * @param userId
	 * @param szSubject
	 */
	public void deleteClientId(int userId, String szSubject);

	/**
	 * 获取缓存的客户端ID
	 * 
	 * @param userId
	 *            医生或患者的ID
	 * @return
	 */
	public String getClientId(int userId, String szSubject);

	/**
	 * 获取缓存的appName
	 * 
	 * @param userId
	 *            医生或患者的ID
	 * @return
	 */
	public String getAppName(int userId, String szSubject);
}
