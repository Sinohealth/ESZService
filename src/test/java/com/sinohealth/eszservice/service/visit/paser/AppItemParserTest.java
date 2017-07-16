package com.sinohealth.eszservice.service.visit.paser;

import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.sinohealth.eszorm.entity.visit.VisitImgEntity;

public class AppItemParserTest {

	@Test
	public void testParse() throws ParseException {
		String data = "[{\"itemId\":123,\"img\":\"http://xxx/1.jpg\",\"thumb\":\"http://xxx/s1.jpg\"},"
				+ "{\"itemId\":123,\"img\":\"http://xxx/2.jpg\",\"thumb\":\"http://xxx/s2.jpg\"},"
				+ "{\"itemId\":124,\"img\":\"http://xxx/3.jpg\",\"thumb\":\"http://xxx/s3.jpg\"},"
				+ "{\"itemId\":124,\"img\":\"http://xxx/4.jpg\",\"thumb\":\"http://xxx/s4.jpg\"}]";

		Set<VisitImgEntity> list = AppItemParser.parse(data);

		Iterator<VisitImgEntity> iterator = list.iterator();
		VisitImgEntity o = (VisitImgEntity) iterator.next();

		Assert.assertNotNull(o);

		// Assert.assertEquals("http://xxx/1.jpg", o.getImg());
	}

	@Test
	public void testCompile() {
	}

}
