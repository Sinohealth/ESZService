package com.sinohealth.eszservice.service.visit.paser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

public class CasesParserTest {

	// @Test
	// public void testParse() throws SystemErrorExecption {
	// String data = "{\"leaveHosDate\":\"2015-03-31\","
	// +
	// "\"leaveHosPics\":[{\"img\":\"1.jpg\",\"thumb\":\"s1.jpg\"},{\"img\":\"2.jpg\",\"thumb\":\"s2.jpg\"}],"
	// + "\"casesDate\":\"2015-07-20\","
	// +
	// "\"casesPics\":[{\"img\":\"3.jpg\",\"thumb\":\"s3.jpg\"},{\"img\":\"4.jpg\",\"thumb\":\"s4.jpg\"}]}";
	// AppCasesComponent cases;
	// try {
	// cases = CasesParser.parse(data);
	//
	// Calendar cal = Calendar.getInstance();
	// cal.clear();
	// cal.set(2015, 2, 31, 0, 0, 0);
	//
	// Assert.assertEquals(cal.getTimeInMillis() / 1000, cases
	// .getLeaveHosDate().getTime() / 1000);
	//
	// cal.set(2015, 6, 20);
	// Assert.assertEquals(cal.getTime().getTime() / 1000, cases
	// .getCasesDate().getTime() / 1000);
	//
	// Assert.assertEquals(2, cases.getLeaveHosPics().size());
	// Assert.assertEquals("1.jpg", cases.getLeaveHosPics().get(0)
	// .getImg());
	// Assert.assertEquals(2, cases.getCasesPics().size());
	// Assert.assertEquals("3.jpg", cases.getCasesPics().get(0).getImg());
	// } catch (SystemErrorExecption e) {
	// e.printStackTrace();
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Test
	// public void testParse2() throws SystemErrorExecption {
	// String data = "{"
	// +
	// "\"leaveHosPics\":[{\"img\":\"1.jpg\",\"thumb\":\"s1.jpg\"},{\"img\":\"2.jpg\",\"thumb\":\"s2.jpg\"}],"
	// + "\"casesDate\":\"\","
	// +
	// "\"casesPics\":[{\"img\":\"3.jpg\",\"thumb\":\"s3.jpg\"},{\"img\":\"4.jpg\",\"thumb\":\"s4.jpg\"}]}";
	// AppCasesComponent cases;
	// try {
	// cases = CasesParser.parse(data);
	//
	// Assert.assertNull(cases.getLeaveHosDate());
	//
	// Assert.assertNull(cases.getCasesDate());
	//
	// Assert.assertEquals(2, cases.getLeaveHosPics().size());
	// Assert.assertEquals("1.jpg", cases.getLeaveHosPics().get(0)
	// .getImg());
	// Assert.assertEquals(2, cases.getCasesPics().size());
	// Assert.assertEquals("3.jpg", cases.getCasesPics().get(0).getImg());
	// } catch (SystemErrorExecption e) {
	// e.printStackTrace();
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }

	// @Test
	// public void testParse3() {
	// String data = "{\"casesDate\":\"2016-4-9\",\"casesPics\":"
	// +
	// "[{\"fileName\":\"/storage/emulated/0/Pictures/Screenshots/Screenshot_2014-12-20-10-29-27.jpeg\","
	// +
	// "\"img\":\"http://7xi7xg.com1.z0.glb.clouddn.com/1428549513133andorid_esfh.jpeg\","
	// +
	// "\"thumb\":\"http://7xi7xg.com1.z0.glb.clouddn.com/1428549513133andorid_esfh_s.jpeg\"}],"
	// + "\"leaveHosDate\":\"2010-4-9\",\"leaveHosPics\":"
	// +
	// "[{\"fileName\":\"/storage/emulated/0/Pictures/Screenshots/IMG_20141231_132249.jpg\","
	// +
	// "\"img\":\"http://7xi7xg.com1.z0.glb.clouddn.com/1428549531178andorid_esfh.jpeg\","
	// +
	// "\"thumb\":\"http://7xi7xg.com1.z0.glb.clouddn.com/1428549531178andorid_esfh_s.jpeg\"}]}";
	// AppCasesComponent cases;
	// try {
	// cases = CasesParser.parse(data);
	//
	// Calendar cal = Calendar.getInstance();
	// cal.set(2015, 3, 9, 0, 0, 0);
	//
	// Assert.assertEquals(cal.getTimeInMillis() / 1000, cases
	// .getLeaveHosDate().getTime() / 1000);
	//
	// cal.set(2015, 6, 20);
	// Assert.assertEquals(cal.getTime().getTime() / 1000, cases
	// .getCasesDate().getTime() / 1000);
	//
	// Assert.assertEquals(2, cases.getLeaveHosPics().size());
	// Assert.assertEquals("1.jpg", cases.getLeaveHosPics().get(0)
	// .getImg());
	// Assert.assertEquals(2, cases.getCasesPics().size());
	// Assert.assertEquals("3.jpg", cases.getCasesPics().get(0).getImg());
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }

	// @Test
	// public void testCompile() throws ParseException, SystemErrorExecption {
	// AppCasesComponent cases = new AppCasesComponent();
	// Calendar cal1 = Calendar.getInstance();
	// cal1.set(2015, 3, 20, 0, 0, 0);
	// cases.setLeaveHosDate(cal1.getTime());
	// Calendar cal2 = Calendar.getInstance();
	// cal2.set(2015, 4, 20, 0, 0, 0);
	// cases.setCasesDate(cal2.getTime());
	//
	// List<VisitImgEntity> orgLeaveHosPics = new ArrayList<>();
	// VisitImgEntity e1 = new VisitImgEntity();
	// e1.setImg("1.jpg");
	// e1.setThumb("s1.jpg");
	// orgLeaveHosPics.add(e1);
	// VisitImgEntity e2 = new VisitImgEntity();
	// e2.setImg("22.jpg");
	// e2.setThumb("s22.jpg");
	// orgLeaveHosPics.add(e2);
	// cases.getApplyImgList().addAll(orgLeaveHosPics);
	//
	// List<VisitImgEntity> orgCasesPics = new ArrayList<>();
	// VisitImgEntity e3 = new VisitImgEntity();
	// e3.setImg("33.jpg");
	// e3.setThumb("s33.jpg");
	// orgCasesPics.add(e3);
	// VisitImgEntity e4 = new VisitImgEntity();
	// e4.setImg("44.jpg");
	// e4.setThumb("s44.jpg");
	// orgCasesPics.add(e4);
	// VisitImgEntity e5 = new VisitImgEntity();
	// e5.setImg("555.jpg");
	// e5.setThumb("s555.jpg");
	// orgCasesPics.add(e5);
	// cases.getApplyImgList().addAll(orgCasesPics);
	//
	// String compiled = CasesParser.compile(cases);
	//
	// // System.out.println(compiled);
	//
	// AppCasesComponent parsed = CasesParser.parse(compiled);
	//
	// Assert.assertEquals(cal1.getTimeInMillis() / 1000, parsed
	// .getLeaveHosDate().getTime() / 1000);
	//
	// Assert.assertEquals(cal2.getTime().getTime() / 1000, parsed
	// .getCasesDate().getTime() / 1000);
	//
	// Assert.assertEquals(2, parsed.getLeaveHosPics().size());
	// Assert.assertEquals("1.jpg", parsed.getLeaveHosPics().get(0).getImg());
	// Assert.assertEquals(3, parsed.getCasesPics().size());
	// Assert.assertEquals("33.jpg", parsed.getCasesPics().get(0).getImg());
	//
	// }
}
