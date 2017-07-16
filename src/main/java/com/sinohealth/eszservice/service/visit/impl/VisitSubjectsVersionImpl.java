package com.sinohealth.eszservice.service.visit.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.visit.VisitSubjectsVersionEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IVisitSubjectsVersionDao;
import com.sinohealth.eszservice.dto.visit.elem.SzSubjectVersionItemElem;
import com.sinohealth.eszservice.service.visit.IVisitSubjectsVersionService;


@Service
public class VisitSubjectsVersionImpl implements IVisitSubjectsVersionService {

	@Autowired
	IVisitSubjectsVersionDao visitSVDao;

	@Override
	public List<VisitSubjectsVersionEntity> getByIdAndVersion(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity) {
        if(visitSubjectsVersionEntity!=null){
        	return visitSVDao.getByIdAndVersion(visitSubjectsVersionEntity);	
        }
		return null;
	}

	@Override
	public Integer getMaxVersion(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity) {
		if(visitSubjectsVersionEntity!=null){
			return visitSVDao.getMaxVersion(visitSubjectsVersionEntity);
		}
		return null;
	}

	@Override
	public List<SzSubjectVersionItemElem> getByVersionValue(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity,
			int beginVal) {
		if(visitSubjectsVersionEntity!=null){
			return visitSVDao.getByVersionValue(visitSubjectsVersionEntity, beginVal);
		}
		return null;
	}

	@Override
	public List<SzSubjectVersionItemElem> getAllBySzSubject(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity) {
		if(visitSubjectsVersionEntity!=null){
			return visitSVDao.getAllBySzSubject(visitSubjectsVersionEntity);
		}
		return null;
	}

}
