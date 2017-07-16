package com.sinohealth.eszservice.dao.visit.impl;

import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.CommentEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.ICommentDao;

@Repository("commentDao")
public class CommentDaoImpl extends SimpleBaseDao<CommentEntity, Integer> implements ICommentDao {

	public CommentDaoImpl() {
		super(CommentEntity.class);
		// TODO Auto-generated constructor stub
	}
	
}
