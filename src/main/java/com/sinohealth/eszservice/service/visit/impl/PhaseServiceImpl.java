package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.VisitItemId;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.pojo.PhaseInspectionPojo;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IPhaseDao;
import com.sinohealth.eszservice.service.base.exception.PermissionException;
import com.sinohealth.eszservice.service.visit.ICheckItemValueService;
import com.sinohealth.eszservice.service.visit.IPhaseService;
import com.sinohealth.eszservice.service.visit.ITemplateService;
import com.sinohealth.eszservice.service.visit.IVisitImgValueService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.IVisitPrescriptionService;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;
import com.sinohealth.eszservice.service.visit.exception.VisitItemNotFoundExecption;

@Service
public class PhaseServiceImpl implements IPhaseService {

	@Autowired
	IPhaseDao phaseDao;

	@Autowired
	ITemplateService templateService;

	@Autowired
	IVisitItemService visitItemService;

	@Autowired
	IVisitPrescriptionService visitPrescriptionService;

	@Autowired
	ICheckItemValueService checkItemValueService;

	@Autowired
	IVisitImgValueService visitImgValueService;

	@Override
	public List<TemplatePhaseEntity> getPhases(int templId) {
		Parameter params = new Parameter(templId);

		List<TemplatePhaseEntity> list = phaseDao.findByHql(
				"from TemplatePhaseEntity where template.templId=:p1", params);

		return list;
	}

	@Override
	public TemplatePhaseEntity addNew(int templId, int timePoint, Date visitTime) {
		TemplateEntity template = new TemplateEntity();
		template.setTemplId(templId);
		TemplatePhaseEntity ent = new TemplatePhaseEntity();
		ent.setTemplate(template);
		ent.setTimePoint(timePoint);
		ent.setVisitTime(visitTime);
		return phaseDao.save(ent);
	}

	@Override
	public TemplatePhaseEntity addNew(int templId, int timePoint,
			Date visitTime, int[] itemIds, int selected, int isFuzhenItem) {
		TemplateEntity template = new TemplateEntity();
		template.setTemplId(templId);
		TemplatePhaseEntity ent = new TemplatePhaseEntity();
		ent.setTemplate(template);
		ent.setTimePoint(timePoint);
		ent.setVisitTime(visitTime);
		ent.setSelected(selected);// v103版本 当前周期是否被选中
		ent.setIsFuzhenItem(isFuzhenItem);// v103版本按医嘱复诊选项
		int itemCount = 0; // 阶段项目总数
		if (itemIds != null && itemIds.length > 0) {
			Set<VisitItemEntity> itms = new HashSet<>();
			for (int j = 0; j < itemIds.length; j++) {
				VisitItemEntity ite = visitItemService.get(itemIds[j]);
				if (null == ite) {
					continue;
				}
				itemCount++;
				itms.add(ite);
			}
			ent.setItems(itms);
		}
		itemCount++; // 处方固定加1
		ent.setItemCount(itemCount);

		TemplatePhaseEntity saved = phaseDao.save(ent);

		return saved;
	}

	/**
	 * 
	 * @throws org.hibernate.ObjectNotFoundException
	 *             数据格式有误，如相应的ItemId不存在
	 */
	@Override
	public TemplatePhaseEntity updateItems(int phaseId, int[] itemIds) {
		TemplatePhaseEntity phase = get(phaseId);
		Set<VisitItemEntity> items = phase.getItems();

		if (null != items) {
			items.clear();
		} else {
			items = new HashSet<>();
		}
		for (int i = 0; i < itemIds.length; i++) {
			int id = itemIds[i];
			if (0 != id) {
				VisitItemEntity item = new VisitItemEntity();
				item.setItemId(id);
				items.add(item);
			}
		}
		phase.setItems(items);
		return phaseDao.save(phase);
	}

	@Override
	public TemplatePhaseEntity get(int id) {
		return phaseDao.get(id);
	}

