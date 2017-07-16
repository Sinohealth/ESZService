package com.sinohealth.eszservice.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.base.HospitalEntity;
import com.sinohealth.eszorm.entity.base.ProvinceEntity;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.base.IHospitalDao;
import com.sinohealth.eszservice.service.base.IHospitalService;

@Service("hospitalService")
public class HospitalServiceImpl implements IHospitalService {

	@Autowired
	private IHospitalDao hospitalDao;

	@Override
	public HospitalEntity getByName(String name) {
		return hospitalDao.getByName(name);
	}

	@Override
	public HospitalEntity getById(int hospitalId) {
		return hospitalDao.get(hospitalId);
	}

	public IHospitalDao getHospitalDao() {
		return hospitalDao;
	}

	public void setHospitalDao(IHospitalDao hospitalDao) {
		this.hospitalDao = hospitalDao;
	}

	@Override
	public PaginationSupport findByName(String nameLike, int provinceId,
			int pageNo, int pageSize,int hashDoctor) {
		StringBuffer hql = new StringBuffer();
		Parameter params = new Parameter();

		hql.append("FROM HospitalEntity ");

		boolean hadCond = false; // 已经有条件

		if (0 != provinceId) {
			hadCond = true;
			hql.append(" WHERE province.id=:provinceId ");
			params.put("provinceId", provinceId);
		}

		if (null != nameLike && !"".equals(nameLike)) {
			if (!hadCond) {
				hadCond = true;
				hql.append(" WHERE ");
			} else {
				hql.append(" AND ");
			}
			hql.append(" hospitalName like :name ");
			params.put("name", "%" + nameLike + "%");
		}
		if (0 != hashDoctor) {
			if (!hadCond) {
				hql.append(" WHERE ");
			} else {
				hql.append(" AND ");
			}
			hql.append(" doctorVisitCount >:doctorVisitCount");
			params.put("doctorVisitCount", 0);
		}
		hql.append(" ORDER BY hospitalName DESC ");
		PaginationSupport pagination = hospitalDao.paginationByHql(
				hql.toString(), pageNo, pageSize, params);
		return pagination;
	}

	@Override
	public HospitalEntity addNew(int provinceId, String name, String address) {
		return addNew(provinceId, name, address, false);
	}

	@Override
	public HospitalEntity addNew(int provinceId, String name, String address,
			boolean isDoctorAdd) {
		HospitalEntity ent = new HospitalEntity();
		ProvinceEntity province = new ProvinceEntity();
		province.setId(provinceId);
		ent.setProvince(province);
		ent.setHospitalName(name);
		ent.setHospitalAddr(address);
		ent.setDoctorAdd(isDoctorAdd);
		return hospitalDao.save(ent);
	}
}
