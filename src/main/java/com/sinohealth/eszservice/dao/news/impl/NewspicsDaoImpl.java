package com.sinohealth.eszservice.dao.news.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.news.NewspicsEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.news.INewspicsDao;

@Repository("newspicsDao")
public class NewspicsDaoImpl extends SimpleBaseDao<NewspicsEntity, BigInteger> implements INewspicsDao {

	public NewspicsDaoImpl() {
		super(NewspicsEntity.class);
	}
	@Override
	public List<NewspicsEntity> findPicsByNewsId(BigInteger newsId) {
		String hql = "from NewspicsEntity where news.id=:newsId";
		Parameter params = new Parameter();
		params.put("newsId", newsId);
		List<NewspicsEntity> listPics = findByHql(hql, params);
		//System.out.println("find pics :"+listPics);
		return listPics;
	}


}
