package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.BodySignEntity;
import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.PhaseComparator;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.dto.ConstantDoctorVisitErrs;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.visit.ITemplateDao;
import com.sinohealth.eszservice.dto.visit.TimelineDto;
import com.sinohealth.eszservice.service.base.impl.HolidayService;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;
import com.sinohealth.eszservice.service.doctor.IDoctorService;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IBodySignService;
import com.sinohealth.eszservice.service.visit.IDailynoteService;
import com.sinohealth.eszservice.service.visit.IPhaseService;
import com.sinohealth.eszservice.service.visit.ITemplateService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.exception.ChangePhaseExecption;
import com.sinohealth.eszservice.service.visit.exception.NoPhaseSelectedExecption;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.TemplIdErrorException;
import com.sinohealth.eszservice.vo.visit.TemplContentVo;
import com.sinohealth.eszservice.vo.visit.TemplContentVo.PhaseVo;

@Service
public class TemplateServiceImpl implements ITemplateService {
	protected final Logger logger = LoggerFactory
			.getLogger(TemplateServiceImpl.class);
	@Autowired
	private ITemplateDao templateDao;

	@Autowired
	private IPhaseService phaseService;

	@Autowired
	private IDailynoteService dailynoteService;

	@Autowired
	private IApplicationService applicationService;

	@Autowired
	private IBodySignService bodysignService;

	@Autowired
	private IDoctorService doctorService;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private IDoctorCountService doctorCountService;

	@Autowired
	private IVisitItemService visitItemService;

	@Override
	public TemplateEntity addTempl(int doctorId, SzSubjectEntity szSubject,
			TemplateEntity tp, Date startDate, String templName,
			DiseaseEntity disease, TemplateEntity stdTemplate)
			throws SystemErrorExecption {
		Assert.hasLength(templName, "templName");

		List<TemplateEntity> list = templateDao.findTemplateByParam(doctorId,
				szSubject.getId(), templName);
		if (null != list && list.size() > 0) {
			throw new SystemErrorExecption("模板名称不能重复", BaseDto.ERRCODE_OTHERS);
		}

		TemplateEntity template = new TemplateEntity();
		template.setDoctorId(doctorId);
		template.setCycleUnit(tp.getCycleUnit());
		template.setSzSubject(szSubject.getId());
		template.setVisible(true);
		template.setDisease(disease.getId());
		template.setStdTemplate(stdTemplate);
		template.setTemplName(templName);

		List<TemplatePhaseEntity> phases = tp.getPhases();
		Calendar calendar = Calendar.getInstance();
		if (null != startDate) {
			calendar.setTime(startDate);
		}

		int cycleLength = 0; // 周期总长
		int cUnit = 2; // 周期单位
		if (null != phases) {
			int size = phases.size();
			for (int i = 0; i < size; i++) {
				TemplatePhaseEntity phase = phases.get(i);

				if (phase.getTimePoint() > cycleLength) {
					cycleLength = phase.getTimePoint();
				}
				template.setCycleLength(cycleLength); // 周期总长
			}
		}

		TemplateEntity saved = templateDao.save(template);

		// ======================
		// 1.体征检测项：
		// ======================
		List<BodySignEntity> bss = new ArrayList<BodySignEntity>();
		if (null != tp.getBodySigns()) {
			for (BodySignEntity be : tp.getBodySigns()) {
				be.setCat(BodySignEntity.BODYSIGN_CAT);
				be.setTemplate(saved);
				bss.add(be);
			}
		}

		// ======================
		// 1.1抗凝指标：(1.4版本)
		// ======================
		if (null != tp.getAnticoags()) {
			for (BodySignEntity anticoag : tp.getAnticoags()) {
				anticoag.setCat(BodySignEntity.ANTICOAG_CAT);
				anticoag.setTemplate(saved);
				bss.add(anticoag);
			}
		}

		if (null != tp.getAnticoags() || null != tp.getBodySigns()) {
			saved.setBodySigns(bss);
		}

		// ======================
		// 2.日常注意事项：
		// ======================
		// 其它注意事项
		// TODO 其它注意事项如果已经存在，不用插入
		Set<DailynoteEntity> dailyNoteList = saved.getDailynotes();
		if (null == dailyNoteList) {
			dailyNoteList = new HashSet<>();
		}
		if (null != tp.getDailynotes()) {
			for (DailynoteEntity de : tp.getDailynotes()) {
				if (dailyNoteList.contains(de)) { // 如果已经存在，则跳过
					dailyNoteList.remove(de);
				} else { // 否则不存在的话，则绑定关系
					DailynoteEntity o = dailynoteService.get(de.getNoteId());
					Set<TemplateEntity> templs = o.getTemplates();
					if (null == templs) {
						templs = new HashSet<>();
					}
					templs.add(saved);
					o.setTemplates(templs);
				}
			}
		}
		String otherNote = tp.getOtherNote();
		if (null != otherNote && !"".equals(otherNote)) {
			DailynoteEntity savedOtherNote = dailynoteService.addNew(otherNote,
					szSubject.getId(), true);
			Set<TemplateEntity> templs = new HashSet<>();
			templs.add(saved);
			savedOtherNote.setTemplates(templs);
		}

		for (DailynoteEntity dailynoteEntity : dailyNoteList) {
			dailynoteEntity.getTemplates().remove(saved);
		}

		// ======================
		// 3.各阶段的数据：
		// ======================
		switch (tp.getCycleUnit()) {
		case 0:
			cUnit = Calendar.DAY_OF_YEAR;
			break;
		case 1:
			cUnit = Calendar.WEEK_OF_YEAR;
			break;
		case 2:
			cUnit = Calendar.MONTH;
			break;
		}
		if (null != phases) {
			for (TemplatePhaseEntity pe : phases) {
				if (null != pe.getItems()) {
					for (Iterator<VisitItemEntity> iterator = pe.getItems()
							.iterator(); iterator.hasNext();) {
						VisitItemEntity e = (VisitItemEntity) iterator.next();
						VisitItemEntity item = visitItemService.get(e
								.getItemId());
						item.getPhases().add(pe);
					}
				}
				calendar.setTime(startDate); // 初始化开始日期
				calendar.add(cUnit, pe.getTimePoint());
				pe.setVisitTime(calendar.getTime());
				pe.setTemplate(saved);
			}
			saved.setPhases(phases);
		}

		// 更新医生的模板总数
		doctorCountService.updateTemplCount(doctorId, szSubject.getId());

		return saved;

	}

