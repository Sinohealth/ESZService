package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sinohealth.eszorm.VisitItemCat;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.HealthArchiveLog;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dao.healthArchive.IHealthArchiveDao;
import com.sinohealth.eszservice.dto.visit.ArchiveListDto;
import com.sinohealth.eszservice.dto.visit.CaseDto;
import com.sinohealth.eszservice.dto.visit.CaseDtov104;
import com.sinohealth.eszservice.dto.visit.FamilyHistoryDto;
import com.sinohealth.eszservice.dto.visit.FamilyHistoryDtoV104;
import com.sinohealth.eszservice.dto.visit.HealthCheckDto;
import com.sinohealth.eszservice.dto.visit.PastHistoryDto;
import com.sinohealth.eszservice.dto.visit.PastHistoryDtoV104;
import com.sinohealth.eszservice.dto.visit.PrescriptionDto;
import com.sinohealth.eszservice.dto.visit.PrescriptionDtoV104;
import com.sinohealth.eszservice.service.sick.ISickOnlineService;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IBodySignValueService;
import com.sinohealth.eszservice.service.visit.ICheckItemValueService;
import com.sinohealth.eszservice.service.visit.IHealthArchiveService;
import com.sinohealth.eszservice.service.visit.IVisitImgValueService;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.IVisitPrescriptionService;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

