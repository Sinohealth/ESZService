package com.sinohealth.eszservice.service.base.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.sinohealth.eszservice.common.utils.PropertiesLoader;
import com.sinohealth.eszservice.service.base.IAppNameService;
import com.sinohealth.eszservice.service.base.exception.AppNameNotSupportException;

@Service
public class AppNameServiceImpl implements IAppNameService {

	/**
	 * 保存全局属性值
	 */
	private final static Map<String, String> map = new HashMap<String, String>();

	/**
	 * 属性文件加载对象
	 */
	private final static PropertiesLoader propertiesLoader = new PropertiesLoader(
			"appName.properties");

	public AppNameServiceImpl() {
		// 启动时，加载全部配置
		Properties props = propertiesLoader.getProperties();

		Enumeration<Object> e = props.keys();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			map.put(key, props.getProperty(key));
		}

	}

	@Override
	public String getSzSubjectId(String appName)
			throws AppNameNotSupportException {
		String value = map.get(appName);

		if (null == value) {
			throw new AppNameNotSupportException(appName);
		}

		return value;
	}

	/**
	 * 根据设备号获取key对应的专科
	 * 
	 * @param key
	 * @return
	 */
	public String getSubject(String key) {
		String value = "";
		if (key.startsWith("eszDoctor") || key.startsWith("eszSick")) {
			if (map.containsKey(key)) {
				value = map.get(key);
			}
		} else {
			if (map.containsValue(key)) {
				value = key;
			}
		}
		return value;
	}

}
