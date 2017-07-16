package com.sinohealth.eszservice.service.visit.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.VisitItemId;
import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.sick.SickProfileEntity;
import com.sinohealth.eszorm.entity.visit.AppCasesComponent;
import com.sinohealth.eszorm.entity.visit.AppCurPhaseComponent;
import com.sinohealth.eszorm.entity.visit.AppInspection;
import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.BodySignEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.ReasonEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszservice.common.config.ErrorMessage;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.dto.ConstantSickVisitErrs;
import com.sinohealth.eszservice.common.persistence.PaginationSupport;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.StringUtil;
import com.sinohealth.eszservice.dao.doctor.IDoctorDao;
import com.sinohealth.eszservice.dao.visit.IApplicationDao;
import com.sinohealth.eszservice.dao.visit.IFinishedReportDao;
import com.sinohealth.eszservice.dao.visit.IReasonDao;
import com.sinohealth.eszservice.dto.doctor.DoctorSearchDto;
import com.sinohealth.eszservice.dto.visit.AppDetailsDto;
import com.sinohealth.eszservice.dto.visit.AppReasonDto;
import com.sinohealth.eszservice.dto.visit.ApplicationDto;
import com.sinohealth.eszservice.dto.visit.ApplyDto;
import com.sinohealth.eszservice.dto.visit.CaseHistoryRateListDto;
import com.sinohealth.eszservice.dto.visit.DoctorStatusCountDto;
import com.sinohealth.eszservice.dto.visit.FinishedReportDto;
import com.sinohealth.eszservice.dto.visit.SickApplicationDto;
import com.sinohealth.eszservice.dto.visit.SickDetailDto;
import com.sinohealth.eszservice.dto.visit.SickSearchDto;
import com.sinohealth.eszservice.dto.visit.elem.ApplicationElem;
import com.sinohealth.eszservice.dto.visit.elem.CaseHistoryRateElem;
import com.sinohealth.eszservice.dto.visit.sick.UpdateArchiveDto;
import com.sinohealth.eszservice.service.base.IAppNameService;
import com.sinohealth.eszservice.service.doctor.IDoctorOnlineService;
import com.sinohealth.eszservice.service.doctor.exception.SubjectValidateException;
import com.sinohealth.eszservice.service.sick.ISickOnlineService;
import com.sinohealth.eszservice.service.sick.ISickService;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IBodySignService;
import com.sinohealth.eszservice.service.visit.ICheckItemValueService;
import com.sinohealth.eszservice.service.visit.ISzSubjectService;
import com.sinohealth.eszservice.service.visit.ITakeMedRecordService;
import com.sinohealth.eszservice.service.visit.ITemplateService;
import com.sinohealth.eszservice.service.visit.IVisitImgValueService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.exception.ApplicationDuplicationExecption;
import com.sinohealth.eszservice.service.visit.exception.ChangePhaseExecption;
import com.sinohealth.eszservice.service.visit.exception.NoPhaseSelectedExecption;
import com.sinohealth.eszservice.service.visit.exception.OtherReasonErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.ReasonErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.StatusErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;
import com.sinohealth.eszservice.service.visit.exception.UpdatePlanExecption;
import com.sinohealth.eszservice.service.visit.exception.VisitItemNotFoundExecption;
import com.sinohealth.eszservice.vo.visit.TemplContentVo;

@Service("applicationService")
public class ApplicationServiceImpl implements IApplicationService {
	private final Logger logger = LoggerFactory
			.getLogger(ApplicationServiceImpl.class);

	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	ISickService sickService;

	@Autowired
	private IDoctorDao doctorDao;

	@Autowired
	IApplicationDao applicationDao;

	@Autowired
	private IFinishedReportDao finishedReportDao;

	@Autowired
	private IReasonDao reasonDao;

	@Autowired
	private IVisitImgValueService visitImgValueService;

	@Autowired
	private IBodySignService bodySignService;

	@Autowired
	private ISzSubjectService szSubjectService;

	@Autowired
	private ITakeMedRecordService takeMedRecordService;

	@Autowired
	private ICheckItemValueService checkItemValueService;

	@Autowired
	private IVisitItemService visitItemService;

	@Autowired
	private ITemplateService templateService;

	@Autowired
	private IAppNameService appNameService;

	@Autowired
	private IDoctorOnlineService doctorOnlineService;

	@Autowired
	private ISickOnlineService sickOnlineService;

