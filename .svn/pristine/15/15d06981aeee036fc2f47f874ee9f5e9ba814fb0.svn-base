package com.sinohealth.eszservice.service.visit.impl;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.ITemplateService;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.paser.ParseException;
import com.sinohealth.eszservice.service.visit.paser.TemplateParser;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/applicationContext.xml" })
// 加载配置文件
public class TemplateServiceImplTest {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ITemplateService templService;

	@Test
	@Transactional
	// 标明此方法需使用事务
	@Rollback(true)
	// 标明使用完此方法后事务回滚,true时为回滚
	public void testAddTempl() throws SystemErrorExecption {
		int doctorId = 1000001;
		SzSubjectEntity szSubject = new SzSubjectEntity();
		szSubject.setId("GXB");
		String data = "{\"bodySigns\":[{\"itemId\":1101,\"freq\":0},{\"itemId\":1102,\"freq\":1}],"
				+ "\"dailynote\":{\"noteIds\":[1,3,4],\"otherNote\":\"其它ABC事项\"},"
				+ "\"visitPoints\":{\"cycleUnit\":1,\"phases\":[{\"timePoint\":3,\"itemIds\":[1301,1303,1305]},"
				+ "{\"timePoint\":6,\"itemIds\":[1301,1303,1305,1307]},{\"timePoint\":12,\"itemIds\":[1301,1303]}]}}";
		TemplateEntity tp;
		DiseaseEntity disease = new DiseaseEntity();
		disease.setId("PCI");
		TemplateEntity stdTempl = new TemplateEntity();
		stdTempl.setTemplId(1);
		try {
			tp = TemplateParser.parse(data);
			TemplateEntity saved = templService.addTempl(doctorId, szSubject,
					tp, DateUtils.parseDate("2014-09-01 00:00:00"),
					"test templ name", disease, stdTempl);

			Assert.assertEquals(12, saved.getCycleLength());
			Assert.assertEquals(1, saved.getCycleUnit());
			Assert.assertEquals("GXB", saved.getSzSubject());
			Assert.assertTrue(saved.isVisible());

			tp = TemplateParser.parse(data);
			TemplateEntity saved2 = templService.addTempl(doctorId, szSubject,
					tp, DateUtils.parseDate("2014-09-01 00:00:00"),
					"This is template Name", disease, stdTempl);

			Assert.assertEquals(12, saved2.getCycleLength());
			Assert.assertEquals(1, saved2.getCycleUnit());
			Assert.assertEquals("GXB", saved2.getSzSubject());
			Assert.assertTrue(saved2.isVisible());
			Assert.assertEquals("This is template Name", saved2.getTemplName());
		} catch (ParseException e) {
			fail("添加模板失败");
		}
	}

}
