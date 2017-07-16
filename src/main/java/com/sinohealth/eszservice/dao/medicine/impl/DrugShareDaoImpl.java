package com.sinohealth.eszservice.dao.medicine.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.medicine.DrugShareEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.medicine.IDrugShareDao;

@Repository("drugShareDao")
public class DrugShareDaoImpl extends SimpleBaseDao<DrugShareEntity, Integer> implements IDrugShareDao {
	public DrugShareDaoImpl() {
		super(DrugShareEntity.class);
	}
}
