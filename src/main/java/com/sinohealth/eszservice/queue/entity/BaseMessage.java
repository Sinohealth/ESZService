package com.sinohealth.eszservice.queue.entity;

import java.io.Serializable;

public abstract class BaseMessage implements Serializable {

	private static final long serialVersionUID = -5397505954567478446L;

	public abstract String getHandlerName();

}
