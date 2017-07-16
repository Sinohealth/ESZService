package com.sinohealth.eszservice.service.sick.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.service.sick.ICacheSickService;
import com.sinohealth.eszservice.service.sick.exception.EntityNotCacheException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class CacheSickServiceImplTest {

	@Autowired
	ICacheSickService cacheSickService;

	@Test
	public void testCache() {
		int userId1 = 19999991;
		SickEntity sick1 = new SickEntity();

		cacheSickService.cache(userId1, sick1);

		int userId2 = 19999992;
		SickEntity sick2 = new SickEntity();
		sick2.setId(userId2);

		cacheSickService.cache(userId2, sick2);

		try {
			Assert.assertEquals(sick1, cacheSickService.get(userId1));
			Assert.assertEquals(sick2, cacheSickService.get(userId2));
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}
	}

	@Test
	public void testCacheMobile() {
		Integer userId1 = 29999991;
		String mobile1 = "mobile1";
		Integer userId2 = 29999992;
		String mobile2 = "mobile2";
		Integer userId3 = null;
		String mobile3 = "mobile3";
		String mobile4 = "mobile4";

		cacheSickService.cacheMobile(mobile1, userId1);
		cacheSickService.cacheMobile(mobile2, userId2);
		cacheSickService.cacheMobile(mobile3, userId3);

		try {
			Assert.assertEquals(userId1,
					cacheSickService.getIdByMobile(mobile1));
			Assert.assertEquals(userId2,
					cacheSickService.getIdByMobile(mobile2));
			Assert.assertEquals(userId3,
					cacheSickService.getIdByMobile(mobile3));
		} catch (EntityNotCacheException e1) {
			Assert.fail();
		}

		try {
			cacheSickService.getIdByMobile(mobile4);
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}
	}

	@Test
	public void testCacheEmail() {
		Integer userId1 = 29999991;
		String email1 = "email1@qq.com";
		Integer userId2 = 29999992;
		String email2 = "email2@qq.com";
		Integer userId3 = null;
		String email3 = "email3@qq.com";
		String email4 = "email4@qq.com";

		cacheSickService.cacheEmail(email1, userId1);
		cacheSickService.cacheEmail(email2, userId2);
		cacheSickService.cacheEmail(email3, userId3);

		try {
			Assert.assertEquals(userId1, cacheSickService.getIdByEmail(email1));
			Assert.assertEquals(userId2, cacheSickService.getIdByEmail(email2));
			Assert.assertEquals(userId3, cacheSickService.getIdByEmail(email3));
		} catch (EntityNotCacheException e1) {
			e1.printStackTrace();
			Assert.fail();
		}

		try {
			cacheSickService.getIdByEmail(email4);
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}
	}

	@Test
	public void testGet() {
		int userId1 = 39999991;
		SickEntity sick1 = new SickEntity();

		cacheSickService.cache(userId1, sick1);

		int userId2 = 39999992;
		SickEntity sick2 = new SickEntity();
		sick2.setId(userId2);

		cacheSickService.cache(userId2, sick2);

		int userId3 = 39999993;

		try {
			Assert.assertEquals(sick1, cacheSickService.get(userId1));
			Assert.assertEquals(sick2, cacheSickService.get(userId2));
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}

		try {
			cacheSickService.get(userId3);
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}
	}

	@Test
	public void testGetIdByMobile() {
		// 已经在testCacheMobile实现
	}

	@Test
	public void testGetIdByEmail() {
		// 已经在testCacheEmail实现
	}

	@Test
	public void testRemove() {
		int userId1 = 49999991;
		SickEntity sick1 = new SickEntity();

		cacheSickService.cache(userId1, sick1);

		int userId2 = 49999992;
		SickEntity sick2 = new SickEntity();
		sick2.setId(userId2);

		cacheSickService.cache(userId2, sick2);

		int userId3 = 49999993;
		String mobile3 = "testRemove-mobile1";
		String email3 = "testRemove-email1";
		SickEntity sick3 = new SickEntity();
		sick3.setId(userId3);
		sick3.setMobile(mobile3);
		sick3.setEmail(email3);

		cacheSickService.cache(userId3, sick3);

		try {
			Assert.assertEquals(sick1, cacheSickService.get(userId1));
			Assert.assertEquals(sick2, cacheSickService.get(userId2));
			Assert.assertEquals(sick3, cacheSickService.get(userId3));
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}

		cacheSickService.remove(userId1);
		try {
			Assert.assertEquals(sick1, cacheSickService.get(userId1));
			Assert.fail();
		} catch (EntityNotCacheException e) {
		}

		cacheSickService.remove(userId2);
		try {
			Assert.assertEquals(sick2, cacheSickService.get(userId2));
			Assert.fail();
		} catch (EntityNotCacheException e) {
		}

		cacheSickService.remove(userId3);
		try {
			Assert.assertEquals(sick3, cacheSickService.get(userId3));
			Assert.fail();
		} catch (EntityNotCacheException e) {
		}

		try {
			cacheSickService.getIdByMobile(mobile3);
			Assert.fail();
		} catch (EntityNotCacheException e) {
		}

		try {
			cacheSickService.getIdByEmail(email3);
			Assert.fail();
		} catch (EntityNotCacheException e) {
		}
	}

	@Test
	public void testIsIdCached() {
		int userId1 = 59999991;
		SickEntity sick1 = new SickEntity();

		cacheSickService.cache(userId1, sick1);

		int userId2 = 59999992;
		SickEntity sick2 = new SickEntity();
		sick2.setId(userId2);

		cacheSickService.cache(userId2, sick2);

		int userId3 = 59999993;

		Assert.assertTrue(cacheSickService.isIdCached(userId1));
		Assert.assertTrue(cacheSickService.isIdCached(userId2));
		Assert.assertFalse(cacheSickService.isIdCached(userId3));
	}

	@Test
	public void testUpdateCached() {
		Integer userId1 = 69999991;
		SickEntity sick1 = new SickEntity();
		sick1.setId(userId1);

		cacheSickService.cache(userId1, sick1);

		String mobile1 = "testUpdateCached-mobile1";
		String email1 = "testUpdateCached-email1";

		sick1.setMobile(mobile1);
		sick1.setEmail(email1);

		cacheSickService.updateCached(sick1);

		try {
			Assert.assertEquals(sick1, cacheSickService.get(userId1));
			Assert.assertEquals(userId1,
					cacheSickService.getIdByMobile(mobile1));
			Assert.assertEquals(userId1, cacheSickService.getIdByEmail(email1));
		} catch (EntityNotCacheException e) {
			e.printStackTrace();
			Assert.fail();
		}

		String mobile2 = "testUpdateCached-mobile2";
		String email2 = "testUpdateCached-email2";

		sick1.setMobile(mobile2);
		sick1.setEmail(email2);

		cacheSickService.updateCached(sick1);

		try {
			Assert.assertEquals(sick1, cacheSickService.get(userId1));
			Assert.assertEquals(userId1,
					cacheSickService.getIdByMobile(mobile2));
			Assert.assertEquals(userId1, cacheSickService.getIdByEmail(email2));
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}

		try {
			cacheSickService.getIdByMobile(mobile1);
			Assert.fail();
		} catch (EntityNotCacheException e) {
		}

		try {
			cacheSickService.getIdByMobile(mobile1);
			cacheSickService.getIdByEmail(email1);
			Assert.fail();
		} catch (EntityNotCacheException e) {
		}

	}

}
