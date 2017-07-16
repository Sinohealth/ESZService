package com.sinohealth.eszservice.service.visit.impl;

import org.junit.Assert;
import org.junit.Test;

import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

public class VisitItemServiceImplTest {

	VisitItemServiceImpl service = new VisitItemServiceImpl();

	@Test
	public void testGetWarnLevelForNumber() throws ValueOutOfRangeException {
		int male = 0;
		int female = 1;
		int unkown = 2;
		VisitItemEntity itm1 = new VisitItemEntity();
		VisitItemEntity itm2 = new VisitItemEntity();
		VisitItemEntity itm3 = new VisitItemEntity();

		itm1.setItemId(123);
		itm1.setOp1(">");
		itm1.setSex1(2);
		itm1.setMinValue1("1.99");
		itm1.setWarnLevel1(6);

		Assert.assertEquals(6, service.getWarnLevelForNumber(itm1, "2", male));
		Assert.assertEquals(6, service.getWarnLevelForNumber(itm1, "2", female));
		Assert.assertEquals(6, service.getWarnLevelForNumber(itm1, "2", unkown));

		itm2.setItemId(124);
		itm2.setOp1("between");
		itm2.setSex1(2);
		itm2.setMinValue1("10");
		itm2.setMaxValue1("99");
		itm2.setWarnLevel1(1);
		itm2.setOp2("between");
		itm2.setSex2(2);
		itm2.setMinValue2("99");
		itm2.setMaxValue2("199");
		itm2.setWarnLevel2(6);

		Assert.assertEquals(1, service.getWarnLevelForNumber(itm2, "20", male));
		Assert.assertEquals(6,
				service.getWarnLevelForNumber(itm2, "199", female));
		try {
			service.getWarnLevelForNumber(itm2, "2", unkown);
			Assert.fail();
		} catch (ValueOutOfRangeException e) {
		}

		itm3.setItemId(1999);
		itm3.setOp1("between");
		itm3.setSex1(1);
		itm3.setMinValue1("10");
		itm3.setMaxValue1("99");
		itm3.setWarnLevel1(6);
		itm3.setOp2("between");
		itm3.setSex2(0);
		itm3.setMinValue2("100");
		itm3.setMaxValue2("199");
		itm3.setWarnLevel2(1);
		itm3.setOp3(">");
		itm3.setSex3(0);
		itm3.setMinValue3("199");
		itm3.setMaxValue3("500");
		itm3.setWarnLevel3(6);

		Assert.assertEquals(6,
				service.getWarnLevelForNumber(itm3, "20", female));
		Assert.assertEquals(1, service.getWarnLevelForNumber(itm3, "100", male));
		Assert.assertEquals(1, service.getWarnLevelForNumber(itm3, "199", male));
		Assert.assertEquals(6, service.getWarnLevelForNumber(itm3, "200", male));

	}

}
