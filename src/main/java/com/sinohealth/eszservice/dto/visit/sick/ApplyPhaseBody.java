package com.sinohealth.eszservice.dto.visit.sick;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class ApplyPhaseBody implements Serializable {

	private static final long serialVersionUID = 6382639225641953769L;

	@JsonIgnoreProperties(value = { "itemIds" })
	@JsonUnwrapped
	@JsonSerialize(using = PhaseSerializer.class)
	private TemplatePhaseEntity phase;

	public TemplatePhaseEntity getPhase() {
		return phase;
	}

	public void setPhase(TemplatePhaseEntity phase) {
		this.phase = phase;
	}

	public PhaseTempl getTempl() {
		PhaseTempl templ = new PhaseTempl();

		if ((null != phase) && (null != phase.getItems())) {
			for (VisitItemEntity e : phase.getItems()) {
				if (e.getCat() == VisitItemCat.CAT_INSPECTION) { // 检验类
					switch (e.getType()) {
					case VisitItemEntity.TYPE_NUMBER: // 数值类型
						templ.getInspNumItems().add(e.getItemId());
						break;
					case VisitItemEntity.TYPE_IMAGE: // 图片类型
						templ.getInspPicItems().add(e.getItemId());
						break;
					}
				} else if (e.getCat() == VisitItemCat.CAT_EXAMINE) { // 检查类
					switch (e.getType()) {
					case VisitItemEntity.TYPE_NUMBER: // 数值类型
						templ.getChecksNumItems().add(e.getItemId());
						break;
					case VisitItemEntity.TYPE_IMAGE: // 图片类型
						templ.getChecksItems().add(e.getItemId());
						break;
					}
				}
			}
		}

		return templ;
	}

	public static class PhaseSerializer extends
			JsonSerializer<TemplatePhaseEntity> {

		private static ObjectMapper mapper = new ObjectMapper();

		@Override
		public void serialize(TemplatePhaseEntity value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {

			// ===========
			// 检验项
			// ===========
			for (VisitImgEntity ve : value.getInspection().getPics()) {
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
			for (VisitImgEntity ve : value.getChecks()) {
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
			for (VisitPrescriptionEntity ve : value.getPrescription().getPics()) {
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

			ObjectNode node = mapper.valueToTree(value);
			node.remove("itemIds"); // 删除掉itemIds参数

			jgen.writeTree(node);
		}
	}

	public static class PhaseTempl implements Serializable {
		private static final long serialVersionUID = -9161905717783808963L;

		private int prescription = 1;
		private List<Integer> inspNumItems;
		private List<Integer> inspPicItems;
		private List<Integer> checksItems;
		private List<Integer> checksNumItems;

		public int getPrescription() {
			return prescription;
		}

		public void setPrescription(int prescription) {
			this.prescription = prescription;
		}

		public List<Integer> getInspNumItems() {
			if (null == inspNumItems) {
				inspNumItems = new ArrayList<>();
			}
			return inspNumItems;
		}

		public void setInspNumItems(List<Integer> inspNumItems) {
			this.inspNumItems = inspNumItems;
		}

		public List<Integer> getInspPicItems() {
			if (null == inspPicItems) {
				inspPicItems = new ArrayList<>();
			}
			return inspPicItems;
		}

		public void setInspPicItems(List<Integer> inspPicItems) {
			this.inspPicItems = inspPicItems;
		}

		public List<Integer> getChecksItems() {
			if (null == checksItems) {
				checksItems = new ArrayList<>();
			}
			return checksItems;
		}

		public void setChecksItems(List<Integer> checksItems) {
			this.checksItems = checksItems;
		}

		public List<Integer> getChecksNumItems() {
			if (null == checksNumItems) {
				checksNumItems = new ArrayList<>();
			}
			return checksNumItems;
		}

		public void setChecksNumItems(List<Integer> checksNumItems) {
			this.checksNumItems = checksNumItems;
		}

	}

}
