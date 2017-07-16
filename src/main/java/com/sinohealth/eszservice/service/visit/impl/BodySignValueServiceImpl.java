package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IBodySignValueDao;
import com.sinohealth.eszservice.dto.visit.BodySignValueDto;
import com.sinohealth.eszservice.dto.visit.sick.OtherBodyElem;
import com.sinohealth.eszservice.service.visit.IBodySignValueService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

@Service
public class BodySignValueServiceImpl implements IBodySignValueService {

	@Autowired
	private IBodySignValueDao dao;

	@Autowired
	private IVisitItemService visitItemService;

	@Override
	public BodySignValueDto saveBodySignValues(int sickId, Date reportDate,
			List<BodySignValueEntity> values) throws ValueOutOfRangeException {

		Calendar cal = Calendar.getInstance();
		Date curTime = cal.getTime(); // 当前时间

		BodySignValueDto dto = new BodySignValueDto();
		List<BodySignValueEntity> valueList = new ArrayList<BodySignValueEntity>();

		for (Iterator<BodySignValueEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			BodySignValueEntity value = (BodySignValueEntity) iterator.next();

			int itemId = value.getItem().getItemId();

			BodySignValueEntity existsValue = get(sickId, itemId, reportDate);
			// System.out.println("op1: "+existsValue.getItem().getOp1());

			if (null != existsValue) {
				value.setResultId(existsValue.getResultId());
				/**
				 * 如果已经在在记录值，并输入的值为空，认为是删除了该输入，删除已经存在的记录
				 */
				if (null == value.getReportValue()
						|| value.getReportValue().equals("")) {
					dao.delete(existsValue);
					continue;
				}
				value.setItem(existsValue.getItem());
			}

			/**
			 * 如果输入的值为空，忽略此条记录
			 */
			if (null == value.getReportValue()
					|| value.getReportValue().equals("")) {
				continue;
			}
			SickEntity sick = new SickEntity();
			sick.setId(sickId);
			value.setPostTime(curTime);
			value.setReportDate(reportDate);
			value.setSick(sick);

			visitItemService.validate(itemId, value.getReportValue());

			int warnLevel = visitItemService.getWarnLevel(itemId,
					value.getReportValue(), sick.getSex());
			value.setReportWarnLevel(warnLevel);

			dao.save(value);
			if (null == existsValue) {
				existsValue = get(sickId, itemId, reportDate);
				value.setItem(existsValue.getItem());
			}
			valueList.add(value);
		}
		dto.setValues(valueList);
		return dto;
	}

	private BodySignValueEntity get(int sickId, Integer itemId, Date reportDate) {
		return dao
				.getByHql(
						"FROM BodySignValueEntity WHERE sick.id=:p1 AND item.itemId=:p2 AND reportDate=:p3",
						(new Parameter(sickId, itemId, reportDate)));

	}

	@Override
	public List<OtherBodyElem> getBodySignBySickIdCat(int sickId, int cat,
			int pageNo, int pageSize) {
		String hql = "From BodySignValueEntity where item.itemId in(select itemId from VisitItemEntity where cat =:p1) and sick.id=:p2 order by postTime desc";
		PaginationSupport pagination = dao.paginationByHql(hql, pageNo,
				pageSize, new Parameter(cat, sickId));
		List<BodySignValueEntity> list = pagination.getItems();
		List<OtherBodyElem> elems = new ArrayList<>();
		if (list.size() > 0) {
			for (BodySignValueEntity value : list) {
				OtherBodyElem elem = new OtherBodyElem();
				elem.setEnName(value.getItem().getEnName());
				elem.setZhName(value.getItem().getZhName());
				elem.setText(value.getReportValue());
				elem.setUpdateTime(value.getPostTime());
				elems.add(elem);
			}
		}
		return elems;
	}

	@Override
	public List<BodySignValueEntity> getList(int sickId, Integer itemId,
			Date startDate, Date endDate) {
		StringBuffer hql = new StringBuffer("FROM BodySignValueEntity ");
		Parameter params = new Parameter();

		hql.append("WHERE sick.id=:sickId");
		params.put("sickId", sickId);

		if (null != itemId) {
			hql.append(" AND item.itemId=:itemId");
			params.put("itemId", itemId);
		}

		// 计算使用>号和<号，而不用>=和<=号，加速SQL查询
		if (null != startDate) {
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.add(Calendar.DATE, -1);
			hql.append(" AND reportDate>:startCal");
			params.put("startCal", startCal.getTime());
		}
		if (null != startDate) {
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.add(Calendar.DATE, 1);
			hql.append(" AND reportDate<:endCal");
			params.put("endCal", endCal.getTime());
		}
		
		hql.append(" ORDER BY reportDate DESC");

		return dao.findByHql(hql.toString(), params);

	}
}
