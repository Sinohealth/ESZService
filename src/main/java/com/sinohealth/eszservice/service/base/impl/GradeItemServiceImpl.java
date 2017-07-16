package com.sinohealth.eszservice.service.base.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.base.GradeItemEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.dao.base.IGradeItemDao;
import com.sinohealth.eszservice.service.base.IGradeItemService;

@Service("gradeItemService")
public class GradeItemServiceImpl implements IGradeItemService {

	@Autowired
	private IGradeItemDao dao;

	@Autowired
	private RedisTemplate<String, GradeItemEntity> redisTemplate;

	private BoundHashOperations<String, String, GradeItemEntity> ops;

	@Override
	public GradeItemEntity getByKey(String key) {
		GradeItemEntity o = null;
		if (null != ops()) {
			if (ops().hasKey(key)) {
				o = ops().get(key);
			} else {
				o = dao.getByKey(key);
				ops().put(key, o);
			}
		} else {
			o = dao.getByKey(key);
		}
		return o;
	}

	private BoundHashOperations<String, String, GradeItemEntity> ops() {
		if (null == ops) {
			ops = redisTemplate.boundHashOps("table:gradeItems");
			int expiredTime = Integer.parseInt(Global
					.getConfig("gradeItems.data.cache.expired"));
			if (0 == expiredTime) { // 如果0表示不使用缓存
				return null;
			}
			ops.expire(expiredTime, TimeUnit.HOURS);
		}
		return ops;
	}
}
