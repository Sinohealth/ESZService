package com.sinohealth.eszservice.service.visit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszservice.dao.visit.IApplicationDao;
import com.sinohealth.eszservice.dto.visit.RejectReasonDto;
import com.sinohealth.eszservice.service.visit.IRejectReasonService;
import com.sinohealth.eszservice.service.visit.exception.SystemErrorExecption;

@Service("rejectReasonService")
public class RejectReasonServiceImpl implements IRejectReasonService {

	@Autowired
	private IApplicationDao applicationDao;

	@Override
	public RejectReasonDto getReason(int applyId) {
		RejectReasonDto dto = new RejectReasonDto();
		try {
			ApplicationEntity application = applicationDao.get(Integer
					.valueOf(applyId));
			if (null!=application&& null!=application.getVisitStatus()) {
				if ((4 == application.getVisitStatus().intValue()) || (5 == application.getVisitStatus().intValue())) {	
					dto.setApplication(application);
				} else {
					throw new SystemErrorExecption("只有拒绝或退出状态才可以获取原因", RejectReasonDto.ERRCODE_OTHERS);
				}
			}	
		} catch (SystemErrorExecption e) {
			dto.setErrCode(e.getErrCode());
			dto.setErrMsg(e.getMessage());
		}
		return dto;
	}

}
