package com.sinohealth.eszservice.service.base.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinohealth.eszservice.service.base.IClientIdService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext.xml" })
public class ClientIdServiceImplTest {
	@Autowired
	private IClientIdService clientIdServiceImpl;

	private String eszDoctorGXBForIOS = "eszDoctorGXBForIOS";
	//private String eszDoctorGXBForAndroid = "eszDoctorGXBForAndroid";
	private String eszSickForIOS = "eszSickForIOS";
	private String eszSickForAndroid = "eszSickForAndroid";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		int doctorId1 = 1000001;
		int doctorId2 = 1000002;
		int sickId1 = 10000004;
		int sickId2 = 10000005;

		clientIdServiceImpl.setClientId(doctorId1, eszDoctorGXBForIOS,
				"doctorId1");
		clientIdServiceImpl.setClientId(doctorId2, eszDoctorGXBForIOS,
				"doctorId2");
		clientIdServiceImpl.setClientId(sickId1, eszSickForIOS, "sickId1");
		clientIdServiceImpl.setClientId(sickId2, eszSickForIOS, "sickId2");

		assertEquals(eszDoctorGXBForIOS,
				clientIdServiceImpl.getAppName(doctorId1, "GXB"));

		assertEquals(eszDoctorGXBForIOS,
				clientIdServiceImpl.getAppName(doctorId2, "GXB"));

		assertEquals(eszSickForIOS,
				clientIdServiceImpl.getAppName(sickId1, "GXB"));

		assertEquals(eszSickForIOS,
				clientIdServiceImpl.getAppName(sickId2, "GXB"));

		assertEquals("doctorId1",
				clientIdServiceImpl.getClientId(doctorId1, "GXB"));

		assertEquals("doctorId2",
				clientIdServiceImpl.getClientId(doctorId2, "GXB"));

		assertEquals("sickId1", clientIdServiceImpl.getClientId(sickId1, "GXB"));

		assertEquals("sickId2", clientIdServiceImpl.getClientId(sickId2, "GXB"));

		assertEquals("sickId2", clientIdServiceImpl.getClientId(sickId2, ""));

		// 同一个医生，到doctorId1的手机上使用
		clientIdServiceImpl.setClientId(doctorId2, eszDoctorGXBForIOS,
				"doctorId1");
		assertEquals("doctorId1",
				clientIdServiceImpl.getClientId(doctorId2, "GXB"));

		// doctorId1又用回了自己的手机
		clientIdServiceImpl.setClientId(doctorId1, eszDoctorGXBForIOS,
				"doctorId1");
		assertEquals("doctorId1",
				clientIdServiceImpl.getClientId(doctorId1, "GXB"));

		// 同一个医生，到doctorId1的手机上使用
		clientIdServiceImpl.setClientId(sickId2, eszSickForIOS, "sickId1");
		assertEquals("sickId1", clientIdServiceImpl.getClientId(sickId2, ""));

		// sickId2又用回了自己的手机
		clientIdServiceImpl.setClientId(sickId2, eszSickForIOS, "sickId2");
		assertEquals("sickId2", clientIdServiceImpl.getClientId(sickId2, "GXB"));
		assertEquals(eszSickForIOS,
				clientIdServiceImpl.getAppName(sickId2, "GXB"));
		clientIdServiceImpl.setClientId(sickId2, eszSickForIOS, "sickId2");
		assertEquals("sickId2", clientIdServiceImpl.getClientId(sickId2, "GXB"));
		assertEquals(eszSickForIOS,
				clientIdServiceImpl.getAppName(sickId2, "GXB"));

		// sickId2又用回了自己的手机
		clientIdServiceImpl.setClientId(sickId2, eszSickForAndroid, "sickId2");
		assertEquals("sickId2", clientIdServiceImpl.getClientId(sickId2, "GXB"));
		assertEquals(eszSickForAndroid,
				clientIdServiceImpl.getAppName(sickId2, "GXB"));
	}

}
