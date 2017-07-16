package com.sinohealth.eszservice.service.help.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.help.CategoryEntity;
import com.sinohealth.eszorm.entity.help.QuestionEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.help.ICategoryDao;
import com.sinohealth.eszservice.dao.help.IQuestionDao;
import com.sinohealth.eszservice.dto.help.QuestionBody;
import com.sinohealth.eszservice.dto.help.QuestionDto;
import com.sinohealth.eszservice.service.help.IQuestionService;

@Service("questionService")
public class QuestionServiceImpl implements IQuestionService{
	@Autowired
	private IQuestionDao dao;	
	@Autowired
	private ICategoryDao categoryDao;	
	
	

	
	public String getQuestionList(String appName,Integer ver){
		String jsonStr = "";
		List<QuestionDto> list = new ArrayList();
		Map<String, Object> data = new LinkedHashMap<String,Object>();
//		String hql = "select a from QuestionEntity a where a.delFlag='0' and a.publishFlag='1' and a.categoryIds like :categoryIds  order by a.id desc";
		String hql = "select distinct a from QuestionEntity a left join a.categoryList as b where a.delFlag='0' and b.delFlag='0' and a.publishFlag='1' and b.appName =:appName order by a.id desc";
		Parameter params = new Parameter();
		params.put("appName",appName.replace("ForAndroid", "").replace("ForIOS", ""));
		
		CategoryEntity category = (CategoryEntity)categoryDao.getByHql("select a from CategoryEntity a where a.delFlag='0' and a.appName='"+appName.replace("ForAndroid", "").replace("ForIOS", "")+"'",null);
		
		List<QuestionEntity> questionList = dao.findByHql(hql, params);

		if(questionList != null && questionList.size()>0 && category!=null && category.getVer() != ver){
			data.put("errCode", 0);
//			data.put("errMsg", "获取成功");
			data.put("ver", category.getVer());
			
			for(int i=0;i<questionList.size();i++){
				QuestionDto dto = new QuestionDto();
				QuestionEntity question = questionList.get(i);
				dto.setId(question.getId());
				dto.setTitle(question.getTitle());
				dto.setLink(question.getLink());
				list.add(dto);
			}
			
			data.put("list", list);
		}else if(category!=null && category.getVer() == ver){
			data.put("errCode", 0);
		}else{
			data.put("errCode", 10401);
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
	
	public String getQuestionList(String appName,String isOften,Integer ver){
		String jsonStr = "";
		List<QuestionDto> list = new ArrayList();
		Map<String, Object> data = new LinkedHashMap<String, Object>();

		String hql = "select distinct a from QuestionEntity a left join a.categoryList as b where a.delFlag='0' and b.delFlag='0' and a.publishFlag='1' and b.appName =:appName and a.isOften = :isOften  order by a.id desc";
		Parameter params = new Parameter();
		params.put("appName", appName.replace("ForAndroid", "").replace("ForIOS", ""));
		params.put("isOften", isOften);
		
		CategoryEntity category = (CategoryEntity)categoryDao.getByHql("select a from CategoryEntity a where a.delFlag='0' and a.appName='"+appName.replace("ForAndroid", "").replace("ForIOS", "")+"'",null);
		
		List<QuestionEntity> questionList = dao.findByHql(hql, params);
		if(questionList != null && questionList.size()>0 && category!=null && category.getVer() != ver){
			data.put("errCode", 0);
//			data.put("errMsg", "获取成功");
			data.put("ver", category.getVer());
			
			for(int i=0;i<questionList.size();i++){
				QuestionDto dto = new QuestionDto();
				QuestionEntity question = questionList.get(i);
				dto.setId(question.getId());
				dto.setTitle(question.getTitle());
				dto.setLink(question.getLink());
				list.add(dto);
			}
			
			data.put("list", list);
		}else if(category!=null && category.getVer() == ver){
			data.put("errCode", 0);
		}else{
			data.put("errCode", 10401);
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
	
	public String getQuestionContent(Integer id){
		String jsonStr = "";
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		QuestionEntity question = dao.get(id);
		if(question != null){
			QuestionBody body = new QuestionBody();
			body.setTitle(question.getTitle());
			body.setContent(question.getAppContent());
			data.put("errCode", 0);
//			data.put("errMsg", "获取成功");
			data.put("body", body);
		}else{
			data.put("errCode", 10402);
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
