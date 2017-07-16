package com.sinohealth.eszservice.service.doctor.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.doctor.DoctorCountEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.doctor.IDoctorCountDao;
import com.sinohealth.eszservice.queue.QueueUtils;
import com.sinohealth.eszservice.queue.entity.DoctorVisitCountMessage;
import com.sinohealth.eszservice.service.doctor.IDoctorCountService;

@Service
public class DoctorCountServiceImpl implements IDoctorCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDoctorCountDao dao;

	@Override
	public void updateTemplCount(int id, String szSubject) {
		dao.updateTemplCount(id, szSubject);
	}

	@Override
	public void updateVisitCount(Integer id, String szSubject) {
		if (null == id) {
			return;
		}
		// dao.updateVisitCount(id, szSubject);
		// 更改为异步处理
		dao.flush();
		DoctorVisitCountMessage message = new DoctorVisitCountMessage();
		message.setDoctorId(id);
		message.setSzSubject(szSubject);
		QueueUtils.push(message);
	}

	@Override
	public void updateVisitCount(Integer id, String szSubject, boolean force) {
		if (null == id) {
			return;
		}
		if (force) {
			dao.updateVisitCount(id, szSubject);
		}
	}

	@Override
	public DoctorCountEntity get(int userId, String szSubject) {

		DoctorCountEntity doctorCount = dao.get(userId, szSubject);
		if (null == doctorCount) {
			// 保存统计记录
			DoctorCountEntity count = new DoctorCountEntity();
			count.setDoctorId(userId);
			count.setSzSubject(szSubject);

			dao.save(count);

			// 更新积分总分
			dao.updateTotalGrade(userId, szSubject);

			return dao.get(userId, szSubject);
		}
		return doctorCount;
	}

	@Override
	public void updateTotalGrade(int userId, String szSubject) {
		dao.updateTotalGrade(userId, szSubject);
	}

	@Override
	public DoctorCountEntity saveOrUpdate(DoctorCountEntity count) {
		return dao.save(count);
	}

	@Override
	public List<DoctorCountEntity> getDoctorCount(Integer doctorId) {
		String hql = "From DoctorCountEntity where doctorId=:p1";
		return dao.findByHql(hql, new Parameter(doctorId.intValue()));
	}

	@Override
	public void add(int userId, String szSubject) {
		DoctorCountEntity count = get(userId, szSubject);
		if (null == count) {
			count = new DoctorCountEntity();
			count.setDoctorId(userId);
			count.setSzSubject(szSubject);
			dao.save(count);
		}
	}
}
