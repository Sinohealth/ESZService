package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;

/**
 * 既往史解析类
 * 
 * @author 黄世莲
 * 
 */
public class PastHistoryParser {
	static ObjectMapper mapper = new ObjectMapper();

	public static final String MERGE_CHAR = "@@";

	/**
	 * 将JSON格式字符串转为AppPastHistoryEntity对象的值<br/>
	 * JSON格式如：
	 * 
	 * <pre>
	 * { disease: ["xxx",…], allergy:, surgical:}
	 * </pre>
	 * 
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static AppPastHistoryComponent parse(String data)
			throws ParseException {
		if (null == data || "".equals(data)) {
			return null;
		}

		AppPastHistoryComponent ent = new AppPastHistoryComponent();

		ObjectNode rootNode;
		try {
			rootNode = mapper.readValue(data, ObjectNode.class);
		} catch (IOException e) {
			throw new ParseException();
		}
		StringBuffer buf = new StringBuffer();

		// 疾病史
		JsonNode diseaseNode = rootNode.path("disease");
		if (diseaseNode.isArray()) {
			buf.setLength(0); // 清空StringBuffer内容
			for (JsonNode jsonNode : diseaseNode) {
				String s = jsonNode.textValue();
				if (null == s || "".endsWith(s)) {
					continue;
				}
				if (buf.length() != 0) {
					buf.append(MERGE_CHAR);
				}
				buf.append(s);
			}
			ent.setMedicalHistories(buf.toString());
		}

		// 过敏史
		JsonNode allergyNode = rootNode.path("allergy");
		if (allergyNode.isArray()) {
			buf.setLength(0); // 清空StringBuffer内容
			for (JsonNode jsonNode : allergyNode) {
				String s = jsonNode.textValue();
				if (null == s || "".endsWith(s)) {
					continue;
				}
				if (buf.length() != 0) {
					buf.append(MERGE_CHAR);
				}
				buf.append(s);
			}
			ent.setAllergyHistories(buf.toString());
		}

		// 手术史
		JsonNode surgicalNode = rootNode.path("surgical");
		if (surgicalNode.isArray()) {
			buf.setLength(0); // 清空StringBuffer内容
			for (JsonNode jsonNode : surgicalNode) {
				String s = jsonNode.textValue();
				if (null == s || "".endsWith(s)) {
					continue;
				}
				if (buf.length() != 0) {
					buf.append(MERGE_CHAR);
				}
				buf.append(s);
			}
			ent.setSurgicalHistories(buf.toString());
		}
		return ent;
	}

	public static String compile(AppPastHistoryComponent appPastHistoryComponent) {
		if (null == appPastHistoryComponent) {
			return "{}";
		}
		Object rootNode = compileToJson(appPastHistoryComponent);
		try {
			return mapper.writeValueAsString(rootNode);
		} catch (IOException e) {
			return "{}";
		}
	}

	public static String compile(
			AppPastHistoryComponent appPastHistoryComponent, String date) {
		if (null == appPastHistoryComponent) {
			return "{}";
		}
		ObjectNode rootNode = mapper.createObjectNode();

		// 疾病史
		if (null != appPastHistoryComponent.getMedicalHistories()
				&& !"".endsWith(appPastHistoryComponent.getMedicalHistories())) {
			ArrayNode diseaseNode = rootNode.putArray("disease");
			// System.err.println("diseaseNode:" + diseaseNode);
			String[] arr = appPastHistoryComponent.getMedicalHistories().split(
					MERGE_CHAR);
			for (String s : arr) {
				diseaseNode.add(s);
			}
		}

		// 过敏史
		if (null != appPastHistoryComponent.getAllergyHistories()
				&& !"".endsWith(appPastHistoryComponent.getAllergyHistories())) {
			ArrayNode allergyNode = rootNode.putArray("allergy");
			String[] arr = appPastHistoryComponent.getAllergyHistories().split(
					MERGE_CHAR);
			for (String s : arr) {
				allergyNode.add(s);
			}
		}

		// 手术史
		if (null != appPastHistoryComponent.getSurgicalHistories()
				&& !"".endsWith(appPastHistoryComponent.getSurgicalHistories())) {
			ArrayNode surgicalNode = rootNode.putArray("surgical");
			String[] arr = appPastHistoryComponent.getSurgicalHistories()
					.split(MERGE_CHAR);
			for (String s : arr) {
				surgicalNode.add(s);
			}
		}
		if (null != date) {
			rootNode.put("updateTime", date);
		}

		try {
			// System.err.println("parse json:"+mapper.writeValueAsString(rootNode));
			return mapper.writeValueAsString(rootNode);
		} catch (IOException e) {
			return "{}";
		}
	}

	public static ObjectNode compileToJson(
			AppPastHistoryComponent appPastHistoryComponent) {

		ObjectNode rootNode = mapper.createObjectNode();

		if (null == appPastHistoryComponent) {
			return rootNode;
		}

		// System.out.println("apphistory: "+appPastHistoryComponent.getMedicalHistories());
		// 疾病史
		ArrayNode diseaseNode = rootNode.putArray("disease");
		if (null != appPastHistoryComponent.getMedicalHistories()
				&& !"".endsWith(appPastHistoryComponent.getMedicalHistories())) {
			String[] arr = appPastHistoryComponent.getMedicalHistories().split(
					MERGE_CHAR);
			for (String s : arr) {
				diseaseNode.add(s);
			}
		}

		// 过敏史
		ArrayNode allergyNode = rootNode.putArray("allergy");
		if (null != appPastHistoryComponent.getAllergyHistories()
				&& !"".endsWith(appPastHistoryComponent.getAllergyHistories())) {
			String[] arr = appPastHistoryComponent.getAllergyHistories().split(
					MERGE_CHAR);
			for (String s : arr) {
				allergyNode.add(s);
			}
		}

		// 手术史
		ArrayNode surgicalNode = rootNode.putArray("surgical");
		if (null != appPastHistoryComponent.getSurgicalHistories()
				&& !"".endsWith(appPastHistoryComponent.getSurgicalHistories())) {
			String[] arr = appPastHistoryComponent.getSurgicalHistories()
					.split(MERGE_CHAR);
			for (String s : arr) {
				surgicalNode.add(s);
			}
		}

		return rootNode;
	}
}
