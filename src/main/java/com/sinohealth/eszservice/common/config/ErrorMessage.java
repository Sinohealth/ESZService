package com.sinohealth.eszservice.common.config;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sinohealth.eszservice.common.utils.PropertiesLoader;

public class ErrorMessage {

	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader = new PropertiesLoader(
			"errCode.properties");

	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (null == value) {
			value = propertiesLoader.getProperty(key);
			map.put(key, value);
		}
		return value;
	}
	
	/**
	 * 获取配置
	 */
	public static String getConfig(int key) {
		return getConfig("errCode."+key);
	}
}
