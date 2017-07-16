package com.sinohealth.eszservice.service.visit;


import java.util.List;

import com.sinohealth.eszorm.entity.visit.VisitSubjectsVersionEntity;
import com.sinohealth.eszservice.dto.visit.elem.SzSubjectVersionItemElem;

public interface IVisitSubjectsVersionService {

	/**
	 * 根据专科id和版本值查询
	 * @param visitSubjectsVersionEntity
	 * @return
	 */
	List<VisitSubjectsVersionEntity> getByIdAndVersion(VisitSubjectsVersionEntity visitSubjectsVersionEntity);
	
	/**
	 * 根据专科id查询最大版本
	 * @param visitSubjectsVersionEntity
	 * @return
	 */
	Integer getMaxVersion(VisitSubjectsVersionEntity visitSubjectsVersionEntity);

	
	/**
	 * 根据版本值范围查询
	 * @param visitSubjectsVersionEntity
	 * @return
	 */
	List<SzSubjectVersionItemElem> getByVersionValue(VisitSubjectsVersionEntity visitSubjectsVersionEntity,int beginVal);
	
	/**
	 * 根据专科名称查询所有item
	 * @param visitSubjectsVersionEntity
	 * @return
	 */
	List<SzSubjectVersionItemElem> getAllBySzSubject(VisitSubjectsVersionEntity visitSubjectsVersionEntity);

}
