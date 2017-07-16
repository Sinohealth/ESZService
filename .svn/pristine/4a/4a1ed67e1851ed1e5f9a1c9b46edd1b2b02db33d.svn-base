package com.sinohealth.eszservice.service.visit;

import com.sinohealth.eszservice.dto.visit.GetCommentDto;
import com.sinohealth.eszservice.dto.visit.SaveCommentDto;

public interface IExecuteService {

	/**
	 * 3.4.19 设置随访评价
	 * 
	 * @param phaseId
	 * @param stars
	 * @param isSick
	 * @param comment
	 * @return
	 */
	SaveCommentDto saveComment(Integer phaseId, int stars, int isSick,
			String comment);

	/**
	 * 3.4.20 获取随访评价信息
	 * 
	 * @param phaseId
	 * @return
	 */
	GetCommentDto getComment(Integer phaseId);


	/**
	 * 3.4.20 根据applyId获取随访评价信息
	 * 
	 * @param phaseId
	 * @return
	 */Object getCommentByApply(int applyId);
}
