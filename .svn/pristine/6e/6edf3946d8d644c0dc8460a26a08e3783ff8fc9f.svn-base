package com.sinohealth.eszservice.service.sick.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszservice.service.sick.ISickOnlineService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class SickOnlineServiceImplTest {

	@Autowired
	private ISickOnlineService sickOnlineService;

	@Test
	public void testCacheToken() {
		String token = "aaaa";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000001;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		assertEquals(token, sickOnlineService.getToken(userId));
		assertEquals(userId, sickOnlineService.getUserId(token));
		assertEquals(lastVisitTime, sickOnlineService.getLastVisitTime(token));
		assertTrue(sickOnlineService.isOnline(token));
		assertTrue(sickOnlineService.hasToken(token));
	}

	@Test
	public void testHasToken() {
		String token = "bbbb";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000002;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		assertTrue(sickOnlineService.hasToken(token));
	}

	@Test
	public void testDeleteTokenString() {
		String token = "cccc";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000003;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		assertTrue(sickOnlineService.hasToken(token));
		sickOnlineService.deleteToken(token);
		assertFalse(sickOnlineService.hasToken(token));
	}

	@Test
	public void testDeleteTokenInt() {
		String token = "dddd";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000004;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		assertTrue(sickOnlineService.hasToken(token));
		sickOnlineService.deleteToken(userId);
		assertFalse(sickOnlineService.hasToken(token));
	}

	@Test
	public void testGetToken() {
		String token = "eeee";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000005;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		assertEquals(token, sickOnlineService.getToken(userId));
	}

	@Test
	public void testGetUserId() {
		String token = "ffffff";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000006;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		assertEquals(userId, sickOnlineService.getUserId(token));
	}

	@Test
	public void testGetLastVisitTime() {
		String token = "gggg";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000007;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		assertEquals(lastVisitTime, sickOnlineService.getLastVisitTime(token));
	}

	@Test
	public void testOffline() {
		String token = "hhhh";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000008;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		sickOnlineService.offline(token);
	}

	@Test
	public void testIsOnline() {
		String token = "iiii";
		long lastVisitTime = Calendar.getInstance().getTimeInMillis();
		int userId = 10000009;
		int online = 1;

		sickOnlineService.cacheToken(token, userId, lastVisitTime, online);
		sickOnlineService.offline(token);
		assertFalse(sickOnlineService.isOnline(token));
	}

}
