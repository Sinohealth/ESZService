package com.sinohealth.eszservice.doctor.user.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sinohealth.eszservice.service.doctor.impl.DoctorServiceImpl;

public class DoctorServiceImplTest {

	@Test
	public void testEntryptPassword() {
		DoctorServiceImpl imp = new DoctorServiceImpl();
		String password = "123456";
		String nOnce = "abcdefg";
		// 123456,
		// 8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92
		assertEquals(
				"8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92",
				imp.encryptPassword(password));

		assertEquals(
				"382A5C9F74C43A2736D6920CE96C4446A873E6353A7EDBD9B1E0D6E0AB83D76E",
				imp.encryptPassword(imp.encryptPassword(password) + nOnce));
	}

}
