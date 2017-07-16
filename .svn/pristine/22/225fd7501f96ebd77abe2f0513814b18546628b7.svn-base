package com.sinohealth.eszservice.dto.visit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 3.4.10 获取随访模板列表
 * 
 * @author 黄世莲
 * 
 */
public class TemplListDto extends BaseDto {

	private static final long serialVersionUID = 8745135901525632322L;

	private List<DiseaseEntity> diseases;

	@JsonSerialize(using = DiseasesSerializer.class)
	public List<DiseaseEntity> getDiseases() {
		if (null == diseases) {
			diseases = new ArrayList<>();
		}
		return diseases;
	}

	public void setDiseases(List<DiseaseEntity> diseases) {
		this.diseases = diseases;
	}

	/**
	 * 体征序列化类
	 * 
	 * @author 黄世莲
	 * 
	 */
	public static class DiseasesSerializer extends
			JsonSerializer<List<DiseaseEntity>> {

		@Override
		public void serialize(List<DiseaseEntity> values, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartArray();

			if (null != values) {
				for (DiseaseEntity val : values) {
					jgen.writeStartObject();
					jgen.writeStringField("id", val.getId());
					jgen.writeStringField("name", val.getName());
					jgen.writeStringField("subname", val.getSubname());
					jgen.writeNumberField("isSickApply", val.getIsSickApply());
					jgen.writeArrayFieldStart("templates");
					if ((null != val.getTemplates())
							&& (!val.getTemplates().isEmpty())) {
						for (TemplateEntity e : val.getTemplates()) {
							jgen.writeStartObject();
							jgen.writeNumberField("templId", e.getTemplId());
							jgen.writeStringField("templName", e.getTemplName());
							jgen.writeStringField("diseaseId", e.getDisease());
							jgen.writeNumberField("stdTemplId",
									e.getStdTemplId());
							jgen.writeNumberField("cycleUnit", e.getCycleUnit());
							jgen.writeNumberField("doctorId", e.getDoctorId());
							jgen.writeNumberField("cycleLength",
									e.getCycleLength());
							jgen.writeStringField("stdTemplName",
									e.getStdTemplName());
							jgen.writeNumberField("isRecommend",
									e.getIsRecommend());
							jgen.writeEndObject();
						}
					}
					jgen.writeEndArray();

					jgen.writeEndObject();
				}
			}
			jgen.writeEndArray();
		}
	}

}
