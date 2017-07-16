package com.sinohealth.eszservice.dao.message;

import com.sinohealth.eszorm.message.MessageEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

/**
 * 消息Dao
 * 
 * @author 黄世莲
 * 
 */
public interface IMessageDao extends IGenericDao<MessageEntity, Long> {

	/**
	 * 获取医生未读消息的数量
	 * 
	 * @param userId
	 * @param szSubject
	 * @return
	 */
	int getDoctorUnreadCount(int userId, String szSubject);

	/**
	 * 获取患者未读消息的数量
	 * 
	 * @param userId
	 * @return
	 */
	int getSickUnreadCount(int userId);

}
