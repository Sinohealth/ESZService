package com.sinohealth.eszservice.queue;

import com.sinohealth.eszservice.common.utils.SpringContextHolder;
import com.sinohealth.eszservice.queue.entity.BaseMessage;

/**
 * 队列处理工具
 * 
 * @author 黄世莲
 * 
 */
public class QueueUtils {
	private static RedisQueue<BaseMessage> instance;

	public static RedisQueue<BaseMessage> getInstance() {
		if (null == instance) {
			instance = SpringContextHolder.getBean("eszserviceQueue");
		}
		return instance;
	}

	public synchronized static void push(BaseMessage message) {
		getInstance().pushFromHead(message);
	}
}