	/**
	 * 更新复诊项处理：每一次提交的是完整的数据，如果录入的处方数据、检验项数据中，没有原来的数据，则视为删除。<br/>
	 * 更新【申请单】中当前随访报告、随访数据的状态，通过AOP实现，见：
	 * {@link com.sinohealth.eszorm.eszservice.aspect.visit.ExeAspect.updateValuesAspectAround}
	 * <br/>
	 * TODO 要写单元测试
	 * 
	 * @param phaseId
	 * @param medicinesData
	 * @param checkItemsData
	 * @param checkPicsData
	 * @return
	 * @throws VisitItemNotFoundExecption
	 * @throws ValueOutOfRangeException
	 * @throws SystemErrorExecption
	 */
	@Override
	public TemplatePhaseEntity updateValues(int phaseId,
			List<VisitPrescriptionEntity> medicinesData,
			PhaseInspectionPojo inspection, List<VisitImgEntity> checks,
			int isFuzhenValue, List<CheckItemValueEntity> checkValueList)
			throws VisitItemNotFoundExecption, ValueOutOfRangeException,
			SystemErrorExecption {
		TemplatePhaseEntity phase;
		SickEntity sick;
		DoctorEntity doctor;
		try {
			phase = get(phaseId);
			sick = phase.getTemplate().getApplication().getSick();
			doctor = phase.getTemplate().getApplication().getDoctor();
		} catch (NullPointerException e) { // 错误的阶段ID
			throw new PermissionException();
		}

		// 执行时间
		Calendar cal = Calendar.getInstance();
		// 用于统计已经上传多少项
		// Set<Integer> submitCount = new HashSet<>();

		// ======================
		// 处方的处理
		// ======================
		// 1. 如果上传的内容包括imgId，则：
		// 1.1 在数据库中查找到包含此imgId的，并且img,thumb字段内容相同，则认为是不作改动。
		// 1.2 在数据库中查找到包含此imgId，但是img,thumb字段内容却不相同，则认为是对原imgId的处方进行了改动。
		// 此时，需要将此imgId的处方标记为重传（reuploaded=1），并将img,thumb插入新的记录。
		// 1.3 如果在数据库找不到imgId，忽略处理；
		// 2. 如果在上传的内容不包括imgId，则插入新的记录
		// Set<VisitPrescriptionEntity> pres = visitPrescriptionService
		// .updateValues(phaseId, medicinesData);
		List<VisitPrescriptionEntity> origPresList = phase.getPrescription()
				.getPics(); // 原有的处方图片
		List<VisitPrescriptionEntity> newPresList = new ArrayList<>(); // 新的处方列表
		for (VisitPrescriptionEntity e : medicinesData) {
			if (!(StringUtils.hasLength(e.getMedPic()) && StringUtils
					.hasLength(e.getSmallMedPic()))) {
				continue;
			}

			int idx = origPresList.indexOf(e);
			if ((e.getId() != null) && (-1 != idx)) { // 如果提交了imgId，并且是数据库已经有的
				VisitPrescriptionEntity origImg = origPresList.get(idx);
				if (!StringUtils.startsWithIgnoreCase(e.getMedPic(), "https")) { // 如果不是https开头
					if (!origImg.getMedPic().equals(e.getMedPic())) { // 图片变化，说明重新上传了
						// 状态改为已重新上传，并重新上传一张
						origImg.setReuploaded(1);
						origImg.setReuploadDate(cal.getTime());
						VisitPrescriptionEntity newImg = new VisitPrescriptionEntity();
						newImg.setMedPic(e.getMedPic());
						newImg.setSmallMedPic(e.getSmallMedPic());
						newImg.setSick(sick);
						newImg.setPhase(phase);
						newImg.setDoctor(doctor);
						newImg.setCreateDate(cal.getTime());

						newPresList.add(newImg);
						origPresList.add(newImg);
					}
				}
				newPresList.add(origImg);
			} else { // 数据库中不存在
				e.setId(null);
				e.setSick(sick);
				e.setPhase(phase);
				e.setDoctor(doctor);
				e.setCreateDate(cal.getTime());
				e.setDoctorMarkRemarks(null);// 过滤数据
				e.setDoctorMarkStatus(0); // 过滤数据
				e.setDoctorMarkTime(null); // 过滤数据
				e.setReuploaded(0); // 过滤数据
				e.setReuploadDate(null); // 过滤数据
				newPresList.add(e);
				origPresList.add(e);
			}
		}

		for (VisitPrescriptionEntity e : origPresList) {
			if ((e.getId() != null) && (!newPresList.contains(e))) { // 如果不在要保存的列表中，则设置为重新上传
				e.setReuploaded(1);
				e.setReuploadDate(cal.getTime());
			}
		}

		phase.getPrescription().setPics(origPresList);

		// ======================
		// 检验项处理
		// ======================
		// 更新【申请单】中当前随访报告、随访数据的状态，通过AOP实现
		// @see{void
		// com.sinohealth.eszservice.aspect.visit.ExeAspect.updateValuesAspectAround}
		// Set<CheckItemValueEntity> itemValues = checkItemValueService
		// .updateValues(phaseId, inspection.getValues());
		List<VisitImgEntity> newPicList = new ArrayList<>(); // 新的检验项图片列表
		for (VisitImgEntity e : inspection.getPics()) {
			int idx = phase.getInspection().getPics().indexOf(e);
			if ((e.getImgId() != null) && (-1 != idx)) { // 如果是已经存在检验项图片
				// 2015.9月新增的功能需要，因为七牛图片由http改为https，所以如果提交的URL是https开头，不更改数据
				VisitImgEntity orgImg = phase.getInspection().getPics()
						.get(idx);
				if (!e.getImg().startsWith("https")) { // 如果是https开头，保存原图
					// TODO 如果图片更改了URL，不覆盖原有图片，应该做修改标记
					if (!orgImg.getImg().equals(e.getImg())) { // 如果图片更改了URL
						orgImg.setImg(e.getImg());
						orgImg.setThumb(e.getThumb());
						e.setPostTime(cal.getTime());
					}
				}
				newPicList.add(orgImg); // 加入到新的列表
			} else { // 数据库还没有检验项图片
				e.setImgId(null);
				e.setSick(sick);
				e.setPhase(phase);
				e.setCat(VisitItemCat.CAT_INSPECTION);
				e.setPostTime(cal.getTime());
				e.setMarkTime(null); // 过滤属性
				e.setRemarks(null); // 过滤属性
				e.setStatus(0); // 过滤属性
				newPicList.add(e); // 加入到新的列表
				phase.getInspection().getPics().add(e);
			}
		}
		phase.getInspection().getPics().retainAll(newPicList); // 取交集

		// 已经在在的结果值
		Set<CheckItemValueEntity> existsValues = phase.getInspection()
				.getValues();
		List<CheckItemValueEntity> existsValueList = new ArrayList<>(
				existsValues);

		if (inspection.getValues().size() == 0) {
			existsValueList.clear();
		}

		// TODO 检验是否为当前阶段的检验项，如果不是则不插入/更新
		// TODO 更新当期复诊的报告状态
		int reportStatus = 0; // 此阶段的数据值的最大告警级别

		for (Iterator<CheckItemValueEntity> iterator = inspection.getValues()
				.iterator(); iterator.hasNext();) {
			CheckItemValueEntity value = (CheckItemValueEntity) iterator.next();

			value.setCat(VisitItemCat.CAT_INSPECTION);
			value.setPhase(phase);
			value.setSick(sick);
			value.setReportTime(cal.getTime());

			// 如果值已经存在，只需要更新
			int idx = existsValueList.indexOf(value);
			if (-1 != idx) {
				if (StringUtils.isEmpty(value.getReportValue())) {// 没有输入值，删除结果值
					existsValueList.remove(idx);
					continue;
				}
				int warnLevel = 0;
				warnLevel = visitItemService.getWarnLevel(value.getVisitItem()
						.getItemId(), value.getReportValue(), sick.getSex());
				value.setReportWarnLevel(warnLevel);
				// System.out.println("update warnLevel:"+warnLevel);
				// itemList.add(oldObj);
				// update(oldObj); // 更新
				// 从当前需要删除的旧值队列中移除
				// existsValues.remove(idx);
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}
				CheckItemValueEntity oldVal = existsValueList.get(idx);
				oldVal.setReportTime(cal.getTime());
				oldVal.setReportWarnLevel(warnLevel);
				oldVal.setReportValue(value.getReportValue());
				// 如果数据库中还不存在值
			} else {
				// 检测值的告警范围
				if (StringUtils.isEmpty(value.getReportValue())) {
					continue;
				}
				int warnLevel = 0;
				warnLevel = visitItemService.getWarnLevel(value.getVisitItem()
						.getItemId(), value.getReportValue(), sick.getSex());

				value.setReportWarnLevel(warnLevel); // 设置此值的告警级别
				value.setSick(sick);
				value.setResultId(null);
				existsValueList.add(value);
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}
				// System.out.println("add warnLevel:"+warnLevel);
			}
			if ((null != value.getReportValue())
					&& (!"".equals(value.getReportValue()))) {
			}
		}

		phase.getInspection().getValues().clear();
		phase.getInspection().getValues()
				.addAll(new HashSet<CheckItemValueEntity>(existsValueList));

		// ======================
		// 检查项数值/文本结果
		// ======================
		// 已经在在的结果值
		Set<CheckItemValueEntity> existsCheckValues = phase.getCheckValues();
		List<CheckItemValueEntity> existsCheckValueList = new ArrayList<>(
				existsCheckValues);
		for (Iterator<CheckItemValueEntity> iterator = checkValueList
				.iterator(); iterator.hasNext();) {
			CheckItemValueEntity value = (CheckItemValueEntity) iterator.next();

			value.setCat(VisitItemCat.CAT_EXAMINE);
			value.setPhase(phase);
			value.setSick(sick);
			value.setReportTime(cal.getTime());

			// 如果值已经存在，只需要更新
			int idx = existsCheckValueList.indexOf(value);
			if (-1 != idx) {
				if (StringUtils.isEmpty(value.getReportValue())) {// 没有输入值，删除结果值
					existsCheckValueList.remove(idx);
					continue;
				}
				int warnLevel = 0;
				warnLevel = visitItemService.getWarnLevel(value.getVisitItem()
						.getItemId(), value.getReportValue(), sick.getSex());
				value.setReportWarnLevel(warnLevel);
				// System.out.println("update warnLevel:"+warnLevel);
				// itemList.add(oldObj);
				// update(oldObj); // 更新
				// 从当前需要删除的旧值队列中移除
				// existsValues.remove(idx);
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}
				CheckItemValueEntity oldVal = existsCheckValueList.get(idx);
				oldVal.setReportTime(cal.getTime());
				oldVal.setReportWarnLevel(warnLevel);
				oldVal.setReportValue(value.getReportValue());
				// 如果数据库中还不存在值
			} else {
				// 检测值的告警范围
				if (StringUtils.isEmpty(value.getReportValue())) {
					continue;
				}
				int warnLevel = 0;
				warnLevel = visitItemService.getWarnLevel(value.getVisitItem()
						.getItemId(), value.getReportValue(), sick.getSex());

				value.setReportWarnLevel(warnLevel); // 设置此值的告警级别
				value.setSick(sick);
				value.setResultId(null);
				existsCheckValueList.add(value);
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}
				// System.out.println("add warnLevel:"+warnLevel);
			}
			if ((null != value.getReportValue())
					&& (!"".equals(value.getReportValue()))) {
			}
		}

		phase.getCheckValues().clear();
		phase.getCheckValues().addAll(
				new HashSet<CheckItemValueEntity>(existsCheckValueList));

		// ======================
		// 结束：检查项数值/文本结果
		// ======================

		// 设置此阶段的告警级别
		phase.setReportStatus(reportStatus);

		// ======================
		// 检查项处理
		// ======================
		// Set<VisitImgEntity> imgSubmitd = visitImgValueService
		// .updateValuesForPhase(phaseId, checks);
		// phase.setImgs(imgSubmitd);

		List<VisitImgEntity> newCheckList = new ArrayList<>(); // 新的检查项图片列表
		for (VisitImgEntity e : checks) {
			if ((e.getImgId() != null) && phase.getChecks().contains(e)) { // 如果是已经存在检验项图片
				int idx = phase.getChecks().indexOf(e);
				VisitImgEntity orgImg = phase.getChecks().get(idx);
				// 2015.9月新增的功能需要，因为七牛图片由http改为https，所以如果提交的URL是https开头，不更改数据
				if (!e.getImg().startsWith("https")) { // 如果不是https开头，保存原图
					// TODO 如果图片更改了URL，不覆盖原有图片，应该做修改标记
					if (!orgImg.getImg().equals(e.getImg())) { // 如果图片更改了URL
						orgImg.setImg(e.getImg());
						orgImg.setThumb(e.getThumb());
						orgImg.setPostTime(cal.getTime());
					}
				}
				newCheckList.add(orgImg); // 加入到新的列表
			} else { // 数据库还没有检验项图片
				VisitImgEntity newImg = new VisitImgEntity();
				newImg.setImg(e.getImg());
				newImg.setThumb(e.getThumb());
				newImg.setSick(sick);
				newImg.setPhase(phase);
				newImg.setItemId(e.getItemId());
				newImg.setCat(VisitItemCat.CAT_EXAMINE);
				newImg.setPostTime(cal.getTime());
				phase.getChecks().add(newImg);
				newCheckList.add(newImg); // 加入到新的列表
			}
		}
		phase.getChecks().retainAll(newCheckList); // 取交集，包括imgId==null的项

		phase.setFuZhenStatus(VisitStatus.FUZHEN_COMPLETED); // 复诊状态设置为已复诊

		// 如果是首次提交，阶段还是可编辑的。这一次提交后，变成不可编辑，并且开放评价
		if (phase.getEditable() != 0) {
			phase.setCommentable(1);
			phase.setEditable(0);
		}

		// 已提交项目总数
		int submitCount = countSubmitted(phase);
		phase.setSubmittedCount(submitCount);

		if (phase.getItemCount() != 0) {
			double submittedRate = (double) phase.getSubmittedCount()
					/ (double) phase.getItemCount();
			phase.setSubmittedRate((int) Math.rint(submittedRate * 100));// 取四舍五入
		} else {
			System.err.println("应填项总数为0，phaseId:" + phase.getTemplPhaseId());
		}

		// updateCommittedCount(saved.getTemplPhaseId());
		phase.setIsFuzhenValue(isFuzhenValue);
		phaseDao.update(phase);

		return phase;
	}

	/**
	 * 更新阶段的提交率
	 * 
	 * @param phaseId
	 * @return
	 */
	@Override
	public void updateCommittedCount(int phaseId) {
		TemplatePhaseEntity phase = get(phaseId);
		int submittedCount = countSubmitted(phase);
		phase.setSubmittedCount(submittedCount); // 已提交的项目的总数
		int submittedRate = phase.getItemCount() > 0 ? (100 * submittedCount / phase
				.getItemCount()) : 0;
		phase.setSubmittedRate(submittedRate); // 提交的完整性比率
		phaseDao.save(phase);
	}

	/**
	 * 统计阶段提交的项目结果
	 * 
	 * @param phaseId
	 * @return
	 */
	public int countSubmitted(TemplatePhaseEntity phase) {
		// 用于统计已经上传多少项，key表示itemId，value:0-表示有错误的数据，1-表示数据正常
		Map<Integer, Integer> submitCountItems = new HashMap<>();
		// 处方
		for (VisitPrescriptionEntity e : phase.getPrescription().getPics()) {
			if (e.getDoctorMarkStatus() == 1) {
				submitCountItems.put(VisitItemId.PRESCRIPTION_ID, 0);
				break; // 跳出
			} else {
				submitCountItems.put(VisitItemId.PRESCRIPTION_ID, 1);
			}
		}

		// 检验项1
		for (CheckItemValueEntity e : phase.getInspection().getValues()) {
			submitCountItems.put(e.getVisitItem().getItemId(), 1); // 用于统计有多少检验项目填写了内容
		}

		// 检验项2
		for (VisitImgEntity e : phase.getInspection().getPics()) {
			int itemId = e.getItemId();
			Integer stat = submitCountItems.get(itemId);
			if ((null != stat) && (stat == 0)) { // 已有错误的图片，跳过
				continue;
			}
			int curStat = e.getStatus() == 0 ? 1 : 0; // 是否无错误
			submitCountItems.put(itemId, curStat); // 用于统计有多少检验项目填写了内容
		}

		// 检查项1
		for (CheckItemValueEntity e : phase.getCheckValues()) {
			submitCountItems.put(e.getVisitItem().getItemId(), 1); // 用于统计有多少检验项目填写了内容
		}

		// 检查项2
		for (VisitImgEntity e : phase.getChecks()) {
			int itemId = e.getItemId();
			Integer stat = submitCountItems.get(itemId);
			if ((null != stat) && (stat == 0)) { // 已有错误的图片，跳过
				continue;
			}
			int curStat = e.getStatus() == 0 ? 1 : 0; // 是否无错误
			submitCountItems.put(itemId, curStat); // 用于统计有多少检验项目填写了内容
		}

		int submitCount = 0;

		Set<Integer> keys = submitCountItems.keySet();
		for (Integer k : keys) {
			if (submitCountItems.get(k) == 1) {
				submitCount++;
			}
		}

		return submitCount;
	}

	@Override
	public int getItemValuesCount(int phaseId) {
		return phaseDao.getValuesCount(phaseId);
	}

	@Override
	public void deleteValues(int phaseId) {
		phaseDao.deleteByHql(
				"DELETE CheckItemValueEntity WHERE phase.templPhaseId=:p1",
				new Parameter(phaseId));
	}

	@Override
	public void deleteCheckPicValues(int phaseId) {
		phaseDao.deleteByHql(
				"DELETE VisitImgEntity WHERE phase.templPhaseId=:p1",
				new Parameter(phaseId));
	}

	@Override
	public void deletePrescriptionValues(int phaseId) {
		phaseDao.deleteByHql(
				"DELETE VisitPrescriptionEntity WHERE phase.templPhaseId=:p1",
				new Parameter(phaseId));
	}

	@Override
	public void update(TemplatePhaseEntity phase, int[] itemIds) {

		// 旧的列表
		List<VisitItemEntity> items = visitItemService.getItemsByPhase(phase
				.getTemplPhaseId());
		if (null == items) {
			items = new ArrayList<>();
		}

		int itemCount = 1; // 阶段项目总数，加上处方
		if (itemIds.length > 0) {
			for (int j = 0; j < itemIds.length; j++) {
				VisitItemEntity ite = visitItemService.get(itemIds[j]);
				if (null == ite) {
					continue;
				}
				itemCount++;
				if (items.contains(ite)) { // 如果是在已经存在的列表中，从将要被删除的旧的列表中移除
					items.remove(ite);
					continue;
				} else {
					if (null != ite.getPhases()) {
						ite.getPhases().add(phase); // 新加关系
					} else {
						Set<TemplatePhaseEntity> set = new HashSet<>();
						set.add(phase);
						ite.setPhases(set);
					}
				}
			}
		}
		phase.setItemCount(itemCount); // 更新阶段项目总数
		phaseDao.save(phase);

		// 移除全部关系
		for (Iterator<VisitItemEntity> iterator = items.iterator(); iterator
				.hasNext();) {
			VisitItemEntity visitItemEntity = (VisitItemEntity) iterator.next();
			if (null != visitItemEntity.getPhases()) {
				visitItemEntity.getPhases().remove(phase);
			}
		}

	}

	@Override
	public void deletePhase(int id) {
		phaseDao.remove(id);
	}

	@Override
	public void update(TemplatePhaseEntity phase) {
		phaseDao.save(phase);
	}

	@Override
	public List<TemplatePhaseEntity> getSeletedListByVisitTime(Date visitTime,
			int... fuZhenStatus) {
		StringBuffer buf = new StringBuffer();
		buf.append("FROM TemplatePhaseEntity WHERE visitTime=:curVisitTime AND selected=1 ");
		Parameter params = new Parameter();
		params.put("curVisitTime", visitTime);
		if ((null != fuZhenStatus) && (fuZhenStatus.length > 0)) {
			buf.append(" AND fuZhenStatus IN (");
			for (int i = 0; i < fuZhenStatus.length; i++) {
				if (i > 0) {
					buf.append(",");
				}
				buf.append(fuZhenStatus[i]);
			}
			buf.append(" ) ");
		}
		return phaseDao.findByHql(buf.toString(), params);
	}

	@Override
	public TemplatePhaseEntity save(TemplatePhaseEntity phase) {
		return phaseDao.save(phase);
	}

	@Override
	public List<TemplatePhaseEntity> getListByTempl(int templId, int selected) {
		String hql = "FROM TemplatePhaseEntity WHERE template.templId=:templId AND selected=:selected";
		Parameter params = new Parameter();
		params.put("templId", templId);
		params.put("selected", selected);
		return phaseDao.findByHql(hql, params);
	}

	@Override
	public int getItemCount(TemplatePhaseEntity phase) {
		int itemCount = 1; // 阶段项目总数，加上处方
		if (null != phase.getItems()) {
			itemCount += phase.getItems().size();
		}
		return itemCount;
	}

	@Override
	public List<TemplatePhaseEntity> getListByVisitTime(Date visitTime,
			int... fuZhenStatus) {
		StringBuffer buf = new StringBuffer();
		buf.append("FROM TemplatePhaseEntity WHERE visitTime=:curVisitTime ");
		Parameter params = new Parameter();
		params.put("curVisitTime", visitTime);
		if ((null != fuZhenStatus) && (fuZhenStatus.length > 0)) {
			buf.append(" AND fuZhenStatus IN (");
			for (int i = 0; i < fuZhenStatus.length; i++) {
				if (i > 0) {
					buf.append(",");
				}
				buf.append(fuZhenStatus[i]);
			}
			buf.append(" ) ");
		}
		return phaseDao.findByHql(buf.toString(), params);
	}
}
