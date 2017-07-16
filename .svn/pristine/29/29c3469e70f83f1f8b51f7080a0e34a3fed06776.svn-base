package com.sinohealth.eszservice.service.medicine.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.medicine.AtcTypeEntity;
import com.sinohealth.eszorm.entity.medicine.DosageFormEntity;
import com.sinohealth.eszorm.entity.medicine.DrugEntity;
import com.sinohealth.eszorm.entity.medicine.HcTypeEntity;
import com.sinohealth.eszorm.entity.medicine.OtcTypeEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.medicine.IDrugDao;
import com.sinohealth.eszservice.dto.medicine.DrugBody;
import com.sinohealth.eszservice.dto.medicine.DrugDto;
import com.sinohealth.eszservice.service.medicine.IDrugService;

@Service("drugService")
public class DrugServiceImpl implements IDrugService{
	@Autowired
	private IDrugDao dao;	
	

	
	public String getDrugList(Integer atcTypeId){
		String jsonStr = "";
		List<DrugDto> list = new ArrayList();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String hql = "select a from DrugEntity a where a.delFlag='0' and a.publishFlag='1' and a.atcType.id = :atcTypeId  order by a.id desc";
		Parameter params = new Parameter();
		params.put("atcTypeId", atcTypeId);

		List<DrugEntity> drugList = dao.findByHql(hql, params);

		if(drugList != null && drugList.size()>0){
			data.put("errCode", 0);
//			data.put("errMsg", "获取成功");
			
			for(int i=0;i<drugList.size();i++){
				DrugDto dto = new DrugDto();
				DrugEntity drug = drugList.get(i);
				String name = "";
				
				if(drug.getBrandName() != null && !drug.getBrandName().equals("") && drug.getCommonName() != null && !drug.getCommonName().equals("")){
					name = drug.getBrandName()+"("+drug.getCommonName()+")";
				}else if(drug.getBrandName() != null && !drug.getBrandName().equals("")){
					name = drug.getBrandName();
				}else if(drug.getCommonName() != null && !drug.getCommonName().equals("")){
					name = drug.getCommonName();
				}
				
				dto.setId(drug.getId());
				dto.setName(name);
				dto.setCompany(drug.getManufacturer());

				list.add(dto);
			}
			
			data.put("list", list);
		}else{
			data.put("errCode", 10702);
//			data.put("errMsg", "没有找到相应内容");			
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonStr = objectMapper.writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonStr;		
	}
	
	public String drguSearch(String keyword){
		String jsonStr = "";
		List<DrugDto> list = new ArrayList();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String hql = "select a from DrugEntity a where a.delFlag='0' and a.publishFlag='1' and (a.brandName like :keyword or a.commonName like :keyword)  order by a.id desc";
		Parameter params = new Parameter();
		params.put("keyword","%"+keyword+"%");

		List<DrugEntity> drugList = dao.findByHql(hql, params);

		if(drugList != null && drugList.size()>0){
			data.put("errCode", 0);
//			data.put("errMsg", "获取成功");
			
			for(int i=0;i<drugList.size();i++){
				DrugDto dto = new DrugDto();
				DrugEntity drug = drugList.get(i);
				String name = "";
				
				if(drug.getBrandName() != null && !drug.getBrandName().equals("") && drug.getCommonName() != null && !drug.getCommonName().equals("")){
					name = drug.getBrandName()+"("+drug.getCommonName()+")";
				}else if(drug.getBrandName() != null && !drug.getBrandName().equals("")){
					name = drug.getBrandName();
				}else if(drug.getCommonName() != null && !drug.getCommonName().equals("")){
					name = drug.getCommonName();
				}
				
				dto.setId(drug.getId());
				dto.setName(name);
				dto.setCompany(drug.getManufacturer());

				list.add(dto);
			}
			
			data.put("list", list);
		}else{
			data.put("errCode", 10703);
//			data.put("errMsg", "没有找到相应内容");			
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonStr = objectMapper.writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonStr;		
	}
	
	public String getDrugContent(Integer id){
		String jsonStr = "";
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		DrugEntity drug = dao.get(id);
		if(drug != null){
			DrugBody body = new DrugBody();
			
			AtcTypeEntity atcType = null;
			DosageFormEntity dosageForm = null;
			HcTypeEntity hcType = null;
			OtcTypeEntity otcType = null;
			
			if(drug.getAtcType()!=null && drug.getAtcType().getName()!=null){
				atcType = drug.getAtcType();
				drug.setAtcType(null);
			}
			if(drug.getDosageForm()!=null && drug.getDosageForm().getName()!=null){
				dosageForm = drug.getDosageForm();
				drug.setDosageForm(null);
			}
			if(drug.getHcType()!=null && drug.getHcType().getName()!=null){
				hcType = drug.getHcType(); 
				drug.setHcType(null);
			}
			if(drug.getOtcType()!=null && drug.getOtcType().getName()!=null){
				otcType = drug.getOtcType();
				drug.setOtcType(null);
			}
			
			BeanUtils.copyProperties(drug, body);
			
			if(atcType != null){
				body.setAtcType(atcType.getName());
			}
			if(dosageForm != null){
				body.setDosageForm(dosageForm.getName());
			}
			if(hcType != null){
				body.setHcType(hcType.getName());
			}
			if(otcType != null){
				body.setOtcType(otcType.getName());
			}
			
			
			data.put("errCode", 0);
//			data.put("errMsg", "获取成功");
			data.put("body", body);
		}else{
			data.put("errCode", 10704);
//			data.put("errMsg", "没有找到相应内容");			
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonStr = objectMapper.writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonStr;
	}
	
}
