package com.sinohealth.eszservice.service.visit.paser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.exception.DailyResultParseExecption;

public class BodySignValueParser {
//	public static List<BodySignValueEntity> parse(String data)
//			throws DailyResultParseExecption {
//		if (null == data || "".equals(data)) {
//			return null;
//		}
//		try {
//			List<BodySignValueEntity> elems = new ArrayList<BodySignValueEntity>();
//
//			JSONArray itemNodes = new JSONArray(data);
//			if (null != itemNodes) {
//				for (int i = 0; i < itemNodes.length(); i++) {
//					JSONObject node = (JSONObject) itemNodes.get(i);
//					int itemId = node.getInt("itemId");
//					String value = node.getString("value");
//					VisitItemEntity item = new VisitItemEntity();
//					item.setItemId(itemId);
//					BodySignValueEntity vo = new BodySignValueEntity();
//					vo.setItem(item);
//					vo.setReportValue(value);
//					elems.add(vo);
//				}
//			}
//			return elems;
//		} catch (JSONException e) {
//			throw new DailyResultParseExecption();
//		}
//	}

	/**
	 * 转换体征项，转为：[{d: "2015-1-10",v: [[123,"80",0],…]}, {d:,…}]
	 * 
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	public static String compile(List<BodySignValueEntity> values)
			throws ParseException {

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode rootNode = mapper.createArrayNode();

		String lastReportDate = null; // 用来计算上一条的报告的时间
		ObjectNode dNode = null;
		ArrayNode vNodes = null;
		for (int i = 0, j = values.size(); i < j; i++) {
			BodySignValueEntity valueEnt = values.get(i);
			String reportDate = DateUtils.formatDate(valueEnt.getReportDate());

			if (null == lastReportDate) {
				lastReportDate = reportDate;
				dNode = rootNode.addObject(); // 创建对象
				dNode.put("d", lastReportDate); // 创建d节点
				vNodes = dNode.putArray("v"); // 创建v节点
				ArrayNode varrayNodes = vNodes.addArray(); // 创建v节点下的元素
				varrayNodes.add(valueEnt.getItem().getItemId());
				varrayNodes.add(valueEnt.getReportValue());
				varrayNodes.add(valueEnt.getReportWarnLevel());
				continue;
			}

			// 日期不相等，说明不在同一天
			if (!lastReportDate.equals(reportDate)) {
				lastReportDate = reportDate;
				dNode = rootNode.addObject(); // 创建对象
				dNode.put("d", lastReportDate); // 创建d节点
				vNodes = dNode.putArray("v"); // 创建v节点
				ArrayNode varrayNodes = vNodes.addArray(); // 创建v节点下的元素
				varrayNodes.add(valueEnt.getItem().getItemId());
				varrayNodes.add(valueEnt.getReportValue());
				varrayNodes.add(valueEnt.getReportWarnLevel());
			} else {
				// 剩下的是同一天的
				ArrayNode varrayNodes = vNodes.addArray(); // 创建v节点下的元素
				varrayNodes.add(valueEnt.getItem().getItemId());
				varrayNodes.add(valueEnt.getReportValue());
				varrayNodes.add(valueEnt.getReportWarnLevel());
			}
		}

		try {
			return mapper.writeValueAsString(rootNode);
		} catch (JsonProcessingException e) {
			throw new ParseException();
		}
	}
}
