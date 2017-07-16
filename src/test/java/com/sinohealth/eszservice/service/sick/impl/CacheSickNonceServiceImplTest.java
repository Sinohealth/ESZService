package com.sinohealth.eszservice.service.sick.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszservice.service.sick.ICacheSickNonceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class CacheSickNonceServiceImplTest {

	@Autowired
	private ICacheSickNonceService cacheSickNonceService;

	@Test
	public void testCacheNonce() {
		String account = "12222223";
		String nonce = "a1111";
		cacheSickNonceService.cacheNonce(account, nonce);
	}

	@Test
	public void testIsNonceCached() {
		String account = "23333334";
		String nonce = "b22222";
		cacheSickNonceService.cacheNonce(account, nonce);
		assertTrue(cacheSickNonceService.isNonceCached(account, nonce));
	}

}