	@Override
	public TemplateEntity updateTempl(int doctorId, SzSubjectEntity szSubject,
			TemplateEntity tp, Date startDate, String templName,
			DiseaseEntity disease, TemplateEntity stdTemplate, int templId)
			throws SystemErrorExecption {
		Assert.hasLength(templName, "templName");

		TemplateEntity template = get(templId);
		Assert.notNull(template, "templId");

		List<TemplateEntity> list = templateDao.findTemplateByParam(doctorId,
				szSubject.getId(), templName);
		if (null != list && list.size() > 0) {
			for (TemplateEntity e : list) {
				// 如果有另外一条模板跟当前要更改的模板名称一样,即为重复
				if (!e.getTemplId().equals(template.getTemplId())) {
					throw new SystemErrorExecption("模板名称不能重复",
							BaseDto.ERRCODE_OTHERS);
				}
			}
		}
		template.setDoctorId(doctorId);
		template.setCycleUnit(tp.getCycleUnit());
		template.setSzSubject(szSubject.getId());
		template.setDisease(disease.getId());
		template.setStdTemplate(stdTemplate);
		template.setTemplName(templName);

		templateDao.update(template);

		List<TemplatePhaseEntity> phases = tp.getPhases();

		Calendar calendar = Calendar.getInstance();
		if (null != startDate) {
			calendar.setTime(startDate);
		}

		int cycleLength = 0; // 周期总长
		int cUnit = 2;
		if (null != phases) {
			Collections.sort(phases, new PhaseComparator()); // 重新排序
			cycleLength = phases.get(phases.size() - 1).getTimePoint();
			template.setCycleLength(cycleLength); // 周期总长
		}

		templateDao.update(template);

		// ======================
		// 1.体征检测项：
		// ======================
		List<BodySignEntity> existsBss = template.getBodySigns(); // 已有的项目
		List<BodySignEntity> newBss = new ArrayList<BodySignEntity>(); // 已有的项目
		List<BodySignEntity> bodySigns = tp.getBodySigns();
		for (BodySignEntity e : bodySigns) {
			e.setTemplate(template);
			e.setCat(BodySignEntity.BODYSIGN_CAT);
			int idx = existsBss.indexOf(e);
			if (-1 != idx) { // 如果已经存在
				BodySignEntity o = existsBss.get(idx);
				o.setFreq(e.getFreq());
			} else {
				existsBss.add(e);
			}
			newBss.add(e);
		}

		// ======================
		// 1.1抗凝指标：
		// ======================
		List<BodySignEntity> anticoags = tp.getAnticoags();
		if (null != anticoags) {
			for (BodySignEntity e : anticoags) {
				e.setTemplate(template);
				e.setCat(BodySignEntity.ANTICOAG_CAT);
				int idx = existsBss.indexOf(e);
				if (-1 != idx) { // 如果已经存在
					BodySignEntity o = existsBss.get(idx);
					o.setFreq(e.getFreq());
				} else {
					existsBss.add(e);
				}
				newBss.add(e);
			}
		}
		existsBss.retainAll(newBss); // 保留交集

		// ======================
		// 2.日常注意事项：
		// ======================
		// 其它注意事项
		// TODO 其它注意事项如果已经存在，不用插入
		Set<DailynoteEntity> dailyNoteList = template.getDailynotes();
		if (null == dailyNoteList) {
			dailyNoteList = new HashSet<>();
		}
		if (null != tp.getDailynotes()) {
			for (DailynoteEntity de : tp.getDailynotes()) {
				if (dailyNoteList.contains(de)) { // 如果已经存在，则跳过
					dailyNoteList.remove(de);
				} else { // 否则不存在的话，则绑定关系
					DailynoteEntity o = dailynoteService.get(de.getNoteId());
					Set<TemplateEntity> templs = o.getTemplates();
					if (null == templs) {
						templs = new HashSet<>();
					}
					templs.add(template);
					o.setTemplates(templs);
				}
			}
		}
		String otherNote = tp.getOtherNote();
		if (null != otherNote && !"".equals(otherNote)) {
			DailynoteEntity savedOtherNote = dailynoteService.addNew(otherNote,
					szSubject.getId(), true);
			Set<TemplateEntity> templs = new HashSet<>();
			templs.add(template);
			savedOtherNote.setTemplates(templs);
		}

		for (DailynoteEntity dailynoteEntity : dailyNoteList) {
			dailynoteEntity.getTemplates().remove(template);
		}

		// ======================
		// 3.各阶段的数据：
		// ======================
		switch (tp.getCycleUnit()) {
		case 0:
			cUnit = Calendar.DAY_OF_YEAR;
			break;
		case 1:
			cUnit = Calendar.WEEK_OF_YEAR;
			break;
		case 2:
			cUnit = Calendar.MONTH;
			break;
		}
		if (null != phases) {
			List<TemplatePhaseEntity> allPhases = new ArrayList<>();
			List<TemplatePhaseEntity> oldPhases = template.getPhases(); // 原有的阶段
			if (null == oldPhases) {
				oldPhases = new ArrayList<>();
			}
			for (TemplatePhaseEntity pe : phases) {
				TemplatePhaseEntity phaseEnt = new TemplatePhaseEntity();
				phaseEnt.setTimePoint(pe.getTimePoint());
				phaseEnt.setTemplate(template);

				calendar.setTime(startDate); // 初始化开始日期
				calendar.add(cUnit, pe.getTimePoint());
				/**
				 * 已经存在了此阶段，可编辑则更新，不可编辑则不做改变
				 */
				TemplatePhaseEntity savedPhase;
				int idx = oldPhases.indexOf(phaseEnt);
				if (-1 != idx) { // 如果找到已经有此阶段了，就只是更新阶段的信息
					TemplatePhaseEntity oldPhase = oldPhases.get(idx);
					oldPhase.setVisitTime(calendar.getTime()); // 更新随访时间
					if (oldPhase.getEditable() == 1) { // 如果是处于可编辑状态才可以进行编辑
						oldPhase.setSelected(pe.getSelected()); // 更新选中状态
						oldPhase.setTimePoint(pe.getTimePoint());
						oldPhase.setIsFuzhenItem(pe.getIsFuzhenItem());
						Set<VisitItemEntity> itemList = oldPhase.getItems();
						if (null != itemList) {
							if (null != pe.getItems()) {
								itemList.removeAll(pe.getItems()); // 差集
							}
							for (VisitItemEntity item : itemList) {
								item.getPhases().remove(oldPhase);// 移除项目与阶段的关系
							}
						}
						if (null != pe.getItems()) {
							for (VisitItemEntity e : pe.getItems()) {
								if (null == e.getItemId()) {
									continue;
								}
								VisitItemEntity o = visitItemService.get(e
										.getItemId());
								o.getPhases().add(oldPhase);
							}
						}
						phaseService.update(oldPhase);
					}
					allPhases.add(oldPhase);
					continue;
				} else {
					// 新建阶段
					// 如遇假期，调整计算日期
					savedPhase = new TemplatePhaseEntity();
					savedPhase.setSelected(pe.getSelected());
					savedPhase.setItems(pe.getItems());
					savedPhase.setTimePoint(pe.getTimePoint());
					savedPhase.setVisitTime(calendar.getTime());
					savedPhase.setIsFuzhenItem(pe.getIsFuzhenItem());
					savedPhase.setTemplate(template);
					savedPhase = phaseService.save(savedPhase);
					allPhases.add(savedPhase);
				}
			}
			template.setPhases(allPhases);
		}

		return template;
	}

