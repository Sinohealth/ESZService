package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

/**
 * 申请提交的检查项的解析类
 * 
 * @author 黄世莲
 * 
 */
public class AppItemParser {

	/**
	 * 将[{itemId:123,pics:[{img:,thumb:,status:},…]},{}]转为AppImgEntity对象
	 * 
	 * @deprecated v1.03版本以后不再建议使用，而是使用fastJson的直接转换
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static Set<VisitImgEntity> parse(String data) throws ParseException {
		if (null == data || "".equals(data)) {
			return new HashSet<VisitImgEntity>();
		}
		ObjectMapper mapper = new ObjectMapper();

		Set<VisitImgEntity> list = new HashSet<VisitImgEntity>();
		ArrayNode rootNode;
		try {
			rootNode = mapper.readValue(data, ArrayNode.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ParseException();
		}
		if (rootNode.isArray()) {
			try {
				for (JsonNode jsonNode : rootNode) {
					VisitImgEntity o = mapper.treeToValue(jsonNode,
							VisitImgEntity.class);
					list.add(o);
				}
			} catch (IOException e) {
				throw new ParseException("解析图片失败");
			}
		}

		return list;
	}

	public static String compile(Set<VisitImgEntity> list) {
		if (null == list) {
			return "[]";
		}

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode rootNode = compileToJsonNode(list);

		try {
			String res = mapper.writeValueAsString(rootNode);
			return res;
		} catch (IOException e) {
			return "[]";
		}
	}

	public static ArrayNode compileToJsonNode(Set<VisitImgEntity> list) {
		if (null == list) {
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode rootNode = mapper.createArrayNode();

		for (Iterator<VisitImgEntity> iterator = list.iterator(); iterator
				.hasNext();) {
			VisitImgEntity o = iterator.next();
			ObjectNode node = rootNode.addObject();
			node.put("imgId", o.getImgId());
			node.put("itemId", o.getItemId());

			// 图像
			String img = o.getImg();
			if (null != img && !"".endsWith(img)) {
				// 图片增加token的处理;
				img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
			}
			node.put("img", img);

			// 缩略图
			String thumb = o.getThumb();
			if (null != thumb && !"".endsWith(thumb)) {
				thumb = QiniuService.getDownloadUrlStr(Space.RECORD, thumb);
			}
			node.put("thumb", thumb);

			node.put("status", o.getStatus());

			node.put("remarks", null == o.getRemarks() ? "" : o.getRemarks());

		}

		return rootNode;
	}
}
