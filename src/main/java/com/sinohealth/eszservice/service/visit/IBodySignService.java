package com.sinohealth.eszservice.service.visit;

import java.util.Date;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.BodySignEntity;
import com.sinohealth.eszservice.dto.visit.elem.CaseHistoryRateElem;

/**
 * 体征Service
 * 
 * @author 黄世莲
 * 
 */
public interface IBodySignService {

	/**
	 * 根据阶段ItemId获取到相应的体征项
	 * 
	 * @param phaseId
	 * @param itemId
	 * @return
	 */
	BodySignEntity getByItemId(Integer phaseId, int itemId);

	BodySignEntity save(BodySignEntity bodySign);

	/**
	 * 根据模板，找到模板的全部项
	 * 
	 * @param templId
	 * @return
	 */
	List<BodySignEntity> getBodySignsByTempl(int templId);

	/**
	 * 根据模板和cat，找到模板的全部项
	 * 
	 * @param templId
	 * @param cat
	 * @return
	 */
	List<BodySignEntity> getBodySignsByTempl(int templId, int cat);

	BodySignEntity addNew(BodySignEntity o);

	/**
	 * 统计体征项报告。<br/>
	 * 返回一个二维数组，int[0]=实填数，int[1]=应填数
	 * 
	 * @param templId
	 * @return
	 */
	int[] countBodySignReport(int templId);

	/**
	 * 获取与随访一致的日常管理项完整性列表
	 * 
	 * @param templId
	 * @return
	 */
	List<CaseHistoryRateElem> getCaseHistoryRateList(int templId);

	/**
	 * 增加日志
	 */
	void addLog(int templId, BodySignEntity bodySign);

	/**
	 * 更新体征项日志
	 * 
	 * @param templId
	 * @param bodySign
	 * @param startDate
	 */
	void updateLog(int templId, BodySignEntity bodySign);

	/**
	 * 将日志更新时间为完成
	 */
	void updateLogToFinished(int templId, int itemId, Date endDate);

}