	@Override
	public TemplateEntity addPlan(int applyId, TemplContentVo templVo,
			int stdTemplId, Date startDate) throws NoPhaseSelectedExecption {
		TemplateEntity stdTempl = get(stdTemplId);

		ApplicationEntity application = applicationService.get(applyId);
		TemplateEntity template = new TemplateEntity();
		template.setDoctorId(application.getDoctor().getId());
		template.setCycleUnit(stdTempl.getCycleUnit());
		template.setSzSubject(stdTempl.getSzSubject());
		template.setDisease(stdTempl.getDisease());
		template.setStdTemplate(stdTempl);
		template = templateDao.save(template);

		// #1977 【病历完整性】刚新增的房颤随访，病历完整性显示为100%
		template.setApplication(application);

		// ======================
		// 1.体征检测项：
		// ======================
		List<BodySignEntity> bodySigns = new ArrayList<BodySignEntity>();
		for (TemplContentVo.BodySignVo bso : templVo.getBodySigns()) {
			BodySignEntity e = new BodySignEntity();
			VisitItemEntity itm = new VisitItemEntity();
			itm.setItemId(bso.getItemId());
			e.setFreq(bso.getFreq());
			e.setItem(itm);
			e.setTemplate(template);
			e.setCat(BodySignEntity.BODYSIGN_CAT);
			bodySigns.add(e);
			bodysignService.addLog(template.getTemplId(), e); // 增加日志
		}
		template.setBodySigns(bodySigns);

		// ======================
		// 1.1：保存抗凝指标 (1.4版本)
		// ======================
		List<BodySignEntity> anticoags = new ArrayList<BodySignEntity>();
		for (TemplContentVo.BodySignVo bso : templVo.getAnticoag()) {
			BodySignEntity e = new BodySignEntity();
			VisitItemEntity itm = new VisitItemEntity();
			itm.setItemId(bso.getItemId());
			e.setFreq(bso.getFreq());
			e.setItem(itm);
			e.setTemplate(template);
			e.setCat(BodySignEntity.ANTICOAG_CAT);
			anticoags.add(e);
			bodysignService.addLog(template.getTemplId(), e); // 增加日志
		}
		template.getBodySigns().addAll(anticoags);

		// ======================
		// 2.日常注意事项：
		// ======================
		Set<DailynoteEntity> newDailyNoteList = new HashSet<>();
		for (Integer noteId : templVo.getDailynote().getNoteIds()) {
			DailynoteEntity de = dailynoteService.get(noteId);
			if (null == de) {
				logger.warn("错误的日常注意事项ID：{}", noteId);
				continue;
			}
			if (null == de.getTemplates()) {
				de.setTemplates(new HashSet<TemplateEntity>());
			}
			de.getTemplates().add(template);
			newDailyNoteList.add(de);
		}
		// 其它注意事项
		String otherNote = templVo.getDailynote().getOtherNote();
		if (StringUtils.hasLength(otherNote)) {
			DailynoteEntity de = new DailynoteEntity();
			de.setContent(otherNote);
			de.setOtherNote(true);
			de.setSzSubject(application.getSzSubject().getId());
			de.setDiseaseId(application.getDisease().getId());
			de = dailynoteService.add(de);
			de.setTemplates(new HashSet<TemplateEntity>());
			de.getTemplates().add(template);
		}

		// ======================
		// 3.各阶段的数据：
		// ======================
		Calendar visitStartCal = Calendar.getInstance();
		visitStartCal.setTime(startDate);
		template.setPhases(new ArrayList<TemplatePhaseEntity>()); // 初始化
		List<PhaseVo> phaseValues = templVo.getVisitPoints().getPhases();
		int prevTimePoint = 0;
		int selectedPhaseFlag = 0; // 选中的阶段的标记
		int cycleLength = 0;
		for (Iterator<TemplatePhaseEntity> iterator = stdTempl.getPhases()
				.iterator(); iterator.hasNext();) {
			TemplatePhaseEntity stdPhase = iterator.next();

			TemplatePhaseEntity phase = new TemplatePhaseEntity();
			phase.setTemplate(template);
			phase.setTimePoint(stdPhase.getTimePoint());

			PhaseVo value = new PhaseVo();
			value.setTimePoint(stdPhase.getTimePoint());

			int vindex = phaseValues.indexOf(value);

			if (-1 != vindex) { // 有提交了数据
				value = phaseValues.get(vindex);
				phase.setSelected(value.getSelected());
				phase.setIsFuzhenItem(value.getIsFuzhenItem());
			} else {
				phase.setSelected(0); // 默认选中为0
			}

			// 修正时间
			if (phase.getSelected() == 1) { // 如果是有选中
				selectedPhaseFlag++;
				Calendar visitTime = holidayService.getVisitDate(visitStartCal,
						template.getCycleUnit(), prevTimePoint,
						phase.getTimePoint());
				phase.setVisitTime(visitTime.getTime());

				/**
				 * 2015-09-09：<br/>
				 * 1.保持初衷，需求不变，随访阶段只能增加不能减少。<br/>
				 * 医生只要第一次设置随访模版成功发送患者后，不能再取消随访阶段。但可以增加随访时间合理的随访阶段。<br/>
				 * 医生取消时提示语：非常抱歉，执行中的随访计划不能取消，如有需要请联系客服：010-85898384。
				 */
				if (phase.getSelected() == 1) {
					phase.setSelectable(0);
				}
			} else {
				Calendar visitTime = holidayService.getVisitDate(visitStartCal,
						template.getCycleUnit(), prevTimePoint,
						phase.getTimePoint(), false);
				phase.setVisitTime(visitTime.getTime());
			}

			if (phase.getSelected() == 1) {
				HashSet<Integer> itemCount = new HashSet<Integer>(); // 计算有多少应填项
				for (Integer itemId : value.getItemIds()) {
					VisitItemEntity item = visitItemService.get(itemId);
					if (null == item) { // 错误的itemId
						logger.warn("错误的itemId：{}", itemId);
						continue;
					}
					if (null == item.getPhases()) {
						item.setPhases(new HashSet<TemplatePhaseEntity>());
					}
					item.getPhases().add(phase);
					itemCount.add(itemId);
				}
				phase.setItemCount(itemCount.size() + 1); // 项目数+处方
				cycleLength = phase.getTimePoint();
			}

			template.getPhases().add(phase);

			// 处理完一个阶段后，准备下一阶段的数据
			prevTimePoint = phase.getTimePoint();
			visitStartCal.setTime(phase.getVisitTime());
		}
		template.setCycleLength(cycleLength);

		if (selectedPhaseFlag < 1) {
			throw new NoPhaseSelectedExecption();
		}

		return template;
	}

