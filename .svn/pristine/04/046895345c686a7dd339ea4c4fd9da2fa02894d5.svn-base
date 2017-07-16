package com.sinohealth.eszservice.service.visit.paser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.BodySignEntity;
import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.common.dto.ConstantDoctorVisitErrs;

public class TemplateParser {
	public static TemplateEntity parse(String data) throws ParseException {
		JSONObject json;
		TemplateEntity templModel = new TemplateEntity();
		if (null != data && !"".equals(data)) {
			try {
				json = new JSONObject(data);

				// bodySigns
				JSONArray bss = json.getJSONArray("bodySigns");

				if (null != bss) {
					List<BodySignEntity> bodySigns = new ArrayList<BodySignEntity>();
					for (int i = 0; i < bss.length(); i++) {
						JSONObject tmp = bss.getJSONObject(i);
						BodySignEntity bodySign = new BodySignEntity();
						VisitItemEntity item = new VisitItemEntity();
						item.setItemId(tmp.getInt("itemId"));
						bodySign.setItem(item);
						bodySign.setFreq(tmp.getInt("freq"));
						bodySigns.add(bodySign);
					}
					templModel.setBodySigns(bodySigns);
				}

				// 抗凝指标

				if (json.has("anticoag")) {
					JSONArray anticoags = json.getJSONArray("anticoag");
					List<BodySignEntity> anticoagList = new ArrayList<BodySignEntity>();
					for (int i = 0; i < anticoags.length(); i++) {
						JSONObject tmp = anticoags.getJSONObject(i);
						BodySignEntity anticoag = new BodySignEntity();
						VisitItemEntity item = new VisitItemEntity();
						item.setItemId(tmp.getInt("itemId"));
						anticoag.setItem(item);
						anticoag.setFreq(tmp.getInt("freq"));
						anticoagList.add(anticoag);
					}
					templModel.setAnticoags(anticoagList);
				}

				// dailynote
				JSONObject dailynote = json.getJSONObject("dailynote");

				if (null != dailynote) {
					JSONArray noteIds = dailynote.getJSONArray("noteIds");

					if (null != noteIds) {
						Set<DailynoteEntity> dailynotes = new HashSet<>();
						for (int i = 0; i < noteIds.length(); i++) {
							DailynoteEntity e = new DailynoteEntity();
							e.setNoteId(noteIds.getInt(i));
							dailynotes.add(e);
						}
						templModel.setDailynotes(dailynotes);
					}
					templModel.setOtherNote(dailynote.getString("otherNote"));
				}

				// visitPoints
				JSONObject visitPoints = json.getJSONObject("visitPoints");
				if (null != visitPoints) {
					templModel.setCycleUnit(visitPoints.getInt("cycleUnit"));

					JSONArray phaseLists = visitPoints.getJSONArray("phases");

					if ((null != phaseLists) && (phaseLists.length() > 0)) {
						List<TemplatePhaseEntity> phases = new ArrayList<TemplatePhaseEntity>();
						for (int i = 0; i < phaseLists.length(); i++) {
							JSONObject phase = phaseLists.getJSONObject(i);
							TemplatePhaseEntity timePoint = new TemplatePhaseEntity();
							timePoint.setTimePoint(phase.getInt("timePoint"));
							// v103版本增加，兼容1.0版本，没有提交此项表示为1。
							if (phase.has("selected")) {
								timePoint.setSelected(phase.getInt("selected"));
							} else {
								timePoint.setSelected(1);
							}
							JSONArray ids = phase.getJSONArray("itemIds");
							if ((timePoint.getSelected() == 1)) {
								if ((null != ids) && (ids.length() > 0)) {
									Set<VisitItemEntity> items = new HashSet<>();
									for (int j = 0; j < ids.length(); j++) {
										VisitItemEntity e = new VisitItemEntity();
										e.setItemId(ids.getInt(j));
										items.add(e);
									}
									timePoint.setItems(items);
								}
								/*
								 * else { } throw new ParseException(
								 * ConstantDoctorVisitErrs.VISIT_ITEM_REQUIRED);
								 * }
								 */
								/*
								 * if (phase.has("selected")) {
								 * if(phase.has("isFuzhenItem")){
								 * if((phase.getInt("selected")!=1)){
								 * 
								 * } }else{ timePoint.setIsFuzhenItem(1); } }
								 * else { timePoint.setIsFuzhenItem(1); }
								 */
							}
							if (phase.has("isFuzhenItem")) {
								timePoint.setIsFuzhenItem(phase
										.getInt("isFuzhenItem"));
							}
							// 2015-9-2 有全科的如果selected=1，isFuzhenItem参数值必须是1
							timePoint.setIsFuzhenItem(1);// 如果selected=1，isFuzhenItem参数值必须是1

							phases.add(timePoint);
						}
						templModel.setPhases(phases);
					} else {
						throw new ParseException(
								ConstantDoctorVisitErrs.PHASE_REQUIRED);
					}

				}

			} catch (JSONException e) {
				throw new ParseException(
						ConstantDoctorVisitErrs.TEMPLATE_CONTENT_INCORRECT);
			}
		}
		return templModel;
	}

