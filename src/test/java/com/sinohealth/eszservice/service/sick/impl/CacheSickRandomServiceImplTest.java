package com.sinohealth.eszservice.service.sick.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszservice.service.sick.ICacheSickRandomService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class CacheSickRandomServiceImplTest {

	@Autowired
	private ICacheSickRandomService cacheSickRandomService;

	@Test
	public void testSetRandomCode() {
		String userId = "12222223";
		String randomCode = "a1111";
		cacheSickRandomService.setRandomCode(userId, randomCode);
	}

	@Test
	public void testGetRandomCode() {
		String userId = "23333334";
		String randomCode = "b2222";
		cacheSickRandomService.setRandomCode(userId, randomCode);
		Assert.assertEquals(randomCode,
				cacheSickRandomService.getRandomCode(userId));
	}

}
