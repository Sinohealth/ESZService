package com.sinohealth.eszservice.dao.visit;

import java.util.List;

import com.sinohealth.eszorm.entity.visit.BodySignLogEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;

/**
 * 体征项日志DAO
 * 
 * @author 黄世莲
 * 
 */
public interface IBodySignLogDao extends
		IGenericDao<BodySignLogEntity, Integer> {

	/**
	 * 体征最后一条修改日志
	 * 
	 * @return
	 */
	BodySignLogEntity getLastLog(int templId, int itemId);

	/**
	 * 获取体征项的日志。按时间顺序排序
	 * 
	 * @return
	 */
	List<BodySignLogEntity> getList(int templId, int itemId);

	/**
	 * 获取体征项的日志。按时间顺序排序
	 * 
	 * @return
	 */
	List<BodySignLogEntity> getList(int templId);
}
