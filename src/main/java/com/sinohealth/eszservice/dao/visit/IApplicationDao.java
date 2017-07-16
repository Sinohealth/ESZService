package com.sinohealth.eszservice.dao.visit;

import java.util.List;

import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.common.persistence.IGenericDao;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;

public interface IApplicationDao extends
		IGenericDao<ApplicationEntity, Integer> {

	PaginationSupport getApplicationReport(int doctorId, String subject,
			int status, int page, int pageSize);

	List<ApplicationEntity> getStatusBySickId(int sickId);

	/**
	 * 统计医生随访状态列表
	 * 
	 * @return
	 */
	List getStatusCountByDoctorId(int doctorId, String szSubject);

}
