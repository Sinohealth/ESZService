package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.base.IProvinceDao;
import com.sinohealth.eszservice.dao.doctor.IDoctorDao;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.exception.ApplicationDuplicationExecption;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.pushservice.action.PushAction;
import com.sinohealth.pushservice.entity.Application;
import com.sinohealth.pushservice.entity.Message;
import com.sinohealth.pushservice.entity.PhoneSystem;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/applicationContext.xml" })
// 加载配置文件
public class ApplicationServiceImplTest {

	@Autowired
	IApplicationService applicationServiceImpl;

	@Autowired
	IProvinceDao provinceDao;

	@Autowired
	IDoctorDao doctorDao;

	@Test
	@Transactional
	// 标明此方法需使用事务
	@Rollback(true)
	// 标明使用完此方法后事务不回滚,true时为回滚
	public void testApplyNew() {
		/*
		 * int sickId = 10000001; int doctorId = 1000001; String szSubject =
		 * "GXB"; String name = "黄世莲"; int sex = 0; String leaveHospitalPics =
		 * "http://www.baidu.com/1.jpg,http:www.baidu.com/2.jpg"; String
		 * smallLeaveHospitalPics =
		 * "http://www.baidu.com/1s.jpg,http:www.baidu.com/2s.jpg"; String
		 * casesPics =
		 * "http://www.baidu.com/1xx.jpg,http:www.baidu.com/2xx.jpg"; String
		 * smallCasesPics =
		 * "http://www.baidu.com/1xxs.jpg,http:www.baidu.com/2xxs.jpg";
		 * 
		 * Date birthday = DateUtils.parseDate("1985-7-11"); Date leaveHosDate =
		 * DateUtils.parseDate("2015-7-11"); Date casesDate =
		 * DateUtils.parseDate("2014-7-11");
		 * 
		 * AppCasesComponent appCases = new AppCasesComponent();
		 * appCases.setCasesDate(casesDate);
		 * appCases.setLeaveHosDate(leaveHosDate);
		 * 
		 * SickEntity sick = new SickEntity(); sick.setId(sickId);
		 * ApplicationEntity saved = applicationServiceImpl.saveNewApply(sick,
		 * szSubject, doctorId, appCases);
		 * 
		 * int applyId = saved.getApplyId();
		 * 
		 * ApplicationEntity ent = applicationServiceImpl.get(applyId);
		 * 
		 * Assert.assertEquals(sickId, ent.getSick().getId().intValue());
		 */
		Message message = new Message();
		message.setTitle("fdlsjfslkj");
		message.setSummary("fdsfsdf");
		// message.setApplication(Application.eszDoctorGXBForIOS);
		// message.setPhoneSystem(PhoneSystem.IOS);

		message.setApplication(Application.eszSickForAndroid);
		message.setPhoneSystem(PhoneSystem.ANDROID);

		// message.setClientId("AoUXkawvIySbkI9hkH-unpFCRdnGDegLZHTXc-tQPYp0");
		message.setClientId("AoUXkawvIySbkI9hkH-unpFCRdnGDegLZHTXc-tQPYp0");
		// AoUXkawvIySbkI9hkH-unpFCRdnGDegLZHTXc-tQPYp0
		// PushAction.pushToSingle(message);
		PushAction.pushToAppForIOS(message);
	}

	@Test
	@Transactional
	// 标明此方法需使用事务
	@Rollback(true)
	// 标明使用完此方法后事务不回滚,true时为回滚
	public void testPerfectArchive() throws SystemErrorExecption,
			ApplicationDuplicationExecption {
		int sickId = 10000001;
		int doctorId = 1000001;
		SzSubjectEntity szSubject = new SzSubjectEntity();
		szSubject.setId("GXB");
		String name = "黄世莲";
		int sex = 0;
		String leaveHospitalPics = "http://www.baidu.com/1.jpg,http:www.baidu.com/2.jpg";
		String smallLeaveHospitalPics = "http://www.baidu.com/1s.jpg,http:www.baidu.com/2s.jpg";
		String casesPics = "http://www.baidu.com/1xx.jpg,http:www.baidu.com/2xx.jpg";
		String smallCasesPics = "http://www.baidu.com/1xxs.jpg,http:www.baidu.com/2xxs.jpg";

		Date birthday = DateUtils.parseDate("1985-7-11");
		Date leaveHosDate = DateUtils.parseDate("2015-7-11");
		Date casesDate = DateUtils.parseDate("2014-7-11");

		AppCasesComponent appCases = new AppCasesComponent();
		appCases.setCasesDate(casesDate);
		appCases.setLeaveHosDate(leaveHosDate);

		DiseaseEntity disease = new DiseaseEntity();
		disease.setId("PCI");
		disease.setName("测试的病种");

		SickEntity sick = new SickEntity();
		sick.setId(sickId);
		ApplicationEntity saved = applicationServiceImpl.saveNewApply(sick,
				szSubject, doctorId, appCases, disease);

		int applyId = saved.getApplyId();

		ApplicationEntity ent = applicationServiceImpl.get(applyId);

		Assert.assertEquals(sickId, ent.getSick().getId().intValue());

		String medPics = "medPics";
		String smallMedPics = "smallMedPics";
		String clinicCheckPics = "clinicCheckPics";
		String smallClinicCheckPics = "smallClinicCheckPics";
		String ecgPics = "ecgPics";
		String smallEcgPics = "smallEcgPics";
		String ultraSoundPics = "ultraSoundPics";
		String smallUltraSoundPics = "smallUltraSoundPics";
		String otherPics = "otherPics";
		String smallOtherPics = "smallOtherPics";
		String medicalHistories = "medicalHistories";
		String allergyHistories = "allergyHistories";
		String surgicalHistories = "surgicalHistories";

		AppPastHistoryComponent pastHistory = new AppPastHistoryComponent();
		pastHistory.setAllergyHistories(allergyHistories);
		pastHistory.setMedicalHistories(medicalHistories);
		pastHistory.setSurgicalHistories(surgicalHistories);

		List<VisitPrescriptionEntity> prescriptions = new ArrayList<>();
		VisitPrescriptionEntity prescription = new VisitPrescriptionEntity();
		prescription.setMedPic(medPics);
		prescription.setSmallMedPic(smallMedPics);
		prescription.setCorrectFlag(1);

		// 检查项
		List<VisitImgEntity> items = new ArrayList<>();
		VisitImgEntity item = new VisitImgEntity();
		item.setItemId(1301);
		item.setImg("test-img");
		item.setThumb("test-thumb");
		items.add(item);

		applicationServiceImpl.updateArchive(ent.getApplyId(), prescriptions,
				items, pastHistory, "[]");

		ApplicationEntity entUpdated = applicationServiceImpl.get(applyId);

		// Assert.assertEquals(prescriptions, entUpdated.getPrescriptions()); //
		// 更新后不一定返回
		Assert.assertEquals(medicalHistories, entUpdated.getAppPastHistory()
				.getMedicalHistories());
		Assert.assertEquals(surgicalHistories, entUpdated.getAppPastHistory()
				.getSurgicalHistories());

	}

}
