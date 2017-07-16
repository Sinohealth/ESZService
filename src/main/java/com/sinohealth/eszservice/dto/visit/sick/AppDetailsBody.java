package com.sinohealth.eszservice.dto.visit.sick;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class AppDetailsBody implements Serializable {

	private static final long serialVersionUID = -3888748268369264867L;

	@JsonSerialize(using = AppDetailsSerializer.class)
	private ApplicationEntity apply;

	@JsonSerialize(using = DoctorSerializer.class)
	private DoctorEntity doctor;

	public ApplicationEntity getApply() {
		return apply;
	}

	public void setApply(ApplicationEntity apply) {
		this.apply = apply;
	}

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	public static class DoctorSerializer extends JsonSerializer<DoctorEntity> {

		@Override
		public void serialize(DoctorEntity value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {

			jgen.writeStartObject();
			jgen.writeObjectField("id", null != value.getId() ? value.getId()
					: 0);
			jgen.writeObjectField("mobile",
					null != value.getMobile() ? value.getMobile() : "");
			jgen.writeObjectField("email",
					null != value.getEmail() ? value.getEmail() : "");
			jgen.writeObjectField("name",
					null != value.getName() ? value.getName() : "");
			jgen.writeObjectField("cert",
					null != value.getCert() ? value.getCert() : "");
			jgen.writeObjectField("provinceId",
					null != value.getProvince()&& null!=value.getProvince().getId()  ? value.getProvince().getId() : 0);
			jgen.writeObjectField("provinceName",
					null != value.getProvince()&& null!=value.getProvince().getProvinceName()  ? value.getProvince().getProvinceName() : "");
			jgen.writeObjectField("hospitalId",
					null != value.getHospital()&& null!=value.getHospital().getId()  ? value.getHospital().getId() : 0);
			jgen.writeObjectField("hospitalName",
					null != value.getHospital()&& null!=value.getHospital().getHospitalName()  ? value.getHospital().getHospitalName() : "");
			jgen.writeObjectField("hospitalAddr",
					null != value.getHospital()&& null!=value.getHospital().getHospitalAddr()  ? value.getHospital().getHospitalAddr() : "");
			jgen.writeObjectField("disciplineId",
					null != value.getDiscipline()&& null!=value.getDiscipline().getId() ? value.getDiscipline().getId() : 0);
			jgen.writeObjectField("disciplineName",
					null != value.getDiscipline()&& null!=value.getDiscipline().getDisciplineName() ? value.getDiscipline().getDisciplineName() : "");
			String url = (null != value.getHeadShot()) ? value.getHeadShot()
					: "";
			if (!"".equals(url)) {
				url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
			}
			jgen.writeStringField("headshot", url);
			String smallHeadshotUrl = (null != value.getSmallHeadshot()) ? value
					.getSmallHeadshot() : "";
			if (!"".equals(smallHeadshotUrl)) {
				smallHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
						smallHeadshotUrl);
			}
			jgen.writeStringField("smallHeadshot", smallHeadshotUrl);
			jgen.writeObjectField("certdStatus",
					null != value.getCertdStatus() ? value.getCertdStatus()
							: "");
			jgen.writeObjectField("recommendCode", null != value
					.getRecommendCode() ? value.getRecommendCode() : "");
			jgen.writeObjectField("qrCode",
					null != value.getQrCode() ? value.getQrCode() : "");

			jgen.writeObjectField("cat",
					null != value.getCat() ? value.getCat() : "");
			
			jgen.writeEndObject();
		}

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