@Service("healthArchiveService")
public class HealthArchiveServiceImpl implements IHealthArchiveService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IApplicationService applicationService;

	@Autowired
	private IBodySignValueService bodySignValueService;

	@Autowired
	private IVisitPrescriptionService visitPrescriptionService;

	@Autowired
	private ICheckItemValueService checkItemValueService;

	@Autowired
	private IVisitImgValueService visitImgValueService;

	@Autowired
	private ISickOnlineService sickOnlineService;

	@Autowired
	private IHealthArchiveDao healthArchiveDao;

	@Autowired
	private IVisitItemService visitItemService;

	@Override
	public ArchiveListDto getArchiveList(String token) {
		ArchiveListDto dto = new ArchiveListDto();
		int sickId = sickOnlineService.getUserId(token);
		List<ApplicationEntity> applications = applicationService
				.getListBySick(sickId);
		// System.err.println("pastHistory.size:"+applications.size());
		// 保存既往史更新记录
		if (null != applications && applications.size() > 0) {
			for (ApplicationEntity application : applications) {
				AppPastHistoryComponent past = application.getAppPastHistory();

				if (null != past) {
					String Allergy = past.getAllergyHistories();
					String Medical = past.getMedicalHistories();
					String Surgical = past.getSurgicalHistories();
					if ((!"".equals(Allergy)) || (!"".equals(Medical))
							|| (!"".equals(Surgical))) {
						dto.setApplication(application);
						break;
					}

				}
			}
		}

		List<ApplicationEntity> familyHistories = applicationService
				.getFamilyHistoryList(sickId);
		// String family = familyHistories.get(0).getFamilyHistory();
		// System.out.println("familyHistories:"+familyHistories.get(0).getFamilyHistory()+" length:"+family.length());
		// 保存家族史更新记录
		if (null != familyHistories && familyHistories.size() > 0) {
			for (int i = 0; i <= familyHistories.size(); i++) {
				if (null != familyHistories.get(i).getFamilyHistory()) {
					dto.setFamilyHistory(familyHistories.get(i));
					break;
				}
			}
		}

		List<VisitImgEntity> caseLeaveHosPics = visitImgValueService
				.getCaseLeaveHosPics(sickId);
		if (null != caseLeaveHosPics && caseLeaveHosPics.size() > 0) {
			dto.setCases(caseLeaveHosPics.get(0));
		}

		List<VisitPrescriptionEntity> prescriptions = visitPrescriptionService
				.getPrescriptions(sickId);
		// 保存处方更新记录
		if (null != prescriptions && prescriptions.size() > 0) {
			dto.setPrescription(prescriptions.get(0));
		}

		// 保存体征更新记录
		List<BodySignValueEntity> bodySignValues = bodySignValueService
				.getList(sickId, null, null, null);
		if (null != bodySignValues && bodySignValues.size() > 0) {
			dto.setBodySign(bodySignValues.get(0));
		}

		// 保存检验更新
		List<CheckItemValueEntity> checkItemValues = checkItemValueService
				.getCheckItem(sickId);

		List itemImgs = visitImgValueService.getItemImgBySickId(sickId);

		if (null != checkItemValues && null != itemImgs) {
			if (checkItemValues.size() > 0 && itemImgs.size() > 0) {
				List list = (List) itemImgs.get(0);
				System.out.println("imgs: " + list);
				if (null != checkItemValues.get(0).getReportTime()
						&& null != list.get(6)) {
					Date itemValueDate = checkItemValues.get(0).getReportTime();

					Date itemImgDate = DateUtils.StrToDate(list.get(6)
							.toString());

					// 检验数据值时间与检验图片时间比较取较晚的时间
					if (itemValueDate.compareTo(itemImgDate) >= 0) {
						Date itemDate = itemValueDate;
						dto.setItemDate(itemDate);
					} else {
						Date itemDate = itemImgDate;
						dto.setItemDate(itemDate);
					}
				}
			} else {
				if (checkItemValues.size() > 0) {
					if (null != checkItemValues.get(0).getReportTime()) {
						Date itemDate = checkItemValues.get(0).getReportTime();
						dto.setItemDate(itemDate);
					}
				}
				if (itemImgs.size() > 0) {
					List list = (List) itemImgs.get(0);

					if (null != list.get(6)) {
						Date itemDate = DateUtils.StrToDate(list.get(6)
								.toString());
						dto.setItemDate(itemDate);
					}
				}
			}

		} else if (null == checkItemValues && null != itemImgs) {
			List list = (List) itemImgs.get(0);

			if (null != list.get(6)) {
				Date itemDate = DateUtils.StrToDate(list.get(6).toString());
				dto.setItemDate(itemDate);
			}
		} else if (null == itemImgs && null != checkItemValues) {
			if (null != checkItemValues.get(0).getReportTime()) {
				Date itemDate = checkItemValues.get(0).getReportTime();
				dto.setItemDate(itemDate);
			}
		}

		// 保存检查更新记录
		List<VisitImgEntity> visitImgs = visitImgValueService
				.getImgsBySickId(sickId);
		// System.err.println("visitImgs.size:" + visitImgs.size());

		if (null != visitImgs && visitImgs.size() > 0) {
			List list = (List) visitImgs.get(0);
			dto.setImg(list);
		}

		return dto;
	}

	@Override
	public PastHistoryDto getPastHistories(String token) {
		PastHistoryDto dto = new PastHistoryDto();
		int sickId = sickOnlineService.getUserId(token);
		List<ApplicationEntity> list = applicationService.getListBySick(sickId);
		// System.err.println("service list:" + list);
		dto.setList(list);
		return dto;
	}

	@Override
	public PastHistoryDtoV104 getPastHistoriesV104(String token) {
		PastHistoryDtoV104 dto = new PastHistoryDtoV104();
		int sickId = sickOnlineService.getUserId(token);
		List<HealthArchiveLog> list = getHealthArchiveLogsBySickId(sickId,
				VisitItemCat.CAT_PAST_HIS);
		dto.setList(list);
		return dto;
	}

	@Override
	public FamilyHistoryDto getFamilyHistories(String token) {
		FamilyHistoryDto dto = new FamilyHistoryDto();
		int sickId = sickOnlineService.getUserId(token);
		List<ApplicationEntity> list = applicationService
				.getFamilyHistoryList(sickId);
		dto.setList(list);
		return dto;
	}

	@Override
	public FamilyHistoryDtoV104 getFamilyHistoriesV104(String token) {
		FamilyHistoryDtoV104 dto = new FamilyHistoryDtoV104();
		int sickId = sickOnlineService.getUserId(token);
		List<HealthArchiveLog> list = getHealthArchiveLogsBySickId(sickId,
				VisitItemCat.CAT_FAMILY_HIS);
		dto.setList(list);
		return dto;
	}

	@Override
	public CaseDto getCases(String token) {
		CaseDto dto = new CaseDto();
		int sickId = sickOnlineService.getUserId(token);
		List<VisitImgEntity> leaveHosPics = visitImgValueService
				.getLeaveHosPics(sickId);
		List<VisitImgEntity> casesPics = visitImgValueService
				.getCasesPics(sickId);
		Map<String, List<VisitImgEntity>> map = new HashMap<String, List<VisitImgEntity>>();
		map.put("leaveHosPics", leaveHosPics);
		map.put("casesPics", casesPics);
		dto.setMap(map);
		return dto;
	}

	@Override
	public PrescriptionDto getPrescriptions(String token) {
		PrescriptionDto dto = new PrescriptionDto();
		int sickId = sickOnlineService.getUserId(token);
		List<VisitPrescriptionEntity> list = visitPrescriptionService
				.getBySickId(sickId);
		dto.setList(list);
		return dto;
	}

	@Override
	public PrescriptionDtoV104 getPrescriptionsV104(String token) {
		PrescriptionDtoV104 dto = new PrescriptionDtoV104();
		int sickId = sickOnlineService.getUserId(token);
		List<HealthArchiveLog> list = getHealthArchiveLogImgBySickId(sickId,
				VisitItemCat.CAT_PRESCRIPTION);
		dto.setList(list);
		return dto;
	}

	@Override
	public HealthCheckDto getHealthCheck(String token) {
		HealthCheckDto dto = new HealthCheckDto();
		int sickId = sickOnlineService.getUserId(token);
		List<CheckItemValueEntity> items = checkItemValueService
				.getitemsBySickId(sickId);
		dto.setItems(items);
		// 获取检查项图片列表
		List<VisitImgEntity> imgs = visitImgValueService
				.getImgsBySickId(sickId);
		List<VisitImgEntity> imgList = convertVisitImgs(imgs);
		dto.setImgs(imgList);

		// 获取检查项图片列表,查出来的数据不能直接转成对象list
		List list = visitImgValueService.getItemImgBySickId(sickId);
		// System.out.println("list.size:" + list.size()+" list:"+list.get(0));
		List<VisitImgEntity> itemImgs = convertVisitImgs(list);
		// System.out.println("itemImg.size: "+itemImgs.size());
		dto.setItemImgs(itemImgs);
		return dto;
	}

	public static List<VisitImgEntity> convertVisitImgs(List list) {
		List<VisitImgEntity> itemImgs = new ArrayList<VisitImgEntity>();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				VisitImgEntity imgEntity = new VisitImgEntity();
				List os = (List) list.get(i);
				// System.out.println("OS: " + os.get(0));
				Long imgId = null != os.get(0) ? Long.parseLong(os.get(0)
						.toString()) : 0;
				imgEntity.setImgId(imgId);
				int itemId = null != os.get(1) ? Integer.valueOf(
						os.get(1).toString()).intValue() : 0;
				imgEntity.setItemId(itemId);

				String img = null != os.get(2) ? os.get(2).toString() : "";
				imgEntity.setImg(img);

				String thumb = null != os.get(3) ? os.get(3).toString() : "";
				imgEntity.setThumb(thumb);

				int status = null != os.get(4) ? Integer.valueOf(
						os.get(4).toString()).intValue() : 0;
				imgEntity.setStatus(status);

				String remark = null != os.get(5) ? os.get(5).toString() : "";
				imgEntity.setRemarks(remark);

				String postTime = null != os.get(6) ? os.get(6).toString() : "";
				imgEntity.setPostTime(DateUtils.StrToDate(postTime));
				itemImgs.add(imgEntity);
			}
		}
		return itemImgs;
	}

	@Override
	public HealthArchiveLog saveHealthArchiveLog(
			HealthArchiveLog healthArchiveLog) {
		return healthArchiveDao.save(healthArchiveLog);
	}

	@Override
	public HealthArchiveLog get(Long id) {
		return healthArchiveDao.get(id);
	}

	@Override
	public HealthArchiveLog getByImg(String img) {
		return healthArchiveDao.findUniqueBy("img", img);
	}

	@Override
	public List<HealthArchiveLog> getCaseList(int sickId) {
		String hql = "FROM HealthArchiveLog where sickId = :p1 and itemId in (1001,1002) and delFlag= 0 order by postTime desc";
		return healthArchiveDao.findByHql(hql, new Parameter(sickId));
	}

	@Override
	public CaseDtov104 getCasesv104(String token) {
		CaseDtov104 dto = new CaseDtov104();
		int sickId = sickOnlineService.getUserId(token);
		// List<VisitImgEntity> leaveHosPics = visitImgValueService
		// .getLeaveHosPics(sickId);

		List<HealthArchiveLog> leaveHosPics = healthArchiveDao
				.findByHql(
						"FROM HealthArchiveLog where sickId = :p1 "
								+ "and itemId=1001 and delFlag=0 order by postTime desc",
						new Parameter(sickId));
		List<HealthArchiveLog> casesPics = healthArchiveDao
				.findByHql(
						"FROM HealthArchiveLog where sickId = :p1 "
								+ "and itemId=1002 and delFlag=0 order by postTime desc",
						new Parameter(sickId));
		Map<String, List<HealthArchiveLog>> map = new HashMap<String, List<HealthArchiveLog>>();
		map.put("leaveHosPics", leaveHosPics);
		map.put("casesPics", casesPics);
		dto.setMap(map);
		return dto;
	}

	@Override
	public void updatePrescriptionImgData(
			List<VisitPrescriptionEntity> newImgList,
			List<VisitPrescriptionEntity> oldImgList, ApplicationEntity app) {
		int newSize = newImgList.size();
		int oldSize = oldImgList.size();

		if (newSize < oldSize) {
			// 遍历旧图片, 前端已被删除的图片从数据库中移除 app.getAppCases().getApplyImgList()
			for (VisitPrescriptionEntity img : oldImgList) {
				if (!StringUtils.hasLength(img.getMedPic())) { // 如果没有传URL过来，跳过
					continue;
				}
				System.out.println("oldImgId:" + img.getMedPic() + "ID:"
						+ img.getItemId());
				// 如果上传的图片带有imgId，则认为不变或修改
				if ((null != img.getId()) && (0 != img.getId().longValue())) {

					// 如果已经存在，不作改变
					int idx = newImgList.indexOf(img);

					// 如果是有这条数据，否则认为是无效数据
					if (-1 != idx) {
						VisitPrescriptionEntity newImg = newImgList.get(idx);
						// 原图片不相等，认为图片已更改
						if (!img.getMedPic().equals(newImg.getMedPic())) {
							// 因为更新的七牛图库的功能需求，如果是https开头，不做处理
							if (!StringUtils.startsWithIgnoreCase(
									newImg.getMedPic(), "https")) {
								logger.info("修改了门诊图片：{}", img);

								HealthArchiveLog healthLog = new HealthArchiveLog();
								healthLog
										.setImgId(Long.valueOf(newImg.getId()));
								healthLog.setSickId(app.getSick().getId());
								healthLog.setItemId(newImg.getItemId());
								healthLog.setPostTime(new Date());
								healthLog.setThumb(newImg.getSmallMedPic());
								healthLog.setImg(newImg.getMedPic());
								healthLog.setCat(2);
								healthLog.setStatus(newImg.getStatus());
								healthLog
										.setOperationFlag(Constants.HEALTH_ARCHIVE_MODIFY);

								saveHealthArchiveLog(healthLog);
							}
						}
					} else {
						HealthArchiveLog healthLog = new HealthArchiveLog();
						System.out.println("删除的IMGID:" + img.getId());
						healthLog.setImgId(Long.valueOf(img.getId()));
						healthLog.setSickId(app.getSick().getId());
						healthLog.setPostTime(new Date());
						healthLog.setThumb(img.getSmallMedPic());
						healthLog.setImg(img.getMedPic());
						healthLog.setItemId(img.getItemId());
						healthLog.setCat(2);
						healthLog.setStatus(img.getStatus());
						healthLog.setDelFlag(1);
						healthLog
								.setOperationFlag(Constants.HEALTH_ARCHIVE_DELETE);
						saveHealthArchiveLog(healthLog);
						// 如果带了imgId，但是没有找到原值，跳过
						logger.info("删除：{}", healthLog);
						continue;
					}
				}
			}
		} else {
			// 遍历新图片
			for (VisitPrescriptionEntity img : newImgList) {
				if (!StringUtils.hasLength(img.getMedPic())) { // 如果没有传URL过来，跳过
					continue;
				}
				if (0 == img.getId().intValue()) {
					HealthArchiveLog healthLog = new HealthArchiveLog();
					healthLog.setImgId(Long.valueOf(img.getId()));
					healthLog.setSickId(app.getSick().getId());
					healthLog.setItemId(img.getItemId());
					healthLog.setPostTime(new Date());
					healthLog.setThumb(img.getSmallMedPic());
					healthLog.setImg(img.getMedPic());
					healthLog.setCat(2);
					healthLog.setStatus(img.getStatus());
					healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
					saveHealthArchiveLog(healthLog);
					logger.info("新增了处方图片记录：{}", healthLog);
				} else {
					HealthArchiveLog healthLog = new HealthArchiveLog();
					healthLog.setImgId(Long.valueOf(img.getId()));
					healthLog.setSickId(app.getSick().getId());
					healthLog.setItemId(img.getItemId());
					healthLog.setPostTime(new Date());
					healthLog.setThumb(img.getSmallMedPic());
					healthLog.setImg(img.getMedPic());
					healthLog.setCat(2);
					healthLog.setStatus(img.getStatus());
					healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_MODIFY);
					saveHealthArchiveLog(healthLog);
				}
			}
		}

	}

	@Override
	public void updateImgData(List<VisitImgEntity> newImgList,
			List<VisitImgEntity> oldImgList, ApplicationEntity app, int cat) {
		int newSize = newImgList.size();
		int oldSize = oldImgList.size();

		if (newSize < oldSize) {
			// 遍历旧图片, 前端已被删除的图片从数据库中移除 app.getAppCases().getApplyImgList()
			for (VisitImgEntity img : oldImgList) {
				if (!StringUtils.hasLength(img.getImg())) { // 如果没有传URL过来，跳过
					continue;
				}
				// appCases.getApplyImgList().contains(o);
				System.out.println("oldImgId:" + img.getImg() + "ID:"
						+ img.getItemId());
				// 如果上传的图片带有imgId，则认为不变或修改
				if ((null != img.getImgId())
						&& (0 != img.getImgId().longValue())) {

					// 如果已经存在，不作改变
					int idx = newImgList.indexOf(img);

					// 如果是有这条数据，否则认为是无效数据
					if (-1 != idx) {
						VisitImgEntity newImg = newImgList.get(idx);
						// 原图片不相等，认为图片已更改
						if (!img.getImg().equals(newImg.getImg())) {
							// 因为更新的七牛图库的功能需求，如果是https开头，不做处理
							if (!StringUtils.startsWithIgnoreCase(
									newImg.getImg(), "https")) {
								logger.info("修改了门诊图片：{}", img);

								HealthArchiveLog healthLog = new HealthArchiveLog();
								healthLog.setImgId(newImg.getImgId());
								healthLog.setSickId(app.getSick().getId());
								healthLog.setItemId(newImg.getItemId());
								healthLog.setPostTime(new Date());
								healthLog.setThumb(newImg.getThumb());
								healthLog.setImg(newImg.getImg());
								healthLog.setCat(cat);
								healthLog.setStatus(newImg.getStatus());
								healthLog
										.setOperationFlag(Constants.HEALTH_ARCHIVE_MODIFY);
								saveHealthArchiveLog(healthLog);
							}
						}
					} else {
						HealthArchiveLog healthLog = new HealthArchiveLog();
						System.out.println("删除的IMGID:" + img.getImgId());
						healthLog.setImgId(img.getImgId());
						healthLog.setSickId(app.getSick().getId());
						healthLog.setPostTime(new Date());
						healthLog.setThumb(img.getThumb());
						healthLog.setImg(img.getImg());
						healthLog.setCat(cat);
						healthLog.setItemId(img.getItemId());
						healthLog.setStatus(img.getStatus());
						healthLog.setDelFlag(1);
						healthLog
								.setOperationFlag(Constants.HEALTH_ARCHIVE_DELETE);
						saveHealthArchiveLog(healthLog);
						logger.info("数据库中找不到原图片{}，删除：{}", img.getImgId());
						continue;
					}
				}
			}
		} else {
			// 遍历新图片
			for (VisitImgEntity img : newImgList) {
				if (!StringUtils.hasLength(img.getImg())) { // 如果没有传URL过来，跳过
					continue;
				}
				if (0 == img.getImgId().longValue()) {
					HealthArchiveLog healthLog = new HealthArchiveLog();
					healthLog.setImgId(img.getImgId());
					healthLog.setSickId(app.getSick().getId());
					healthLog.setItemId(img.getItemId());
					healthLog.setPostTime(new Date());
					healthLog.setThumb(img.getThumb());
					healthLog.setImg(img.getImg());
					healthLog.setCat(cat);
					healthLog.setStatus(img.getStatus());
					healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
					saveHealthArchiveLog(healthLog);
					logger.info("新增了cat类型：" + cat + "记录：{}", healthLog);
				} else {
					HealthArchiveLog healthLog = new HealthArchiveLog();
					healthLog.setImgId(img.getImgId());
					healthLog.setSickId(app.getSick().getId());
					healthLog.setItemId(img.getItemId());
					healthLog.setPostTime(new Date());
					healthLog.setThumb(img.getThumb());
					healthLog.setImg(img.getImg());
					healthLog.setCat(cat);
					healthLog.setStatus(img.getStatus());
					healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_MODIFY);
					saveHealthArchiveLog(healthLog);
				}
			}
		}

	}

	@Override
	public void updateValues(List<CheckItemValueEntity> newValues,
			List<CheckItemValueEntity> oldValues, ApplicationEntity app, int cat)
			throws ValueOutOfRangeException {
		Calendar cal = Calendar.getInstance();
		for (Iterator<CheckItemValueEntity> iterator = newValues.iterator(); iterator
				.hasNext();) {
			CheckItemValueEntity value = (CheckItemValueEntity) iterator.next();
			SickEntity sick = app.getSick();
			value.setCat(cat);
			value.setSick(sick);
			value.setReportTime(cal.getTime());

			// 如果值已经存在，只需要更新
			int idx = oldValues.indexOf(value);
			if (-1 != idx) {
				if (StringUtils.isEmpty(value.getReportValue())) {// 没有输入值，删除结果值
					oldValues.remove(idx);
					continue;
				}
				int warnLevel = 0;
				warnLevel = visitItemService.getWarnLevel(value.getVisitItem()
						.getItemId(), value.getReportValue(), sick.getSex());
				CheckItemValueEntity oldVal = oldValues.get(idx);
				HealthArchiveLog healthLog = new HealthArchiveLog();
				healthLog.setSickId(app.getSick().getId());
				healthLog.setItemId(oldVal.getVisitItem().getItemId());
				healthLog.setPostTime(cal.getTime());
				healthLog.setValue(oldVal.getReportValue());
				healthLog.setReportWarnLevel(warnLevel);
				healthLog.setResultId(oldVal.getResultId());
				healthLog.setCat(cat);
				healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_MODIFY);
				saveHealthArchiveLog(healthLog);
				// 如果数据库中还不存在值
			} else {
				// 检测值的告警范围
				if (StringUtils.isEmpty(value.getReportValue())) {
					continue;
				}
				int warnLevel = 0;
				warnLevel = visitItemService.getWarnLevel(value.getVisitItem()
						.getItemId(), value.getReportValue(), sick.getSex());

				HealthArchiveLog healthLog = new HealthArchiveLog();
				healthLog.setSickId(app.getSick().getId());
				healthLog.setItemId(value.getVisitItem().getItemId());
				healthLog.setPostTime(cal.getTime());
				healthLog.setValue(value.getReportValue());
				healthLog.setReportWarnLevel(warnLevel);
				healthLog.setResultId(value.getResultId());
				healthLog.setCat(cat);
				healthLog.setOperationFlag(Constants.HEALTH_ARCHIVE_ADD);
				saveHealthArchiveLog(healthLog);
				logger.info("新增了cat类型：" + cat + "记录：{}", healthLog);

			}

		}

	}

	@Override
	public List<HealthArchiveLog> getHealthArchiveLogsBySickId(int sickId,
			int cat) {
		String hql = "From HealthArchiveLog where sickId =:p1 and cat =:p2 and value not in ('[]','[{}]','') order by postTime desc";
		return healthArchiveDao.findByHql(hql, new Parameter(sickId, cat));
	}

	@Override
	public List<HealthArchiveLog> getHealthArchiveLogImgBySickId(int sickId,
			int cat) {
		String hql = "From HealthArchiveLog where sickId =:p1 and cat =:p2 order by postTime desc";
		return healthArchiveDao.findByHql(hql, new Parameter(sickId, cat));
	}

	@Override
	public List<HealthArchiveLog> getImgsLogBySickId(int sickId, int cat) {
		String hql = "From HealthArchiveLog where sickId =:p1 and cat =:p2 and img not in ('','null') order by postTime desc";
		return healthArchiveDao.findByHql(hql, new Parameter(sickId, cat));
	}

	@Override
	public List<HealthArchiveLog> getDatasLogBySickId(int sickId, int cat) {
		String hql = "From HealthArchiveLog where sickId =:p1 and cat =:p2 and value not in ('','null') order by postTime desc";
		return healthArchiveDao.findByHql(hql, new Parameter(sickId, cat));
	}

}
