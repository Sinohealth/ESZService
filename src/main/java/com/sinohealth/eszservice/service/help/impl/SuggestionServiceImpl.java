package com.sinohealth.eszservice.service.help.impl;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.sinohealth.eszorm.entity.help.SuggestionEntity;
import com.sinohealth.eszservice.dao.help.ISuggestionDao;
import com.sinohealth.eszservice.dto.help.SuggestionDto;
import com.sinohealth.eszservice.service.help.ISuggestionService;

@Service("suggestionService")
public class SuggestionServiceImpl implements ISuggestionService{
	@Autowired
	@Qualifier("suggestionDao")
	private ISuggestionDao dao;	
	
	public String save(SuggestionDto dto){
		String jsonStr = "";
		Map<String, Object> data = Maps.newHashMap();
		
		SuggestionEntity suggestion = new SuggestionEntity();
		BeanUtils.copyProperties(dto, suggestion);
		
		Date date = new Date();
		suggestion.setCreateDate(date);
		
		try{
			dao.save(suggestion);			
			data.put("errCode", 0);
		}catch(Exception e){
			data.put("errCode", 10403);
			e.printStackTrace();
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
