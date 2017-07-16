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
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.medicine.IAtcTypeDao;
import com.sinohealth.eszservice.dto.medicine.AtcTypeDto;
import com.sinohealth.eszservice.service.medicine.IAtcTypeService;

@Service("atcTypeService")
public class AtcTypeServiceImpl implements IAtcTypeService{
	@Autowired
	private IAtcTypeDao dao;		

	
	public String getAtcTypeList(Integer parentId){
		String jsonStr = "";
		List<AtcTypeDto> list = new ArrayList();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String hql = "select a from AtcTypeEntity a where a.delFlag='0' and a.publishFlag='1' and a.parent.id = :parentId  order by a.pinyin asc";
		Parameter params = new Parameter();
		params.put("parentId", parentId);

		List<AtcTypeEntity> atcTypeList = dao.findByHql(hql, params);

		if(atcTypeList != null && atcTypeList.size()>0){
			data.put("errCode", 0);
//			data.put("errMsg", "获取成功");
			
			for(int i=0;i<atcTypeList.size();i++){
				AtcTypeDto dto = new AtcTypeDto();
				AtcTypeEntity atcType = atcTypeList.get(i);
				
				BeanUtils.copyProperties(atcType, dto);
				
				list.add(dto);
			}
			
			data.put("list", list);
		}else{
			data.put("errCode", 10701);
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
