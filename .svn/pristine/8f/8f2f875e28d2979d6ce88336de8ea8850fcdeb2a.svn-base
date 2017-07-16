package com.sinohealth.eszservice.service.visit.paser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;

public class BodySignValueParserTest {
	List<BodySignValueEntity> values1 = new ArrayList<>();
	List<BodySignValueEntity> values2 = new ArrayList<>();
	List<BodySignValueEntity> values3 = new ArrayList<>();

	@Before
	public void setup() {
		Calendar c1 = Calendar.getInstance();
		c1.clear();
		c1.set(2015, 2, 11);
		Calendar c2 = Calendar.getInstance();
		c2.clear();
		c2.set(2015, 3, 20);
		Calendar c3 = Calendar.getInstance();
		c3.clear();
		c3.set(2015, 3, 21);

		VisitItemEntity item1011 = new VisitItemEntity();
		item1011.setItemId(1011);
		VisitItemEntity item1012 = new VisitItemEntity();
		item1012.setItemId(1012);

		BodySignValueEntity e1 = new BodySignValueEntity();
		e1.setReportDate(c1.getTime());
		e1.setItem(item1011);
		e1.setReportValue("100");
		e1.setReportWarnLevel(1);
		BodySignValueEntity e2 = new BodySignValueEntity();
		e2.setReportDate(c1.getTime());
		e2.setItem(item1012);
		e2.setReportValue("100");
		e2.setReportWarnLevel(1);
		BodySignValueEntity e3 = new BodySignValueEntity();
		e3.setReportDate(c2.getTime());
		e3.setItem(item1011);
		e3.setReportValue("100");
		e3.setReportWarnLevel(1);
		BodySignValueEntity e4 = new BodySignValueEntity();
		e4.setReportDate(c3.getTime());
		e4.setItem(item1012);
		e4.setReportValue("100");
		e4.setReportWarnLevel(1);

		values1.add(e1);
		values1.add(e2);

		values2.add(e1);
		values2.add(e3);
	}

	@Test
	public void testParse() {

	}

	@Test
	public void testCompile1() throws ParseException {
		String s1 = "[{\"d\":\"2015-03-11\",\"v\":[[1011,\"100\",1],[1012,\"100\",1]]}]";
		Assert.assertEquals(s1, BodySignValueParser.compile(values1));
	}

	@Test
	public void testCompile2() throws ParseException {
		String s1 = "[{\"d\":\"2015-03-11\",\"v\":[[1011,\"100\",1]]},{\"d\":\"2015-04-20\",\"v\":[[1011,\"100\",1]]}]";
		Assert.assertEquals(s1, BodySignValueParser.compile(values2));
	}
}