	@Override
	public void updatePlan(int applyId, TemplContentVo templVo,
			Date visitStartDate) throws ChangePhaseExecption {
		logger.debug("修改随访计划applyId:{}", applyId);

		ApplicationEntity application = applicationService.get(applyId);
		TemplateEntity template = application.getTemplate();

		Calendar yesterdayCal = Calendar.getInstance();
		yesterdayCal.add(Calendar.DAY_OF_YEAR, -1); // 昨天

		// 随访开始时间不能
		Date startDate = application.getVisitStartDate();

		// ======================
		// 1.体征检测项：
		// ======================
		List<BodySignEntity> existsBss = template.getBodySigns(); // 已有的项目
		List<BodySignEntity> newBss = new ArrayList<BodySignEntity>(); // 已有的项目
		for (TemplContentVo.BodySignVo bso : templVo.getBodySigns()) {
			BodySignEntity e = new BodySignEntity();
			VisitItemEntity itm = new VisitItemEntity();
			itm.setItemId(bso.getItemId());
			e.setItem(itm);
			e.setTemplate(template);

			int idx = existsBss.indexOf(e);
			if (-1 != idx) {
				BodySignEntity o = existsBss.get(idx);
				o.setFreq(bso.getFreq());
				newBss.add(o);
			} else {
				e.setFreq(bso.getFreq());
				e.setCat(BodySignEntity.BODYSIGN_CAT);
				newBss.add(e);
				existsBss.add(e);
			}
		}

		// ======================
		// 1.1：保存抗凝指标 (1.4版本)
		// ======================
		for (TemplContentVo.BodySignVo bso : templVo.getAnticoag()) {
			BodySignEntity e = new BodySignEntity();
			VisitItemEntity itm = new VisitItemEntity();
			itm.setItemId(bso.getItemId());
			e.setFreq(bso.getFreq());
			e.setItem(itm);
			e.setTemplate(template);

			int idx = existsBss.indexOf(e);
			if (-1 != idx) {
				BodySignEntity o = existsBss.get(idx);
				o.setFreq(bso.getFreq());
				newBss.add(o);
			} else {
				e.setFreq(bso.getFreq());
				e.setCat(BodySignEntity.ANTICOAG_CAT);
				newBss.add(e);
				existsBss.add(e);
			}
		}
		List<BodySignEntity> removeList = new ArrayList<BodySignEntity>(
				existsBss);
		existsBss.retainAll(newBss); // 保留交集
		removeList.removeAll(newBss); // 计算差集-已删除项
		for (BodySignEntity e : removeList) {
			bodysignService.updateLogToFinished(template.getTemplId(), e
					.getItem().getItemId(), yesterdayCal.getTime());
		}
		for (BodySignEntity e : existsBss) {
			bodysignService.updateLog(template.getTemplId(), e);
		}

		template.setBodySigns(existsBss);

		// ======================
		// 2.日常注意事项：
		// ======================
		// 其它注意事项如果已经存在，不用插入
		Set<DailynoteEntity> dailyNoteList = template.getDailynotes();
		if (null == dailyNoteList) {
			dailyNoteList = new HashSet<>();
		}
		Set<DailynoteEntity> newDailyNoteList = new HashSet<>();
		for (Integer noteId : templVo.getDailynote().getNoteIds()) {
			DailynoteEntity de = dailynoteService.get(noteId);
			if (null == de) {
				logger.warn("错误的日常注意事项ID：{}", noteId);
				continue;
			}
			if (null == de.getTemplates()) {
				de.setTemplates(new HashSet<TemplateEntity>());
			}
			de.getTemplates().add(template);
			if (!dailyNoteList.contains(de)) {
				dailyNoteList.add(de);
			}
			newDailyNoteList.add(de);
		}
		// 其它“注意事项”如果已经存在，不用插入
		String otherNote = templVo.getDailynote().getOtherNote();
		if (StringUtils.hasLength(otherNote)) {
			boolean hasOtherNoteFlag = false;
			for (DailynoteEntity e : dailyNoteList) {
				if (e.isOtherNote() && otherNote.equals(e.getContent())) { // 查找到已经存在的“其它注意事项”
					newDailyNoteList.add(e);
					hasOtherNoteFlag = true;
					break;
				}
			}
			if (!hasOtherNoteFlag) {
				DailynoteEntity de = new DailynoteEntity();
				de.setContent(otherNote);
				de.setOtherNote(true);
				de.setSzSubject(application.getSzSubject().getId());
				de.setDiseaseId(application.getDisease().getId());
				de = dailynoteService.add(de);
				de.setTemplates(new HashSet<TemplateEntity>());
				de.getTemplates().add(template);
			}
		}

		// 计算差集，删掉没提交的
		boolean changed = dailyNoteList.removeAll(newDailyNoteList);
		if (changed) {
			for (DailynoteEntity e : dailyNoteList) {
				e.getTemplates().remove(template);
				if (e.isOtherNote()) {
					dailynoteService.delete(e);
				}
			}
		}

		// ======================
		// 3.各阶段的数据：
		// ======================
		Calendar calendar = Calendar.getInstance();
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1); // 加1天
		tomorrow.set(Calendar.HOUR_OF_DAY, 0); // 将时分秒设置为0
		tomorrow.set(Calendar.MINUTE, 0);
		tomorrow.set(Calendar.SECOND, 0);
		tomorrow.set(Calendar.MILLISECOND, 0);

