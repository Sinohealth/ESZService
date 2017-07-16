package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

/**
 * 患者属性解析
 * 
 * @author 黄世莲
 * 
 */
public class SickProfileParser {
	static ObjectMapper mapper = new ObjectMapper();

	public static SickEntity parse(String data) throws ParseException {
		if (null == data || "".equals(data)) {
			return null;
		}
		try {
			SickEntity sick = mapper.readValue(data, SickEntity.class);
			return sick;
		} catch (IOException e) {
			throw new ParseException();
		}
	}

	public static String compile(SickEntity sick) {
		try {
			ObjectNode node = compileToJson(sick);
			return mapper.writeValueAsString(node);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "[]";
	}

	public static ObjectNode compileToJson(SickEntity sick) {
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("sickId", sick.getId());
		rootNode.put("name", sick.getName());
		rootNode.put("sex", sick.getSex());
		if (null != sick.getBirthday()) {
			rootNode.put("birthday", DateUtils.formatDate(sick.getBirthday()));
			// 计算年龄
			int curYear = Integer.parseInt(DateUtils.getYear());
			int userYear = Integer.parseInt(DateUtils.formatDate(
					sick.getBirthday(), "yyyy"));
			rootNode.put("age", curYear - userYear);
		} else {
			rootNode.put("birthday", "");
			rootNode.put("age", 0);
		}
		
		String url = null != sick.getHeadShot() ? sick.getHeadShot() : "";
		if (!"".equals(url)) {
			url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
		}
		rootNode.put("headshot", url);

		String smallHeadshotUrl = (null != sick.getSmallHeadshot()) ? sick
				.getSmallHeadshot() : "";
		if (!"".equals(smallHeadshotUrl)) {
			smallHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
					smallHeadshotUrl);
		}
		rootNode.put("smallHeadshot", url);
		return rootNode;
	}
}