	/**
	 * 对处方的处理为：<br/>
	 * <ul>
	 * <li>1. 如果上传的内容包括imgId，则：
	 * <ul>
	 * <li>1.1 在数据库中查找到包含此imgId的，并且img,thumb字段内容相同，则认为是不作改动。</li>
	 * <li>1.2 在数据库中查找到包含此imgId，但是img,thumb字段内容却不相同，则认为是对原imgId的处方进行了改动。</li>
	 * 此时，需要将此imgId的处方标记为重传（reuploaded=1），并将img,thumb插入新的记录。
	 * <li>1.3 如果在数据库找不到imgId，忽略处理；</li>
	 * </ul>
	 * <li>2. 如果在上传的内容不包括imgId，则插入新的记录
	 * </ul>
	 */
	@Override
	public UpdateArchiveDto updateArchive(int applyId,
			List<VisitPrescriptionEntity> prescriptions,
			List<VisitImgEntity> items, AppPastHistoryComponent pastHistory,
			String familyHistory) {
		UpdateArchiveDto dto = new UpdateArchiveDto();
		try {
			ApplicationEntity app = applicationDao.get(applyId);
			if (null == app) {
				throw new SystemErrorExecption("找不到相应的申请信息",
						BaseDto.ERRCODE_OTHERS);
			}
			Assert.notNull(app, "找不到相应的申请信息");

			Calendar cal = Calendar.getInstance();

			// ===============================
			// ---- 处方 -------------------
			// ===============================
			List<VisitPrescriptionEntity> origPresList = app.getPrescription()
					.getPics();
			List<VisitPrescriptionEntity> newPresList = new ArrayList<>(); // 新的处方列表
			for (VisitPrescriptionEntity e : prescriptions) {
				if (!(StringUtils.hasLength(e.getMedPic()) && StringUtils
						.hasLength(e.getSmallMedPic()))) {
					continue;
				}

				int idx = origPresList.indexOf(e);
				if ((e.getId() != null) && (-1 != idx)) { // 如果提交了imgId，并且是数据库已经有的
					VisitPrescriptionEntity origImg = origPresList.get(idx);
					if (!StringUtils.startsWithIgnoreCase(e.getMedPic(),
							"https")) { // 如果不是https开头
						if (!origImg.getMedPic().equals(e.getMedPic())) { // 图片变化，说明重新上传了
							logger.info("修改处方：{}改为{}", origImg, e);
							// 状态改为已重新上传，并重新上传一张
							origImg.setReuploaded(1);
							origImg.setReuploadDate(cal.getTime());
							VisitPrescriptionEntity newImg = new VisitPrescriptionEntity();
							newImg.setMedPic(e.getMedPic());
							newImg.setSmallMedPic(e.getSmallMedPic());
							newImg.setSick(app.getSick());
							newImg.setApplication(app);
							newImg.setDoctor(app.getDoctor());
							newImg.setCreateDate(cal.getTime());

							newPresList.add(newImg);
							origPresList.add(newImg);
						}
					}
					newPresList.add(origImg);
				} else { // 数据库中不存在
					VisitPrescriptionEntity newImg = new VisitPrescriptionEntity();
					newImg.setMedPic(e.getMedPic());
					newImg.setSmallMedPic(e.getSmallMedPic());
					newImg.setSick(app.getSick());
					newImg.setApplication(app);
					newImg.setDoctor(app.getDoctor());
					newImg.setCreateDate(cal.getTime());
					newPresList.add(newImg);
					origPresList.add(newImg);
					logger.info("新增处方：{}", e);
				}
			}

			for (VisitPrescriptionEntity e : origPresList) {
				if ((e.getId() != null) && (!newPresList.contains(e))) { // 如果不在要保存的列表中，则设置为重新上传
					e.setReuploaded(1);
					e.setReuploadDate(cal.getTime());
				}
			}

			app.getPrescription().setPics(origPresList);

			// ===============================
			// ---- 处方处理 end -------------------
			// ===============================

			app.setAppPastHistory(pastHistory);

			if (null != items) {

				visitImgValueService.updateCheckValuesForApply(applyId, items);
			}

			app.setFamilyHistory(familyHistory);

			dto.getResult().setApplyId(applyId);
			applicationDao.save(app);
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(ApplyDto.ERRCODE_OTHERS);
			dto.setErrMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			// return new ApplyDto(BaseDto.ERRCODE_OTHERS, e.getMessage());
			dto.setErrCode(ApplyDto.ERRCODE_OTHERS);
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	/**
	 * 对处方的处理为：<br/>
	 * <ul>
	 * <li>1. 如果上传的内容包括imgId，则：
	 * <ul>
	 * <li>1.1 在数据库中查找到包含此imgId的，并且img,thumb字段内容相同，则认为是不作改动。</li>
	 * <li>1.2 在数据库中查找到包含此imgId，但是img,thumb字段内容却不相同，则认为是对原imgId的处方进行了改动。</li>
	 * 此时，需要将此imgId的处方标记为重传（reuploaded=1），并将img,thumb插入新的记录。
	 * <li>1.3 如果在数据库找不到imgId，忽略处理；</li>
	 * </ul>
	 * <li>2. 如果在上传的内容不包括imgId，则插入新的记录
	 * </ul>
	 */
	@Override
	public ApplicationEntity updateArchive(int applyId,
			List<VisitPrescriptionEntity> prescriptions,
			AppInspection inspection, List<VisitImgEntity> checks,
			AppPastHistoryComponent pastHistory, String familyHistory) {
		ApplicationEntity app = applicationDao.get(applyId);
		Assert.notNull(app, "找不到相应的申请信息");
		SickEntity sick = app.getSick();
		Assert.notNull(sick, "错误的患者信息");

		Calendar cal = Calendar.getInstance();

		// ===============================
		// ---- 处方 -------------------
		// ===============================
		List<VisitPrescriptionEntity> origPresList = app.getPrescription()
				.getPics();
		List<VisitPrescriptionEntity> newPresList = new ArrayList<>(); // 新的处方列表
		for (VisitPrescriptionEntity e : prescriptions) {
			if (!(StringUtils.hasLength(e.getMedPic()) && StringUtils
					.hasLength(e.getSmallMedPic()))) {
				continue;
			}

			int idx = origPresList.indexOf(e);
			if ((e.getId() != null) && (-1 != idx)) { // 如果提交了imgId，并且是数据库已经有的
				VisitPrescriptionEntity origImg = origPresList.get(idx);
				if (!StringUtils.startsWithIgnoreCase(e.getMedPic(), "https")) { // 如果不是https开头
					if (!origImg.getMedPic().equals(e.getMedPic())) { // 图片变化，说明重新上传了
						logger.info("修改处方：{}改为{}", origImg, e);
						// 状态改为已重新上传，并重新上传一张
						origImg.setReuploaded(1);
						origImg.setReuploadDate(cal.getTime());
						VisitPrescriptionEntity newImg = new VisitPrescriptionEntity();
						newImg.setMedPic(e.getMedPic());
						newImg.setSmallMedPic(e.getSmallMedPic());
						newImg.setSick(app.getSick());
						newImg.setApplication(app);
						newImg.setDoctor(app.getDoctor());
						newImg.setCreateDate(cal.getTime());

						newPresList.add(newImg);
						origPresList.add(newImg);
					}
				}
				newPresList.add(origImg);
			} else { // 数据库中不存在
				VisitPrescriptionEntity newImg = new VisitPrescriptionEntity();
				newImg.setMedPic(e.getMedPic());
				newImg.setSmallMedPic(e.getSmallMedPic());
				newImg.setSick(app.getSick());
				newImg.setApplication(app);
				newImg.setDoctor(app.getDoctor());
				newImg.setCreateDate(cal.getTime());
				newPresList.add(newImg);
				origPresList.add(newImg);
				logger.info("新增处方：{}", e);
			}
		}

		for (VisitPrescriptionEntity e : origPresList) {
			if ((e.getId() != null) && (!newPresList.contains(e))) { // 如果不在要保存的列表中，则设置为重新上传
				e.setReuploaded(1);
				e.setReuploadDate(cal.getTime());
			}
		}

		app.getPrescription().setPics(origPresList);

		// ===============================
		// ---- 处方处理 end -------------------
		// ===============================

		// ===============================
		// ---- 检验项处理 -------------------
		// ===============================
		List<VisitImgEntity> newPicList = new ArrayList<>(); // 新的检验项图片列表
		for (VisitImgEntity e : inspection.getPics()) {
			int idx = app.getInspection().getPics().indexOf(e);
			if ((e.getImgId() != null) && (-1 != idx)) { // 如果是已经存在检验项图片
				// 2015.9月新增的功能需要，因为七牛图片由http改为https，所以如果提交的URL是https开头，不更改数据
				VisitImgEntity orgImg = app.getInspection().getPics().get(idx);
				if (!e.getImg().startsWith("https")) { // 如果是https开头，保存原图
					// TODO 如果图片更改了URL，不覆盖原有图片，应该做修改标记
					if (!orgImg.getImg().equals(e.getImg())) { // 如果图片更改了URL
						orgImg.setImg(e.getImg());
						orgImg.setThumb(e.getThumb());
						e.setPostTime(cal.getTime());
					}
				}
				newPicList.add(orgImg); // 加入到新的列表
			} else { // 数据库还没有检验项图片
				VisitImgEntity newImg = new VisitImgEntity();
				newImg.setSick(sick);
				newImg.setApplication(app);
				newImg.setCat(VisitItemCat.CAT_INSPECTION);
				newImg.setPostTime(cal.getTime());
				newImg.setImg(e.getImg());
				newImg.setThumb(e.getThumb());
				newImg.setItemId(e.getItemId());
				newPicList.add(newImg); // 加入到新的列表
				app.getInspection().getPics().add(newImg);
			}
		}
		app.getInspection().getPics().retainAll(newPicList); // 取交集
		// ===============================
		// ---- 检验项处理 end -------------------
		// ===============================

		// ===============================
		// ---- 检查项处理 -------------------
		// ===============================

		List<VisitImgEntity> newCheckList = new ArrayList<>(); // 新的检查项图片列表
		for (VisitImgEntity e : checks) {
			int idx = app.getChecks().indexOf(e);
			if ((e.getImgId() != null) && (-1 != idx)) { // 如果是已经存在检验项图片
				// 2015.9月新增的功能需要，因为七牛图片由http改为https，所以如果提交的URL是https开头，不更改数据
				VisitImgEntity orgImg = app.getChecks().get(idx);
				if (!e.getImg().startsWith("https")) { // 如果不是https开头，保存原图
					// TODO 如果图片更改了URL，不覆盖原有图片，应该做修改标记
					if (!orgImg.getImg().equals(e.getImg())) { // 如果图片更改了URL
						orgImg.setImg(e.getImg());
						orgImg.setThumb(e.getThumb());
						orgImg.setPostTime(cal.getTime());
					}
				}
				newCheckList.add(orgImg); // 加入到新的列表
			} else { // 数据库还没有检验项图片
				VisitImgEntity newImg = new VisitImgEntity();
				newImg.setSick(sick);
				newImg.setApplication(app);
				newImg.setCat(VisitItemCat.CAT_EXAMINE);
				newImg.setPostTime(cal.getTime());
				newImg.setImg(e.getImg());
				newImg.setThumb(e.getThumb());
				newImg.setItemId(e.getItemId());
				newCheckList.add(newImg); // 加入到新的列表
				app.getChecks().add(newImg);
			}
		}
		app.getChecks().retainAll(newCheckList); // 取交集，包括imgId==null的项

		// ===============================
		// ---- 检查项处理 end -------------------
		// ===============================

		app.setAppPastHistory(pastHistory);
		app.setFamilyHistory(familyHistory);

		ApplicationEntity saved = applicationDao.save(app);

		return saved;
	}

	@Override
	public ApplicationEntity get(int applyId) {
		return applicationDao.get(applyId);
	}

	@Override
	public AppDetailsDto getDetail(int applyId) {
		AppDetailsDto dto = new AppDetailsDto();
		ApplicationEntity app = applicationDao.get(applyId);
		try {
			if (null == app) {
				throw new SystemErrorExecption("applyId不存在",
						BaseDto.ERRCODE_OTHERS);
			}
			dto.setApplication(app);

		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}

		// 相关联的申请
		List<ApplicationEntity> rels = getListBySick(app.getSick().getId(),
				VisitStatus.APPLY_VISITING);
		dto.setRelations(rels);

		return dto;
	}

	@Override
	public ApplicationDto getSickApplicationDetail(String token,
			String appName, Integer status, Integer page, Integer pageSize,
			int isPaging) {
		ApplicationDto dto = new ApplicationDto();
		try {
			if (null != page) {
				if (page.intValue() < 1) {
					throw new SystemErrorExecption("当前页码错误",
							BaseDto.ERRCODE_OTHERS);
				}
			}
			if (null != pageSize) {
				if (pageSize.intValue() < 1) {
					throw new SystemErrorExecption("每页记录数错误",
							BaseDto.ERRCODE_OTHERS);
				}
			}

			// 判断是否分页（0，1）
			int[] isPagings = new int[] { 0, 1 };
			boolean flag = ArrayUtils.contains(isPagings, isPaging);
			if (!flag) {
				throw new SystemErrorExecption("是否分页范围0或1",
						BaseDto.ERRCODE_OTHERS);
			}
			int doctorId = doctorOnlineService.getUserId(token);
			String subject = appNameService.getSzSubjectId(appName);
			List<ApplicationEntity> list = null;
			if (isPaging == 1) {
				dto.setIsPaging(isPaging);
				PaginationSupport pagination = applicationDao
						.getApplicationReport(doctorId, subject, status, page,
								pageSize);
				// 从第一页到当前页码的总记录数
				int actualCount = page * pageSize;
				// 此表的总记录数
				int totalCount = pagination.getTotalCount();
				if (null != pagination) {
					if (actualCount < totalCount) {
						dto.setLastPage(0);
					} else {
						dto.setLastPage(1);
					}
					list = pagination.getItems();
					dto.setList(list);
				}
			} else {
				dto.setIsPaging(isPaging);
				String hql = "from ApplicationEntity where doctor.id=:doctorId and szSubject.id="
						+ ":szSubject and visitStatus =:status order by applyTime desc, finishTime desc";
				Parameter params = new Parameter();
				params.put("doctorId", doctorId);
				params.put("szSubject", subject);
				params.put("status", status);
				list = applicationDao.findByHql(hql, params);
				if (null != list) {
					dto.setList(list);
				}
			}
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	/**
	 * 3.4.4 获取“已完成”患者列表
	 */
	public FinishedReportDto getFinished(String token, String appName,
			Integer status, Integer page, Integer pageSize) {
		FinishedReportDto dto = new FinishedReportDto();
		int doctorId = doctorOnlineService.getUserId(token);

		try {

			if (null != page) {
				if (page.intValue() < 1) {
					throw new SystemErrorExecption("当前页码错误",
							BaseDto.ERRCODE_OTHERS);
				}
			}
			if (null != pageSize) {
				if (pageSize.intValue() < 1) {
					throw new SystemErrorExecption("每页记录数错误",
							BaseDto.ERRCODE_OTHERS);
				}
			}
			String subject = appNameService.getSzSubjectId(appName);

			PaginationSupport pagination = finishedReportDao.getFinishedReport(
					doctorId, subject, status, page, pageSize);
			if (null != pagination) {
				if (page * pageSize < pagination.getTotalCount()) {
					dto.setLastPage(0);
				} else {
					dto.setLastPage(1);
				}
				dto.setPagination(pagination);
			}
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	/**
	 * 3.4.1 获取随访状态统计
	 */
	public DoctorStatusCountDto getStat(String token, String appName) {
		DoctorStatusCountDto dto = new DoctorStatusCountDto();

		try {
			int userId = validateIsSubject(token, appName);
			String szSubject = appNameService.getSzSubjectId(appName);

			List list = applicationDao.getStatusCountByDoctorId(userId,
					szSubject);

			Map<Integer, Integer> report = new HashMap<>();
			if (null != list) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					int s = (Integer) map.get("s");
					int c = ((BigInteger) map.get("c")).intValue();
					report.put(s, c);
				}
				dto.setMap(report);
			}

		} catch (SubjectValidateException e) {
			// e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	// 判断医生有无参加此APP专科的随访
	public int validateIsSubject(String token, String appName)
			throws SubjectValidateException {
		int userId = doctorOnlineService.getUserId(token);
		DoctorEntity doctor = doctorDao.get(userId);
		String szSubjectId = appNameService.getSzSubjectId(appName);
		boolean subjectJoined = doctor.containSzSubject(szSubjectId);
		if (!subjectJoined) {
			throw new SubjectValidateException("医生未参加此专科的随访",
					DoctorStatusCountDto.ERRCODE_ISJOINED_VISIT);
		}
		return userId;
	}

	@Override
	public SickApplicationDto getSickStat(String token, String visitStatus) {
		SickApplicationDto dto = new SickApplicationDto();
		List<ApplicationEntity> list = null;
		List<String> statList = null;

		// TODO 怎么能全部查出来再过滤？？？！！！，应该在查询的时候就过滤了
		if (!"".equals(visitStatus)) {
			statList = Arrays.asList(visitStatus.split(","));

			dto.setStatList(statList);
		} else {
			statList = new ArrayList<>();
			statList.add("" + VisitStatus.APPLY_COMPLETED);
			statList.add("" + VisitStatus.APPLY_EXITED);
			statList.add("" + VisitStatus.APPLY_PENDING);
			statList.add("" + VisitStatus.APPLY_REJECTED);
			statList.add("" + VisitStatus.APPLY_VISITING);
			dto.setStatList(statList);
		}
		int sickId = sickOnlineService.getUserId(token);
		// System.out.println("sickId:" + sickId);
		list = applicationDao.getStatusBySickId(sickId);
		// System.out.println("statList: "+statList.size());
		dto.setList(list);
		return dto;
	}

	@Override
	public DoctorSearchDto doctorSearch(String doctorId, String recommendCode,
			int hospitalId) {
		DoctorSearchDto dto = new DoctorSearchDto();
		try {
			// System.out.println("doctor length:"+doctorId.length()+" isLength:"+(doctorId.length()!=7));
			if (!"".equals(doctorId)) {
				if (doctorId.length() != 7) {
					throw new SystemErrorExecption("医生ID必须7位数字",
							BaseDto.ERRCODE_OTHERS);
				}
				if (!StringUtil.isNumber(doctorId)) {
					throw new SystemErrorExecption("医生ID必须纯数字",
							BaseDto.ERRCODE_OTHERS);
				}
			}
			if (!"".equals(recommendCode)) {
				if (recommendCode.length() != 7) {
					throw new SystemErrorExecption("推荐码必须7位数字",
							BaseDto.ERRCODE_OTHERS);
				}
				if (!StringUtil.isNumber(recommendCode)) {
					throw new SystemErrorExecption("推荐码必须纯数字",
							BaseDto.ERRCODE_OTHERS);
				}
			}
			if ("".equals(doctorId) && "".equals(recommendCode)
					&& hospitalId == 0) {
				throw new SystemErrorExecption("至少填一项有效的参数",
						BaseDto.ERRCODE_OTHERS);
			}

			List<DoctorEntity> list = null;
			Parameter params = new Parameter();
			String hql = "";
			if (hospitalId == 0 && ("".equals(doctorId))) {
				hql = "from DoctorEntity where recommendCode =:recommendCode AND szSubjects IS NOT NULL AND  szSubjects <> '' order by registerDate desc";
				params.put("recommendCode", recommendCode);
			} else if ("".equals(recommendCode) && ("".equals(doctorId))) {
				hql = "from DoctorEntity where hospital.id =:hospitalId AND szSubjects IS NOT NULL AND  szSubjects <> '' order by registerDate desc";
				params.put("hospitalId", hospitalId);
			} else if ("".equals(recommendCode) && hospitalId == 0) {
				hql = "from DoctorEntity where id =:doctorId AND szSubjects IS NOT NULL AND  szSubjects <> '' order by registerDate desc";
				params.put("doctorId", Integer.valueOf(doctorId));
			} else if (!"".equals(recommendCode) && !"".equals(doctorId)
					&& hospitalId == 0) {
				hql = "from DoctorEntity where id =:doctorId AND recommendCode =:recommendCode AND szSubjects IS NOT NULL AND  szSubjects <> '' order by registerDate desc";
				params.put("doctorId", Integer.valueOf(doctorId));
				params.put("recommendCode", recommendCode);
			} else if (0 != hospitalId && !"".equals(recommendCode)
					&& "".equals(doctorId)) {
				hql = "from DoctorEntity where hospital.id =:hospitalId AND recommendCode =:recommendCode AND szSubjects IS NOT NULL AND  szSubjects <> '' order by registerDate desc";
				params.put("hospitalId", hospitalId);
				params.put("recommendCode", recommendCode);
			} else if (0 != hospitalId && "".equals(recommendCode)
					&& !"".equals(doctorId)) {
				hql = "from DoctorEntity where hospital.id =:hospitalId AND id =:doctorId AND szSubjects IS NOT NULL AND  szSubjects <> '' order by registerDate desc";
				params.put("hospitalId", hospitalId);
				params.put("doctorId", Integer.valueOf(doctorId));
			} else {
				hql = "from DoctorEntity where hospital.id =:hospitalId AND recommendCode =:recommendCode AND szSubjects IS NOT NULL AND  szSubjects <> '' order by registerDate desc";
				params.put("hospitalId", hospitalId);
				params.put("recommendCode", recommendCode);
			}

			list = doctorDao.searchDoctor(hql, params);
			Map<String, String> subMap = new HashMap<String, String>();
			if (null != list) {
				for (DoctorEntity doctor : list) {
					String[] subArray = {};
					if (null != doctor.getSzSubjects()) {
						subArray = doctor.getSzSubjects().split(",");
					}
					List<String> subjects = Arrays.asList(subArray);
					int subSize = subjects.size();
					for (int j = 0; j < subSize; j++) {
						String subjectId = subjects.get(j);

						SzSubjectEntity subject = szSubjectService
								.get(subjectId);

						if (null == subject) {
							throw new SystemErrorExecption("医生对应的专科不合法",
									BaseDto.ERRCODE_OTHERS);
						}
						String subjectValue = subject.getName();
						subMap.put(subjectId, subjectValue);
					}
				}
				dto.setSubMap(subMap);
				dto.setList(list);
			}

		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}

		return dto;
	}

	/**
	 * 获取第一个和最后一个阶段
	 * 
	 * @param phases
	 * @return
	 */
	public TemplatePhaseEntity[] getFirstAndLastPhase(
			List<TemplatePhaseEntity> phases) {
		if (null == phases) {
			return null;
		}

		TemplatePhaseEntity firstPhase = null;
		TemplatePhaseEntity lastPhase = null;

		for (int i = 0; i < phases.size(); i++) {
			TemplatePhaseEntity phase = phases.get(i);
			if (phase.getSelected() == 0) { // 如果不是被选中的阶段，跳过
				continue;
			}
			if (null == firstPhase) {
				firstPhase = phase;
				lastPhase = phase;
			} else {
				if (firstPhase.getTimePoint() > phase.getTimePoint()) {
					firstPhase = phase;
				}
				if (lastPhase.getTimePoint() < phase.getTimePoint()) {
					lastPhase = phase;
				}
			}
		}

		return new TemplatePhaseEntity[] { firstPhase, lastPhase };

	}

	/**
	 * @deprecated 不再使用
	 */
	@Override
	public void saveAppTemplate(int applyId, TemplateEntity template,
			Date visitStartDate) {
		ApplicationEntity application = get(applyId);
		application.setTemplate(template);

		// 设置当前随访阶段
		List<TemplatePhaseEntity> phases = template.getPhases();

		if (null != phases) {
			TemplatePhaseEntity firstPhase = null;
			TemplatePhaseEntity lastPhase = null;
			AppCurPhaseComponent curPhase = new AppCurPhaseComponent();

			for (int i = 0; i < phases.size(); i++) {
				TemplatePhaseEntity phase = phases.get(i);
				if (phase.getSelected() == 0) { // 如果不是被选中的阶段，跳过
					continue;
				}
				if (null == firstPhase) {
					firstPhase = phase;
					lastPhase = phase;
				} else {
					if (firstPhase.getTimePoint() > phase.getTimePoint()) {
						firstPhase = phase;
					}
					if (lastPhase.getTimePoint() < phase.getTimePoint()) {
						lastPhase = phase;
					}
				}
			}
			if (null != firstPhase) {
				curPhase.setCurPhaseId(firstPhase.getTemplPhaseId());
				curPhase.setCurTimePoint(firstPhase.getTimePoint());
				curPhase.setCurVisitTime(firstPhase.getVisitTime());
				curPhase.setCycleUnit(template.getCycleUnit());
				curPhase.setReportStatus(VisitStatus.DATA_NOMAL);
				curPhase.setFuZhenStatus(firstPhase.getFuZhenStatus());
				application.setCurPhase(curPhase);
			}

			// 预期结束日期
			if (null != lastPhase) {
				application.setExpectedFinishTime(lastPhase.getVisitTime());
			}
		}

		application.setVisitStartDate(visitStartDate); // 随访开始日期

		applicationDao.save(application);
	}

	@Override
	public SickSearchDto sickSearch(String token, String sickName,
			String appName) {
		SickSearchDto dto = new SickSearchDto();
		int doctorId = doctorOnlineService.getUserId(token);
		String szSubject = appNameService.getSzSubjectId(appName);
		String hql = "";

		Query query = null;
		if (!"0".equals(sickName)) {
			hql = "select count(a.sick_id) records, a.sick_id sickId, s.name name,s.sex sex,s.birthday birthday,a.doctor_id doctorId "
					+ "from visit_applications a, sicks s where s.id=a.sick_id and a.doctor_id=:doctorId and s.name like :name and a.sz_subject=:szSubject group by a.sick_id";
			query = applicationDao.getSession().createSQLQuery(hql)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter("doctorId", doctorId);
			query.setParameter("szSubject", szSubject);
			query.setParameter("name", "%" + sickName + "%");
		} else {
			hql = "select count(a.sick_id) records, a.sick_id sickId, s.name name,s.sex sex,s.birthday birthday,a.doctor_id doctorId "
					+ "from visit_applications a, sicks s where s.id=a.sick_id and a.doctor_id=:doctorId and a.sz_subject=:szSubject group by a.sick_id";
			query = applicationDao.getSession().createSQLQuery(hql)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter("doctorId", doctorId);
			query.setParameter("szSubject", szSubject);
		}
		List list = query.list();
		// System.out.println("list.size(): " + list.size());
		dto.setTotal(list.size());
		dto.setList(list);
		return dto;
	}

	/**
	 * v103 搜索患者 返回增加bmi参数
	 */
	@Override
	public SickSearchDto sickSearchv103(String token, String sickName,
			String appName) {
		SickSearchDto dto = new SickSearchDto();
		int doctorId = doctorOnlineService.getUserId(token);
		String szSubject = appNameService.getSzSubjectId(appName);
		String hql = "";

		Query query = null;
		if (!"0".equals(sickName)) {
			hql = "select count(a.sick_id) records, a.sick_id sickId, s.name name,s.sex sex,s.birthday birthday,a.doctor_id doctorId ,s.height height,s.weight weight "
					+ "from visit_applications a, sicks s where s.id=a.sick_id and a.doctor_id=:doctorId and s.name like :name and a.sz_subject=:szSubject group by a.sick_id";
			query = applicationDao.getSession().createSQLQuery(hql)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter("doctorId", doctorId);
			query.setParameter("szSubject", szSubject);
			query.setParameter("name", "%" + sickName + "%");
		} else {
			hql = "select count(a.sick_id) records, a.sick_id sickId, s.name name,s.sex sex,s.birthday birthday,a.doctor_id doctorId ,s.height height,s.weight weight "
					+ "from visit_applications a, sicks s where s.id=a.sick_id and a.doctor_id=:doctorId and a.sz_subject=:szSubject group by a.sick_id";
			query = applicationDao.getSession().createSQLQuery(hql)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setParameter("doctorId", doctorId);
			query.setParameter("szSubject", szSubject);
		}
		List list = query.list();
		// System.out.println("list.size(): " + list.size());
		dto.setTotal(list.size());
		dto.setList(list);
		return dto;
	}

	@Override
	public SickDetailDto getSickRecords(String token, Integer sickId,
			String appName) {
		SickDetailDto dto = new SickDetailDto();
		String szSubject = appNameService.getSzSubjectId(appName);
		try {
			if (sickId.intValue() != 0) {
				Integer doctorId = doctorOnlineService.getUserId(token);
				// System.out.println("doctorId: " + doctorId);
				dto.setDoctorId(doctorId);
				dto.setSubject(szSubject);
				String hql = "from ApplicationEntity where sick.id =:sickId";
				Parameter params = new Parameter();
				params.put("sickId", sickId);
				List<ApplicationEntity> list = applicationDao.findByHql(hql,
						params);
				if (list.size() > 0) {
					SickEntity sick = sickService.get(sickId);
					dto.setSick(sick);
				}
				dto.setList(list);
			} else {
				throw new SystemErrorExecption("患者详情ID不能为空",
						SickDetailDto.ERRCODE_SYSTEM_ERROR);
			}
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public Set<ApplicationElem> getSickRecordsV103(String token,
			Integer sickId, String appName) throws SystemErrorExecption {
		String szSubject = appNameService.getSzSubjectId(appName);
		int doctorId = doctorOnlineService.getUserId(token);
		Set<ApplicationElem> apps = new HashSet<>();
		if (sickId.intValue() != 0) {
			String hql = "from ApplicationEntity where sick.id =:sickId";
			Parameter params = new Parameter();
			params.put("sickId", sickId);
			List<ApplicationEntity> list = applicationDao
					.findByHql(hql, params);

			if (list.size() > 0) {
				for (ApplicationEntity app : list) {
					// 随访状态的范围（1，2 ，3，4，5）
					int[] status = new int[] { 1, 2, 3, 4, 5 };
					boolean flag = ArrayUtils.contains(status, null != app
							.getVisitStatus() ? app.getVisitStatus() : 0);
					if (flag) {
						ApplicationElem appElem = new ApplicationElem();
						Assert.notNull(app.getDoctor(), "doctorId不能为空");
						int elemDoctorId = app.getDoctor().getId().intValue();
						String tempSubject = null != app.getSzSubject().getId() ? app
								.getSzSubject().getId() : "";
						appElem.setApplyId((!szSubject.equals(tempSubject) || doctorId != elemDoctorId) ? 0
								: app.getApplyId());
						appElem.setApplyTime(app.getApplyTime());
						appElem.setExpFinishTime(app.getExpectedFinishTime());
						appElem.setFinishTime(app.getFinishTime());
						appElem.setSzSubject(app.getSzSubject().getName());
						appElem.setVisitStatus(app.getVisitStatus());
						apps.add(appElem);
					}
				}
			}
		} else {
			throw new SystemErrorExecption("患者详情ID不能为空",
					SickDetailDto.ERRCODE_SYSTEM_ERROR);
		}
		return apps;
	}

	@Override
	public AppReasonDto saveRejectReason(Integer applyId, Integer reasonId,
			String otherReason) {
		AppReasonDto dto = new AppReasonDto();
		ApplicationEntity application = applicationDao.get(applyId);
		int status = application.getVisitStatus();
		try {
			if (status == 1) {
				ReasonEntity returnReason;
				int typeStatus = 4;
				List<ReasonEntity> reasonList = new ArrayList<ReasonEntity>();
				if ("0".equals(String.valueOf(reasonId))) {
					if (null == otherReason || "".equals(otherReason)) {
						throw new SystemErrorExecption("请输入有效的拒绝原因",
								BaseDto.ERRCODE_OTHERS);
					}
					// System.out.println("reasonId==0");
					returnReason = saveReason(otherReason, typeStatus,
							application);
					if (null == returnReason) {
						throw new OtherReasonErrorExecption(
								ErrorMessage
										.getConfig(ConstantSickVisitErrs.ERRCODE_NOT_FOUND_DEFAULT_REASON),
								ConstantSickVisitErrs.ERRCODE_NOT_FOUND_DEFAULT_REASON);
					}
				} else {
					returnReason = reasonDao.get(reasonId);
					// reasonList = getMappingReason(String.valueOf(reasonId),
					// application,otherReason);
					if (null == returnReason) {
						throw new ReasonErrorExecption(
								ErrorMessage
										.getConfig(ConstantSickVisitErrs.ERRCODE_NOT_FOUND_DEFAULT_REASON),
								ConstantSickVisitErrs.ERRCODE_NOT_FOUND_DEFAULT_REASON);
					}
				}
				reasonList.add(returnReason);// 把新旧原因放一起
				saveApplication(typeStatus, application, reasonList);
			} else {
				throw new StatusErrorExecption(
						ErrorMessage
								.getConfig(ConstantSickVisitErrs.ERRCODE_REJECT_STATUS_ERROR),
						ConstantSickVisitErrs.ERRCODE_REJECT_STATUS_ERROR);
			}
		} catch (ReasonErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (OtherReasonErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (StatusErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	public AppReasonDto saveExitReason(Integer applyId, String reasonIds,
			String otherReason) {
		AppReasonDto dto = new AppReasonDto();

		try {
			if ("0".equals(reasonIds) && "".equals(otherReason)) {
				throw new SystemErrorExecption(
						ErrorMessage
								.getConfig(ConstantSickVisitErrs.INVALID_REASONID_OTHERREASON),
						ConstantSickVisitErrs.INVALID_REASONID_OTHERREASON);
			}
			ApplicationEntity application = applicationDao.get(applyId);
			if (null != application) {
				int status = application.getVisitStatus();
				if (1 == status || 2 == status) {
					// 获取旧原因列表
					List<ReasonEntity> reasonList = getMappingReason(reasonIds,
							application, otherReason);

					int typeStatus = 5;// 保存退出后的状态
					ReasonEntity reason = null;

					// 保存新其它原因
					if (!"".equals(otherReason)) {
						reason = saveReason(otherReason, typeStatus,
								application);
						reasonList.add(reason);// 新原因与旧原因放在一起
					}

					// 更新applyID对应的记录及 与原因表建立关系
					saveApplication(typeStatus, application, reasonList);
				} else {
					throw new StatusErrorExecption(
							ErrorMessage
									.getConfig(ConstantSickVisitErrs.ERRCODE_REJECT_STATUS_ERROR),
							ConstantSickVisitErrs.ERRCODE_REJECT_STATUS_ERROR);
				}
			} else {
				throw new SystemErrorExecption("applyId不存在",
						BaseDto.ERRCODE_OTHERS);
			}

		} catch (StatusErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (SystemErrorExecption e) {
			e.printStackTrace();
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	/**
	 * 获取旧原因列表及根据原因ID查询结果是否为空
	 * 
	 * @param reasonIds
	 * @param application
	 * @param otherReason
	 * @return
	 * @throws SystemErrorExecption
	 */
	public List<ReasonEntity> getMappingReason(String reasonIds,
			ApplicationEntity application, String otherReason)
			throws SystemErrorExecption {
		List<ReasonEntity> reasonList = new ArrayList<ReasonEntity>();
		List<ReasonEntity> Reasons = new ArrayList<ReasonEntity>();
		List<String> ids = Arrays.asList(reasonIds.split(","));

		// 获取旧原因
		reasonList = application.getReasons();
		for (int i = 0; i < ids.size(); i++) {
			ReasonEntity reason = reasonDao.get(Integer.valueOf(ids.get(i)));
			if (null != reason) {
				Reasons.add(reason);
				reasonList.add(reason);
			}
		}

		if (Reasons.size() == 0 && "".equals(otherReason)) {
			throw new SystemErrorExecption(
					ErrorMessage
							.getConfig(ConstantSickVisitErrs.INVALID_REASONID_OTHERREASON),
					ConstantSickVisitErrs.INVALID_REASONID_OTHERREASON);
		}
		return removeDuplicate(reasonList);
	}

	public List<ReasonEntity> removeDuplicate(List<ReasonEntity> list) {
		List<ReasonEntity> reasonList = new ArrayList<ReasonEntity>();
		for (ReasonEntity reason : list) {
			if (!reasonList.contains(reason)) {
				reasonList.add(reason);
			}
		}
		return reasonList;
	}

	/**
	 * ReasonEntity 对象保存新原因到表visit_reasons
	 * 
	 * @param otherReason
	 * @param typeStatus
	 * @param application
	 * @return
	 */
	public ReasonEntity saveReason(String otherReason, int typeStatus,
			ApplicationEntity application) {
		ReasonEntity reason = new ReasonEntity();
		ReasonEntity returnReason = new ReasonEntity();
		reason.setOtherReason(4 == typeStatus ? 1 : 3);
		reason.setReasonDesc(otherReason);
		reason.setType(typeStatus);
		reason.setSzSubject(null != application.getDoctor().getSzSubjects() ? application
				.getDoctor().getSzSubjects() : "");
		reason.setSort(100);

		returnReason = reasonDao.save(reason);

		return returnReason;
	}

	/**
	 * 设置退出与拒绝理由applyId 与reasonId保存至ApplicatioEntity关联的中间表visit_app_reason_map
	 * 
	 * @param returnReason
	 *            表示新增的原因
	 * @param typeStatus设置后要保存的状态
	 * @param application
	 *            applyId 对应的记录
	 * @param reasons旧原因列表
	 * @return
	 */
	public ApplicationEntity saveApplication(int typeStatus,
			ApplicationEntity application, List<ReasonEntity> reasons) {

		application.setVisitStatus(typeStatus);
		application.setFinishTime(new Date());

		// 增加冗余的原因内容
		String reasonDesc = "";
		// System.out.println(" reason.size: "+reasons.size());
		if (null != reasons && reasons.size() > 0) {
			for (ReasonEntity reasonEntity : reasons) {
				if (!"".equals(reasonDesc)) {
					System.out
							.println("desc!!!=null: "
									+ reasonEntity.getReasonDesc() + "   "
									+ reasonDesc);
					reasonDesc += "；";
				}
				reasonDesc += reasonEntity.getReasonDesc();
				// applyId对应的记录与旧原因建立关系
				if (null != reasonEntity.getApplications()) {
					reasonEntity.getApplications().add(application);
				} else {
					List<ApplicationEntity> applications = new ArrayList<ApplicationEntity>();
					applications.add(application);
					reasonEntity.setApplications(applications);
				}
				// System.out.println("desc==null: "+reasonEntity.getReasonDesc()+"   "+reasonDesc);
			}
			// System.out.println("application desc: "+reasonDesc);
		}
		application.setReasonDesc(reasonDesc);

		/*
		 * if (null != returnReason) { // applyId对应的记录与新原因建立关系
		 * System.out.println
		 * ("return.app: "+returnReason+" application: "+returnReason
		 * .getApplications()); if (null == returnReason.getApplications()) {
		 * List<ApplicationEntity> appList = new ArrayList<ApplicationEntity>();
		 * if (null != application) { appList.add(application); }
		 * returnReason.setApplications(appList);
		 * System.out.println("原因中的申请记录： "
		 * +returnReason.getApplications().size()); } else {
		 * returnReason.getApplications().add(application); } }
		 */
		ApplicationEntity returnApplication = applicationDao.save(application);

		return returnApplication;
	}

	@Override
	public void updateVisitStatus(int applyId, int visitStatus) {
		ApplicationEntity app = get(applyId);
		app.setVisitStatus(visitStatus);
		app.setLastUpdateTime(new Date());
		applicationDao.save(app);
	}

	@Override
	public ApplicationEntity saveNewApply(SickEntity sick,
			SzSubjectEntity szSubject, int doctorId,
			AppCasesComponent appCases, DiseaseEntity disease)
			throws SystemErrorExecption, ApplicationDuplicationExecption {

		List<ApplicationEntity> applicationExists = getListForSick(
				sick.getId(), VisitStatus.APPLY_PENDING, szSubject.getId(),
				disease);

		// 此病种已经存在
		if ((null != applicationExists) && (applicationExists.size() > 0)) {
			throw new ApplicationDuplicationExecption();
		}

		applicationExists = getListForSick(sick.getId(),
				VisitStatus.APPLY_VISITING, szSubject.getId(), disease);

		// 此病种已经存在
		if ((null != applicationExists) && (applicationExists.size() > 0)) {
			throw new ApplicationDuplicationExecption();
		}

		ApplicationEntity app = new ApplicationEntity();
		app.setSick(sick);
		app.setAppCases(appCases);
		app.setSzSubject(szSubject);
		app.setVisitStatus(VisitStatus.APPLY_PENDING);
		app.setDisease(disease);

		DoctorEntity doctor = doctorDao.get(Integer.valueOf(doctorId));

		if (null == doctor) {
			throw new SystemErrorExecption(
					ErrorMessage
							.getConfig(ConstantSickVisitErrs.DOCTOR_NOT_EXIST),
					ConstantSickVisitErrs.DOCTOR_NOT_EXIST);
		}
		app.setDoctor(doctor);

		app.setApplyTime(new Date());

		List<VisitImgEntity> values = new ArrayList<>();
		// 将出院和门诊图存到图片表
		if (null != appCases.getCasesPics()
				|| null != appCases.getLeaveHosPics()) {
			List<VisitImgEntity> leaveHosPics = appCases.getLeaveHosPics();
			if (null != leaveHosPics) {
				for (int i = 0; i < leaveHosPics.size(); i++) {
					VisitImgEntity e = leaveHosPics.get(i);
					e.setItemId(VisitItemId.LEAVE_HOS_ID);
					e.setApplication(app);
					values.add(e);
				}
			}
			List<VisitImgEntity> casesPics = appCases.getCasesPics();
			if (null != casesPics) {
				for (int i = 0; i < casesPics.size(); i++) {
					VisitImgEntity e = casesPics.get(i);
					e.setItemId(VisitItemId.CASE_RECORD_ID);
					e.setApplication(app);
					values.add(e);
				}
			}
		}

		ApplicationEntity saved = applicationDao.save(app);

		// 更新门诊、住院图片
		visitImgValueService.updateCasesValuesForApply(saved.getApplyId(),
				values);

		return saved;
	}

	@Override
	public ApplicationEntity saveNewApplyV103(SickEntity sick,
			SzSubjectEntity szSubject, int doctorId,
			AppCasesComponent appCases, DiseaseEntity disease,
			Set<CheckItemValueEntity> itemValues) throws SystemErrorExecption,
			VisitItemNotFoundExecption, ApplicationDuplicationExecption {

		List<ApplicationEntity> applicationExists = getListForSick(
				sick.getId(), VisitStatus.APPLY_PENDING, szSubject.getId(),
				disease);

		// 此病种已经存在
		if ((null != applicationExists) && (applicationExists.size() > 0)) {
			throw new ApplicationDuplicationExecption();
		}

		applicationExists = getListForSick(sick.getId(),
				VisitStatus.APPLY_VISITING, szSubject.getId(), disease);

		// 此病种已经存在
		if ((null != applicationExists) && (applicationExists.size() > 0)) {
			throw new ApplicationDuplicationExecption();
		}

		ApplicationEntity app = new ApplicationEntity();
		app.setSick(sick);
		app.setSzSubject(szSubject);
		app.setVisitStatus(VisitStatus.APPLY_PENDING);
		app.setDisease(disease);
		app.setAppCases(appCases);

		DoctorEntity doctor = doctorDao.get(Integer.valueOf(doctorId));

		if (null == doctor) {
			throw new SystemErrorExecption(
					ErrorMessage
							.getConfig(ConstantSickVisitErrs.DOCTOR_NOT_EXIST),
					ConstantSickVisitErrs.DOCTOR_NOT_EXIST);
		}
		app.setDoctor(doctor);

		app.setApplyTime(new Date());

		for (CheckItemValueEntity e : itemValues) {
			e.setSick(sick);
			e.setReportTime(new Date());
			e.setApplication(app);
		}

		app.getPersonalHistory().setValues(itemValues);

		ApplicationEntity saved = applicationDao.save(app);

		updateArchiveCases(saved.getApplyId(), appCases); // 更新门诊记录

		return saved;
	}

	@Override
	public List<ApplicationEntity> getListForSick(int sickId, int visitStatus,
			String szSubject, DiseaseEntity disease) {
		String hql = "FROM ApplicationEntity WHERE sick.id=:sickId AND visitStatus=:visitStatus "
				+ " AND szSubject.id=:szSubject AND disease.id=:disease";
		Parameter params = new Parameter();
		params.put("sickId", sickId);
		params.put("visitStatus", visitStatus);
		params.put("szSubject", szSubject);
		params.put("disease", disease.getId());
		return applicationDao.findByHql(hql, params);
	}

	@Override
	public void updateRecommitApply(int applyId, Integer newApplyId) {
		ApplicationEntity oldApp = get(applyId);
		oldApp.setVisitStatus(VisitStatus.APPLY_RECOMMITED);
		oldApp.setNewApplyId(newApplyId);
		applicationDao.save(oldApp);

	}

	@Override
	public List<ApplicationEntity> getHistory(int sickId) {
		String hql = "FROM ApplicationEntity where sick.id = :p1 order by applyTime";
		return applicationDao.findByHql(hql, new Parameter(sickId));
	}

	/**
	 * <p>
	 * 新增随访计划。包括三部分：
	 * </p>
	 * {一)、体征项。保存体征项时，要更新计划的的体征项修改记录，以便随时更新体征项的统计方式。<br/>
	 * (二)、日常注意事项。<br/>
	 * (三)、各阶段的数据。有以下情况时，是不能保存的：<br/>
	 * <ul>
	 * <li>同一个患者的随访病种不能重复（医生不能够选择患者待同意、随访中的病种）</li>
	 * <li>第一个阶段开始的日期不能早于明天</li>
	 * <li>一个阶段都没有选中</li>
	 * </ul>
	 * <p>
	 * 新增计划后，要同时更新：
	 * </p>
	 * <ul>
	 * <li>随访当前所处阶段</li>
	 * <li>预期结束时间</li>
	 * <li>随访统计</li>
	 * <li>每个阶段应填项目总数</li>
	 * <li>更新统计报告：病历完整性、药物依从性、复诊到诊率</li>
	 * <li>给医生增加积分</li>
	 * <li>推送消息通知患者</li>
	 * </ul>
	 */
	@Override
	public void addPlanForApp(int applyId, TemplContentVo templVo,
			int stdTemplId, Date visitStartDate)
			throws NoPhaseSelectedExecption {
		logger.debug("申请单保存计划applyId:{}", applyId);

		TemplateEntity template = templateService.addPlan(applyId, templVo,
				stdTemplId, visitStartDate);

		applicationDao.flush();

		ApplicationEntity application = get(applyId);

		application.setTemplate(template);
		application.getCurPhase().setCycleUnit(template.getCycleUnit());

		// 更新开始和结束日期
		TemplatePhaseEntity[] firstAndLastPhases = getFirstAndLastPhase(template
				.getPhases());
		application.setExpectedFinishTime(firstAndLastPhases[1].getVisitTime());

		// 当前阶段
		AppCurPhaseComponent curPhase = application.getCurPhase();
		if (null == curPhase) {
			curPhase = new AppCurPhaseComponent();
		}
		TemplatePhaseEntity firstPhase = firstAndLastPhases[0];

		curPhase.setCurPhaseId(firstPhase.getTemplPhaseId());
		curPhase.setCurTimePoint(firstPhase.getTimePoint());
		curPhase.setCurVisitTime(firstPhase.getVisitTime());
		curPhase.setCycleUnit(template.getCycleUnit());
		curPhase.setFuZhenStatus(firstPhase.getFuZhenStatus());
		curPhase.setReportStatus(firstPhase.getReportStatus());

		application.setCurPhase(curPhase);

		// 更新状态
		application.setVisitStatus(VisitStatus.APPLY_VISITING);

		application.setLastUpdateTime(new Date());

		application.setVisitStartDate(visitStartDate); // 随访开始日期

		// 更新同意随访时间
		application.setApprovalTime(new Date());

		applicationDao.update(application);

		// TODO bodySignService.getBodySignsByTempl改为靠template.getBodySigns来获取，
		// 但是这样的话就要求template保存模板的时候，使用hibernate的方式来保存记录
		updateSickProfile(application.getSick(), application.getDisease()
				.getId(), bodySignService.getBodySignsByTempl(template
				.getTemplId()));
	}

	/**
	 * 更新患者的个人属性，包括：患者参与过的病种（医生同意的）、医生定制过的体征项目
	 * 
	 * @param sick
	 * @param diseaseId
	 * @param bodySigns
	 */
	public void updateSickProfile(SickEntity sick, String diseaseId,
			List<BodySignEntity> bodySigns) {
		// =========================
		// 修改患者个人属性，记录患者申请的过的病种和需要填写的体征项
		// =========================

		SickProfileEntity bodysignItemsProfile = sick
				.getProfile(SickProfileEntity.BODY_SIGN_ITEMS);
		// 抗凝指标
		SickProfileEntity anticoagItemsProfile = sick
				.getProfile(SickProfileEntity.ANTICOAG_ITEMS);

		String bodysignItemsProfileVal = bodysignItemsProfile.getValue();
		Set<Integer> bodysignItems = new HashSet<>();
		if (StringUtils.hasLength(bodysignItemsProfileVal)) {
			try {
				Integer[] bodysignItemsArr = mapper.readValue(
						bodysignItemsProfileVal, Integer[].class);
				Collections.addAll(bodysignItems, bodysignItemsArr);
			} catch (JsonParseException | JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 修改的时候之前保存的抗凝指标记录不需要删除
		String anticoagItemsProfileVal = anticoagItemsProfile.getValue();
		Set<Integer> anticoagItems = new HashSet<>();
		if (StringUtils.hasLength(anticoagItemsProfileVal)) {
			try {
				Integer[] anticoagItemsArr = mapper.readValue(
						anticoagItemsProfileVal, Integer[].class);
				Collections.addAll(anticoagItems, anticoagItemsArr);
			} catch (JsonParseException | JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 血压和心率必须加上
		bodysignItems.add(VisitItemId.BLOOD_PRESSURE_LOW);
		bodysignItems.add(VisitItemId.BLOOD_PRESSURE_HEIGHT);
		bodysignItems.add(VisitItemId.HEART_RATE);
		// 添加医生定制的此病种的体征项和抗凝指标
		if (null != bodySigns) {
			for (BodySignEntity e : bodySigns) {
				if (e.getCat() == BodySignEntity.BODYSIGN_CAT) {
					bodysignItems.add(e.getItem().getItemId());
				} else if (e.getCat() == BodySignEntity.ANTICOAG_CAT) {
					anticoagItems.add(e.getItem().getItemId());
				}
			}
		}

		// 添加体征项后的字符串
		try {
			bodysignItemsProfileVal = mapper.writeValueAsString(bodysignItems);
			anticoagItemsProfileVal = mapper.writeValueAsString(anticoagItems);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		bodysignItemsProfile.setValue(bodysignItemsProfileVal);
		anticoagItemsProfile.setValue(anticoagItemsProfileVal);
		sick.getProfiles().add(bodysignItemsProfile); // 更新后的体征项
		sick.getProfiles().add(anticoagItemsProfile);// 更新后的抗凝指标
		// ====================
		// 更新患者参与的病种项目
		// ====================
		SickProfileEntity joinedDiseasesProfile = sick
				.getProfile(SickProfileEntity.JOINED_DISEASES);

		String joinedDiseasesProfileVal = joinedDiseasesProfile.getValue();
		Set<String> joinedDiseases = new HashSet<>();
		if (StringUtils.hasLength(joinedDiseasesProfileVal)) {
			try {
				String[] bodysignItemsArr = mapper.readValue(
						joinedDiseasesProfileVal, String[].class);
				Collections.addAll(joinedDiseases, bodysignItemsArr);
			} catch (JsonParseException | JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		joinedDiseases.add(diseaseId);

		// 更新参与病种项目
		try {
			joinedDiseasesProfileVal = mapper
					.writeValueAsString(joinedDiseases);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		joinedDiseasesProfile.setValue(joinedDiseasesProfileVal);
		sick.getProfiles().add(joinedDiseasesProfile); // 更新参与病种项目
		sickService.update(sick);
	}

	/**
	 * <p>
	 * 更新随访计划。包括三部分：
	 * </p>
	 * {一)、体征项。保存体征项时，要更新计划的的体征项修改记录，以便随时更新体征项的统计方式。<br/>
	 * (二)、日常注意事项。<br/>
	 * (三)、各阶段的数据。有以下情况时，是不能更改的：<br/>
	 * <ul>
	 * <li>selectable参数为0时，selected参数不到修改。这就要求自动任务到了时间，就将阶段状态selectable由1变为0</li>
	 * <li>当selectable=1时，如果阶段算出来的日期早于明天，则selected不能由selected=0变为selected=1。
	 * 这个是为应对可能由自动任务执行错误造成的问题。如果是自动任务造成，在这里不能修改成功/li>
	 * <li>
	 * 阶段可以取消选中（使selected=0），但是至少要保留一个【未到时间
	 * ，不需要复诊】状态的阶段处于选中状态（selected=1）。阶段变更后，随访处于当前阶段为【未到时间 ，不需要复诊】状态的第1个阶段</li>
	 * <li>当editable=0时，检验检查项不能修改。所以要求在患者提交复诊时，更改editable的状态</li>
	 * </ul>
	 * <p>
	 * 更新计划后，要同时更新：
	 * </p>
	 * <ul>
	 * <li>随访当前所处阶段</li>
	 * <li>预期结束时间</li>
	 * <li>随访统计</li>
	 * <li>每个阶段应填项目总数</li>
	 * <li>更新统计报告：病历完整性、药物依从性、复诊到诊率</li>
	 * <li>推送消息通知患者</li>
	 * </ul>
	 */
	@Override
	public void updatePlanForApp(int applyId, TemplContentVo templVo,
			Date visitStartDate) throws UpdatePlanExecption {
		logger.debug("修改随访计划applyId:{}", applyId);

		try {
			templateService.updatePlan(applyId, templVo, visitStartDate);
		} catch (ChangePhaseExecption e) {
			throw new UpdatePlanExecption(e.getMessage());
		}

		ApplicationEntity application = get(applyId);
		TemplateEntity template = application.getTemplate();

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		TemplatePhaseEntity lastPhase = null;
		TemplatePhaseEntity curPhase = null;
		for (TemplatePhaseEntity phase : template.getPhases()) {
			// 选中的最小阶段，更改为当前阶段
			if ((curPhase == null) && (phase.getSelected() == 1)
					&& (today.getTime().compareTo(phase.getVisitTime()) <= 0)) { //
				curPhase = phase;
			}
			if (phase.getSelected() == 1) {
				if (lastPhase == null) {
					lastPhase = phase;
				} else if (lastPhase.getTimePoint() < phase.getTimePoint()) {
					lastPhase = phase;
				}
			}
		}

		// 更新当前阶段
		/*
		 * 阶段可以取消选中（使selected=0），但是至少要保留一个处于选中状态（selected=1）。
		 * 阶段变更后，随访处于当前阶段为【未到时间不需要复诊】状态的第1个阶段
		 */
		Assert.state(null != curPhase, "找不到当前所处阶段");

		application.getCurPhase().setCurPhaseId(curPhase.getTemplPhaseId());
		application.getCurPhase().setCurTimePoint(curPhase.getTimePoint());
		application.getCurPhase().setCurVisitTime(curPhase.getVisitTime());
		application.getCurPhase().setFuZhenStatus(curPhase.getFuZhenStatus());
		application.getCurPhase().setReportStatus(curPhase.getReportStatus());

		// 预期结束时间
		application.setExpectedFinishTime(lastPhase.getVisitTime());

		// 更新最好更新时间
		application.setLastUpdateTime(new Date());

		// 更新同意随访时间
		application.setApprovalTime(new Date());

		applicationDao.update(application);

		// 更新患者的个人属性
		updateSickProfile(application.getSick(), application.getDisease()
				.getId(), template.getBodySigns());
	}

	@Override
	public void updateArchiveCases(int applyId, AppCasesComponent appCases) {
		ApplicationEntity app = get(applyId);
		if (null == app) {
			return;
		}

		List<VisitImgEntity> newList = new ArrayList<>();

		Calendar cal = Calendar.getInstance();

		// =================================
		// 门诊、出院图片
		// =================================
		for (VisitImgEntity img : appCases.getApplyImgList()) {
			if (!StringUtils.hasLength(img.getImg())) { // 如果没有传URL过来，跳过
				continue;
			}
			// 如果上传的图片带有imgId，则认为不变或修改
			if ((null != img.getImgId()) && (0 != img.getImgId().longValue())) {
				// 如果已经存在，不作改变
				int idx = app.getAppCases().getApplyImgList().indexOf(img);
				// 如果是有这条数据，否则认为是无效数据
				if (-1 != idx) {
					VisitImgEntity old = app.getAppCases().getApplyImgList()
							.get(idx);
					// 原图片不相等，认为图片已更改
					if (!img.getImg().equals(old.getImg())) {
						// 因为更新的七牛图库的功能需求，如果是https开头，不做处理
						if (!StringUtils.startsWithIgnoreCase(img.getImg(),
								"https")) {
							logger.info("修改了门诊图片：{}", img);
							old.setPostTime(cal.getTime());
							old.setImg(img.getImg());
							old.setThumb(img.getThumb());
						}
					}
					newList.add(old);
				} else {
					VisitImgEntity newImg = new VisitImgEntity();
					newImg.setApplication(app);
					newImg.setSick(app.getSick());
					newImg.setPostTime(cal.getTime());
					newImg.setImg(img.getImg());
					newImg.setItemId(img.getItemId());
					newImg.setThumb(img.getThumb());
					app.getAppCases().getApplyImgList().add(newImg);
					newList.add(newImg);
					// 如果带了imgId，但是没有找到原值，跳过
					logger.info("数据库中找不到原图片{}，新增：{}", newImg);
					continue;
				}
			} else {
				VisitImgEntity newImg = new VisitImgEntity();
				newImg.setApplication(app);
				newImg.setSick(app.getSick());
				newImg.setPostTime(cal.getTime());
				newImg.setImg(img.getImg());
				newImg.setItemId(img.getItemId());
				newImg.setThumb(img.getThumb());
				app.getAppCases().getApplyImgList().add(newImg);
				newList.add(newImg);
				logger.info("新增了门诊图片：{}", newImg);
			}
		}
		// app.setAppCases(appCases);
		// =================================
		// 门诊、出院图片 end
		// =================================
		app.getAppCases().getApplyImgList().retainAll(newList); // 取交集
		app.getAppCases().setConfirmDate(appCases.getConfirmDate());
		app.getAppCases().setLeaveHosDate(appCases.getLeaveHosDate());
		app.getAppCases().setCasesDate(appCases.getCasesDate());

	}

	@Override
	public void updateArchiveCasesV103(int applyId, AppCasesComponent appCases,
			Set<CheckItemValueEntity> itemValues) throws SystemErrorExecption,
			VisitItemNotFoundExecption {
		ApplicationEntity apply = get(applyId);
		if (null == apply) {
			return;
		}
		updateArchiveCases(applyId, appCases);

		checkItemValueService.updatePersonalValues(applyId, itemValues);
	}

	@Override
	public List<ApplicationEntity> getRelationApplications(int doctorId,
			String szSubject) {
		String hql = "From ApplicationEntity where doctor.id =:p1 and szSubject.id=:p2 and template.templId IS NOT NULL and visitStatus in (2,3,5)";
		return applicationDao
				.findByHql(hql, new Parameter(doctorId, szSubject));
	}

	@Override
	public ApplicationEntity saveApplication(ApplicationEntity application) {
		return applicationDao.save(application);
	}

	@Override
	public List<ApplicationEntity> getVisitingListByPhaseVisitTime(
			Date visitTime, boolean notFuzhen) {
		String hql = "FROM ApplicationEntity WHERE curPhase.curVisitTime<=:curVisitTime AND visitStatus=:visitStatus AND curPhase.fuZhenStatus=:fuZhenStatus";
		Parameter params = new Parameter();
		params.put("curVisitTime", visitTime);
		params.put("visitStatus", VisitStatus.APPLY_VISITING);
		params.put("fuZhenStatus", VisitStatus.FUZHEN_UNCOMMITTED);
		return applicationDao.findByHql(hql, params);
	}

	@Override
	public void updateVisitStatusToCompleted(Date expectedFinishTime) {
		Parameter parameter = new Parameter();
		parameter.put("finishVisitStatus", VisitStatus.APPLY_COMPLETED);
		parameter.put("finishTime", expectedFinishTime);
		parameter.put("visitingVisitStatus", VisitStatus.APPLY_VISITING);
		parameter.put("expectedFinishTime", expectedFinishTime);

		String hql = "UPDATE ApplicationEntity SET visitStatus=:finishVisitStatus, finishTime=:finishTime WHERE visitStatus=:visitingVisitStatus AND expectedFinishTime=:expectedFinishTime";
		applicationDao.updateByHql(hql, parameter);
	}

	@Override
	public List<ApplicationEntity> getListByVisitStartDate(Date visitStartDate,
			int visitStatus) {
		String hql = "FROM ApplicationEntity WHERE visitStartDate=:visitStartDate AND visitStatus=:visitStatus";
		Parameter params = new Parameter();
		params.put("visitStartDate", visitStartDate);
		params.put("visitStatus", visitStatus);
		return applicationDao.findByHql(hql, params);
	}

	@Override
	public List<ApplicationEntity> getListWithExpectedFinishTime(
			Date expectedFinishTime, int visitStatus) {
		String hql = "FROM ApplicationEntity WHERE expectedFinishTime=:expectedFinishTime AND visitStatus=:visitStatus";
		Parameter params = new Parameter();
		params.put("expectedFinishTime", expectedFinishTime);
		params.put("visitStatus", visitStatus);
		return applicationDao.findByHql(hql, params);
	}

	@Override
	public List<ApplicationEntity> getListByApplyTime(Date startApplyTime,
			Date endApplyTime, int visitStatus) {
		Parameter params = new Parameter();
		params.put("visitStatus", visitStatus);
		StringBuffer buf = new StringBuffer(
				"FROM ApplicationEntity WHERE visitStatus=:visitStatus ");
		Calendar cal = Calendar.getInstance();

		if (null != startApplyTime) {
			cal.setTime(startApplyTime);
			// -1秒，使用sql使用“>”而不使用“>=”
			cal.add(Calendar.MINUTE, -1);
			buf.append(" AND applyTime>:startApplyTime");
			params.put("startApplyTime", cal.getTime());
		}
		if (null != endApplyTime) {
			cal.setTime(endApplyTime);
			// -1秒，使用sql使用“>”而不使用“>=”
			cal.add(Calendar.MINUTE, 1);
			buf.append(" AND applyTime<:endApplyTime");
			params.put("endApplyTime", cal.getTime());
		}
		return applicationDao.findByHql(buf.toString(), params);
	}

	@Override
	public void updateVisitStatusToCompleted(int applyId) {
		ApplicationEntity application = get(applyId);
		application.setVisitStatus(VisitStatus.APPLY_COMPLETED);
		application.setFinishTime(Calendar.getInstance().getTime());
		applicationDao.update(application);
	}

	/**
	 * 获取复诊到诊率数据<br/>
	 * 复诊到诊率 = （到诊次数/随访周期总复诊次数）*100%
	 */
	@Override
	public float getFuZhenRate(int applyId) {
		ApplicationEntity application = get(applyId);

		TemplateEntity template = application.getTemplate();

		if (null == template) {
			return 0;
		}

		List<TemplatePhaseEntity> phases = template.getPhases();

		if (null == phases) {
			return 0;
		}

		int passed = 0, // 到诊次数
		total = 0; // 随访周期总复诊次数
		boolean fuzhenTimeNotYet = true; // 还未到复诊时间
		for (int i = 0, j = phases.size(); i < j; i++) {
			TemplatePhaseEntity phase = phases.get(i);
			if (0 == phase.getSelected()) { // 如果不是选中的阶段，不做处理
				// #1729 【随访】【随访报告】[依从性]复诊到诊率异常，定制计划只有一个月的随访提交报告后显示复诊到诊率为25%
				continue;
			}
			total++;
			// 如果不是不需要复诊状态，也不是未复诊状态，则认为已经提交
			if ((phase.getFuZhenStatus() != VisitStatus.FUZHEN_TIME_NOT_YET)
					&& (phase.getFuZhenStatus() != VisitStatus.FUZHEN_UNCOMMITTED)) {
				passed++;
			}
			if (phase.getFuZhenStatus() != VisitStatus.FUZHEN_TIME_NOT_YET) {
				fuzhenTimeNotYet = false;
			}
		}
		if (fuzhenTimeNotYet) {
			return -1;
		}

		return (float) Math.rint((double) (total == 0 ? 100
				: ((double) passed / (double) total) * 100));
	}

	@Override
	public void updateFuZhenRate(int applyId) {
		float rate = getFuZhenRate(applyId);
		ApplicationEntity application = get(applyId);
		application.getRateCount().setFuZhenRate((int) rate);
		applicationDao.update(application);
	}

	@Override
	public void updateCaseHistoryRate(int applyId) {
		// 异步统计处理
		// CaseHistoryRateMessage message = new CaseHistoryRateMessage(applyId);
		// RedisQueue<CaseHistoryRateMessage> redisQueue = SpringContextHolder
		// .getBean("eszserviceQueue");
		// redisQueue.pushFromHead(message);
		float rate = countCaseHistoryRate(applyId);
		ApplicationEntity application = get(applyId);
		application.getRateCount().setCaseHistoryRate((int) Math.rint(rate));
		applicationDao.update(application);

	}

	@Override
	public List<ApplicationEntity> getListBySick(int sickId, int... visitStatus) {
		StringBuffer hql = new StringBuffer(
				"FROM ApplicationEntity WHERE sick.id=:sickId");
		Parameter params = new Parameter();
		params.put("sickId", sickId);

		if ((null != visitStatus) && (visitStatus.length > 0)) {
			hql.append(" AND visitStatus IN ( ");

			for (int i = 0; i < visitStatus.length; i++) {
				if (i > 0) {
					hql.append(",");
				}
				hql.append(visitStatus[i]);
			}
			hql.append(" ) ");
		}
		hql.append(" ORDER BY applyTime DESC");
		return applicationDao.findByHql(hql.toString(), params);
	}

	/**
	 * 患者个人信息统计项。<br/>
	 * 返回一个二维数组，int[0]=实填数，int[1]=应填数<br/>
	 * 
	 * @param applyId
	 * @return
	 */
	public int[] countSickReport(SickEntity sick) {
		int passed = 0, total = 5;
		// 姓名
		if (null != sick.getName()) {
			passed++;
		}
		// 性别
		if (null != sick.getSex()) {
			passed++;
		}
		// 出生日期
		if (null != sick.getBirthday()) {
			passed++;
		}
		// 身高
		if (0 != sick.getHeight()) {
			passed++;
		}
		// 体重
		if (0 != sick.getWeight()) {
			passed++;
		}
		return new int[] { passed, total };
	}

	/**
	 * 统计体征项报告。<br/>
	 * 返回一个二维数组，int[0]=实填数，int[1]=应填数<br/>
	 * 
	 * @see {@link com.sinohealth.eszservice.service.visit.IBodySignService#countBodySignReport}
	 * 
	 * @param applyId
	 * @return
	 */
	public int[] countBodySignReport(int applyId) {
		ApplicationEntity application = get(applyId);
		if (null == application.getTemplate()) {
			return new int[] { 0, 0 };
		}
		return bodySignService.countBodySignReport(application.getTemplate()
				.getTemplId());
	}

	/**
	 * 统计阶段项报告。<br/>
	 * 返回一个二维数组，int[0]=实填数，int[1]=应填数<br/>
	 * 
	 * @param phases
	 * @return
	 */
	public int[] countPhasesReport(List<TemplatePhaseEntity> phases) {
		int passed = 0, total = 0;
		if (null != phases) {
			for (TemplatePhaseEntity phase : phases) {
				// 如果selected==0时，不参与统计
				// #1850 【随访变更】随访变更后，病历完整性显示不对
				if (phase.getSelected() == 0) {
					continue;
				}
				// 如果已经提交，统计已经提交和应提交的项目
				if (phase.getFuZhenStatus() != VisitStatus.FUZHEN_TIME_NOT_YET) {
					passed += phase.getSubmittedCount();
					total += phase.getItemCount();
				}
			}
		}
		return new int[] { passed, total };
	}

	@Override
	public float countCaseHistoryRate(int applyId) {

		int passed = 0, // 已填写必填项目数量
		total = 0; // 总必填项目数量

		ApplicationEntity apply = get(applyId);

		TemplateEntity templ = apply.getTemplate();

		// 1.患者申请随访模版：必填项目4项
		int[] sickNum = countSickReport(apply.getSick());
		passed += sickNum[0];
		total += sickNum[1];

		// 就诊记录
		total++;
		AppCasesComponent appCases = apply.getAppCases();
		if ((null != appCases.getLeaveHosDate())
				|| (null != appCases.getCasesDate())
				|| (null != appCases.getConfirmDate())) {
			passed++;
		}

		// 个人史
		total++;
		Set<CheckItemValueEntity> personalHis = apply.getPersonalHistory()
				.getValues();
		if ((null != personalHis) && (personalHis.size() > 0)) {
			passed++;
		}

		// TODO 随访报告统计
		// 2.自我管理 体征项目：如2项，每周检查，per个月随访，共24项
		int[] num = countBodySignReport(applyId);
		passed += num[0];
		total += num[1];

		// 3.复诊检验：如共复诊1项，复诊了1次
		// 4.复诊检查：如共11项，填写了11项
		// 5.上传处方：如1项，没上传，填写了0项
		// 三项合并查询
		if (null != templ) {
			int[] phasesNum = countPhasesReport(templ.getPhases());
			passed += phasesNum[0];
			total += phasesNum[1];
		}

		return total == 0 ? 0 : ((float) passed / (float) total) * 100;
	}

	@Override
	public void updateDisease(int applyId, DiseaseEntity disease)
			throws ApplicationDuplicationExecption {
		ApplicationEntity application = get(applyId);
		if (disease.getId().equals(application.getDisease().getId())) {
			return;
		}
		// 已经在在的【待同意】和【随访中】的随访
		List<ApplicationEntity> exists = getListBySick(application.getSick()
				.getId(), VisitStatus.APPLY_PENDING, VisitStatus.APPLY_VISITING);

		if (null != exists) {
			exists.remove(application); // 移除当前申请
			for (ApplicationEntity applicationEntity : exists) {
				if (applicationEntity.getDisease().getId()
						.equals(disease.getId())) {
					throw new ApplicationDuplicationExecption();
				}
			}
		}
		application.setDisease(disease);
		saveApplication(application);

	}

	@Override
	public CaseHistoryRateListDto getCaseHistoryRateList(int applyId) {
		CaseHistoryRateListDto dto = new CaseHistoryRateListDto();
		int passed = 0, // 已填写必填项目数量
		total = 0; // 总必填项目数量

		ApplicationEntity apply = get(applyId);
		TemplateEntity templ = apply.getTemplate();

		// 1.患者申请随访模版：必填项目4项
		total += 6;
		SickEntity sick = apply.getSick();
		// 姓名
		if (null != sick.getName()) {
			passed++;
		}
		dto.addItem("姓名", 1, (null != sick.getName()) ? 1 : 0);
		// 性别
		if (null != sick.getSex()) {
			passed++;
		}
		dto.addItem("性别", 1, (null != sick.getSex()) ? 1 : 0);
		// 出生日期
		if (null != sick.getBirthday()) {
			passed++;
		}
		dto.addItem("出生日期", 1, (null != sick.getBirthday()) ? 1 : 0);
		// 身高
		if (0 != sick.getHeight()) {
			passed++;
		}
		dto.addItem("身高", 1, (0 != sick.getHeight()) ? 1 : 0);
		// 体重
		if (0 != sick.getWeight()) {
			passed++;
		}
		dto.addItem("体重", 1, (0 != sick.getWeight()) ? 1 : 0);

		// 就诊记录
		AppCasesComponent appCases = apply.getAppCases();
		int casePassedFlag = 0;
		if ((null != appCases.getLeaveHosDate())
				|| (null != appCases.getCasesDate())
				|| (null != appCases.getConfirmDate())) {
			passed++;
			casePassedFlag = 1;
		}
		dto.addItem("就诊记录", 1, casePassedFlag);

		// 个人史
		int personalHisFlag = 0;
		Set<CheckItemValueEntity> personalHis = apply.getPersonalHistory()
				.getValues();
		if ((null != personalHis) && (personalHis.size() > 0)) {
			passed++;
			personalHisFlag = 1;
		}
		total++;
		dto.addItem("个人史", 1, personalHisFlag);

		// TODO 随访报告统计
		// 2.自我管理 体征项目：如2项，每周检查，3个月随访，共24项
		if (null != templ) {
			int[] num = bodySignService.countBodySignReport(templ.getTemplId());
			passed += num[0];
			total += num[1];
			List<CaseHistoryRateElem> bodysignElems = bodySignService
					.getCaseHistoryRateList(templ.getTemplId());
			dto.getItems().addAll(bodysignElems);

			// 3.复诊检验：如共复诊1项，复诊了1次
			// 4.复诊检查：如共11项，填写了11项
			// 5.上传处方：如1项，没上传，填写了0项
			// 三项合并查询
			List<TemplatePhaseEntity> phases = templ.getPhases();
			if (null != phases) {
				for (TemplatePhaseEntity phase : phases) {
					// 如果selected==0时，不参与统计
					// #1850 【随访变更】随访变更后，病历完整性显示不对
					if (phase.getSelected() == 0) {
						continue;
					}
					// 如果已经提交，统计已经提交和应提交的项目
					if (phase.getFuZhenStatus() != VisitStatus.FUZHEN_TIME_NOT_YET) {
						dto.addItem("第" + phase.getTimePoint()
								+ getUnitText(templ.getCycleUnit()),
								phase.getItemCount(), phase.getSubmittedCount());
						passed += phase.getSubmittedCount();
						total += phase.getItemCount();
					}
				}
			}
		}

		dto.setPassed(passed);
		dto.setTotal(total);
		return dto;
	}

	@Override
	public void updateTakedMedRate(int applyId) {
		ApplicationEntity application = get(applyId);
		if (null == application) {
			return;
		}
		float rate = takeMedRecordService.getTakedRate(application.getSick()
				.getId(), application.getVisitStartDate(), application
				.getExpectedFinishTime());
		int intRate = (int) Math.rint(rate * 100); // 四舍五入
		application.getRateCount().setTakeMedRate(intRate);
		saveApplication(application);

	}

	@Override
	public List<ApplicationEntity> getFamilyHistoryList(int sickId) {
		String hql = "FROM ApplicationEntity WHERE sick.id=:p1 and familyHistory is not null and familyHistory not in('','[]') order by applyTime desc";
		return applicationDao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public void updateCaseHistoryRateBySickId(int sickId) {
		// TODO 应该异步操作
		List<ApplicationEntity> list = getListBySick(sickId,
				VisitStatus.APPLY_VISITING);
		if (null != list) {
			for (ApplicationEntity application : list) {
				updateCaseHistoryRate(application.getApplyId());
			}
		}
	}

	@Override
	public void update(ApplicationEntity application) {
		applicationDao.update(application);
	}

	public String getUnitText(int unit) {
		switch (unit) {
		case 0:
			return "日";
		case 1:
			return "周";
		case 2:
			return "月";
		}
		return null;
	}

	@Override
	public List<ApplicationEntity> getApplications(String szSubject,
			Date startDate, Date endDate) {
		String hql = "From ApplicationEntity where visitStatus=2 and "
				+ "szSubject.id=:p1 and sick.id not in(select b.sick.id From BodySignValueEntity b "
				+ "where b.reportDate>= :p2 and b.reportDate <=:p3 and b.item.itemId=:p4) and approvalTime is not null and approvalTime <=:p5";
		return applicationDao.findByHql(hql, new Parameter(szSubject,
				startDate, endDate, VisitItemId.WEIGHT, endDate));
	}
}
