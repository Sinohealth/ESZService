package com.sinohealth.eszservice.queue;

import com.sinohealth.eszservice.common.utils.SpringContextHolder;
import com.sinohealth.eszservice.queue.entity.BaseMessage;
import com.sinohealth.eszservice.queue.handler.IQueueHandler;

/**
 * 读取队列消息，根据消息内容推送消息
 * 
 * @author huanghonggen
 * 
 * @param <String>
 */
public class RedisQueueListener implements QueueListener<BaseMessage> {

	@Override
	public void onMessage(BaseMessage message) {
		try {
			IQueueHandler handler = SpringContextHolder.getBean(message
					.getHandlerName());
			handler.process(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}