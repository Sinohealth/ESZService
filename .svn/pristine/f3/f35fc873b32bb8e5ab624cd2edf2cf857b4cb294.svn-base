package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.SystemException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.VisitItemId;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

/**
 * 门诊或住院记录数据包解析
 * 
 * @author 黄世莲
 * 
 */
public class CasesParser {

	/**
	 * 
	 * 
	 * @param data
	 * @return
	 * @throws ParseException
	 * @throws SystemErrorExecption
	 * @throws SystemException
	 */
	public static AppCasesComponent parse(String data) throws ParseException,
			SystemErrorExecption {
		if (null == data || "".equals(data)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		AppCasesComponent appCasesEntity = new AppCasesComponent();
		ObjectNode rootNode;
		try {
			rootNode = mapper.readValue(data, ObjectNode.class);
		} catch (IOException e) {
			throw new ParseException();
		}
		// 出院日期
		Date leaveHosDate = null;
		if (null != rootNode.get("leaveHosDate")) {
			String leaveHosDateStr = rootNode.get("leaveHosDate").textValue();
			if (!"".equals(leaveHosDateStr)) {
				leaveHosDate = DateUtils.parseDate(leaveHosDateStr);
				if (null == leaveHosDate) {
					throw new SystemErrorExecption("leaveHosDate数据格式有误",
							BaseDto.ERRCODE_OTHERS);
				}
			}
			appCasesEntity.setLeaveHosDate(leaveHosDate);
		}

		// 末次门诊日期
		Date casesDate = null;
		if (null != rootNode.get("casesDate")) {
			String casesDateStr = rootNode.get("casesDate").textValue();
			if (!"".equals(casesDateStr)) {
				casesDate = DateUtils.parseDate(casesDateStr);
				if (null == casesDate) {
					throw new SystemErrorExecption("casesDate数据格式有误",
							BaseDto.ERRCODE_OTHERS);
				}
			}

			appCasesEntity.setCasesDate(casesDate);
		}

		// 出院诊断证明图URL
		JsonNode leaveHosPicsNode = rootNode.get("leaveHosPics");
		if (null != leaveHosPicsNode && leaveHosPicsNode.isArray()) {
			List<VisitImgEntity> leaveHosPics = new ArrayList<VisitImgEntity>();
			for (JsonNode jsonNode : leaveHosPicsNode) {
				VisitImgEntity e;
				try {
					e = mapper.treeToValue(jsonNode, VisitImgEntity.class);
					e.setItemId(VisitItemId.LEAVE_HOS_ID);
					e.setCat(VisitItemCat.CAT_APPLY);
					leaveHosPics.add(e);
				} catch (IOException e1) {
					throw new ParseException("解析leaveHosPics参数失败");
				}
			}
			appCasesEntity.getApplyImgList().addAll(leaveHosPics);
		}

		// 末次门诊图URL
		JsonNode casesPicsNode = rootNode.get("casesPics");
		if (null != casesPicsNode && casesPicsNode.isArray()) {
			ArrayList<VisitImgEntity> casesPics = new ArrayList<VisitImgEntity>();
			for (JsonNode jsonNode : casesPicsNode) {
				VisitImgEntity e;
				try {
					e = mapper.treeToValue(jsonNode, VisitImgEntity.class);
					e.setItemId(VisitItemId.CASE_RECORD_ID);
					e.setCat(VisitItemCat.CAT_APPLY);
					casesPics.add(e);
				} catch (IOException e1) {
					throw new ParseException("解析casesPics参数失败");
				}
			}
			appCasesEntity.getApplyImgList().addAll(casesPics);
		}

		// 确诊日期
		JsonNode confirmDateNode = rootNode.get("confirmDate");
		if (null != confirmDateNode) {
			String confirmDateStr = confirmDateNode.textValue();
			if ((null != confirmDateStr) && (!"".equals(confirmDateStr))) {
				Date confirmDate = DateUtils.parseDate(confirmDateStr);
				if (null == confirmDate) {
					throw new ParseException("解析confirmDate参数失败");
				}
				appCasesEntity.setConfirmDate(confirmDate);
			}
		}

		return appCasesEntity;
	}

	public static String compile(AppCasesComponent appCases) {
		if (null == appCases) {
			return "[]";
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		// 出院
		rootNode.put("leaveHosDate", (null == appCases.getLeaveHosDate() ? ""
				: DateUtils.formatDate(appCases.getLeaveHosDate())));
		// 出院图
		ArrayNode leaveHosPicsNode = rootNode.putArray("leaveHosPics");
		if (null != appCases.getLeaveHosPics()) {
			List<VisitImgEntity> leaveHosPics = appCases.getLeaveHosPics();
			for (int j = 0; j < leaveHosPics.size(); j++) {
				leaveHosPicsNode.addPOJO(leaveHosPics.get(j));
			}
		}

		// 末次门诊
		rootNode.put("casesDate", (null == appCases.getCasesDate() ? ""
				: DateUtils.formatDate(appCases.getCasesDate())));
		// 末次门诊图
		ArrayNode casesPicsNode = rootNode.putArray("casesPics");
		if (null != appCases.getCasesPics()) {
			List<VisitImgEntity> casesPics = appCases.getCasesPics();
			for (int j = 0; j < casesPics.size(); j++) {
				casesPicsNode.addPOJO(casesPics.get(j));
			}
		}

		// TODO
		try {
			return mapper.writeValueAsString(rootNode);
		} catch (IOException e) {
			e.printStackTrace();
			return "[]";
		}
	}
}
