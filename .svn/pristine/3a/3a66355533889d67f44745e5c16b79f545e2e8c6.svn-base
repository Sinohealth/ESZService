package com.sinohealth.eszservice.dto.visit.sick;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.TakeMedRecordEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dto.visit.sick.SickDailyResultDto.DailyNoteElem;
import com.sinohealth.eszservice.dto.visit.sick.SickDailyResultDto.UnwellElem;

public class SickDailyResultBody implements Serializable {
	private static final long serialVersionUID = -9124950154420451128L;

	private final static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 需要填写的体征项列表ID，如[1501,1502]
	 */
	@JsonRawValue
	private String bodysignItems;

	@JsonSerialize(using = BodysignValuesSerializer.class)
	// 使用BodysignValuesSerializer来序列化
	private List<BodySignValueEntity> bodysigns;

	@JsonSerialize(using = TakeMedsSerializer.class)
	// 使用TakeMedsSerializer来序列化
	private List<TakeMedRecordEntity> takeMeds;

	private List<DailyNoteElem> dailyNotes;

	private List<UnwellElem> unwellItems;

	@JsonIgnore
	private List<VisitItemEntity> anticoagItemList = new ArrayList<VisitItemEntity>();

	public JsonNode getAnticoagItems() {
		ArrayNode rootNode = mapper.createArrayNode();
		for (VisitItemEntity e : anticoagItemList) {
			ObjectNode node = rootNode.addObject();
			node.put("itemId", e.getItemId());
			node.put("zhName", e.getZhName());
			node.put("enName", e.getEnName());
			node.put("type", e.getType());
			node.put("unit", e.getUnit());
			node.put("options", e.getOptions() == null ? "" : e.getOptions());
		}
		return rootNode;
	}

	public List<VisitItemEntity> getAnticoagItemList() {
		return anticoagItemList;
	}

	public void setAnticoagItemList(List<VisitItemEntity> anticoagItemList) {
		this.anticoagItemList = anticoagItemList;
	}

	public String getBodysignItems() {
		return bodysignItems;
	}

	public void setBodysignItems(String bodysignItems) {
		this.bodysignItems = bodysignItems;
	}

	public List<BodySignValueEntity> getBodysigns() {
		if (null == bodysigns) {
			bodysigns = new ArrayList<BodySignValueEntity>();
		}
		return bodysigns;
	}

	public void setBodysigns(List<BodySignValueEntity> bodysigns) {
		this.bodysigns = bodysigns;
	}

	public List<TakeMedRecordEntity> getTakeMeds() {
		if (null == takeMeds) {
			takeMeds = new ArrayList<TakeMedRecordEntity>();
		}
		return takeMeds;
	}

	public void setTakeMeds(List<TakeMedRecordEntity> takeMeds) {
		this.takeMeds = takeMeds;
	}

	public List<DailyNoteElem> getDailyNotes() {
		if (null == dailyNotes) {
			dailyNotes = new ArrayList<DailyNoteElem>();
		}
		return dailyNotes;
	}

	public void setDailyNotes(List<DailyNoteElem> dailyNotes) {
		this.dailyNotes = dailyNotes;
	}

	public List<UnwellElem> getUnwellItems() {
		if (null == unwellItems) {
			unwellItems = new ArrayList<UnwellElem>();
		}
		return unwellItems;
	}

	public void setUnwellItems(List<UnwellElem> unwellItems) {
		this.unwellItems = unwellItems;
	}

	public int getHasAnticoag() {
		return (null != anticoagItemList) && (anticoagItemList.size() > 0) ? 1
				: 0;
	}

	/**
	 * 体征序列化类
	 * 
	 * @author 黄世莲
	 * 
	 */
	public static class BodysignValuesSerializer extends
			JsonSerializer<List<BodySignValueEntity>> {

		@Override
		public void serialize(List<BodySignValueEntity> values,
				JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			ArrayNode rootNode = mapper.createArrayNode();

			String lastReportDate = null; // 用来计算上一条的报告的时间
			ObjectNode dNode = null;
			ArrayNode vNodes = null;
			for (int i = 0, j = values.size(); i < j; i++) {
				BodySignValueEntity valueEnt = values.get(i);
				String reportDate = DateUtils.formatDate(valueEnt
						.getReportDate());

				if (null == lastReportDate) {
					lastReportDate = reportDate;
					dNode = rootNode.addObject(); // 创建对象
					dNode.put("d", lastReportDate); // 创建d节点
					vNodes = dNode.putArray("v"); // 创建v节点
					ArrayNode varrayNodes = vNodes.addArray(); // 创建v节点下的元素
					varrayNodes.add(valueEnt.getItem().getItemId());
					varrayNodes.add(valueEnt.getReportValue());
					varrayNodes.add(valueEnt.getReportWarnLevel());
					varrayNodes.add(valueEnt.getType());
					varrayNodes.add(valueEnt.getValue2() == null ? ""
							: valueEnt.getValue2());
					varrayNodes.add(valueEnt.getText() == null ? "" : valueEnt
							.getText());
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
					varrayNodes.add(valueEnt.getType());
					varrayNodes.add(valueEnt.getValue2() == null ? ""
							: valueEnt.getValue2());
					varrayNodes.add(valueEnt.getText() == null ? "" : valueEnt
							.getText());
				} else {
					// 剩下的是同一天的
					ArrayNode varrayNodes = vNodes.addArray(); // 创建v节点下的元素
					varrayNodes.add(valueEnt.getItem().getItemId());
					varrayNodes.add(valueEnt.getReportValue());
					varrayNodes.add(valueEnt.getReportWarnLevel());
					varrayNodes.add(valueEnt.getType());
					varrayNodes.add(valueEnt.getValue2() == null ? ""
							: valueEnt.getValue2());
					varrayNodes.add(valueEnt.getText() == null ? "" : valueEnt
							.getText());
				}
			}
			jgen.writeTree(rootNode);
		}
	}

	/**
	 * 服药序列化类
	 * 
	 * @author 黄世莲
	 * 
	 */
	public static class TakeMedsSerializer extends
			JsonSerializer<List<TakeMedRecordEntity>> {

		@Override
		public void serialize(List<TakeMedRecordEntity> values,
				JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			ArrayNode takeMedsNode = mapper.createArrayNode();
			for (TakeMedRecordEntity e : values) {
				ArrayNode n = takeMedsNode.addArray();
				n.add(DateUtils.formatDate(e.getCurDate()));
				n.add(e.getTaked());
			}
			jgen.writeTree(takeMedsNode);
		}
	}

}
