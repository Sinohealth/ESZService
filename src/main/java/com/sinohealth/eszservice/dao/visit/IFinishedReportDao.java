package com.sinohealth.eszservice.dao.visit;


import com.sinohealth.eszorm.entity.visit.FinishedReportEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;

/**
 * 3.4.4获取“已完成”患者列表
 * @author 陈学宏
 *
 */
public interface IFinishedReportDao extends IGenericDao<FinishedReportEntity, Integer> {
	PaginationSupport getFinishedReport(int doctorId,String subject,int status,int page,int pageSize);
}
