package com.sinohealth.eszservice.service.visit;

import java.util.Date;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.visit.exception.ChangePhaseExecption;
import com.sinohealth.eszservice.service.visit.exception.NoPhaseSelectedExecption;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.TemplIdErrorException;
import com.sinohealth.eszservice.vo.visit.TemplContentVo;

public interface ITemplateService {

	/**
	 * 保存模板
	 * 
	 * @param doctorId
	 *            医生ID
	 * @param szSubject
	 *            专科
	 * @param tp
	 *            模板内容
	 * @param visible
	 *            是否可见
	 * @param startDate
	 *            随访开始时间
	 * @param templName
	 *            模板名称
	 * @param disease
	 *            病种
	 * @param stdTemplate
	 *            标准模板
	 * @return
	 * @throws SystemErrorExecption
	 */
	public TemplateEntity addTempl(int doctorId, SzSubjectEntity szSubject,
			TemplateEntity tp, Date startDate, String templName,
			DiseaseEntity disease, TemplateEntity stdTemplate)
			throws SystemErrorExecption;

	public TemplateEntity updateTempl(int doctorId, SzSubjectEntity szSubject,
			TemplateEntity tp, Date startDate, String templName,
			DiseaseEntity disease, TemplateEntity stdTemplate, int templId)
			throws SystemErrorExecption;

	/**
	 * 删除模板
	 * 
	 * @param templId
	 * @return
	 */
	void deleteTempl(String templIds) throws TemplIdErrorException;

	TemplateEntity get(int id);

	/**
	 * 根据科室，找到医生的模板
	 * 
	 * @param szSubject
	 * @param doctorId
	 * @return
	 */
	List<TemplateEntity> getTemplates(String szSubject, int doctorId);

	/**
	 * 根据申请ID，获取时间轴信息
	 * 
	 * @param applyId
	 * @return
	 */
	BaseDto getTimeline(int applyId);

	/**
	 * 根据科室，找到医生的模板
	 * 
	 * @param szSubject
	 * @param doctorId
	 * @param visible
	 * @return
	 */
	List<TemplateEntity> getList(String szSubject, int doctorId, boolean visible);

	public TemplateEntity addPlan(int applyId, TemplContentVo templVo,
			int stdTemplId, Date visitStartDate)
			throws NoPhaseSelectedExecption;

	void updatePlan(int applyId, TemplContentVo templVo, Date visitStartDate)
			throws ChangePhaseExecption;

	public void update(TemplateEntity template);
}
