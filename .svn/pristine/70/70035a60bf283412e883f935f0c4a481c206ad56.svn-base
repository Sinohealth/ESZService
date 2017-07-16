package com.sinohealth.eszservice.service.medicine.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.medicine.DrugEntity;
import com.sinohealth.eszorm.entity.medicine.DrugShareEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.dao.medicine.IDrugShareDao;
import com.sinohealth.eszservice.service.medicine.IDrugShareService;

@Service("drugShareService")
public class DrugShareServiceImpl implements IDrugShareService{
	@Autowired
	private IDrugShareDao dao;
	
	public String drugShare(Integer doctorId,String sickIds,Integer drugId){
		String jsonStr = "{\"errCode\":10705}";
		
        if(sickIds != null && !sickIds.equals("")){
        	String[] sickArr = sickIds.split(",");
        	for(int i=0;i<sickArr.length;i++){
        		DrugShareEntity drugShare = new DrugShareEntity();
        		DoctorEntity doctor = new DoctorEntity();
        		SickEntity sick = new SickEntity();
        		DrugEntity drug = new DrugEntity();
        		Date date = new Date();
        		
        		doctor.setId(doctorId);
        		sick.setId(Integer.parseInt(sickArr[i]));
        		drug.setId(drugId);
        		
        		drugShare.setDoctor(doctor);
        		drugShare.setSick(sick);
        		drugShare.setDrug(drug);
        		drugShare.setCreateDate(date);
        		dao.save(drugShare);        		
        	}
        	jsonStr = "{\"errCode\":0}";
        }
		return jsonStr;		
	}
	
}
