package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;

/**
 * 检验项数据解析
 * 
 * @author 黄世莲
 * 
 */
public class ValueParser {

	/**
	 * 将[{itemId:123,value""},{}]转为CheckItemValueEntity对象
	 * 
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static Set<CheckItemValueEntity> parse(String data)
			throws ParseException {
		if (null == data || "".equals(data)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();

		Set<CheckItemValueEntity> list = new HashSet<>();
		ArrayNode rootNode;
		try {
			rootNode = mapper.readValue(data, ArrayNode.class);
		} catch (IOException e) {
			throw new ParseException();
		}
		if (null != rootNode && rootNode.isArray()) {
			for (JsonNode jsonNode : rootNode) {
				CheckItemValueEntity val = new CheckItemValueEntity();
				if (null == jsonNode.get("itemId")) {
					throw new ParseException("缺少itemId参数");
				}
				int itemId = jsonNode.get("itemId").intValue();
//				if (0 == itemId) {
//					throw new ParseException("itemId不能为0");
//				}
				// 如果
				String reportValue = null != jsonNode.get("value") ? jsonNode
						.get("value").textValue() : "";
				//value为空字符串不保存到集合
				if (!"".equals(reportValue)) {
					val.setReportValue(reportValue);
					VisitItemEntity item = new VisitItemEntity();
					item.setItemId(itemId);
					val.setVisitItem(item);
					list.add(val);
				}
			}
		}

		return list;
	}
	
	public static void main(String[] args) throws Exception {
/*		String str="{\"student\":[{\"name\":\"leilei\",\"age\":23},{\"name\":\"leilei02\",\"age\":23}]}";	
        Student stu = null;
        List<Student> list = null;
        try {
        	ObjectMapper mapper = new ObjectMapper();
//			ArrayNode arrayNode = mapper.readValue(str,ArrayNode.class);
        	ObjectNode objectNode = mapper.readValue(str, ObjectNode.class);
			System.out.println("=================");
			
			JsonNode jsonNode = objectNode.path("student");
			System.out.println("student:"+jsonNode);
			if (jsonNode.isArray()) {
				for (JsonNode node:jsonNode) {
					String name = node.get("name").textValue();
					int age = node.get("age").intValue();
					System.out.println("name:"+name+" age:");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
		String str="[{\"name\":\"leilei\",\"age\":23},{\"name\":\"leilei02\",\"age\":23}]";
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.readValue(str, ArrayNode.class);
        for (JsonNode node:arrayNode) {
        	System.out.println("node:"+node);
        }
	}
}
