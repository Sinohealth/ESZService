package com.sinohealth.eszservice.service.visit.paser;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.personal.SmokingHistory;
import com.sinohealth.eszorm.entity.visit.personal.ThrombosisHistory;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

public class SickProfileParserTest {

	@Test
	public void testParse() throws ParseException {
		String data = "{\"name\":\"张三\",\"sex\":1,\"birthday\":\"2015-03-31\"}";
		SickEntity sick = SickProfileParser.parse(data);
		Assert.assertEquals("张三", sick.getName());
		Assert.assertEquals(1, sick.getSex().intValue());
		String birthday = DateUtils.formatDate(sick.getBirthday());
		Assert.assertEquals("2015-03-31", birthday);
	}

	@Test
	public void testCompile() {
		SickEntity sick = new SickEntity();
		sick.setName("张三");
		sick.setBirthday(new Date());
		sick.setSex(1);
		System.out.println("SickProfileParser.compile(sick):"
				+ SickProfileParser.compile(sick));
		System.out.println("SickProfileParser.compile(sick):"+SickProfileParser.compileToJson(sick));
	}
	
	@Test
	public void testPersonalCompile() throws JsonProcessingException {
		SmokingHistory smoking = new SmokingHistory();
		smoking.setPerDay(5);
		smoking.setYear(5);
		System.out.println("smokingCompile:"+PersonalHistoryParser.smokingCompile(smoking));
	}
	
	@Test
	public void testSmokeParse() {
		String data = "{\"itemId\":1701,\"value\":0,\"year\":5,\"perDay\":5,\"stopped\":1,\"stoppedYear\":2,\"zhName\":null,\"enName\":\"吸烟史\",\"text\":null}";
		try {
			SmokingHistory smoking = PersonalHistoryParser.smokingParse(data);
			System.out.println("smokingParse: "+smoking.getEnName()+" itemId:"+smoking.getItemId()+" year:"+smoking.getYear());
			System.out.println("validate: "+smoking.validate()+" text: "+smoking.getText());
		} catch (SystemErrorExecption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testThrombosisParse() throws IOException {
		String data = "{\"itemId\":1702,\"value\":0,\"parts\":\"上肢\",\"comfirmDate\":\"2015-07-27\",\"zhName\":null,\"enName\":\"血栓史\",\"text\":null}";
		try {
			ThrombosisHistory thrombosis = PersonalHistoryParser.thrombosisParse(data);
			//System.out.println("thrombosisParse: "+thrombosis.getEnName()+" itemId:"+thrombosis.getItemId());
			System.out.println("validate: "+thrombosis.validate()+" text: "+thrombosis.getText());
		} catch (SystemErrorExecption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPersonal() throws SystemErrorExecption, ParseException {
		String data = "{\"smoke\":{\"value\":0,\"itemId\":1701,\"year\":5,\"perDay\":3,\"stopped\":0,\"stoppedYear\":0,\"zhName\":\"吸烟史\"},"
				+ "\"sickbed\":{\"value\":0,\"itemId\":1702,\"startDate\":\"2015-05-18\",\"endDate\":\"2015-06-28\",\"zhName\":\"卧床史\"},"
				+ "\"thrombosis\":{\"value\":0,\"itemId\":1703,\"zhName\":\"血栓史\"},"
				+ "\"fracture\":{\"value\":0,\"itemId\":1704,\"zhName\":\"骨折史\"},"
				+ "\"pregnancy\":{\"value\":0,\"itemId\":1705,\"startDate\":\"2015-05-18\",\"endDate\":\"2015-06-28\",\"zhName\":\"怀孕史\"}}";
		try {			
			PersonalHistoryParser.PersonalParse(data);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
