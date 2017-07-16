package com.sinohealth.eszservice.service.base;

import java.util.Date;
import java.util.List;

import com.sinohealth.eszorm.entity.base.GradeEntity;
import com.sinohealth.eszservice.dto.doctor.DoctorGradesDto;

/**
 * 积分service
 * 
 * @author 黄世莲
 * 
 */
public interface IGradeService {

	/**
	 * 根据ID与专科，找到所有获得的积分项目
	 * 
	 * @param userId
	 * @return
	 */
	public List<GradeEntity> findAll(Integer userId, String szSubject);

	/**
	 * 根据ID，找到所有获得的积分项目
	 * 
	 * @param userId
	 * @return
	 */
	public List<GradeEntity> findAll(Integer userId);

	/**
	 * 根据Token，获得积分信息
	 * 
	 * @param userId
	 * @param szSbuject
	 * @return
	 */
	DoctorGradesDto getDoctorGrades(int userId, String szSbuject);

	void save(GradeEntity grade);

	public Object getSickGrades(String token);

	/**
	 * 用户增加积分
	 * 
	 * @param userId
	 * @param actionName
	 *            积分项ID
	 * @param szSubject
	 *            专科
	 */
	public void addAction(int userId, String actionName, String szSubject);

	/**
	 * 查找这一天是否已经有此项积分
	 * 
	 * @param doctorId
	 * @param actionKey
	 * @param szSubject
	 * @param today
	 * @return
	 */
	public GradeEntity getGrade(Integer doctorId, String actionKey,
			String szSubject, Date date);

	/**
	 * 如果今天还没有，就添加
	 * 
	 * @param userId
	 * @param patientTakeMedicine
	 * @param szSubject
	 */
	public void addActionIfNotExistToday(int userId,
			String patientTakeMedicine, String szSubject);

}
