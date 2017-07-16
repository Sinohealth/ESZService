package com.sinohealth.eszservice.dao.message.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.message.MessageEntity;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.message.IMessageDao;

@Repository
public class MessageDaoImpl extends SimpleBaseDao<MessageEntity, Long>
		implements IMessageDao {

	public MessageDaoImpl() {
		super(MessageEntity.class);
	}

	@Override
	public int getDoctorUnreadCount(int userId, String szSubject) {
		String hql = "SELECT COUNT(*) FROM DoctorMessageEntity WHERE doctor.id=:userId AND szSubject=:szSubject AND isread=1";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("szSubject", szSubject);
		Number r = (Number) query.uniqueResult();
		return r.intValue();
	}

	@Override
	public int getSickUnreadCount(int userId) {
		String hql = "SELECT COUNT(*) FROM SickMessageEntity WHERE sick.id=:userId AND isread=1";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		Number r = (Number) query.uniqueResult();
		return r.intValue();
	}
}