	public static String compile(TemplateEntity templModel)
			throws ParseException {
		JSONObject json = new JSONObject();

		try {

			// 1、体征项 bodySigns
			JSONArray bss = new JSONArray();

			if (null != templModel.getBodySigns()) {
				List<BodySignEntity> bodySigns = templModel.getBodySigns();
				for (int i = 0, j = bodySigns.size(); i < j; i++) {
					BodySignEntity bodySign = bodySigns.get(i);
					JSONObject tmp = new JSONObject();
					tmp.put("itemId", bodySign.getItemId());
					tmp.put("freq", bodySign.getFreq());
					bss.put(tmp);
				}
			}

			// 2、日常注意事项 dailynote
			JSONObject dailynote = new JSONObject();

			if (null != templModel.getDailynotes()) {
				JSONArray noteIds = new JSONArray();
				Set<DailynoteEntity> ids = templModel.getDailynotes();
				for (DailynoteEntity e : ids) {
					noteIds.put(e.getNoteId());
				}
				dailynote.put("noteIds", noteIds);
			}
			if (null != templModel.getOtherNote()) {
				dailynote.put("otherNote", templModel.getOtherNote());
			}else{
				dailynote.put("otherNote", "");
			}

			// 3 随访阶段 visitPoints
			JSONObject visitPoints = new JSONObject();
			if (null != templModel.getPhases()) {
				visitPoints.put("cycleUnit", templModel.getCycleUnit());

				JSONArray phaseLists = new JSONArray(); // visitPoints.getJSONArray("phases");

				List<TemplatePhaseEntity> phases = templModel.getPhases();

				for (int i = 0; i < phases.size(); i++) {
					JSONObject phaseJo = new JSONObject();
					TemplatePhaseEntity phase = phases.get(i);
					phaseJo.put("phaseId", phase.getTemplPhaseId());
					phaseJo.put("timePoint", phase.getTimePoint());
					phaseJo.put("itemIds", phase.getItemIds());
					phaseJo.put("editable", phase.getEditable());
					phaseJo.put("selected", phase.getSelected());// 该随访点是否选中

					phaseLists.put(phaseJo);
				}
				visitPoints.put("phases", phaseLists);
			}
			JSONObject templNode = new JSONObject();
			TemplateEntity templ = templModel;
			templNode.put("templId", templ.getTemplId());
			templNode.put("templName", templ.getTemplName());
			templNode.put("diseaseId", templ.getDisease());
			TemplateEntity stdTempl = null;
			if (templ.getDoctorId() > 0) {
				stdTempl = templ.getStdTemplate();
			}
			templNode.put("stdTemplId",
					null == stdTempl ? 0 : stdTempl.getTemplId());

			json = new JSONObject();
			json.put("bodySigns", bss);
			json.put("dailynote", dailynote);
			json.put("visitPoints", visitPoints);
			json.put("templ", templNode);
		} catch (JSONException e) {
			throw new ParseException();
		} catch (Exception e) {
			throw new ParseException();
		}

		return json.toString();
	}
}
