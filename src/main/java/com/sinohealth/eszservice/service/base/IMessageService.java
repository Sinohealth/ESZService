package com.sinohealth.eszservice.service.base;

import java.util.Date;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszorm.message.MessageEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.dto.message.MessageListDto;

/**
 * 消息service
 * 
 * @author 黄世莲
 * 
 */
public interface IMessageService {

	/**
	 * 增加随访消息
	 * 
	 * @param userId
	 * @param title
	 * @param appContent
	 * @return
	 */
	MessageEntity addMessage(int userId, String title, String appContent);

	/**
	 * 增加随访消息
	 * 
	 * @param doctorId
	 * @param title
	 * @param content
	 * @param szSubject
	 * @return
	 */
	MessageEntity addMessage(int doctorId, String title, String content,
			String szSubject);

	/**
	 * 删除一条消息
	 * 
	 * @param userId
	 * @param id
	 *            要删除的消息ID，多个Id，用逗号”,“隔开
	 */
	BaseDto deleteForApi(int userId, String id);

	/**
	 * 获取随访消息列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param beginTime
	 *            获取这一个时间之后的消息
	 * @param cTime
	 *            获取这一个时间之前的消息
	 * @param num
	 *            获取记录数
	 * @return
	 */
	MessageListDto getListForApi(int userId, Date beginTime, Date createTime,
			int num);

	/**
	 * 获取随访消息列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param beginTime
	 *            获取这一个时间之后的消息
	 * @param cTime
	 *            获取这一个时间之前的消息
	 * @param pageSize
	 *            获取记录数
	 * @return
	 */
	MessageListDto getListForApi(int userId, Date beginTime, Date cTime,
			int pageSize, String szSubject);

	/**
	 * 获取随访消息列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param bTime
	 *            获取这一个时间之后的消息
	 * @param cTime
	 *            获取这一个时间之前的消息
	 * @param pageSize
	 *            获取记录数
	 * @return
	 */
	List<MessageEntity> getDoctorList(int userId, Date beginTime, Date cTime,
			int pageSize, String szSubject);

	/**
	 * 获取随访消息列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param bTime
	 *            获取这一个时间之后的消息
	 * @param cTime
	 *            获取这一个时间之前的消息
	 * @param pageSize
	 *            获取记录数
	 * @return
	 */
	List<MessageEntity> getSickList(int userId, Date bTime, Date cTime,
			int pageSize);

	/**
	 * 将记录更新为已读状态
	 * 
	 * @param list
	 */
	void updateMessageRead(List<MessageEntity> list);

	/**
	 * @param appName
	 * @param pageSizei
	 * @param cTime
	 * @param bTime
	 * @return
	 */
	List<MessageEntity> getSystemMessage(String appName, Date bTime,
			Date cTime, int pageSize);

	/**
	 * 获取未读的随访消息的数量
	 * 
	 * @param userId
	 * @param szSubject
	 * @return
	 */
	BaseDto getUnreadCountForApi(int userId, String szSubject);

	void addMessage(int sickId, String title, String message,
			SzSubjectEntity szSubject);

}
