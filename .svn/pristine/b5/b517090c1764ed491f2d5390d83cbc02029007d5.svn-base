package com.sinohealth.eszservice.service.news.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sinohealth.eszorm.entity.ad.ContentEntity;
import com.sinohealth.eszorm.entity.news.NewsEntity;
import com.sinohealth.eszorm.entity.news.NewspicsEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.ad.IContentDao;
import com.sinohealth.eszservice.dao.news.INewsDao;
import com.sinohealth.eszservice.dao.news.INewspicsDao;
import com.sinohealth.eszservice.dto.news.NewsBodyDto;
import com.sinohealth.eszservice.dto.news.NewsDto;
import com.sinohealth.eszservice.dto.news.NewsIsRecommendDto;
import com.sinohealth.eszservice.dto.news.NewsLikeNumDto;
import com.sinohealth.eszservice.service.base.IAppNameService;
import com.sinohealth.eszservice.service.base.exception.AppNameNotSupportException;
import com.sinohealth.eszservice.service.news.INewsService;
import com.sinohealth.eszservice.service.news.exception.NoChangeException;
import com.sinohealth.eszservice.service.news.exception.NoFoundDataException;
import com.sinohealth.eszservice.service.news.exception.NoPublishOrDeleteException;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

@Service("newsService")
public class NewsServiceImpl implements INewsService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private INewsDao newsDao;

	@Autowired
	private INewspicsDao newspicsDao;

	@Autowired
	private IContentDao contentDao;

	@Autowired
	private IAppNameService appNameService;

	@Override
	public NewsDto getNextPageNews(Integer colIds, BigInteger newsId,
			Integer pageNum) {
		NewsDto dto = new NewsDto();
		NewsEntity news = new NewsEntity();

		try {
			Assert.isTrue(null != colIds, "录入栏目ID不能为空");
			Assert.isTrue(null != pageNum, "本页新闻数不能为空");
		} catch (Exception e) {
			dto.setErrMsg(e.getMessage());
			dto.setErrCode(BaseDto.ERRCODE_OTHERS);
			return dto;
		}

		List<NewsEntity> list = new LinkedList<NewsEntity>();
		int inputNewsId = newsId.intValue();

		// 不输入newsId默认为1，按发布时间降序查询
		if (1 != inputNewsId) {
			// String sqlStr =
			// "from NewsEntity where id < :newsId and colIds like :colIds and status =:status and delFlag =:delFlag "
			// + "order by publishDate desc";
			// TODO 现数据库数据有问题，暂时用ID排序，到时用有效的数据时再改回来
			String sqlStr = "select distinct a from NewsEntity a left join a.colList as b where a.delFlag='0' and b.delFlag='0' and a.id < :newsId and b.id= :colId and a.status =:status  "
					+ "order by a.id desc";
			Parameter params = new Parameter();
			params.put("newsId", newsId);
			// params.put("colIds", "%" + colIds + "%");
			params.put("colId", colIds);
			params.put("status", 1);
			// params.put("delFlag", 0);
			Integer pageNum1 = pageNum + 1;
			list = newsDao.findNewsByHql(sqlStr, pageNum1, params);
		} else {
			// String sqlStr =
			// "from NewsEntity where colIds like :colIds and status =:status and delFlag =:delFlag "
			// + "order by publishDate desc";
			String sqlStr = "select distinct a from NewsEntity a left join a.colList as b where a.delFlag='0' and b.delFlag='0' and b.id= :colId and a.status =:status  "
					+ "order by a.id desc";
			Parameter params = new Parameter();
			// params.put("colIds", "%" + colIds + "%");
			params.put("colId", colIds);
			params.put("status", 1);
			// params.put("delFlag", 0);
			Integer pageNum1 = pageNum + 1;
			list = newsDao.findNewsByHql(sqlStr, pageNum1, params);
		}
		// System.out.println("查询到的记录数： " + list.size());
		// 是否最后一页
		if (list.size() > pageNum) {
			news.setLastPage(0);
		} else {
			news.setLastPage(1);
		}
		dto.setNews(news);
		dto.setNewsDtoElem(list);
		return dto;
	}

	@Override
	public NewsBodyDto getNewsContent(Integer ver, BigInteger newsId) {
		NewsBodyDto dto = new NewsBodyDto();
		// System.err.println("输入的ver 值： "+ver);
		try {
			NewsEntity newsEntity = newsDao.get(newsId);

			Integer dbVer = 0;
			if (null != newsEntity) {
				dbVer = newsEntity.getVer();// 1
			} else {
				throw new NoFoundDataException(
						NewsBodyDto.ERRCODE_NOFOUND_CONTENT, "ID: " + newsId
								+ " 找不到相应的内容.");
			}
			logger.debug("数据库版本：" + dbVer + "  录入或默认版本：" + ver + " 是否要查询："
					+ (dbVer > ver));
			// 数据库中的版本号大于输入的版本号才显示查询内容
			if (dbVer > ver) {
				if (1 == newsEntity.getStatus() && 0 == newsEntity.getDelFlag()) {
					if (2 == newsEntity.getType()) {
						List<NewspicsEntity> bigPics = newspicsDao
								.findPicsByNewsId(newsId);
						if (null != bigPics) {
							newsEntity.setPicList(bigPics);
						}
					}
					// System.err.println("根据newsId 查出的newsEntity: "+newsEntity);
					dto.setNews(newsEntity);
				} else {// status =0或delFlag=1时抛此异常
					throw new NoPublishOrDeleteException(
							NewsBodyDto.ERRCODE_NOPUBLISH_OR_DELETE,
							"资讯未发布、或已删除");
				}
			} else {
				throw new NoChangeException(NewsBodyDto.ERRCODE_NOT_Change,
						"版本无变化，无需更新.");
			}
		} catch (NoPublishOrDeleteException e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (NoChangeException e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (NoFoundDataException e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrCode(NewsBodyDto.ERRCODE_NOFOUND_CONTENT);
			dto.setErrMsg("ID: " + newsId + " 找不到相应的内容.");
		}
		return dto;
	}

	@Override
	public NewsLikeNumDto saveLikeNum(BigInteger newsId) {
		NewsLikeNumDto dto = new NewsLikeNumDto();
		NewsEntity newsEntity = new NewsEntity();
		try {
			// System.out.println("newsId: " + newsId);
			if (0 == newsId.intValue()) {
				throw new SystemErrorExecption("请输入newsId",
						NewsLikeNumDto.ERRCODE_OTHERS);
			}
			newsEntity = newsDao.get(newsId);
			if (null != newsEntity) {
				int likeNum = newsEntity.getLikeNum() + 1;
				// System.out.println("likeNum: " + likeNum);
				newsEntity.setLikeNum(likeNum);
				newsDao.save(newsEntity);
			} else {
				throw new SystemErrorExecption("请输入newsId不存在",
						NewsLikeNumDto.ERRCODE_OTHERS);
			}

			dto.setNews(newsEntity);

		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public NewsIsRecommendDto getIsRecommendNews(Integer colIds,
			Integer isRecommend, String appName) {
		NewsIsRecommendDto dto = new NewsIsRecommendDto();

		try {
			String esSubject = appNameService.getSzSubjectId(appName);

			Integer categoryId = 0;
			if (appName.startsWith("eszDoctor")) {
				categoryId = 2;
			} else {
				categoryId = 3;
			}

			// String hql =
			// "from NewsEntity where colIds like :colIds and status =:status and delFlag =:delFlag "
			// + "and isRecommend =:isRecommend order by publishDate desc";
			String hql = "select distinct a from NewsEntity a left join a.colList as b where a.delFlag='0' and b.delFlag='0' and b.id= :colId and a.status =:status  "
					+ "and a.isRecommend =:isRecommend order by a.publishDate desc";

			Parameter params = new Parameter();
			// params.put("colIds", "%" + colIds + "%");
			params.put("colId", colIds);
			params.put("status", 1);
			// params.put("delFlag", 0);
			params.put("isRecommend", isRecommend);
			Integer pageSize = 4;
			List<NewsEntity> list = newsDao
					.findNewsByHql(hql, pageSize, params);
			// System.out.println("query records: " + list.size());
			List<NewsEntity> adList = getAdList(colIds, categoryId);
			// System.out.println("adList: "+adList.size());
			if (null != list) {
				dto.setList(list);
			}
			if (null != adList) {
				dto.setAdList(adList);
			}
		} catch (AppNameNotSupportException e) {
			dto.setErrCode(BaseDto.ERRCODE_OTHERS);
			dto.setErrMsg("appName错误:" + e.getAppName());
		}
		return dto;
	}

	// 四条推荐资讯中至少有一条是新闻，其它是广告，如没新闻则全是广告，如没广告可以全是新闻s
	public List<NewsEntity> getAdList(Integer colIds, Integer categoryId) {
		List<NewsEntity> list = new ArrayList<NewsEntity>();

		String currentdate = DateUtils.getDate();
		// String hql =
		// "select a from ContentEntity a where a.delFlag='0' and a.publishFlag='1' and (a.beginDate <= :currentdate and a.endDate >= :currentdate) "
		// +
		// "and a.categoryIds like :categoryIds and a.colIds like :colIds and a.position ='3' order by a.updateDate desc";
		String hql = "select distinct a from ContentEntity a left join a.categoryList as b where a.delFlag='0' and b.delFlag='0' and a.publishFlag='1' and (a.beginDate <= :currentdate and a.endDate >= :currentdate) "
				+ "and b.id= :categoryId and a.colIds like :colIds and a.position ='3' order by a.updateDate desc";
		Parameter params = new Parameter();
		params.put("currentdate", DateUtils.parseDate(currentdate));
		// params.put("categoryIds", "%" + categoryId + "%");
		params.put("categoryId", categoryId);
		params.put("colIds", "%" + colIds + "%");

		List<ContentEntity> conList = contentDao.findByHql(hql, params, 4);
		for (int i = 0; i < conList.size(); i++) {
			NewsEntity news = new NewsEntity();
			news.setId(BigInteger.valueOf(conList.get(i).getId()));
			news.setTitle(conList.get(i).getTitle());
			news.setTitlePic(conList.get(i).getPic());
			news.setShareUrl(conList.get(i).getLink());
			list.add(news);
		}
		return list;
	}

}
