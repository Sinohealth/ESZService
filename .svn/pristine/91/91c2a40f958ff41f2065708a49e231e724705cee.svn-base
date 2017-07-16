package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.ICheckItemValueDao;
import com.sinohealth.eszservice.dto.visit.CheckItemValueDto;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.ICheckItemValueService;
import com.sinohealth.eszservice.service.visit.IPhaseService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;
import com.sinohealth.eszservice.service.visit.exception.VisitItemNotFoundExecption;

@Service
public class CheckItemValueServiceImpl implements ICheckItemValueService {

	@Autowired
	ICheckItemValueDao dao;

	@Autowired
	IPhaseService phaseService;

	@Autowired
	IVisitItemService visitItemService;

	@Autowired
	IApplicationService applicationService;

	@Override
	public Set<CheckItemValueEntity> updateValues(int phaseId,
			Set<CheckItemValueEntity> values)
			throws VisitItemNotFoundExecption, ValueOutOfRangeException,
			SystemErrorExecption {
		Map<Integer, Integer> submitCount = new HashMap<Integer, Integer>();
		TemplatePhaseEntity phase = phaseService.get(phaseId);

		if (null == phase) {
			throw new SystemErrorExecption("找不到相关阶段数据", BaseDto.ERRCODE_OTHERS);
		}

		SickEntity sick = phase.getTemplate().getApplication().getSick();
		CheckItemValueDto dto = new CheckItemValueDto();
		// 执行时间
		Calendar cal = Calendar.getInstance();

		// 已经在在的结果值
		List<CheckItemValueEntity> existsValues = getListByPhaseId(phaseId);

		// 前端值与警告级别存在此为DTO用
		Set<CheckItemValueEntity> itemList = new HashSet<>();
		if (null == existsValues) {
			existsValues = new ArrayList<>();
		}
		// TODO 检验是否为当前阶段的检验项，如果不是则不插入/更新
		// TODO 更新当期复诊的报告状态
		int reportStatus = 0; // 此阶段的数据值的最大告警级别
		for (Iterator<CheckItemValueEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			CheckItemValueEntity value = iterator.next();
			value.setPhase(phase);

			int idx = existsValues.indexOf(value);

			// 如果值已经存在，只需要更新
			if (-1 != idx) {
				CheckItemValueEntity oldObj = existsValues.get(idx);
				oldObj.setReportValue(value.getReportValue());
				oldObj.setReportTime(cal.getTime());
				int warnLevel = 0;
				if (!"".equals(value)) {
					warnLevel = visitItemService.getWarnLevel(oldObj
							.getVisitItem().getItemId(),
							value.getReportValue(), sick.getSex());
					oldObj.setReportWarnLevel(warnLevel);
				}
				// System.out.println("update warnLevel:"+warnLevel);
				itemList.add(oldObj);
				update(oldObj); // 更新
				// 从当前需要删除的旧值队列中移除
				existsValues.remove(idx);
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}

				// 如果数据库中还不存在值
			} else {
				value.setReportTime(cal.getTime());
				// 检测值的告警范围
				int warnLevel = 0;
				if (!"".equals(value.getReportValue())) {
					warnLevel = visitItemService.getWarnLevel(value
							.getVisitItem().getItemId(),
							value.getReportValue(), sick.getSex());
				}

				value.setReportWarnLevel(warnLevel); // 设置此值的告警级别
				value.setSick(sick);
				try {
					itemList.add(value);
					addNew(value);
				} catch (ObjectNotFoundException e) {
					throw new VisitItemNotFoundExecption(value.getVisitItem()
							.getItemId());
				}
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}
				// System.out.println("add warnLevel:"+warnLevel);
			}
			if ((null != value.getReportValue())
					&& (!"".equals(value.getReportValue()))) {
				submitCount.put(value.getVisitItem().getItemId(), 1); // 用于统计有多少项目填写了内容
			}
		}

		// 设置此阶段的告警级别
		phase.setReportStatus(reportStatus);
		phaseService.update(phase);

		// 删除原有的检查项结果
		for (CheckItemValueEntity o : existsValues) {
			delete(o);
		}
		dto.setSubmitedValueCount(submitCount.size());
		return itemList;
	}

	@Override
	public void updatePersonalValues(int applyId,
			Set<CheckItemValueEntity> values) throws SystemErrorExecption,
			VisitItemNotFoundExecption {
		// TemplatePhaseEntity phase = phaseService.get(phaseId);
		ApplicationEntity application = applicationService.get(applyId);

		if (null == application) {
			throw new SystemErrorExecption("找不到相关申请记录数据",
					BaseDto.ERRCODE_OTHERS);
		}
		SickEntity sick = application.getSick();
		// 执行时间
		Calendar cal = Calendar.getInstance();

		// 已经存在的结果值
		List<CheckItemValueEntity> existsValues = new ArrayList<>(application
				.getPersonalHistory().getValues());

		// TODO 检验是否为当前阶段的检验项，如果不是则不插入/更新
		// TODO 更新当期复诊的报告状态
		for (Iterator<CheckItemValueEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			CheckItemValueEntity value = iterator.next();
			value.setApplication(application);

			int idx = existsValues.indexOf(value);

			// 如果值已经存在，只需要更新
			if (-1 != idx) {
				CheckItemValueEntity oldObj = existsValues.get(idx);
				oldObj.setReportValue(value.getReportValue());
				oldObj.setReportTime(cal.getTime());

				update(oldObj); // 更新
				// 从当前需要删除的旧值队列中移除
				existsValues.remove(idx);

				// 如果数据库中还不存在值
			} else {
				value.setReportTime(cal.getTime());
				value.setSick(sick);
				addNew(value);
			}

		}
	}

	private void delete(CheckItemValueEntity o) {
		dao.delete(o);
	}

	public void addNew(CheckItemValueEntity value) {
		value.setResultId(null);
		dao.save(value);
	}

	public void update(CheckItemValueEntity value) {
		dao.save(value);
	}

	public List<CheckItemValueEntity> getListByPhaseId(int phaseId) {
		String hql = "FROM CheckItemValueEntity WHERE phase.templPhaseId=:p1";
		return dao.findByHql(hql, new Parameter(phaseId));
	}

	public List<CheckItemValueEntity> getListByApplyId(int applyId) {
		String hql = "FROM CheckItemValueEntity WHERE application.applyId=:p1";
		return dao.findByHql(hql, new Parameter(applyId));
	}

	@Override
	public List<CheckItemValueEntity> getitemsBySickId(int sickId) {
		String hql = "FROM CheckItemValueEntity WHERE sick.id=:p1 and cat=3 order by reportTime desc";
		return dao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public List<CheckItemValueEntity> getitemsBySickId(int sickId, int applyId) {
		String hql = "FROM CheckItemValueEntity WHERE sick.id=:p1 and cat=3 and application.applyId=:p2 order by reportTime desc";
		return dao.findByHql(hql, new Parameter(sickId, applyId));
	}

	@Override
	public List<CheckItemValueEntity> getPersonsBySickId(int sickId) {
		String hql = "FROM CheckItemValueEntity WHERE sick.id=:p1 and cat=7 order by reportTime desc";
		return dao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public List<CheckItemValueEntity> getCheckItem(int sickId) {
		String hql = "FROM CheckItemValueEntity where sick.id = :p1 and cat=3"
				+ "order by reportTime desc";
		return dao.findByHql(hql, new Parameter(sickId), 1, 0);
	}

	@Override
	public int getWarnCountByPhase(TemplatePhaseEntity phase, int level) {
		String hql = "SELECT COUNT(*) FROM CheckItemValueEntity WHERE phase.templPhaseId=:phaseId AND reportWarnLevel=:level";
		Parameter params = new Parameter();
		params.put("phaseId", phase.getTemplPhaseId());
		params.put("level", level);
		Query query = dao.prepareQuery(dao.getSession(), hql, params);
		Number r = (Number) query.uniqueResult();
		return null != r ? r.intValue() : 0;
	}

	@Override
	public CheckItemValueDto updateCheckItemValues(int phaseId,
			Set<CheckItemValueEntity> values)
			throws VisitItemNotFoundExecption, ValueOutOfRangeException,
			SystemErrorExecption {
		Map<Integer, Integer> submitCount = new HashMap<Integer, Integer>();
		TemplatePhaseEntity phase = phaseService.get(phaseId);

		if (null == phase) {
			throw new SystemErrorExecption("找不到相关阶段数据", BaseDto.ERRCODE_OTHERS);
		}

		SickEntity sick = phase.getTemplate().getApplication().getSick();
		CheckItemValueDto dto = new CheckItemValueDto();
		// 执行时间
		Calendar cal = Calendar.getInstance();

		// 已经在在的结果值
		List<CheckItemValueEntity> existsValues = getListByPhaseId(phaseId);

		// 前端值与警告级别存在此为DTO用
		Set<CheckItemValueEntity> itemList = new HashSet<>();
		if (null == existsValues) {
			existsValues = new ArrayList<>();
		}
		// TODO 检验是否为当前阶段的检验项，如果不是则不插入/更新
		// TODO 更新当期复诊的报告状态
		int reportStatus = 0; // 此阶段的数据值的最大告警级别
		for (Iterator<CheckItemValueEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			CheckItemValueEntity value = iterator.next();
			value.setPhase(phase);

			int idx = existsValues.indexOf(value);

			// 如果值已经存在，只需要更新
			if (-1 != idx) {
				CheckItemValueEntity oldObj = existsValues.get(idx);
				oldObj.setReportValue(value.getReportValue());
				oldObj.setReportTime(cal.getTime());
				int warnLevel = 0;
				if (!"".equals(value)) {
					warnLevel = visitItemService.getWarnLevel(oldObj
							.getVisitItem().getItemId(),
							value.getReportValue(), sick.getSex());
					oldObj.setReportWarnLevel(warnLevel);
				}
				// System.out.println("update warnLevel:"+warnLevel);
				itemList.add(oldObj);
				update(oldObj); // 更新
				// 从当前需要删除的旧值队列中移除
				existsValues.remove(idx);
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}

				// 如果数据库中还不存在值
			} else {
				value.setReportTime(cal.getTime());
				// 检测值的告警范围
				int warnLevel = 0;
				if (!"".equals(value.getReportValue())) {
					warnLevel = visitItemService.getWarnLevel(value
							.getVisitItem().getItemId(),
							value.getReportValue(), sick.getSex());
				}

				value.setReportWarnLevel(warnLevel); // 设置此值的告警级别
				value.setSick(sick);
				try {
					itemList.add(value);
					addNew(value);
				} catch (ObjectNotFoundException e) {
					throw new VisitItemNotFoundExecption(value.getVisitItem()
							.getItemId());
				}
				// 如果数据告警级别比原来的高，则设置为更高
				if (warnLevel > reportStatus) {
					reportStatus = warnLevel;
				}
				// System.out.println("add warnLevel:"+warnLevel);
			}
			if ((null != value.getReportValue())
					&& (!"".equals(value.getReportValue()))) {
				submitCount.put(value.getVisitItem().getItemId(), 1); // 用于统计有多少项目填写了内容
			}
		}

		// 设置此阶段的告警级别
		phase.setReportStatus(reportStatus);
		phaseService.update(phase);

		dto.setValues(itemList);

		// 删除原有的检查项结果
		for (CheckItemValueEntity o : existsValues) {
			delete(o);
		}
		dto.setSubmitedValueCount(submitCount.size());
		return dto;
	}

	@Override
	public List<CheckItemValueEntity> getListBySick(int sickId, int cat) {
		String hql = "FROM CheckItemValueEntity WHERE sick.id=:p1 and cat=:p2 order by reportTime desc";
		return dao.findByHql(hql, new Parameter(sickId, cat));
	}

}
