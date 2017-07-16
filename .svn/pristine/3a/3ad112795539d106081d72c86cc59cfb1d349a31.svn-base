package com.sinohealth.eszservice.service.doctor.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszservice.service.doctor.ICacheDoctorNonceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class CacheDoctorNonceServiceImplTest {
	@Autowired
	ICacheDoctorNonceService cacheDoctorNonceService;

	@Test
	public void testCacheNonce() {
		String account = "1234567";
		String nonce = "hao123";
		cacheDoctorNonceService.cacheNonce(account, nonce);
	}

	@Test
	public void testIsNonceCached() {
		String account = "1234567@qg123.com";
		String nonce = "hao123";
		// assertFalse(CacheDoctorNonceUtils.isNonceCached(account, nonce));
		cacheDoctorNonceService.cacheNonce(account, nonce);
		assertTrue(cacheDoctorNonceService.isNonceCached(account, nonce));
	}

}
