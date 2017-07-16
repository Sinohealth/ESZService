package com.sinohealth.eszservice.queue.handler;

import org.springframework.stereotype.Component;

import com.sinohealth.eszservice.queue.entity.BaseMessage;

@Component
public class StringHandler implements IQueueHandler {

	@Override
	public void process(BaseMessage message) {
		System.out.println("StringHandler==");
	}

}
