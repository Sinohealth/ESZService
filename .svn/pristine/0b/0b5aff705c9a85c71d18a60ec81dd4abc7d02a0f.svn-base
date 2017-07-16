package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.VisitItemId;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IVisitImgValueDao;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IPhaseService;
import com.sinohealth.eszservice.service.visit.IVisitImgValueService;

/**
 * @author 黄世莲
 * 
 */
@Service
public class VisitImgValueServiceImpl implements IVisitImgValueService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IVisitImgValueDao dao;

	@Autowired
	IPhaseService phaseService;

	@Autowired
	IApplicationService applicationService;

	@Override
	public Set<VisitImgEntity> updateValuesForPhase(int phaseId,
			Set<VisitImgEntity> values) {
		Set<VisitImgEntity> savedPics = new HashSet<>(); // 保存的图片

		Map<Integer, Integer> itemIdCount = new HashMap<>();
		TemplatePhaseEntity phase = phaseService.get(phaseId);

		if (null == phase) {
			return savedPics;
		}

		SickEntity sick = phase.getTemplate().getApplication().getSick();

		// 执行时间
		Calendar cal = Calendar.getInstance();

		List<VisitImgEntity> exists = getListByPhaseId(phaseId);
		if (null == exists) {
			exists = new ArrayList<>();
		}

		for (Iterator<VisitImgEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			VisitImgEntity img = iterator.next();

			// 如果上传的图片带有imgId，则认为不变或修改
			if ((null != img.getImgId()) && (0 != img.getImgId().longValue())) {
				// 如果已经存在，不作改变
				int idx = exists.indexOf(img);
				// 如果是有这条数据，否则认为是无效数据
				if (-1 != idx) {
					VisitImgEntity old = exists.get(idx);
					// 原图片和缩略图相等，认为图片没有做更改
					if ((null != old.getImg())
							&& (old.getImg().equals(img.getImg()))
							&& (null != old.getThumb())
							&& (old.getThumb().equals(img.getThumb()))) {
					} else { // 否则认为是更改
						img.setPhase(phase);
						img.setPostTime(cal.getTime());
						update(img);
					}
					savedPics.add(old);
					exists.remove(idx);
				} else {
					// 如果带了imgId，但是没有找到原值，跳过
					continue;
				}
			} else {
				img.setImgId(null);
				img.setPhase(phase);
				img.setPostTime(cal.getTime());
				img.setSick(sick);
				VisitImgEntity saved = addNew(img);
				savedPics.add(saved);
			}
			itemIdCount.put(img.getItemId(), 1);
		}

		// 删除原有的，但未上传的值
		for (VisitImgEntity visitImgEntity : exists) {
			delete(visitImgEntity);
		}

		return savedPics;
	}

	public void delete(VisitImgEntity o) {
		dao.delete(o);
	}

	public VisitImgEntity addNew(VisitImgEntity o) {
		return dao.save(o);

	}

	public void update(VisitImgEntity o) {
		dao.save(o);
	}

	private List<VisitImgEntity> getListByPhaseId(int phaseId) {
		String hql = "FROM VisitImgEntity WHERE phase.templPhaseId=:p1";
		return dao.findByHql(hql, new Parameter(phaseId));
	}

	@Override
	public List<VisitImgEntity> getImgBySickId(int sickId) {
		// TODO 检验项itemID以后得改为动态配置
		String hql = "FROM VisitImgEntity WHERE sick.id=:p1 and itemId IN (1401,1402,1403,1404,1405,"
				+ "1406,1407,1408,1409,1410,1411,1412,1413,1414,1415,1416,1417,1418,1419,1420,1421,1422,1423)";
		return dao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public List<VisitImgEntity> getImgsBySickId(int sickId) {
		String hql = "select i.img_id as imgId,i.item_id as itemId,i.img as img,i.thumb as thumb,i.status as status,i.remarks as remarks,i.post_time as postTime "
				+ "from visit_img_results i, visit_items m "
				+ "where i.item_id = m.item_id and m.cat =4 and sick_id=? order by i.post_time desc";

		Query query = dao.getSession().createSQLQuery(hql)
				.setInteger(0, Integer.valueOf(sickId))
				.setResultTransformer(Transformers.TO_LIST);

		List<VisitImgEntity> list = query.list();
		return list;
	}

	@Override
	public List<VisitImgEntity> getItemImgBySickId(int sickId) {
		String hql = "select i.img_id as imgId,i.item_id as itemId,i.img as img,i.thumb as thumb,i.status as status,i.remarks,i.post_time "
				+ "from visit_img_results i, visit_items m "
				+ "where i.item_id = m.item_id and m.cat =3 and sick_id=? order by i.post_time desc";
		Query query = dao.getSession().createSQLQuery(hql)
				.setInteger(0, Integer.valueOf(sickId))
				.setResultTransformer(Transformers.TO_LIST);

		// query.setParameter("sickId", sickId);
		List<VisitImgEntity> list = query.list();
		return list;
	}

	@Override
	public List<VisitImgEntity> getItemImgBySickId(int sickId, int applyId) {
		// TODO 检验项itemID以后得改为动态配置
		String hql = "FROM VisitImgEntity WHERE sick.id=:p1 and application.applyId=:p2 and cat=3";
		return dao.findByHql(hql, new Parameter(sickId, applyId));
	}

	@Override
	public void updateStatus(long imgId, int status, String remarks) {
		VisitImgEntity o = get(imgId);
		if (null == o) {
			return;
		}
		o.setStatus(status);
		o.setRemarks(remarks);
	}

	@Override
	public VisitImgEntity get(long id) {
		return dao.get(id);

	}

	@Override
	public List<VisitImgEntity> getLeaveHosPics(int sickId) {
		String hql = "FROM VisitImgEntity where sick.id = :p1 and itemId=1001 order by postTime desc";
		return dao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public List<VisitImgEntity> getCasesPics(int sickId) {
		String hql = "FROM VisitImgEntity where sick.id = :p1 and itemId=1002 order by postTime desc";
		return dao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public List<VisitImgEntity> getCaseLeaveHosPics(int sickId) {
		// TODO 检验项itemID以后得改为动态配置
		String hql = "FROM VisitImgEntity where sick.id = :p1 and itemId in (1001,1002) order by postTime desc";
		return dao.findByHql(hql, new Parameter(sickId), 1, 0);
	}

	/**
	 * @deprecated v1.03后，不再使用
	 */
	@Override
	public void updateCheckValuesForApply(int applyId,
			List<VisitImgEntity> values) {
		logger.debug("更新申请单的图片：{}，{}张", applyId, values.size());

		ApplicationEntity apply = applicationService.get(applyId);
		if (null == apply) {
			return;
		}
		List<VisitImgEntity> exists = getCasesByAoplyId(applyId);
		if (null == exists) {
			exists = new ArrayList<>();
		}

		// 执行时间
		Calendar cal = Calendar.getInstance();

		for (Iterator<VisitImgEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			VisitImgEntity img = iterator.next();

			// 如果上传的图片带有imgId，则认为不变或修改
			if ((null != img.getImgId()) && (0 != img.getImgId().longValue())) {
				// 如果已经存在，不作改变
				int idx = exists.indexOf(img);
				// 如果是有这条数据，否则认为是无效数据
				if (-1 != idx) {
					VisitImgEntity old = exists.get(idx);
					// 原图片和缩略图相等，认为图片没有做更改
					if ((null != old.getImg())
							&& (old.getImg().equals(img.getImg()))
							&& (null != old.getThumb())
							&& (old.getThumb().equals(img.getThumb()))) {
						exists.remove(idx);
						logger.debug("图片未更改：{}", old);
					} else { // 否则认为是更改
						logger.info("修改了复诊图片：{}", img);
						img.setApplication(apply);
						img.setPostTime(cal.getTime());
						img.setSick(apply.getSick());
						update(img);
						exists.remove(idx);
					}
				} else {
					logger.info("找不到原图片：{}", img);
				}
				// 如果带了imgId，但是没有找到原值，跳过
				continue;
			} else if ((null != img.getImgId())
					&& (img.getImgId().longValue() == 0)) {
				img.setImgId(null);
			}
			img.setApplication(apply);
			img.setPostTime(cal.getTime());
			img.setSick(apply.getSick());
			addNew(img);
			logger.info("新增了=了复诊图片：{}", img);
		}

		// 删除原有的，但未上传的值
		for (VisitImgEntity visitImgEntity : exists) {
			// 并且不是门诊、住院记录
			if (!((visitImgEntity.getItemId() == VisitItemId.CASE_RECORD_ID) || (visitImgEntity
					.getItemId() == VisitItemId.LEAVE_HOS_ID))) {
				delete(visitImgEntity);
			}
		}

	}

	/**
	 * @deprecated
	 */
	public List<VisitImgEntity> getCasesByAoplyId(int applyId) {
		String hql = "FROM VisitImgEntity WHERE application.applyId=:p1";
		return dao.findByHql(hql, new Parameter(applyId));
	}

	/**
	 * @deprecated
	 */
	@Override
	public void updateCasesValuesForApply(int applyId,
			List<VisitImgEntity> values) {
		logger.debug("更新申请单的图片：{}，{}张", applyId, values.size());

		ApplicationEntity apply = applicationService.get(applyId);
		if (null == apply) {
			return;
		}
		List<VisitImgEntity> exists = getCasesByAoplyId(applyId);
		if (null == exists) {
			exists = new ArrayList<>();
		}

		// 执行时间
		Calendar cal = Calendar.getInstance();

		for (Iterator<VisitImgEntity> iterator = values.iterator(); iterator
				.hasNext();) {
			VisitImgEntity img = iterator.next();

			// 如果上传的图片带有imgId，则认为不变或修改
			if ((null != img.getImgId()) && (0 != img.getImgId().longValue())) {
				// 如果已经存在，不作改变
				int idx = exists.indexOf(img);
				// 如果是有这条数据，否则认为是无效数据
				if (-1 != idx) {
					VisitImgEntity old = exists.get(idx);
					// 原图片和缩略图相等，认为图片没有做更改
					if ((null != old.getImg())
							&& (old.getImg().equals(img.getImg()))
							&& (null != old.getThumb())
							&& (old.getThumb().equals(img.getThumb()))) {
						exists.remove(idx);
						logger.debug("图片未更改：{}", old);
					} else { // 否则认为是更改
						logger.info("修改了复诊图片：{}", img);
						img.setApplication(apply);
						img.setPostTime(cal.getTime());
						img.setSick(apply.getSick());
						update(img);
						exists.remove(idx);
					}
				} else {
					logger.info("找不到原图片：{}", img);
				}
				// 如果带了imgId，但是没有找到原值，跳过
				continue;
			} else if ((null != img.getImgId())
					&& (img.getImgId().longValue() == 0)) {
				img.setImgId(null);
			}
			img.setApplication(apply);
			img.setPostTime(cal.getTime());
			img.setSick(apply.getSick());
			addNew(img);
			logger.info("新增了=了复诊图片：{}", img);
		}

		// 删除原有的，但未上传的值
		for (VisitImgEntity visitImgEntity : exists) {
			// 并且是门诊、住院记录
			// 这里要注意，因为门诊住院和检查检验是同一张表，所以不能删除检查检验的数据
			if ((visitImgEntity.getItemId() == VisitItemId.CASE_RECORD_ID)
					|| (visitImgEntity.getItemId() == VisitItemId.LEAVE_HOS_ID)) {
				delete(visitImgEntity);
			}
		}

	}

	@Override
	public boolean isDataFormatCorrectWithPhase(int phaseId) {
		Parameter params = new Parameter();
		params.put("phaseId", phaseId);

		String hql = "SELECT COUNT(*) FROM VisitImgEntity WHERE phase.templPhaseId=:phaseId AND status<>0";
		Query query = dao.prepareQuery(dao.getSession(), hql, params);
		Number r = (Number) query.uniqueResult();

		// TODO 如果重新提交的图片不是删除而是标记删除

		return r.intValue() == 0;
	}

	@Override
	public boolean isDataFormatCorrectWithApply(int applyId) {
		Parameter params = new Parameter();
		params.put("applyId", applyId);

		String hql = "SELECT COUNT(*) FROM VisitImgEntity WHERE application.applyId=:applyId AND status<>0";
		Query query = dao.prepareQuery(dao.getSession(), hql, params);
		Number r = (Number) query.uniqueResult();

		// TODO 如果重新提交的图片不是删除而是标记删除

		return r.intValue() == 0;
	}

	@Override
	public int getCorrectCountByPhase(Integer templPhaseId) {
		Parameter params = new Parameter();
		params.put("templPhaseId", templPhaseId);

		String hql = "SELECT COUNT(*) FROM VisitImgEntity WHERE phase.templPhaseId=:templPhaseId AND status<>0";
		Query query = dao.prepareQuery(dao.getSession(), hql, params);
		Number r = (Number) query.uniqueResult();

		// TODO 如果重新提交的图片不是删除而是标记删除

		return r.intValue();
	}
}
