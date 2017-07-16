package com.sinohealth.eszservice.service.base;

import java.util.List;

import com.sinohealth.eszorm.entity.doctor.LoginHistoryEntity;

public interface ILoginHistoryService {

	LoginHistoryEntity saveLoginHistory(LoginHistoryEntity loginHistory);
	
	List<LoginHistoryEntity> getLoginHistory(int userId);
}
