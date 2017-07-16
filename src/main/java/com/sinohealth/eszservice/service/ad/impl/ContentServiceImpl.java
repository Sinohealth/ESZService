package com.sinohealth.eszservice.service.ad.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.ad.ContentEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.ad.IContentDao;
import com.sinohealth.eszservice.dto.ad.ContentBody;
import com.sinohealth.eszservice.dto.ad.ContentDto;
import com.sinohealth.eszservice.service.ad.IContentService;

@Service("contentService")
public class ContentServiceImpl implements IContentService{
	@Autowired
	private IContentDao dao;	
	

	
	public String getAdList(ContentDto dto){
		String jsonStr = "";
		List<ContentBody> list = new ArrayList();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String currentdate = DateUtils.getDate();
//		String hql = "select a from ContentEntity a where a.delFlag='0' and a.publishFlag='1' and (a.beginDate <= :currentdate and a.endDate >= :currentdate) and a.categoryIds like :categoryIds and a.position = :position ";
//		String hql = "select distinct a from ContentEntity a left join a.categoryList as b where a.delFlag='0' and b.delFlag='0' and a.publishFlag='1' and (a.beginDate <= :currentdate and a.endDate >= :currentdate) and b.id= :categoryId and a.position = :position ";
//		Parameter params = new Parameter();
//		params.put("currentdate", DateUtils.parseDate(currentdate));
//		params.put("categoryId",dto.getCategoryId());
//		params.put("position", dto.getPosition());
		
		String hql = "select distinct a from ContentEntity a left join a.categoryList as b where a.delFlag='0' and b.delFlag='0' and a.publishFlag='1' and (a.beginDate <= :currentdate and a.endDate >= :currentdate) and b.appName= :appName and a.position = :position ";
		Parameter params = new Parameter();
		params.put("currentdate", DateUtils.parseDate(currentdate));
		params.put("appName",dto.getAppName().replace("ForAndroid", "").replace("ForIOS", ""));
		params.put("position", dto.getPosition());		
		
		if(dto.getColId() != null && !dto.getColId().equals("")){
			hql += "  and a.colIds like :colId ";
			params.put("colId", "%"+dto.getColId()+"%");
		}
		
		hql += "  order by a.updateDate desc";

		List<ContentEntity> contentList = dao.findByHql(hql, params,dto.getNum());

		if(contentList != null && contentList.size()>0){
			data.put("errCode", 0);
			
			for(int i=0;i<contentList.size();i++){
				ContentBody body = new ContentBody();
				ContentEntity content = contentList.get(i);
				body.setId(content.getId());
				body.setTitle(content.getTitle());
				body.setPic(content.getPic());
				body.setLink(content.getLink());
				list.add(body);
			}
			
			data.put("list", list);
		}else{
			data.put("errCode", 10601);
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonStr = objectMapper.writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonStr;		
	}
	
	public String updateHits(Integer id){
		String jsonStr = "{\"errCode\":0}";
		ContentEntity content = dao.get(id);
		content.setHits(content.getHits() + 1);
		return jsonStr;
	}
	
}
