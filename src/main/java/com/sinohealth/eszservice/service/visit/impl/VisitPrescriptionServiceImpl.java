package com.sinohealth.eszservice.service.visit.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IVisitPrescriptionDao;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IPhaseService;
import com.sinohealth.eszservice.service.visit.IVisitPrescriptionService;

@Service
public class VisitPrescriptionServiceImpl implements IVisitPrescriptionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IVisitPrescriptionDao dao;

	@Autowired
	IPhaseService phaseService;

	@Autowired
	IApplicationService applicationService;

	@Override
	public Set<VisitPrescriptionEntity> updateValues(int phaseId, Set<VisitPrescriptionEntity> values) {
		Set<VisitPrescriptionEntity> pres = new HashSet<>();
		TemplatePhaseEntity phase = phaseService.get(phaseId);

		if (null == phase) {
			return pres;
		}

		ApplicationEntity application = phase.getTemplate().getApplication();

		SickEntity sick = application.getSick();
		DoctorEntity doctor = application.getDoctor();

		Calendar cal = Calendar.getInstance();

		// TODO 每个阶段只能上传6张图片
		List<VisitPrescriptionEntity> prescriptions = getByPhaseId(phaseId,
				VisitPrescriptionEntity.REPULOADED_NO); // 原有的处方
		// logger.debug("原有的处方：{}", prescriptions);
		for (Iterator<VisitPrescriptionEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			VisitPrescriptionEntity prescription = iterator.next();
			if (null != prescription.getId()) {
				if (null != prescriptions) { // 如果已经存在了此药方，不做处理
					int idx = prescriptions.indexOf(prescription);
					if (-1 != idx) {
						// logger.debug("处方已存在不做处理：{}", prescription);
						pres.add(prescriptions.get(idx));
						prescriptions.remove(idx); // 从已有的处方中移除
						continue;
					}
				}
				// logger.debug("处方已经被更改：{}", prescription);
				// 如果传了此id过来，说明是修改了图片。
				// 要把原图片标记为已经重新上传，并且保存新的图片
				if (0 != prescription.getId().intValue()) {
					// 从原有的图片中找出旧的处方
					for (VisitPrescriptionEntity o : prescriptions) {
						if (prescription.getId() == o.getId()) {
							updateToReuploaded(prescription.getId(),
									cal.getTime()); // 标记为重传
							prescription.setId(null); // 将id重新设置为null，将会被作为新记录插入
							prescription.setPhase(phase);
							prescription.setCreateDate(cal.getTime());
							prescription.setSick(sick);
							prescription.setDoctor(doctor);
							VisitPrescriptionEntity saved = addNew(prescription);
							pres.add(o);
							pres.add(saved);
							prescriptions.remove(o); // 从已有的处方中移除
							break;
						}
					}
				} else if (0 == prescription.getId().intValue()) {
					prescription.setId(null);
				} else {
					// 如果有imgId，但是又没有在已存在的数据库，则跳过不做处理
					continue;
				}
			}
			// 如果是没有填imgId的，直接保存
			prescription.setPhase(phase);
			prescription.setCreateDate(cal.getTime());
			prescription.setSick(sick);
			prescription.setDoctor(doctor);
			// logger.debug("保存新的处方：{}", prescription);
			VisitPrescriptionEntity saved = addNew(prescription);
			pres.add(saved);
		}
		if (null != prescriptions) { // 对原有的，没提交的imgId的处方，设置为重传
			for (VisitPrescriptionEntity e : prescriptions) {
				updateToReuploaded(e.getId(), cal.getTime());
			}
		}
		
		return pres;

	}

	@Override
	public VisitPrescriptionEntity addNew(VisitPrescriptionEntity prescription) {
		return dao.save(prescription);
	}

	public void updateToReuploaded(int id, Date date) {
		VisitPrescriptionEntity o = get(id);
		o.setReuploaded(VisitPrescriptionEntity.REPULOADED_YES);
		o.setReuploadDate(date);
		dao.save(o);
	}

	@Override
	public VisitPrescriptionEntity get(int id) {
		return dao.get(id);

	}

	public List<VisitPrescriptionEntity> getByPhaseId(int phaseId,
			int reuploaded) {
		String hql = "FROM VisitPrescriptionEntity WHERE phase.templPhaseId=:p1 AND reuploaded=:p2";
		return dao.findByHql(hql, new Parameter(phaseId, reuploaded));
	}

	@Override
	public void updateStatusByDoctorMark(int id, int status, String remarks) {
		Calendar cal = Calendar.getInstance();
		VisitPrescriptionEntity o = get(id);
		if (null == o) {
			return;
		}
		o.setStatus(status);
		o.setDoctorMarkStatus(status);
		o.setDoctorMarkRemarks(remarks);
		o.setDoctorMarkTime(cal.getTime());
		dao.save(o);
	}

	@Override
	public void updateValuesForApply(int applyId,
			Set<VisitPrescriptionEntity> values) {
		if (null == values) {
			return;
		}

		ApplicationEntity application = applicationService.get(applyId);

		if (null == application) {
			return;
		}

		SickEntity sick = application.getSick();
		DoctorEntity doctor = application.getDoctor();

		Calendar cal = Calendar.getInstance();

		// TODO 每个阶段只能上传6张图片
		List<VisitPrescriptionEntity> prescriptions = getByApplyId(applyId,
				VisitPrescriptionEntity.REPULOADED_NO); // 原有的处方
		for (Iterator<VisitPrescriptionEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			VisitPrescriptionEntity prescription = iterator.next();
			if (null != prescription.getId()) {
				if ((null != prescriptions)
						&& (prescriptions.contains(prescription))) { // 如果已经在在了此药方，不做处理
					prescriptions.remove(prescription); // 从已有的处方中移除
					continue;
				}
				// 如果传了此id过来，说明是修改了图片。
				// 要把原图片标记为已经重新上传，并且保存新的图片
				if (0 != prescription.getId()) {
					// 从原有的图片中找出旧的处方
					for (VisitPrescriptionEntity o : prescriptions) {
						if (prescription.getId() == o.getId()) {
							updateToReuploaded(prescription.getId(),
									cal.getTime()); // 标记为重传
							prescription.setId(null); // 将id重新设置为null，将会被作为新记录插入
							prescription.setApplication(application);
							prescription.setCreateDate(cal.getTime());
							prescription.setSick(sick);
							prescription.setDoctor(doctor);
							addNew(prescription);
							prescriptions.remove(o); // 从已有的处方中移除
							break;
						}
					}
				} else if (0 == prescription.getId()) {
					prescription.setId(null);
				} else {
					// 如果有imgId，但是又没有在已存在的数据库，则跳过不做处理
					continue;
				}
			}
			// 如果是没有填imgId的，直接保存
			prescription.setApplication(application);
			prescription.setCreateDate(cal.getTime());
			prescription.setSick(sick);
			prescription.setDoctor(doctor);
			addNew(prescription);
		}
		if (null != prescriptions) { // 对原有的，没提交的imgId的处方，设置为重传
			for (VisitPrescriptionEntity e : prescriptions) {
				updateToReuploaded(e.getId(), cal.getTime());
			}
		}

	}

	public List<VisitPrescriptionEntity> getByApplyId(int applyId,
			int reuploaded) {
		String hql = "FROM VisitPrescriptionEntity WHERE application.applyId=:p1 AND reuploaded=:p2";
		return dao.findByHql(hql, new Parameter(applyId, reuploaded));
	}

	@Override
	public List<VisitPrescriptionEntity> getBySickId(int sickId) {
		String hql = "FROM VisitPrescriptionEntity WHERE sick.id=:p1";
		return dao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public List<VisitPrescriptionEntity> getPrescriptions(int sickId) {
		String hql = "FROM VisitPrescriptionEntity where sick.id = :p1 order by createDate desc";
		return dao.findByHql(hql, new Parameter(sickId), 1, 0);
	}

	@Override
	public boolean isDataFormatCorrectWithPhase(int phaseId) {
		Parameter params = new Parameter();
		params.put("phaseId", phaseId);
		params.put("reuploaded", VisitPrescriptionEntity.REPULOADED_NO);

		String hql = "SELECT COUNT(*) FROM VisitPrescriptionEntity WHERE phase.templPhaseId=:phaseId AND ( doctorMarkStatus<>0 AND reuploaded=:reuploaded )";
		Query query = dao.prepareQuery(dao.getSession(), hql, params);
		Number r = (Number) query.uniqueResult();
		// 如果有批示了，并且未重新上传的，则认为不完整
		return r.intValue() == 0;
	}

	@Override
	public boolean isDataFormatCorrectWithApply(int applyId) {
		Parameter params = new Parameter();
		params.put("applyId", applyId);
		params.put("reuploaded", VisitPrescriptionEntity.REPULOADED_NO);

		String hql = "SELECT COUNT(*) FROM VisitPrescriptionEntity WHERE application.applyId=:applyId AND ( doctorMarkStatus<>0 AND reuploaded=:reuploaded )";
		Query query = dao.prepareQuery(dao.getSession(), hql, params);
		Number r = (Number) query.uniqueResult();
		// 如果有批示了，并且未重新上传的，则认为不完整
		return r.intValue() == 0;
	}

	@Override
	public int getCorrectCountByPhase(Integer templPhaseId) {
		Parameter params = new Parameter();
		params.put("templPhaseId", templPhaseId);
		params.put("reuploaded", VisitPrescriptionEntity.REPULOADED_NO);

		String hql = "SELECT COUNT(*) FROM VisitPrescriptionEntity WHERE phase.templPhaseId=:templPhaseId AND ( doctorMarkStatus<>0 AND reuploaded=:reuploaded )";
		Query query = dao.prepareQuery(dao.getSession(), hql, params);
		Number r = (Number) query.uniqueResult();
		// 如果有批示了，并且未重新上传的，则认为不完整
		return r.intValue();
	}
}
