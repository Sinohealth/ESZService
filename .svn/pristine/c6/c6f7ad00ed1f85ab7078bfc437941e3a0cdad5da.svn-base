package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.VisitItemId;
import com.sinohealth.eszorm.entity.medicine.DosageFormEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionItemEntity;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

/**
 * 处方解析类
 * 
 * @author 黄世莲
 * 
 */
public class PrescriptionParser {

	/**
	 * 
	 * 
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static List<VisitPrescriptionEntity> parse(String data)
			throws ParseException {
		if (null == data || "".equals(data)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		List<VisitPrescriptionEntity> prescriptions = new ArrayList<>();
		ObjectNode rootNode;
		try {
			rootNode = mapper.readValue(data, ObjectNode.class);
		} catch (IOException e) {
			throw new ParseException();
		}

		JsonNode picsNode = rootNode.path("pics");

		if (picsNode.isArray()) {
			for (JsonNode jsonNode : picsNode) {
				VisitPrescriptionEntity e = new VisitPrescriptionEntity();
				e.setId((null != jsonNode.get("imgId")) ? jsonNode.get("imgId")
						.intValue() : null);
				e.setMedPic(jsonNode.get("img").textValue());
				e.setSmallMedPic(jsonNode.get("thumb").textValue());
				prescriptions.add(e);
			}
		}

		return prescriptions;
	}

	public static String compile(Set<VisitPrescriptionEntity> prescriptions) {
		if (null == prescriptions) {
			return "{}";
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = compileToJsonNode(prescriptions);

		try {
			return mapper.writeValueAsString(node);
		} catch (IOException e) {
			return "{}";
		}
	}

	public static ObjectNode compileToJsonNode(
			Set<VisitPrescriptionEntity> prescriptions) {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ArrayNode picsNode = rootNode.putArray("pics");
		ArrayNode textNode = rootNode.putArray("text");

		// 就算没有数据，也要返回结构
		if (null == prescriptions) {
			return null;
		}

		for (Iterator<VisitPrescriptionEntity> iterator = prescriptions
				.iterator(); iterator.hasNext();) {
			VisitPrescriptionEntity AppPrescriptionEntity = (VisitPrescriptionEntity) iterator
					.next();
			// 如果是患者已经重传的图片，路过
			if (AppPrescriptionEntity.getReuploaded() == new Integer(1)) {
				continue;
			}
			ObjectNode node = picsNode.addObject();
			node.put("imgId", AppPrescriptionEntity.getId());

			node.put("itemId", VisitItemId.PRESCRIPTION_ID);

			// 图像
			String img = AppPrescriptionEntity.getMedPic();
			if (null != img && !"".endsWith(img)) {
				// 图片增加token的处理;
				img = QiniuService.getDownloadUrlStr(Space.PRESCRIPTION, img);
			}
			node.put("img", img);

			// 缩略图
			String thumb = AppPrescriptionEntity.getSmallMedPic();
			if (null != img && !"".endsWith(img)) {
				thumb = QiniuService.getDownloadUrlStr(Space.PRESCRIPTION,
						thumb);
			}
			node.put("thumb", thumb);

			node.put("status", AppPrescriptionEntity.getDoctorMarkStatus());

			node.put(
					"remarks",
					(null != AppPrescriptionEntity.getDoctorMarkRemarks()) ? AppPrescriptionEntity
							.getDoctorMarkRemarks() : "");

			Set<VisitPrescriptionItemEntity> items = AppPrescriptionEntity
					.getItems();
			if (null != items) {
				for (Iterator<VisitPrescriptionItemEntity> iterator2 = items
						.iterator(); iterator2.hasNext();) {
					VisitPrescriptionItemEntity appPrescriptionItemEntity = (VisitPrescriptionItemEntity) iterator2
							.next();
					ObjectNode itemNode = textNode.addObject();
					itemNode.put("imgId", AppPrescriptionEntity.getId());
					itemNode.put("name", appPrescriptionItemEntity.getName());
					itemNode.put("standard",
							appPrescriptionItemEntity.getStandard());
					itemNode.put("num", appPrescriptionItemEntity.getNum());
					DosageFormEntity dosageForm = appPrescriptionItemEntity
							.getDosageForm();
					itemNode.put("dosage_from",
							(null != dosageForm ? dosageForm.getName() : ""));
					itemNode.put("dosage",
							appPrescriptionItemEntity.getDosage());
				}
			}
		}

		return rootNode;
	}

	public static ObjectNode inspectionToJsonNode(
			Set<CheckItemValueEntity> items, Set<VisitImgEntity> imgs) {
		if (null == items && null == imgs) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ArrayNode valuesNode = rootNode.putArray("values");
		ArrayNode picsNode = rootNode.putArray("pics");
		for (CheckItemValueEntity item : items) {
			if (null != item) {
				ObjectNode node = valuesNode.addObject();
				node.put("resultId",
						null != item.getResultId() ? item.getResultId() : 0);
				node.put("itemId",
						null != item.getVisitItem().getItemId() ? item
								.getVisitItem().getItemId() : 0);
				node.put("value",
						null != item.getReportValue() ? item.getReportValue()
								: "");
				node.put("warnLevel", item.getReportWarnLevel());
			}
		}

		for (VisitImgEntity o : imgs) {
			if (null != o) {
				ObjectNode node = picsNode.addObject();
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

				node.put("remarks",
						null == o.getRemarks() ? "" : o.getRemarks());
			}
		}
		return rootNode;
	}

}
