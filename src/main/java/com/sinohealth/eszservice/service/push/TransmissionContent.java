package com.sinohealth.eszservice.service.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TransmissionContent implements Serializable {

	private static final long serialVersionUID = 6911552484057484842L;

	private String path = "";

	private Map<String, Object> params = new HashMap<String, Object>();

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void addParam(String key, Object value) {
		params.put(key, value);
	}

}
