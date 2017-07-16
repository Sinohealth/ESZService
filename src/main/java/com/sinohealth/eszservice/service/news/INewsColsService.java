package com.sinohealth.eszservice.service.news;

import com.sinohealth.eszservice.dto.news.NewsColsDto;


/**
 * 新闻资讯栏目
 * 
 * @author 陈学宏
 * 
 */
public interface INewsColsService {

	/**
	 * 获取新闻资讯栏目
	 * 
	 * @param ver
	 * @return
	 */
	NewsColsDto getNewsCols(Integer ver,String appName);
}
