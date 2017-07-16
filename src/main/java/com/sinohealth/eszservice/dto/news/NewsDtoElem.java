package com.sinohealth.eszservice.dto.news;

import java.io.Serializable;
import java.util.List;

import com.sinohealth.eszorm.entity.news.NewsEntity;

public class NewsDtoElem implements Serializable{

	private static final long serialVersionUID = -6631893224046131483L;
	
	private List<NewsEntity> list;

	
	public NewsDtoElem(List<NewsEntity> list) {
		super();
		this.list = list;
	}

	public List<NewsEntity> getList() {
		return list;
	}

	public void setList(List<NewsEntity> list) {
		this.list = list;
	}
	
}
