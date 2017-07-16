package com.sinohealth.eszservice.queue;

public interface QueueListener<T> {
	public void onMessage(T message);
}