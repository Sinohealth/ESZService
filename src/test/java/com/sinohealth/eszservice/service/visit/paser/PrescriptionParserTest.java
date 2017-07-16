package com.sinohealth.eszservice.service.visit.paser;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;

public class PrescriptionParserTest {

	@Test
	public void testParse() throws ParseException {
		String data = "{\"pics\": [{\"img\":\"http://xxx/1.jpg\", \"thumb\":\"http://xxx/s1.jpg\"},"
				+ "{\"img\":\"http://xxx/2.jpg\", \"thumb\":\"http://xxx/s2.jpg\"}]}";

		List<VisitPrescriptionEntity> set = PrescriptionParser.parse(data);

		Iterator<VisitPrescriptionEntity> iterator = set.iterator();
		VisitPrescriptionEntity appPrescriptionItemEntity = (VisitPrescriptionEntity) iterator
				.next();

		Assert.assertEquals(2,set.size());
//		Assert.assertEquals("http://xxx/2.jpg",
//				appPrescriptionItemEntity.getMedPic());
//		Assert.assertEquals("http://xxx/s2.jpg",
//				appPrescriptionItemEntity.getSmallMedPic());
		// Assert.assertEquals(0, appPrescriptionItemEntity.getCorrectFlag()
		// .intValue());
	}

	@Test
	public void testCompile() {
	}

}