		if (null != startDate) {
			calendar.setTime(startDate);
		}

		int cycleLength = 0; // 周期总长

		List<PhaseVo> phaseValueList = templVo.getVisitPoints().getPhases(); // 提交的阶段

		// 假定数据是正确的，所以就不用找标准模板了
		int prevTimePoint = 0;
		boolean hasNextPhase = false;
		for (TemplatePhaseEntity oldPhase : template.getPhases()) {
			PhaseVo pv = new PhaseVo();
			pv.setTimePoint(oldPhase.getTimePoint());

			int oIdx = phaseValueList.indexOf(pv);

			Calendar visitTime = Calendar.getInstance();
			if (-1 == oIdx) { // 没提交提交值
				prevTimePoint = oldPhase.getTimePoint();
				visitTime.setTime(oldPhase.getVisitTime());
				continue;
			}

			PhaseVo value = phaseValueList.get(oIdx);

			/**
			 * ================== selected 状态改变了 ==================
			 */
			if (value.getSelected() != oldPhase.getSelected()) { // 改变阶段变化状态
				if (oldPhase.getSelectable() == 0) { // 不能变更的状态
					throw new ChangePhaseExecption(
							oldPhase.getTimePoint(),
							template.getCycleUnit(),
							String.format(
									"医生取消时提示语：非常抱歉，执行中的随访计划[第%s%s]不能取消，如有需要请联系客服：010-85898384。",
									oldPhase.getTimePoint(),
									getUnitText(template.getCycleUnit())));
				}

				/** 从【未选择】，变成【已选择】 */
				if (oldPhase.getSelected() == 0) {
					visitTime = holidayService.getVisitDate(calendar,
							template.getCycleUnit(), prevTimePoint,
							oldPhase.getTimePoint());
					if (visitTime.compareTo(tomorrow) < 0) { // 早于明天
						logger.error("阶段selectable参数应该为0，phaseId:{}",
								oldPhase.getTemplPhaseId());
						throw new ChangePhaseExecption(oldPhase.getTimePoint(),
								template.getCycleUnit(), String.format(
										"第%s%s的随访开始时间[%s]将早于明天，不能选择", oldPhase
												.getTimePoint(),
										getUnitText(template.getCycleUnit()),
										DateUtils.formatDate(visitTime
												.getTime())));
					}
					/** 从【已选择】，变成【未选择】 */
				} else {

					if (oldPhase.getFuZhenStatus() > -1) { // 如果已经提交过复诊，不能更改阶段选中状态
						throw new ChangePhaseExecption(oldPhase.getTimePoint(),
								template.getCycleUnit(), String.format(
										"第%s%s患者已经提交复诊，不能删除阶段",
										oldPhase.getTimePoint(),
										getUnitText(template.getCycleUnit())));
					}

					visitTime = holidayService.getVisitDate(calendar,
							template.getCycleUnit(), prevTimePoint,
							oldPhase.getTimePoint(), true); // 随访开始日期不跳过假期
				}

				oldPhase.setIsFuzhenItem(value.getIsFuzhenItem());
				oldPhase.setVisitTime(visitTime.getTime()); // 修正时间
				oldPhase.setSelected(value.getSelected());
				prevTimePoint = oldPhase.getTimePoint();
			}

			/**
			 * 2015-09-09：<br/>
			 * 1.保持初衷，需求不变，随访阶段只能增加不能减少。<br/>
			 * 医生只要第一次设置随访模版成功发送患者后，不能再取消随访阶段。但可以增加随访时间合理的随访阶段。<br/>
			 * 医生取消时提示语：非常抱歉，执行中的随访计划不能取消，如有需要请联系客服：010-85898384。
			 */
			if (oldPhase.getSelected() == 1) {
				oldPhase.setSelectable(0);
			}

			/**
			 * ================== selected 状态改变了 END ==================
			 */

			/**
			 * 处理关联的item项目： 1、先将新增的加入到新增表； 2、计算差集，将没有提交的选项删除；
			 */
			Set<VisitItemEntity> existsItems = oldPhase.getItems();
			if (null == existsItems) {
				existsItems = new HashSet<VisitItemEntity>();
			}
			Set<VisitItemEntity> newItemList = new HashSet<>();
			for (Integer itemId : value.getItemIds()) {
				VisitItemEntity item = visitItemService.get(itemId);
				if (null == item) {
					logger.warn("错误的itemId：{}", itemId);
					continue;
				}
				if (null == item.getPhases()) {
					item.setPhases(new HashSet<TemplatePhaseEntity>());
				}
				item.getPhases().add(oldPhase);
				existsItems.add(item);
				newItemList.add(item);
			}
			// 2、计算差集，将没有提交的选项删除；
			existsItems.removeAll(newItemList);
			for (Iterator<VisitItemEntity> iterator = existsItems.iterator(); iterator
					.hasNext();) {
				VisitItemEntity item = iterator.next();
				item.getPhases().remove(oldPhase);
			}

			oldPhase.setItemCount(newItemList.size() + 1); // 应填项目总数（+1处方），用于统计提交率

			if (oldPhase.getSelected() == 1) {
				cycleLength = oldPhase.getTimePoint(); // 也许总周长就是它

				if (oldPhase.getFuZhenStatus() == VisitStatus.FUZHEN_TIME_NOT_YET) {
					hasNextPhase = true;
				}
			}
		}

