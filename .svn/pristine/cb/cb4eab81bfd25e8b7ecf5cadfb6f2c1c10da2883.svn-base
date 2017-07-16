package com.sinohealth.eszservice.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.doctor.LoginHistoryEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.doctor.ILoginHistoryDao;
import com.sinohealth.eszservice.service.base.ILoginHistoryService;

@Service("loginHistoryService")
public class LoginHistoryServiceImpl implements ILoginHistoryService {

	@Autowired
	private ILoginHistoryDao loginHistoryDao;
	
	@Override
	public LoginHistoryEntity saveLoginHistory(LoginHistoryEntity loginHistory) {
		return loginHistoryDao.save(loginHistory);
	}

	@Override
	public List<LoginHistoryEntity> getLoginHistory(int userId) {
		String hql ="From LoginHistoryEntity where userId =:p1 order by loginTime desc";
		return loginHistoryDao.findByHql(hql, new Parameter(userId));
	}
}
