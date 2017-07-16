package com.sinohealth.eszservice.dto.visit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.dto.visit.elem.ApplyDetailsDtoElem;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;
import com.sinohealth.eszservice.service.visit.paser.PastHistoryParser;
import com.sinohealth.eszservice.service.visit.paser.PrescriptionParser;
import com.sinohealth.eszservice.service.visit.paser.SickProfileParser;

/**
 * 随访申请详细信息
 * 
 * @author 黄世莲
 * 
 */
public class AppDetailsDto extends BaseDto {

	private static final long serialVersionUID = 6090487644126165037L;

	@JsonIgnore
	private ApplicationEntity application;

	private ApplyDetailsDtoElem details = new ApplyDetailsDtoElem();

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 放到details里显示
	 */
	@JsonIgnore
	private List<ApplicationEntity> relations = new ArrayList<>();

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public ApplicationEntity getApplication() {
		return application;
	}

	public ApplyDetailsDtoElem getDetails() {
		if (null != application) {
			ObjectNode profile = SickProfileParser.compileToJson(application
					.getSick());
			details.setProfile(profile);

			// =======================
			// 出院、门诊记录
			// =======================
			AppCasesComponent appCases = application.getAppCases();
			appCases.parseImgList(); // 将图片分为leaveHosPics和casesPics列表
			/**
			 * 给图像增加token
			 */
			if (null != appCases.getCasesPics()) {
				for (VisitImgEntity ve : appCases.getCasesPics()) {
					// 图像
					String img = ve.getImg();
					if (null != img && !"".endsWith(img)) {
						// 图片增加token的处理;
						img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
						ve.setImg(img);
					}

					// 缩略图
					String thumb = ve.getThumb();
					if (null != thumb && !"".endsWith(thumb)) {
						thumb = QiniuService.getDownloadUrlStr(Space.RECORD,
								thumb);
						ve.setThumb(thumb);
					}
				}

			}
			/**
			 * 给图像增加token
			 */
			if (null != appCases.getLeaveHosPics()) {
				for (VisitImgEntity ve : appCases.getLeaveHosPics()) {
					// 图像
					String img = ve.getImg();
					if (null != img && !"".endsWith(img)) {
						// 图片增加token的处理;
						img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
						ve.setImg(img);
					}

					// 缩略图
					String thumb = ve.getThumb();
					if (null != thumb && !"".endsWith(thumb)) {
						thumb = QiniuService.getDownloadUrlStr(Space.RECORD,
								thumb);
						ve.setThumb(thumb);
					}
				}

			}
			details.setCases(appCases);

			// 既往史
			ObjectNode pastHistory = PastHistoryParser
					.compileToJson(application.getAppPastHistory());
			details.setPastHistory(pastHistory);

			// 家族史
			ArrayNode familyHistory;
			if ((null != application.getFamilyHistory())
					&& (!"".equals(application.getFamilyHistory()))) {
				try {
					familyHistory = (ArrayNode) mapper.readTree(application
							.getFamilyHistory());
					details.setFamilyHistory(familyHistory);
				} catch (IOException e) {
					familyHistory = mapper.createArrayNode();
					e.printStackTrace();
				}
			} else {
				familyHistory = mapper.createArrayNode();
			}
			details.setFamilyHistory(familyHistory);

			// =======================
			// 处方
			// =======================
			ObjectNode medicines = PrescriptionParser
					.compileToJsonNode(new HashSet(application
							.getPrescription().getPics()));
			details.setMedicines(medicines);

			// =======================
			// 检查项
			// =======================
			Set<VisitImgEntity> checkImgs = new HashSet<>();
			checkImgs.addAll(application.getChecks());
			checkImgs.addAll(application.getInspection().getPics());

			for (Iterator<VisitImgEntity> iterator = checkImgs.iterator(); iterator
					.hasNext();) {
				VisitImgEntity o = iterator.next();
				// 图像
				String img = o.getImg();
				if (null != img && !"".endsWith(img)) {
					// 图片增加token的处理;
					img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
				}
				// 缩略图
				String thumb = o.getThumb();
				if (null != thumb && !"".endsWith(thumb)) {
					thumb = QiniuService.getDownloadUrlStr(Space.RECORD, thumb);
				}
				o.setImg(img);
				o.setThumb(thumb);
			}
			details.setItems(checkImgs);

			String reasonDesc = application.getReasonDesc();
			if (null == reasonDesc) {
				reasonDesc = "";
			}
			details.setReasonDesc(reasonDesc);

			details.setVisitStatus(application.getVisitStatus());

			details.setApplyTime(application.getApplyTime());

			details.setFinishTime(application.getFinishTime());

			details.setSzSubjectId(application.getSzSubject().getId());

			details.setSzSubjectName(application.getSzSubject().getName());

			details.setDiseaseId(application.getDisease().getId());

			details.setDiseaseName(application.getDisease().getName());
		}

		details.setRelations(relations);

		return details;
	}

	public void setDetails(ApplyDetailsDtoElem details) {
		this.details = details;
	}

	public List<SimpleApplicationStructs> getRelations() {
		List<SimpleApplicationStructs> list = new ArrayList<>();
		for (ApplicationEntity e : relations) {
			list.add(new SimpleApplicationStructs(e));
		}
		return list;
	}

	public void setRelations(List<ApplicationEntity> relations) {
		this.relations = relations;
	}

	/**
	 * 相关联的随访信息
	 */
	// fieldVisibility = Visibility.ANY 表示全部private权限也做为属性输出
	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	public static class SimpleApplicationStructs {
		private int applyId;
		private String szSubject;
		private String szSubjectName;
		@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
		private Date applyTime;
		@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
		private Date visitStartDate;
		@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
		private Date expectedFinishTime;
		private String disease;
		private String diseaseName;

		public SimpleApplicationStructs(ApplicationEntity application) {
			applyId = application.getApplyId();
			szSubject = application.getSzSubject().getId();
			szSubjectName = application.getSzSubject().getName();
			applyTime = application.getApplyTime();
			visitStartDate = application.getVisitStartDate();
			expectedFinishTime = application.getExpectedFinishTime();
			disease = application.getDisease().getId();
			diseaseName = application.getDisease().getName();
		}
	}

}