		/*
		 * 阶段可以取消选中（使selected=0），但是至少要保留一个处于选中状态（selected=1）。
		 * 阶段变更后，随访处于当前阶段为【未到时间不需要复诊】状态的第1个阶段
		 */
		if (!hasNextPhase) {
			throw new ChangePhaseExecption(0, template.getCycleUnit(),
					"请选择下一个随访阶段");
		}

		template.setCycleLength(cycleLength);

		templateDao.update(template);
	}

	@Override
	public TemplateEntity get(int id) {
		return templateDao.get(id);
	}

	@Override
	public List<TemplateEntity> getTemplates(String szSubject, int doctorId) {
		Parameter params = new Parameter(szSubject, doctorId);
		String hql = "FROM TemplateEntity WHERE szSubject=:p1 and visible=1 and doctorId = :p2";
		List<TemplateEntity> list = templateDao.findByHql(hql, params);
		return list;
	}

	@Override
	public void deleteTempl(String templIds) throws TemplIdErrorException {
		List<String> idList;
		if (!"".equals(templIds)) {
			idList = Arrays.asList(templIds.split(","));
		} else {
			throw new TemplIdErrorException(
					ConstantDoctorVisitErrs.TEMPL_DELETE_ID_ERROR, "请输入正确的模板ID");
		}
		boolean flag = false;
		String szSubjectId = null;
		int doctorId = 0;
		for (int i = 0; i < idList.size(); i++) {
			String hql = "from TemplateEntity where templId =:templId and doctorId >:doctorId and visible=1";
			Parameter params = new Parameter();
			params.put("templId", Integer.valueOf(idList.get(i)));
			params.put("doctorId", 0);
			TemplateEntity template = templateDao.getByHql(hql, params);
			doctorId = template.getDoctorId();
			if (null != template) {
				if (null == szSubjectId) {
					szSubjectId = template.getSzSubject();
				}
				flag = true;
				templateDao.delete(template);
			}
			if (!flag) {
				throw new TemplIdErrorException(
						ConstantDoctorVisitErrs.TEMPL_DELETE_ID_ERROR,
						"输入错误的模板ID");
			}
		}
		// 更新医生的模板总数
		if ((doctorId != 0) && (null != szSubjectId)) {
			doctorCountService.updateTemplCount(doctorId, szSubjectId);
		}
	}

	@Override
	public BaseDto getTimeline(int applyId) {

		ApplicationEntity application = applicationService.get(applyId);
		TimelineDto dto = new TimelineDto();
		try {
			if (null == application) {
				throw new SystemErrorExecption("applyId不存在",
						BaseDto.ERRCODE_OTHERS);
			}
			// 计划模板
			TemplateEntity template = application.getTemplate();

			dto.setApplication(application);

			dto.setTemplate(template);

			if (null != template) {
				List<TemplatePhaseEntity> phases = phaseService.getListByTempl(
						template.getTemplId(), 1);
				dto.setPhases(phases);
			}

		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public List<TemplateEntity> getList(String szSubject, int doctorId,
			boolean visible) {
		Parameter params = new Parameter(szSubject, doctorId, visible);
		String hql = "FROM TemplateEntity WHERE szSubject=:p1 and visible=:p3 and doctorId = :p2";
		List<TemplateEntity> list = templateDao.findByHql(hql, params);
		return list;
	}

	public String getUnitText(int unit) {
		switch (unit) {
		case 0:
			return "日";
		case 1:
			return "周";
		case 2:
			return "月";
		}
		return null;
	}

	@Override
	public void update(TemplateEntity template) {
		templateDao.update(template);
	}
}
