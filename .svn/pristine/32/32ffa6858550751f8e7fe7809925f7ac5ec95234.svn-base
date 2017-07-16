package com.sinohealth.eszservice.service.news;

import java.math.BigInteger;

import com.sinohealth.eszservice.dto.news.NewsBodyDto;
import com.sinohealth.eszservice.dto.news.NewsDto;
import com.sinohealth.eszservice.dto.news.NewsIsRecommendDto;
import com.sinohealth.eszservice.dto.news.NewsLikeNumDto;

/**
 * 新闻资讯
 * 
 * @author 陈学宏
 *
 */
public interface INewsService {
	/**
	 * 获取某栏目下下一页的资讯
	 * @param colIds
	 * @param newsId
	 * @param pageNum
	 * @param isRecommend
	 * @return
	 */
	NewsDto getNextPageNews(Integer colIds, BigInteger newsId,Integer pageNum);
	
	/**
	 * 获取推荐资讯
	 * @param colIds
	 * @param isRecommend
	 * @return
	 */
	NewsIsRecommendDto getIsRecommendNews(Integer colIds,Integer isRecommend,String appName);
	
	/**
	 * 点赞
	 * @param newsId
	 * @return
	 */
	NewsLikeNumDto saveLikeNum(BigInteger newsId);
	
	/**
	 * 获取某条资讯正文
	 * @param ver
	 * @param newsId
	 * @return
	 */
	NewsBodyDto getNewsContent(Integer ver, BigInteger newsId);
}
