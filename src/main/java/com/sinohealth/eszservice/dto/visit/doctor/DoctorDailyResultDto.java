package com.sinohealth.eszservice.dto.visit.doctor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.paser.ParseException;

public class DoctorDailyResultDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -1049160176251934342L;

	@JsonIgnore
	private List<BodySignValueEntity> bodysigns;

	@JsonIgnore
	private DailyNoteElem dailyNotes;

	@JsonIgnore
	private UnwellElem unwellItems;
	
	@JsonIgnore
	private List<VisitItemEntity> anticoagItemList=new ArrayList<VisitItemEntity>(); 

	private ObjectMapper mapper = new ObjectMapper();

	public DoctorDailyResultDto() {
		super();
		dailyNotes = new DailyNoteElem();
		unwellItems = new UnwellElem();
	}

	/**
	 * 输出的结果
	 * 
	 * @return
	 */
	public JsonNode getResults() {
		ObjectNode rootNode = mapper.createObjectNode();
		ArrayNode bodysignsNode = rootNode.putArray("bodysigns");

		// 体征项
		if (null != bodysigns) {
			bodysignsNode.addAll(compileBodysigns(bodysigns));
		}

		// 日常注意事项
		if (null != dailyNotes) {
			rootNode.putPOJO("dailyNotes", dailyNotes);
		} else {
			rootNode.putArray("dailyNotes"); // 就算没有值，也要返回结构
		}

		// 不适症状项目
		if (null != unwellItems) {
			rootNode.putPOJO("unwellItems", unwellItems);
		} else {
			rootNode.putArray("unwellItems");
		}

		// 不适症状结果
		
		//抗凝指标项目
		ArrayNode anticoagItems = rootNode.putArray("anticoagItems");
		anticoagItems.addAll(compileAnticoagItems());

		return rootNode;
	}
	
	public ArrayNode compileAnticoagItems() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode rootNode = mapper.createArrayNode();
		for (VisitItemEntity e : anticoagItemList) {
			ObjectNode node = rootNode.addObject();
			node.put("itemId", e.getItemId());
			node.put("zhName", e.getZhName());
			node.put("enName", e.getEnName());
			node.put("type", e.getType());
		}
		return rootNode;
	}

	public List<VisitItemEntity> getAnticoagItemList() {
		return anticoagItemList;
	}

	public void setAnticoagItemList(List<VisitItemEntity> anticoagItemList) {
		this.anticoagItemList = anticoagItemList;
	}

	/**
	 * 转换体征项，转为：[{d: "2015-1-10",v: [[123,"80",0],…]}, {d:,…}]
	 * 
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	public ArrayNode compileBodysigns(List<BodySignValueEntity> values) {
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
		return rootNode;
	}

	public List<BodySignValueEntity> getBodysigns() {
		return bodysigns;
	}

	public void setBodysigns(List<BodySignValueEntity> bodysigns) {
		this.bodysigns = bodysigns;
	}

	public DailyNoteElem getDailyNotes() {
		return dailyNotes;
	}

	public void setDailyNotes(DailyNoteElem dailyNotes) {
		this.dailyNotes = dailyNotes;
	}

	public UnwellElem getUnwellItems() {
		return unwellItems;
	}

	public void setUnwellItems(UnwellElem unwellItems) {
		this.unwellItems = unwellItems;
	}

	public static class DailyNoteElem implements Serializable {

		private static final long serialVersionUID = -6927362172643701119L;

		@JsonIgnore
		private DiseaseEntity disease;

		private List<DailynoteEntity> notes = new ArrayList<>();

		public DiseaseEntity getDisease() {
			return disease;
		}

		public void setDisease(DiseaseEntity disease) {
			this.disease = disease;
		}

		public List<DailynoteEntity> getNotes() {
			return notes;
		}

		public void setNotes(List<DailynoteEntity> notes) {
			this.notes = notes;
		}

		public String getDiseaseId() {
			return disease.getId();
		}

		public String getDiseaseName() {
			return disease.getName();
		}

	}

	public static class UnwellElem implements Serializable {

		private static final long serialVersionUID = -6927362172643701119L;

		@JsonIgnore
		private DiseaseEntity disease;

		private List<VisitItemEntity> items = new ArrayList<>();

		public DiseaseEntity getDisease() {
			return disease;
		}

		public void setDisease(DiseaseEntity disease) {
			this.disease = disease;
		}

		public JsonNode getItems() {
			ObjectMapper mapper = new ObjectMapper();

			ArrayNode rootNode = mapper.createArrayNode();

			for (VisitItemEntity e : items) {
				ObjectNode node = rootNode.addObject();
				node.put("itemId", e.getItemId());
				node.put("zhName", e.getZhName());
				node.put("type", e.getType());
			}

			return rootNode;
		}

		public void setItems(List<VisitItemEntity> items) {
			this.items = items;
		}

		public String getDiseaseId() {
			return disease.getId();
		}

		public String getDiseaseName() {
			return disease.getName();
		}
		
		

	}

}
