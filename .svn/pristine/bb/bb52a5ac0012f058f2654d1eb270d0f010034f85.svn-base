package com.sinohealth.eszservice.service.doctor.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.service.doctor.ICacheDoctorService;
import com.sinohealth.eszservice.service.doctor.exception.EntityNotCacheException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class DoctorServiceImplTest {
	@Autowired
	private ICacheDoctorService cacheDoctorService;

	@Test
	public void testCache() {
		Integer id = 1222221;
		DoctorEntity doctor = new DoctorEntity();
		cacheDoctorService.cache(id, doctor);

		Integer id2 = 1222221;
		DoctorEntity doctor2 = new DoctorEntity();
		doctor2.setId(id2);
		cacheDoctorService.cache(id2, doctor2);

		cacheDoctorService.cache(0, null);
	}

	@Test
	public void testCacheMobile() {
		Integer userId = 1222221;
		String mobile = "12345678901";
		cacheDoctorService.cacheMobile(mobile, userId);
		cacheDoctorService.cacheMobile(mobile, null);
		cacheDoctorService.cacheMobile(null, null);
	}

	@Test
	public void testCacheEmail() {
		Integer userId = 1222221;
		String email = "12345678901@163.com";
		cacheDoctorService.cacheEmail(email, userId);
		cacheDoctorService.cacheEmail(email, null);
		cacheDoctorService.cacheEmail(null, null);
	}

	@Test
	public void testGet() throws EntityNotCacheException {
		Integer id = 3222221;
		DoctorEntity doctor = new DoctorEntity();
		cacheDoctorService.cache(id, doctor);

		Assert.assertEquals(doctor, cacheDoctorService.get(id));

		Integer id2 = 3222222;
		DoctorEntity doctor2 = new DoctorEntity();
		doctor2.setId(id2);
		cacheDoctorService.cache(id2, doctor2);

		Assert.assertEquals(doctor2, cacheDoctorService.get(id2));
		Assert.assertEquals(doctor, cacheDoctorService.get(id));

		cacheDoctorService.cache(0, null);
		Assert.assertNull(cacheDoctorService.get(0));
		Assert.assertEquals(doctor2, cacheDoctorService.get(id2));
		Assert.assertEquals(doctor, cacheDoctorService.get(id));
	}

	@Test
	public void testGetIdByMobile() throws EntityNotCacheException {
		Integer userId1 = 4222221;
		String mobile1 = "12345600000";
		cacheDoctorService.cacheMobile(mobile1, userId1);
		Assert.assertEquals(userId1, cacheDoctorService.getIdByMobile(mobile1));

		Integer userId2 = null;
		String mobile2 = "12345600001";
		cacheDoctorService.cacheMobile(mobile2, userId2);
		Assert.assertEquals(userId2, cacheDoctorService.getIdByMobile(mobile2));
		Assert.assertEquals(userId1, cacheDoctorService.getIdByMobile(mobile1)); // 再验一遍上面

		String mobile3 = "12345600003";

		try {
			cacheDoctorService.getIdByMobile(mobile3);
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}
	}

	@Test
	public void testGetIdByEmail() throws EntityNotCacheException {
		Integer userId1 = 4222221;
		String email1 = "12345600000@qq.com";
		cacheDoctorService.cacheEmail(email1, userId1);
		Assert.assertEquals(userId1, cacheDoctorService.getIdByEmail(email1));

		Integer userId2 = null;
		String email2 = "12345600001@qq.com";
		cacheDoctorService.cacheEmail(email2, userId2);
		Assert.assertEquals(userId2, cacheDoctorService.getIdByEmail(email2));
		Assert.assertEquals(userId1, cacheDoctorService.getIdByEmail(email1)); // 再验一遍上面

		String email3 = "12345600003@qq.com";

		try {
			cacheDoctorService.getIdByEmail(email3);
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}
	}

	@Test
	public void testRemove() {
		Integer id = 5222221;
		DoctorEntity doctor = new DoctorEntity();
		cacheDoctorService.cache(id, doctor);

		Integer id2 = 5222222;
		DoctorEntity doctor2 = new DoctorEntity();
		String mobile2 = "51111111111";
		String email2 = "51111111111@qq.com";
		doctor2.setId(id2);
		doctor2.setMobile(mobile2);
		doctor2.setEmail(email2);

		cacheDoctorService.cache(id2, doctor2);

		try {
			Assert.assertEquals(id2, cacheDoctorService.getIdByMobile(mobile2));
			Assert.assertEquals(id2, cacheDoctorService.getIdByEmail(email2));
			Assert.assertEquals(doctor2, cacheDoctorService.get(id2));
			Assert.assertEquals(doctor, cacheDoctorService.get(id));
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}

		cacheDoctorService.remove(id);

		try {
			Assert.assertEquals(doctor, cacheDoctorService.get(id));
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}

		try {
			Assert.assertEquals(doctor2, cacheDoctorService.get(id2)); // id2仍然在
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}

		cacheDoctorService.remove(id2);

		try {
			Assert.assertEquals(doctor, cacheDoctorService.get(id2));
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}
		try {
			Assert.assertEquals(id2, cacheDoctorService.getIdByMobile(mobile2));
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}
		try {
			Assert.assertEquals(id2, cacheDoctorService.getIdByEmail(email2));
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 正常
		}

	}

	@Test
	public void testUpdateCached() {
		Integer id = 6222221;
		String mobile = "61111111111";
		DoctorEntity doctor = new DoctorEntity();
		doctor.setId(id);
		doctor.setMobile(mobile);
		cacheDoctorService.cache(id, doctor);

		String mobile2 = "622222222222";
		String email2 = "61111111111@qq.com";

		cacheDoctorService.cache(id, doctor);

		try {
			Assert.assertEquals(doctor, cacheDoctorService.get(id));
			Assert.assertEquals(id, cacheDoctorService.getIdByMobile(mobile));
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}

		doctor.setMobile(mobile2);
		doctor.setEmail(email2);

		// 更新缓存
		cacheDoctorService.updateCached(doctor);

		try {
			Assert.assertEquals(doctor, cacheDoctorService.get(id));
			Assert.assertEquals(id, cacheDoctorService.getIdByMobile(mobile2));
			Assert.assertEquals(id, cacheDoctorService.getIdByEmail(email2));
		} catch (EntityNotCacheException e) {
			Assert.fail();
		}

		try {
			Assert.assertNotEquals(id, cacheDoctorService.getIdByMobile(mobile));
			Assert.fail();
		} catch (EntityNotCacheException e) {
			// 已经没有了缓存
		}
	}

	@Test
	public void testIsIdCached() {
		Integer id = 7222221;
		String mobile = "71111111111";
		DoctorEntity doctor = new DoctorEntity();
		doctor.setId(id);
		doctor.setMobile(mobile);
		cacheDoctorService.cache(id, doctor);

		Assert.assertTrue(cacheDoctorService.isIdCached(id));
	}

}
