package com.sinohealth.eszservice.service.base.impl;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class HolidayServiceTest {
	private HolidayService holidayService;

	@Before
	public void setUp() throws Exception {
		holidayService = new HolidayService();
	}

	@Test
	public void testIsHoliday() {
		Calendar cal = Calendar.getInstance();

		cal.set(2015, 4, 1, 0, 0, 0); // 5.1
		assertTrue(holidayService.isHoliday(cal.getTime()));

		cal.set(2015, 4, 2, 0, 0, 0); // 5.2
		assertTrue(holidayService.isHoliday(cal.getTime()));

		cal.set(2015, 4, 4, 0, 0, 0); // 5.4
		assertFalse(holidayService.isHoliday(cal.getTime()));

		cal.set(2015, 8, 3, 0, 0, 0); // 9.3
		assertTrue(holidayService.isHoliday(cal.getTime()));
	}

}
