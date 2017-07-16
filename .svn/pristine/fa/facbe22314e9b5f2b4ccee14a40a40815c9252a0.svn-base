package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.entity.visit.AppCurPhaseComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.paser.AppItemParser;
import com.sinohealth.eszservice.service.visit.paser.PrescriptionParser;

/**
 * 医生随访时间轴
 * 
 * @author 黄世莲
 * 
 */
public class TimelineDto extends BaseDto {
	private static final long serialVersionUID = -5914085148909017623L;

	private final static String dateFormat = "yyyy-MM-dd";

	private ApplicationEntity application;

	private TemplateEntity template;

	private List<TemplatePhaseEntity> phases;

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public TemplateEntity getTemplate() {
		return template;
	}

	public void setTemplate(TemplateEntity template) {
		this.template = template;
	}

	@Override
	public String toString() {

		ObjectMapper mapper = new ObjectMapper();

		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("errCode", errCode);
		if (null != errMsg && (!"".equals(errMsg))) {
			rootNode.put("errMsg", errMsg);
		}
		ObjectNode timeLineNode = rootNode.putObject("timeLine");
		ObjectNode templNode = timeLineNode.putObject("templ");
		ObjectNode applyNode = timeLineNode.putObject("apply");
		ArrayNode phasesNode = timeLineNode.putArray("phases");
		ArrayNode dailynotesNode = timeLineNode.putArray("dailynotes");

		if (null != application) {

			// 申请信息节点
			AppCurPhaseComponent curPhase = application.getCurPhase();
			if (null != curPhase) {
				applyNode.put(
						"curPhaseId",
						null != curPhase.getCurPhaseId() ? curPhase
								.getCurPhaseId() : 0);
				applyNode.put("curTimePoint", null != curPhase
						.getCurTimePoint() ? curPhase.getCurTimePoint() : 0);
				Date curVisitTime = curPhase.getCurVisitTime();
				applyNode.put(
						"curVisitDate",
						(null != curVisitTime) ? DateUtils.formatDate(
								curVisitTime, dateFormat) : "");
				applyNode.put("fuZhenStatus", curPhase.getFuZhenStatus());
			}
			applyNode.put(
					"visitStartDate",
					null != application.getVisitStartDate() ? DateUtils
							.formatDate(application.getVisitStartDate(),
									dateFormat) : "");

			Date finishTime = application.getFinishTime();
			applyNode.put(
					"finishTime",
					null != finishTime ? DateUtils.formatDate(finishTime,
							dateFormat) : "");
			// 预计完成日期
			Date expFinishTime = application.getExpectedFinishTime();
			applyNode.put(
					"expFinishTime",
					null != expFinishTime ? DateUtils.formatDate(expFinishTime,
							dateFormat) : "");

			applyNode.put(
					"reportStatus",
					null != curPhase.getReportStatus() ? curPhase
							.getReportStatus() : 0);

			applyNode.put(
					"reasonDesc",
					null != application.getReasonDesc() ? application
							.getReasonDesc() : "");

			applyNode.put("visitStatus", application.getVisitStatus());
			applyNode.put("diseaseId", application.getDisease().getId());
		}

		// 模板信息
		if (null != template) {
			templNode.put("templId", template.getTemplId());
			templNode.put("templName",
					null != template.getTemplName() ? template.getTemplName()
							: "");
			templNode.put("cycleUnit", template.getCycleUnit());
			templNode.put("cycleLength", template.getCycleLength());
			// 本计划的所有阶段
			if (null != phases) {
				for (int i = 0; i < phases.size(); i++) {
					ObjectNode phaseNode = phasesNode.addObject();
					TemplatePhaseEntity phase = phases.get(i);
					phaseNode.put("phaseId", phase.getTemplPhaseId());
					phaseNode.put("timePoint", phase.getTimePoint());
					phaseNode.put(
							"visitDate",
							null != phase.getVisitTime() ? DateUtils
									.formatDate(phase.getVisitTime(),
											dateFormat) : "");
					phaseNode.put("fuZhenStatus", phase.getFuZhenStatus());
					phaseNode.put("reportStatus", phase.getReportStatus());
					phaseNode.put("editable", phase.getEditable());
					phaseNode.put("commentable", phase.getCommentable());
					phaseNode.put("submittedRate", phase.getSubmittedRate());
					phaseNode.put("isFuzhenItem", phase.getIsFuzhenItem());
					phaseNode.put("isFuzhenValue", phase.getIsFuzhenValue());

					// 上传的处方
					ObjectNode medicinesNode = phaseNode.putObject("medicines");
					Set<VisitPrescriptionEntity> medicines = new HashSet<>();
					if (null != phase.getPrescription().getPics()) {
						medicines = new HashSet<>(phase.getPrescription()
								.getPics());
					}
					if (null != medicines && medicines.size() > 0) {
						// 处方
						ObjectNode medicineStr = PrescriptionParser
								.compileToJsonNode(medicines);
						medicinesNode.putAll(medicineStr);
					}

					// 上传的检验类
					ArrayNode checkItemsNode = phaseNode.putArray("checkItems");
					List<CheckItemValueEntity> values = new ArrayList<>(phase
							.getInspection().getValues());
					// 2015-09-23:检查项的结果值也在这里返回
					if (null != phase.getCheckValues()) {
						values.addAll(phase.getCheckValues());
					}
					if (null != values) {
						Set<VisitItemEntity> items = phase.getItems();
						if (null == items) {
							items = new HashSet<>();
						}
						for (VisitItemEntity item : items) { // 遍历全部此阶段选中的项目
							if (item.getType() != VisitItemEntity.TYPE_NUMBER) {
								continue;
							}

							CheckItemValueEntity curValObj = new CheckItemValueEntity();
							curValObj.setPhase(phase);
							curValObj.setVisitItem(item);
							int idx = values.indexOf(curValObj); // 找到此项结果值的位置
							ObjectNode itemNode = checkItemsNode.addObject();
							itemNode.put("itemId", item.getItemId());
							// 是否有值
							if (-1 == idx) { // 找不到，还没有值
								itemNode.put("value", "");
								itemNode.put("warnLevel",
										VisitStatus.DATA_NOMAL);
							} else { // 如果是找到值的
								CheckItemValueEntity val = values.get(idx);
								itemNode.put("value", val.getReportValue());
								itemNode.put("warnLevel",
										val.getReportWarnLevel());
							}
						}
					}

					// 上传的检查类，全部是图片
					ArrayNode checkPicsNode = phaseNode.putArray("checkPics");
					Set<VisitImgEntity> checkPics = new HashSet<>();
					checkPics.addAll(phase.getInspection().getPics());
					checkPics.addAll(phase.getChecks());
					if (null != checkPics) {

						// 检查项
						ArrayNode node = AppItemParser
								.compileToJsonNode(checkPics);

						checkPicsNode.addAll(node);
					}
				}
			}

			// 日常注意事项
			Set<DailynoteEntity> dailynotes = template.getDailynotes();
			for (DailynoteEntity dailynote : dailynotes) {
				dailynotesNode.addPOJO(dailynote);
			}
		}

		try {
			return mapper.writeValueAsString(rootNode);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}

	}

	public List<TemplatePhaseEntity> getPhases() {
		return phases;
	}

	public void setPhases(List<TemplatePhaseEntity> phases) {
		this.phases = phases;
	}

}
