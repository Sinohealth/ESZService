package com.sinohealth.eszservice.dto.visit.doctor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.BodySignEntity;
import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;

/**
 * @author lianhuang
 * @since 1.03
 * 
 */
public class TemplDetailBody implements Serializable {
	private static final long serialVersionUID = 446841781228474377L;

	private static ObjectMapper mapper = new ObjectMapper();

	@JsonIgnoreProperties(value = { "bodysignId" })
	private List<BodySignEntity> bodySigns;
	
	@JsonIgnoreProperties(value = { "bodysignId" })
	private List<BodySignEntity> anticoag;//抗凝指标

	@JsonSerialize(using = DailynotesSerializer.class)
	private List<DailynoteEntity> dailynote;

	private TemplateEntity templ;

	public List<BodySignEntity> getAnticoag() {
		if (null == anticoag) {
			anticoag = new ArrayList<>();
		}
		return anticoag;
	}

	public void setAnticoag(List<BodySignEntity> anticoag) {
		this.anticoag = anticoag;
	}

	public List<BodySignEntity> getBodySigns() {
		if (null == bodySigns) {
			bodySigns = new ArrayList<>();
		}
		return bodySigns;
	}

	public void setBodySigns(List<BodySignEntity> bodySigns) {
		this.bodySigns = bodySigns;
	}

	public List<DailynoteEntity> getDailynote() {
		if (null == dailynote) {
			dailynote = new ArrayList<>();
		}
		return dailynote;
	}

	public void setDailynote(List<DailynoteEntity> dailynote) {
		this.dailynote = dailynote;
	}

	public TemplateEntity getTempl() {
		if (null == templ) {
			templ = new TemplateEntity();
		}
		return templ;
	}

	public void setTempl(TemplateEntity templ) {
		this.templ = templ;
	}

	public ObjectNode getVisitPoints() {
		ObjectNode node = mapper.createObjectNode();
		node.put("cycleUnit", templ.getCycleUnit());
		ArrayNode phases = node.putArray("phases");
		if (null != templ.getPhases()) {

			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_YEAR, 1);
			tomorrow.set(Calendar.HOUR, 0);
			tomorrow.set(Calendar.MINUTE, 0);
			tomorrow.set(Calendar.SECOND, 0);

			for (TemplatePhaseEntity p : templ.getPhases()) {
				ObjectNode n = phases.addObject();
				n.put("timePoint", p.getTimePoint());
				n.putPOJO("itemIds", p.getItemIds());
				n.put("editable", p.getEditable());
				n.put("isFuzhenItem", p.getIsFuzhenItem());
				n.put("selected", p.getSelected());

				/*
				 * 2015年9-2日修改为按随访日期开始时间去控制，早于明天的随访不能选择
				 */
				int selectable = p.getSelectable();
				// 2015年9-8日因为计算出来的时间可能会有误差，所以有可能返回的跟实际的不一样，所以直接返回数据库所给的。
				// 如果自动任务有错误，则在保存计划时做过滤
				//
				// if (!templ.isVisible()) { // 如果是计划
				// if (null != p.getVisitTime()) {
				// cal.setTime(p.getVisitTime());
				// if (cal.compareTo(tomorrow) < 0) { // 如果是小于明天，不能修改
				// selectable = 0;
				// }
				// }
				// }
				n.put("selectable", selectable);// 是否可以改变阶段的selected状态。0-不可，1-可以。阶段在【未复诊】状态时为1
			}
		}
		return node;
	}

	/**
	 * 体征序列化类
	 * 
	 * @author 黄世莲
	 * 
	 */
	public static class DailynotesSerializer extends
			JsonSerializer<List<DailynoteEntity>> {

		@Override
		public void serialize(List<DailynoteEntity> values, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			ObjectNode node = mapper.createObjectNode();
			ArrayNode dailynoteIds = node.putArray("noteIds");
			node.put("otherNote", "");

			for (DailynoteEntity e : values) {
				if (!e.isOtherNote()) { // 如果不是其它注意事项
					dailynoteIds.add(e.getNoteId()); // 加入数组内
				} else {
					node.put("otherNote", e.getContent());
				}
			}
			jgen.writeTree(node);
		}
	}

}
