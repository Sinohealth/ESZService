package com.sinohealth.eszservice.doctor.user.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszorm.entity.base.ProvinceEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.dao.base.IProvinceDao;
import com.sinohealth.eszservice.dao.sick.ISickDao;
import com.sinohealth.eszservice.service.base.IProvinceService;
import com.sinohealth.eszservice.service.base.impl.ProvinceServiceImpl;
import com.sinohealth.eszservice.service.doctor.impl.DoctorServiceImpl;
import com.sinohealth.eszservice.service.sick.ISickService;
import com.sinohealth.eszservice.service.sick.impl.SickServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })

public class SickServiceImplTest {

	@Autowired
	private IProvinceService provinceService;
	
	@Autowired
	private ISickService sickService;
	
	
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
	
//	@Test
//	public void provinceTest() {
//		// ProvinceEntity province = provinceService.get(14);
//		// System.out.println(province.getProvinceName());
//		/*
//		 * List<CityEntity> list = province.getCities(); for (CityEntity
//		 * entity:list) {
//		 * System.out.println(entity.getId()+" : "+entity.getCityName()); }
//		 */
//	}
//	
//	@Test
//	public void findCityBySick() {
////		SickEntity sick =  sickService.get(10000001);
////		System.out.println(sick.getCity().getCityName()+" 省份： "+sick.getProvince().getProvinceName());
////		assertEquals("黄山市", sick.getCity().getCityName());
////		assertEquals("安徽", sick.getProvince().getProvinceName());
//	}

}
