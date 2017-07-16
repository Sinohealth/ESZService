package com.sinohealth.eszservice.service.visit.paser;

import org.junit.Assert;
import org.junit.Test;

import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;

public class PastHistoryParserTest {

	@Test
	public void testParse() throws ParseException {
		String data = "{ \"disease\": [\"aaa\",\"bbb\"], \"allergy\":[\"ddd\"], \"surgical\":[]}";

		AppPastHistoryComponent pastHistory = PastHistoryParser.parse(data);

		Assert.assertEquals("aaa@@bbb", pastHistory.getMedicalHistories());
		Assert.assertEquals("ddd", pastHistory.getAllergyHistories());
		Assert.assertEquals("", pastHistory.getSurgicalHistories());

		String data1 = "{ \"disease\": [\"aaa\",\"\", \"ccc\"], \"allergy\":[\"ddd\"]}";
		AppPastHistoryComponent pastHistory1 = PastHistoryParser.parse(data1);

		Assert.assertEquals("aaa@@ccc", pastHistory1.getMedicalHistories());
		Assert.assertEquals("ddd", pastHistory1.getAllergyHistories());
		//Assert.assertNull(pastHistory1.getSurgicalHistories());
	}

}
