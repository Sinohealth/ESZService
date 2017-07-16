package com.sinohealth.eszservice.service.visit.paser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.sinohealth.eszorm.entity.visit.BodySignEntity;
import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;

public class TemplatePaserTest {

	@Test
	public void testGetBodySigns() throws ParseException {
		String data = "{\"bodySigns\":[{\"itemId\":123,\"freq\":0},{\"itemId\":124,\"freq\":1}],"
				+ "\"dailynote\":{\"noteIds\":[1,3,4],\"otherNote\":\"其它ABC事项\"},"
				+ "\"visitPoints\":{\"cycleUnit\":1,\"phases\":[{\"timePoint\":3,\"itemIds\":[1,3,5]},"
				+ "{\"timePoint\":6,\"itemIds\":[1,3,5,7]},{\"timePoint\":12,\"itemIds\":[1,3]}]}}";

		TemplateEntity tp = TemplateParser.parse(data);

		// bodySigns
		List<BodySignEntity> bss = tp.getBodySigns();

		BodySignEntity bs = bss.get(0);
		Assert.assertEquals(123, bs.getItemId());
		Assert.assertEquals(0, bs.getFreq());

		BodySignEntity bs1 = bss.get(1);
		Assert.assertEquals(124, bs1.getItemId());
		Assert.assertEquals(1, bs1.getFreq());

		// dailynote
		Set<DailynoteEntity> dailynotes = new HashSet<>();
		DailynoteEntity d1 = new DailynoteEntity();
		d1.setNoteId(1);
		dailynotes.add(d1);
		DailynoteEntity d3 = new DailynoteEntity();
		d3.setNoteId(3);
		dailynotes.add(d3);
		DailynoteEntity d4 = new DailynoteEntity();
		d4.setNoteId(4);
		dailynotes.add(d4);
		Assert.assertEquals(dailynotes, tp.getDailynotes());
		Assert.assertEquals("其它ABC事项", tp.getOtherNote());

		// visitPoints
		Assert.assertEquals(1, tp.getCycleUnit());
		List<TemplatePhaseEntity> phases = tp.getPhases();
		TemplatePhaseEntity phase = phases.get(0);
		Assert.assertEquals(3, phase.getTimePoint());
		Assert.assertArrayEquals(new Integer[] { 5, 3, 1 }, phase.getItemIds()
				.toArray(new Integer[3]));
		TemplatePhaseEntity phase1 = phases.get(1);
		Assert.assertEquals(6, phase1.getTimePoint());
		Assert.assertArrayEquals(new Integer[] { 5, 7, 3, 1 }, phase1
				.getItemIds().toArray(new Integer[4]));
		TemplatePhaseEntity phase2 = phases.get(2);
		Assert.assertEquals(12, phase2.getTimePoint());
		Assert.assertArrayEquals(new Integer[] { 3, 1 }, phase2.getItemIds()
				.toArray(new Integer[2]));
	}
	// @Test
	// public void testCompile() {
	// TemplateModel tp = new TemplateModel();
	//
	// List<BodySignElem> bodySigns = new ArrayList<BodySignElem>();
	// BodySignElem bsi = new BodySignElem();
	// bsi.setFreq(1);
	// bsi.setItemId(10);
	// bodySigns.add(bsi);
	//
	// int cycleUnit = 1;
	//
	// Integer[] dailynotes = new Integer[] { 1, 3, 5, 7 };
	//
	// String otherNote = "This is other note";
	//
	// List<TimePointElem> phases = new ArrayList<TimePointElem>();
	// TimePointElem tpe = new TimePointElem();
	// tpe.setItemIds(new int[] { 1, 100, 1001, 10009 });
	// tpe.setTimePoint(2);
	// phases.add(tpe);
	//
	// tp.setBodySigns(bodySigns);
	// tp.setDailynotes(dailynotes);
	// tp.setCycleUnit(cycleUnit);
	// tp.setOtherNote(otherNote);
	// tp.setPhases(phases);
	//
	// String data = null;
	// try {
	// data = TemplateParser.compile(tp);
	//
	// // System.out.println(data);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// TemplateModel tpModel;
	// try {
	// tpModel = TemplateParser.parse(data);
	//
	// // bodySigns
	// Assert.assertEquals(bodySigns.get(0).getItemId(), tpModel
	// .getBodySigns().get(0).getItemId());
	// Assert.assertEquals(bodySigns.get(0).getFreq(), tpModel
	// .getBodySigns().get(0).getFreq());
	//
	// // dailynote
	// Assert.assertArrayEquals(dailynotes, tpModel.getDailynotes());
	// Assert.assertEquals(otherNote, tpModel.getOtherNote());
	//
	// // visitPoints
	// Assert.assertEquals(cycleUnit, tpModel.getCycleUnit());
	// Assert.assertEquals(phases.get(0).getTimePoint(), tpModel
	// .getPhases().get(0).getTimePoint());
	// Assert.assertArrayEquals(phases.get(0).getItemIds(), tpModel
	// .getPhases().get(0).getItemIds());
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// }
}
