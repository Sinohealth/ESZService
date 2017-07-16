package com.sinohealth.eszservice.service.visit;

import com.sinohealth.eszservice.dto.visit.RejectReasonDto;

public interface IRejectReasonService {

	/**
	 * 3.5.21	获取拒绝原因
	 * @param applyId
	 * @return
	 */
	RejectReasonDto getReason(int applyId);
}
