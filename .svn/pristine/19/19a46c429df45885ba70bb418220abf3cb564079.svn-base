package com.sinohealth.eszservice.dto.visit.doctor;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class AppDetailsBody implements Serializable {

	private static final long serialVersionUID = -3888748268369264867L;

	@JsonSerialize(using = AppDetailsSerializer.class)
	private ApplicationEntity apply;

	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
			"applyId", "familyHistory", "fuZhenStatus", "reportStatus",
			"curPhaseId", "curTimePoint", "cycleUnit", "curVisitTime",
			"checks", "personalHistory", "inspection", "visitStatus",
			"lastUpdateTime", "finishTime", "reasonDesc", "newApplyId",
			"fuZhenRate", "caseHistoryRate", "cases", "pastHistory",
			"medicines", "prescription", "curPhase", "rateCount" })
	private List<ApplicationEntity> relations;

	// @JsonIgnoreProperties
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler",
			"mobile", "account", "email", "provinceId", "cityId",
			"lastLoginDate", "registerDate", "totalGrade" })
	public SickEntity getProfile() {
		SickEntity sick = apply.getSick();
		if (null == sick) {
			return null;
		}
		String url = sick.getHeadShot();
		if (!StringUtils.isEmpty(url)) {
			url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
			sick.setHeadShot(url);
		}
		String thumb = sick.getSmallHeadshot();
		if (!StringUtils.isEmpty(thumb)) {
			thumb = QiniuService.getDownloadUrl(Space.PERSONAL, thumb);
			sick.setSmallHeadshot(thumb);
		}
		return sick;
	}

	public ApplicationEntity getApply() {
		return apply;
	}

	public void setApply(ApplicationEntity apply) {
		this.apply = apply;
	}

	public List<ApplicationEntity> getRelations() {
		return relations;
	}

	public void setRelations(List<ApplicationEntity> relations) {
		this.relations = relations;
	}

	/**
	 * 序列化ApplicationEntity，确保只在返回时，只执行一次
	 * 
	 * 
	 * @author 黄世莲
	 * 
	 */
	public static class AppDetailsSerializer extends
			JsonSerializer<ApplicationEntity> {

		@Override
		public void serialize(ApplicationEntity apply, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			apply.getAppCases().parseImgList(); // 将图片列表切分
			for (VisitImgEntity ve : apply.getAppCases().getLeaveHosPics()) {
				// 图像
				String img = ve.getImg();
				if (StringUtils.hasText(img)) {
					// 图片增加token的处理;
					img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
					ve.setImg(img);
				}

				// 缩略图
				String thumb = ve.getThumb();
				if (StringUtils.hasText(thumb)) {
					thumb = QiniuService.getDownloadUrlStr(Space.RECORD, thumb);
					ve.setThumb(thumb);
				}
			}
			for (VisitImgEntity ve : apply.getAppCases().getCasesPics()) {
				// 图像
				String img = ve.getImg();
				if (StringUtils.hasText(img)) {
					// 图片增加token的处理;
					img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
					ve.setImg(img);
				}

				// 缩略图
				String thumb = ve.getThumb();
				if (StringUtils.hasText(thumb)) {
					thumb = QiniuService.getDownloadUrlStr(Space.RECORD, thumb);
					ve.setThumb(thumb);
				}
			}

			// ===========
			// 检验项
			// ===========
			for (VisitImgEntity ve : apply.getInspection().getPics()) {
				// 图像
				String img = ve.getImg();
				if (StringUtils.hasText(img)) {
					// 图片增加token的处理;
					img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
					ve.setImg(img);
				}

				// 缩略图
				String thumb = ve.getThumb();
				if (StringUtils.hasText(thumb)) {
					thumb = QiniuService.getDownloadUrlStr(Space.RECORD, thumb);
					ve.setThumb(thumb);
				}
			}

			// ===========
			// 检查项
			// ===========
			for (VisitImgEntity ve : apply.getChecks()) {
				// 图像
				String img = ve.getImg();
				if (StringUtils.hasText(img)) {
					// 图片增加token的处理;
					img = QiniuService.getDownloadUrlStr(Space.RECORD, img);
					ve.setImg(img);
				}

				// 缩略图
				String thumb = ve.getThumb();
				if (StringUtils.hasText(thumb)) {
					thumb = QiniuService.getDownloadUrlStr(Space.RECORD, thumb);
					ve.setThumb(thumb);
				}
			}

			// ===========
			// 处方
			// ===========
			for (VisitPrescriptionEntity ve : apply.getPrescription().getPics()) {
				// 图像
				String img = ve.getMedPic();
				if (StringUtils.hasText(img)) {
					// 图片增加token的处理;
					img = QiniuService.getDownloadUrlStr(Space.PRESCRIPTION,
							img);
					ve.setMedPic(img);
				}

				// 缩略图
				String thumb = ve.getSmallMedPic();
				if (StringUtils.hasText(thumb)) {
					thumb = QiniuService.getDownloadUrlStr(Space.PRESCRIPTION,
							thumb);
					ve.setSmallMedPic(thumb);
				}
			}

			jgen.writeObject(apply);
		}

	}

}
