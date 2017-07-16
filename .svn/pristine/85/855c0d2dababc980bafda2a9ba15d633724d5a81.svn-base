package com.sinohealth.eszservice.service.doctor.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszservice.service.doctor.ICacheDoctorRandomService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class CacheDoctorRandomServiceImplTest {
	@Autowired
	ICacheDoctorRandomService cacheDoctorRandomServiceImpl;

	@Test
	public void testSetRandomCode() {
		String userId = "1222223";
		String randomCode = "a12344";
		cacheDoctorRandomServiceImpl.setRandomCode(userId, randomCode);
	}

	@Test
	public void testGetRandomCode() {
		String userId = "2333334";
		String randomCode = "b2222";
		String rs = cacheDoctorRandomServiceImpl.getRandomCode(userId);
		// Assert.assertNull(rs);
		cacheDoctorRandomServiceImpl.setRandomCode(userId, randomCode);
		rs = cacheDoctorRandomServiceImpl.getRandomCode(userId);
		Assert.assertEquals(randomCode, rs);
	}

}
