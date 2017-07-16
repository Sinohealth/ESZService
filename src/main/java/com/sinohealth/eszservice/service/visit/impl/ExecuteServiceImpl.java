package com.sinohealth.eszservice.service.visit.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.CommentEntity;
import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszservice.common.config.ErrorMessage;
import com.sinohealth.eszservice.common.dto.ConstantDoctorVisitErrs;
import com.sinohealth.eszservice.common.dto.ConstantSickVisitErrs;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.ICommentDao;
import com.sinohealth.eszservice.dto.visit.GetCommentByApplyDto;
import com.sinohealth.eszservice.dto.visit.GetCommentDto;
import com.sinohealth.eszservice.dto.visit.SaveCommentDto;
import com.sinohealth.eszservice.service.visit.IExecuteService;
import com.sinohealth.eszservice.service.visit.IPhaseService;
import com.sinohealth.eszservice.service.visit.exception.PhaseIdDuplicateExecption;
import com.sinohealth.eszservice.service.visit.exception.SaveCommentExecption;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

@Service("executeService")
public class ExecuteServiceImpl implements IExecuteService {

	@Autowired
	private ICommentDao commentDao;

	@Autowired
	private IPhaseService phaseService;

	@Override
	public SaveCommentDto saveComment(Integer phaseId, int stars, int isSick,
			String comment) {
		/**
		 * TODO 未评价的阶段，到阶段中取commentable属性，判断是否可以评价
		 */
		SaveCommentDto dto = new SaveCommentDto();

		TemplatePhaseEntity phase = phaseService.get(phaseId);
		// System.out.println("isSick:"+isSick);
		try {
			String hql = "from CommentEntity where phase.templPhaseId =:templPhaseId and isSick =:isSick";
			Parameter params = new Parameter();
			params.put("templPhaseId", phaseId);
			params.put("isSick", isSick);
			List<CommentEntity> list = commentDao.findByHql(hql, params);
			// System.out.println("commentList: "+list.size()+" ");
			CommentEntity returnComm = null;
			if (list.size() == 0) {
				if (null != phase) {
					if (1 == phase.getCommentable()) {
						CommentEntity comEntity = new CommentEntity();
						comEntity.setPhase(phase);
						comEntity.setApplication(phase.getTemplate()
								.getApplication());
						comEntity.setTimePoint(phase.getTimePoint());

						/* stars 只能在1~5之间 */
						if (stars < 1) {
							stars = 1;
						} else if (stars > 5) {
							stars = 5;
						}

						comEntity.setStars(stars);
						comEntity.setIsSick(isSick);
						comEntity.setComment(comment);
						returnComm = commentDao.save(comEntity);
					} else {
						throw new SaveCommentExecption(
								ErrorMessage
										.getConfig(ConstantSickVisitErrs.COMMENT_NOT_OPENED),
								ConstantSickVisitErrs.COMMENT_NOT_OPENED);
					}
				} else {
					throw new SystemErrorExecption("找不到阶段记录 ",
							SaveCommentDto.ERRCODE_OTHERS);
				}

				if (returnComm == null) {
					throw new SystemErrorExecption(
							ErrorMessage
									.getConfig(SaveCommentDto.ERRCODE_SYSTEM_ERROR),
							SaveCommentDto.ERRCODE_SYSTEM_ERROR);
				}

			} else {
				String duplicateStr = isSick == 0 ? "此医生已参加过随访评价"
						: "此患者已参加过随访评价";
				int errorCode = isSick == 0 ? ConstantDoctorVisitErrs.ERRCODE_DUPLICATE_DOCTOR_PHASEID
						: ConstantDoctorVisitErrs.ERRCODE_DUPLICATE_SICK_PHASEID;
				throw new PhaseIdDuplicateExecption(duplicateStr, errorCode);
			}
		} catch (SaveCommentExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		} catch (PhaseIdDuplicateExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(ErrorMessage.getConfig(e.getErrCode()));
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

	@Override
	public GetCommentDto getComment(Integer phaseId) {
		GetCommentDto dto = new GetCommentDto();
		String hql = "from CommentEntity where phase.templPhaseId =:templPhaseId";
		Parameter params = new Parameter();
		params.put("templPhaseId", phaseId);
		List<CommentEntity> list = commentDao.findByHql(hql, params);
		dto.setList(list);

		TemplatePhaseEntity phase = phaseService.get(phaseId);

		ApplicationEntity application = phase.getTemplate().getApplication();

		dto.setSick(application.getSick());

		dto.setDoctor(application.getDoctor());

		return dto;
	}

	@Override
	public GetCommentByApplyDto getCommentByApply(int applyId) {
		GetCommentByApplyDto dto = new GetCommentByApplyDto();
		String hql = "from CommentEntity where application.applyId =:applyId";
		Parameter params = new Parameter();
		params.put("applyId", applyId);
		List<CommentEntity> list = commentDao.findByHql(hql, params);
		dto.setComments(list);

		return dto;
	}
}
