package com.sinohealth.eszservice.service.doctor.impl;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszservice.service.doctor.IDoctorOnlineService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class DoctorOnlineServiceImplTest {

	@Autowired
	private IDoctorOnlineService doctorOnlineService;

	@Test
	public void testCacheToken() {
		String token1 = "A1111";
		int userId1 = 1000001;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;

		String token2 = "A2222";
		int userId2 = 1000002;
		String szSubject2 = "SJ";
		long lastVisitTime2 = Calendar.getInstance().getTimeInMillis();
		int online2 = 1;

		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		doctorOnlineService.cacheToken(token2, userId2, szSubject2,
				lastVisitTime2, online2);
	}

	@Test
	public void testHasToken() {
		String token1 = "B1111";
		int userId1 = 1000003;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		Assert.assertTrue(doctorOnlineService.hasToken(token1));
	}

	@Test
	public void testDeleteToken() {
		String token1 = "C1111";
		int userId1 = 1000004;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		Assert.assertTrue(doctorOnlineService.hasToken(token1));
		doctorOnlineService.deleteToken(token1);
		Assert.assertFalse(doctorOnlineService.hasToken(token1));
	}

	@Test
	public void testDeleteTokenInt() {
		String token1 = "F1111";
		int userId1 = 1000014;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		String token2 = "F2222";
		int userId2 = 1000014;
		String szSubject2 = "SJ";
		long lastVisitTime2 = Calendar.getInstance().getTimeInMillis();
		int online2 = 1;

		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		doctorOnlineService.cacheToken(token2, userId2, szSubject2,
				lastVisitTime2, online2);

		Assert.assertTrue(doctorOnlineService.hasToken(token1));
		Assert.assertTrue(doctorOnlineService.hasToken(token2));
		doctorOnlineService.deleteToken(userId1);
		Assert.assertFalse(doctorOnlineService.hasToken(token1));
		Assert.assertFalse(doctorOnlineService.hasToken(token2));
	}

	@Test
	public void testGetUserId() {
		String token1 = "testGetUserId";
		int userId1 = 1000005;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		Assert.assertEquals(userId1, doctorOnlineService.getUserId(token1));
	}

	@Test
	public void testGetLastVisitTime() {
		String token1 = "E1111";
		int userId1 = 1000006;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		Assert.assertEquals(lastVisitTime1,
				doctorOnlineService.getLastVisitTime(token1));
	}

	@Test
	public void testSetLastVisitTime() {
		String token1 = "F1111";
		int userId1 = 1000007;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		long lastVisitTime2 = 99999L;
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		Assert.assertEquals(lastVisitTime1,
				doctorOnlineService.getLastVisitTime(token1));

		doctorOnlineService.setLastVisitTime(token1, lastVisitTime2);
		Assert.assertEquals(lastVisitTime2,
				doctorOnlineService.getLastVisitTime(token1));
	}

	@Test
	public void testOffline() {
		String token1 = "testOffline";
		int userId1 = 1000008;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		doctorOnlineService.offline(token1);
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		doctorOnlineService.offline(token1);
	}

	@Test
	public void testIsOnline() {
		String token1 = "testIsOnline";
		int userId1 = 1000009;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		Assert.assertTrue(doctorOnlineService.isOnline(token1));
		doctorOnlineService.offline(token1);
		Assert.assertFalse(doctorOnlineService.isOnline(token1));
	}

	@Test
	public void testGetSzSubject() {
		String token1 = "testIsOnline1";
		int userId1 = 1000010;
		String szSubject1 = "GXB";
		long lastVisitTime1 = Calendar.getInstance().getTimeInMillis();
		int online1 = 1;
		doctorOnlineService.cacheToken(token1, userId1, szSubject1,
				lastVisitTime1, online1);
		Assert.assertEquals(szSubject1,
				doctorOnlineService.getSzSubject(token1));

		String token2 = "testIsOnline2";
		int userId2 = 1000010;
		String szSubject2 = "SJ";
		long lastVisitTime2 = Calendar.getInstance().getTimeInMillis();
		int online2 = 1;
		doctorOnlineService.cacheToken(token2, userId2, szSubject2,
				lastVisitTime2, online2);
		Assert.assertEquals(szSubject2,
				doctorOnlineService.getSzSubject(token2));

		Assert.assertEquals(szSubject1,
				doctorOnlineService.getSzSubject(token1));
	}
}
