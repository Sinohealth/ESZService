package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.VisitItemId;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.personal.FractureHistory;
import com.sinohealth.eszorm.entity.visit.personal.PregnancyHistory;
import com.sinohealth.eszorm.entity.visit.personal.SickbedHistory;
import com.sinohealth.eszorm.entity.visit.personal.SmokingHistory;
import com.sinohealth.eszorm.entity.visit.personal.ThrombosisHistory;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

/**
 * 门诊或住院记录数据包解析
 * 
 * @author 黄世莲
 * 
 */
public class PersonalHistoryParser {

	static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.getSerializerProvider().setNullValueSerializer(
				new NullSerializer());
	}

	public static Set<CheckItemValueEntity> PersonalParse(String data)
			throws JsonParseException, JsonMappingException, IOException,
			SystemErrorExecption, ParseException {

		Set<CheckItemValueEntity> itemValues = new HashSet<CheckItemValueEntity>();
		CheckItemValueEntity itemValue = new CheckItemValueEntity();
		ObjectNode objectNode = mapper.readValue(data, ObjectNode.class);

		JsonNode smokeNode = objectNode.get("smoke");

		if (null != smokeNode) {
			// 获取吸烟史对象
			SmokingHistory smoke = smokingParse(smokeNode.toString());
			if (null != smoke) {
				if (1 == smoke.getValue()) {
					Assert.isTrue(!(0 == smoke.getYear()), "吸烟年份不能为空");
					Assert.isTrue(!(0 == smoke.getPerDay()), "每天吸烟量不能为空");
				}
				if (1 == smoke.getStopped()) {
					Assert.isTrue(!(0 == smoke.getStoppedYear()), "戒烟年份不能为空");
				}
			}

			smoke.validate();
			if (smoke.getValue() == 1) {
				// ObjectMapper mapper = new ObjectMapper();
				String str = "";
				try {
					str = mapper.writeValueAsString(smoke);
					System.out.println("smoke: " + str);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				VisitItemEntity item = new VisitItemEntity();
				item.setItemId(smoke.getItemId());
				item.setEnName(smoke.getEnName());
				item.setZhName(smoke.getZhName());
				itemValue.setVisitItem(item);
				itemValue.setReportValue(str);
				itemValue.setCat(VisitItemCat.CAT_PERSONAL_HIS);
				itemValues.add(itemValue);
			}
		}
		// 获取血栓史对象
		JsonNode thrombosisNode = objectNode.get("thrombosis");
		if (null != thrombosisNode) {
			ThrombosisHistory thrombosis = thrombosisParse(thrombosisNode
					.toString());
			if (null != thrombosis) {
				if (1 == thrombosis.getValue()) {
					Assert.isTrue(!"".equals(thrombosis.getParts()),
							"血栓史部位(parts)不能为空");
					Assert.isTrue(null != thrombosis.getComfirmDate(),
							"血栓史诊断日期不能为空");
				}
			}

			thrombosis.validate();
			// System.out.println("thrombosis text: " + thrombosis.getText());
			if (thrombosis.getValue() == 1) {
				String str = "";
				try {
					str = mapper.writeValueAsString(thrombosis);
					System.out.println("Thrombosis: " + str);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				itemValue = new CheckItemValueEntity();
				VisitItemEntity item = new VisitItemEntity();
				item.setItemId(thrombosis.getItemId());
				item.setEnName(thrombosis.getEnName());
				item.setZhName(thrombosis.getZhName());
				itemValue.setVisitItem(item);
				itemValue.setReportValue(str);
				itemValue.setCat(VisitItemCat.CAT_PERSONAL_HIS);
				itemValues.add(itemValue);
			}
		}
		// 获取骨折史对象
		JsonNode fractureNode = objectNode.get("fracture");
		if (null != fractureNode) {
			FractureHistory fracture = fractureParse(fractureNode.toString());
			if (null != fracture) {
				if (1 == fracture.getValue()) {
					Assert.isTrue(!"".equals(fracture.getParts()),
							"骨折史具体部位(parts)不能为空");
					Assert.isTrue(!"".equals(fracture.getBody()),
							"骨折史部位(body)不能为空");
					Assert.isTrue(null != fracture.getComfirmDate(),
							"骨折史诊断日期不能为空");
				}

			}
			fracture.validate();
			// System.out.println("fracture text: " + fracture.getText());
			if (fracture.getValue() == 1) {
				String str = "";
				try {
					str = mapper.writeValueAsString(fracture);
					System.out.println("fracture: " + str);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				itemValue = new CheckItemValueEntity();
				VisitItemEntity item = new VisitItemEntity();
				item.setItemId(fracture.getItemId());
				item.setEnName(fracture.getEnName());
				item.setZhName(fracture.getZhName());
				itemValue.setVisitItem(item);
				itemValue.setReportValue(str);
				itemValue.setCat(VisitItemCat.CAT_PERSONAL_HIS);
				itemValues.add(itemValue);
			}
		}
		// 获对卧床史对象
		JsonNode sickbedNode = objectNode.get("sickbed");
		if (null != sickbedNode) {
			SickbedHistory sickbed = SickbedParse(sickbedNode.toString());
			if (null != sickbed) {
				if (1 == sickbed.getValue()) {
					Assert.isTrue(null != sickbed.getStartDate(), "卧床史开始日期不能为空");
					// Assert.isTrue(null != sickbed.getEndDate(),
					// "卧床史结束日期不能为空");
					boolean flag = validateDate(sickbed.getStartDate(),
							sickbed.getEndDate());
					if (!flag) {
						throw new SystemErrorExecption("卧床史开始日期不能大于结束日期",
								BaseDto.ERRCODE_OTHERS);
					}
				}
			}

			sickbed.validate();
			// System.out.println("sickbed text: " + sickbed.getText());
			if (sickbed.getValue() == 1) {
				String str = "";
				try {
					str = mapper.writeValueAsString(sickbed);
					System.out.println("sickbed: " + str);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				itemValue = new CheckItemValueEntity();
				VisitItemEntity item = new VisitItemEntity();
				item.setItemId(sickbed.getItemId());
				item.setEnName(sickbed.getEnName());
				item.setZhName(sickbed.getZhName());
				itemValue.setVisitItem(item);
				itemValue.setReportValue(str);
				itemValue.setCat(VisitItemCat.CAT_PERSONAL_HIS);
				itemValues.add(itemValue);
			}
		}
		// 获取怀孕史对象
		JsonNode pregnancyNode = objectNode.get("pregnancy");
		if (null != pregnancyNode) {
			PregnancyHistory pregnancy = pregnantParse(pregnancyNode.toString());

			if (null != pregnancy) {
				if (1 == pregnancy.getValue()) {
					Assert.isTrue(null != pregnancy.getStartDate(),
							"怀孕史开始日期不能为空");
					Assert.isTrue(null != pregnancy.getEndDate(), "怀孕史结束日期不能为空");
					boolean flag = validateDate(pregnancy.getStartDate(),
							pregnancy.getEndDate());
					if (!flag) {
						throw new SystemErrorExecption("怀孕史开始日期不能大于结束日期",
								BaseDto.ERRCODE_OTHERS);
					}
				}
			}

			pregnancy.validate();
			// System.out.println("pregnancy text: " + pregnancy.getText());
			if (pregnancy.getValue() == 1) {
				String str = "";
				try {
					str = mapper.writeValueAsString(pregnancy);
					System.out.println("pregnancy: " + str);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				itemValue = new CheckItemValueEntity();
				VisitItemEntity item = new VisitItemEntity();
				item.setItemId(pregnancy.getItemId());
				item.setEnName(pregnancy.getEnName());
				item.setZhName(pregnancy.getZhName());
				itemValue.setVisitItem(item);
				itemValue.setReportValue(str);
				itemValue.setCat(VisitItemCat.CAT_PERSONAL_HIS);
				itemValues.add(itemValue);
			}
		}
		return itemValues;
	}

	public static SmokingHistory smokingParse(String data) throws IOException,
			SystemErrorExecption {
		if (null == data || "".equals(data)) {
			return null;
		}
		try {
			SmokingHistory smokingHistory = mapper.readValue(data,
					SmokingHistory.class);
			return smokingHistory;
		} catch (IOException e) {
			throw new SystemErrorExecption("吸烟史解释失败", BaseDto.ERRCODE_OTHERS);
		}
	}

	public static String smokingCompile(SmokingHistory smokingHistory) {
		if (null != smokingHistory) {
			try {
				return mapper.writeValueAsString(smokingHistory);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "{}";
	}

	public static ThrombosisHistory thrombosisParse(String data)
			throws SystemErrorExecption {
		if (null == data || "".equals(data)) {
			return null;
		}
		try {
			ThrombosisHistory thrombosisHistory = mapper.readValue(data,
					ThrombosisHistory.class);
			return thrombosisHistory;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemErrorExecption("血栓史解释失败", BaseDto.ERRCODE_OTHERS);
		}
	}

	public static String thrombosisCompile(ThrombosisHistory thrombosisHistory) {
		if (null != thrombosisHistory) {
			try {
				return mapper.writeValueAsString(thrombosisHistory);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return "{}";
	}

	public static FractureHistory fractureParse(String data)
			throws SystemErrorExecption {
		if (null == data || "".equals(data)) {
			return null;
		}
		try {
			FractureHistory fractureHistory = mapper.readValue(data,
					FractureHistory.class);
			return fractureHistory;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemErrorExecption("骨折史解释失败", BaseDto.ERRCODE_OTHERS);
		}
	}

	public static String fractureCompile(FractureHistory fractureHistory) {
		if (null != fractureHistory) {
			try {
				return mapper.writeValueAsString(fractureHistory);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return "{}";
	}

	public static SickbedHistory SickbedParse(String data)
			throws SystemErrorExecption {

		if (null == data || "".equals(data)) {
			return null;
		}
		try {
			SickbedHistory sickbedHistory = mapper.readValue(data,
					SickbedHistory.class);
			return sickbedHistory;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemErrorExecption("卧床史解释失败", BaseDto.ERRCODE_OTHERS);
		}
	}

	public static String sickbedCompile(SickbedHistory sickbedHistory) {
		if (null != sickbedHistory) {
			try {
				return mapper.writeValueAsString(sickbedHistory);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return "{}";
	}

	public static PregnancyHistory pregnantParse(String data)
			throws SystemErrorExecption {

		if (null == data || "".equals(data)) {
			return null;
		}
		try {
			PregnancyHistory pregnantHistory = mapper.readValue(data,
					PregnancyHistory.class);
			return pregnantHistory;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemErrorExecption("怀孕史解释失败", BaseDto.ERRCODE_OTHERS);
		}
	}

	public static String pregnantCompile(PregnancyHistory pregnantHistory) {
		if (null != pregnantHistory) {
			try {
				return mapper.writeValueAsString(pregnantHistory);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return "{}";
	}

	public static boolean validateDate(Date startDate, Date endDate) {
		boolean flag = true;
		if (null != startDate && null != endDate) {
			flag = startDate.compareTo(endDate) >= 0 ? false : true;
		}
		return flag;
	}

	public static void main(String[] args) throws JsonProcessingException {
		PregnancyHistory o = new PregnancyHistory();

		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializerProvider().setNullValueSerializer(
				new NullSerializer());

		String s = mapper.writeValueAsString(o);

		System.out.println(s);
	}

	private static class NullSerializer extends JsonSerializer<Object> {
		public void serialize(Object value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeString("");
		}
	}

	public static String compile(HealthArchiveLog healthArchiveLog, String date) {
		if (null == healthArchiveLog) {
			return "{}";
		}
		ObjectNode rootNode = mapper.createObjectNode();
		ObjectNode dataNode = mapper.createObjectNode();

		switch (healthArchiveLog.getItemId()) {
		case VisitItemId.SMOKING_HISTORY:
			dataNode.put("smoke", healthArchiveLog.getValue());
			break;
		case VisitItemId.THROMBOSIS_HISTORY:
			dataNode.put("thrombosis", healthArchiveLog.getValue());
			break;
		case VisitItemId.FRACTURE_HISTORY:
			dataNode.put("fracture", healthArchiveLog.getValue());
			break;
		case VisitItemId.SICKBED_HISTORY:
			dataNode.put("sickbed", healthArchiveLog.getValue());
			break;

		case VisitItemId.PREGNANCY_HISTORY:
			dataNode.put("pregnancy", healthArchiveLog.getValue());
			break;
		}

		rootNode.put("updateTime", date);
		rootNode.put("data", dataNode);

		try {
			// System.err.println("parse json:"+mapper.writeValueAsString(rootNode));
			return mapper.writeValueAsString(rootNode);
		} catch (IOException e) {
			return "{}";
		}
	}

}
