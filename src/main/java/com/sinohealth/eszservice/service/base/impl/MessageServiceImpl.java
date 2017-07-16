package com.sinohealth.eszservice.service.base.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.base.SzSubjectEntity;
import com.sinohealth.eszorm.message.DoctorMessageEntity;
import com.sinohealth.eszorm.message.MessageEntity;
import com.sinohealth.eszorm.message.SickMessageEntity;
import com.sinohealth.eszservice.common.Constants;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.message.IMessageDao;
import com.sinohealth.eszservice.dto.message.MessageListDto;
import com.sinohealth.eszservice.dto.message.UnreadMessageCountDto;
import com.sinohealth.eszservice.service.base.IMessageService;

@Service
public class MessageServiceImpl implements IMessageService {

	@Autowired
	private IMessageDao dao;

	public DoctorMessageEntity add(DoctorMessageEntity doctorMessage) {
		return (DoctorMessageEntity) dao.save(doctorMessage);
	}

	public SickMessageEntity add(SickMessageEntity sickMessageEntity) {
		return (SickMessageEntity) dao.save(sickMessageEntity);
	}

	@Override
	public MessageEntity addMessage(int userId, String title, String appContent) {
		return addMessage(userId, title, appContent, "");
	}

	@Override
	public MessageEntity addMessage(int userId, String title,
			String appContent, String szSubject) {
		Calendar cal = Calendar.getInstance();
		if ((userId > Constants.DOCTOR_USER_ID_START)
				&& (userId < Constants.DOCTOR_USER_ID_END)) {
			DoctorMessageEntity e = new DoctorMessageEntity();
			DoctorEntity doctor = new DoctorEntity();
			doctor.setId(userId);
			e.setDoctor(doctor);
			e.setTitle(title);
			e.setAppContent(appContent);
			e.setCreateTime(cal.getTime());
			e.setSzSubject(szSubject);
			return add(e);
		} else if (userId > Constants.SICK_USER_ID_START) {
			SickMessageEntity e = new SickMessageEntity();
			SickEntity sick = new SickEntity();
			sick.setId(userId);
			e.setSick(sick);
			e.setTitle(title);
			e.setAppContent(appContent);
			e.setCreateTime(cal.getTime());
			// e.setSzSubject(szSubject);
			return add(e);
		} else {
			System.err.println("用户ID超出范围：" + userId);
			return null;
		}
	}

	@Override
	public BaseDto deleteForApi(int userId, String ids) {
		BaseDto dto = new BaseDto();
		Calendar cal = Calendar.getInstance();
		String[] idArr = ids.split(",");
		if ((userId > Constants.DOCTOR_USER_ID_START)
				&& (userId < Constants.DOCTOR_USER_ID_END)) {
			if (null != idArr) {
				for (String s : idArr) {
					try {
						long id = Long.parseLong(s);
						dao.updateByHql(
								"UPDATE DoctorMessageEntity SET deleted=1, deleteTime=:p3 WHERE id=:p1 AND doctor.id=:p2",
								new Parameter(id, userId, cal.getTime()));
					} catch (NumberFormatException e) {
						// 字符串不能转为数据，不予处理
					}
				}
			}
		} else if (userId > Constants.SICK_USER_ID_START) {
			if (null != idArr) {
				for (String s : idArr) {
					try {
						long id = Long.parseLong(s);
						dao.updateByHql(
								"UPDATE SickMessageEntity SET deleted=1, deleteTime=:p3 WHERE id=:p1 AND sick.id=:p2",
								new Parameter(id, userId, cal.getTime()));
					} catch (NumberFormatException e) {
						// 字符串不能转为数据，不予处理
					}
				}
			}
		} else {
			return null; // 不能用户ID不合格
		}
		return dto;
	}

	@Override
	public MessageListDto getListForApi(int userId, Date beginTime,
			Date createTime, int num) {
		return getListForApi(userId, beginTime, createTime, num, null);
	}

	@Override
	public MessageListDto getListForApi(int userId, Date bTime, Date cTime,
			int pageSize, String szSubject) {
		MessageListDto dto = new MessageListDto();

		List<MessageEntity> list = null;
		if ((userId > Constants.DOCTOR_USER_ID_START)
				&& (userId < Constants.DOCTOR_USER_ID_END)) {
			list = getDoctorList(userId, bTime, cTime, pageSize + 1, szSubject); // 查询的时候，多查询一条出来，用来判断是否最后一页
		} else if (userId > Constants.SICK_USER_ID_START) {
			list = getSickList(userId, bTime, cTime, pageSize + 1);
		}
		// 上面查多一条，如果查出来的结果比pageSize大，则表明不是最后一页
		if ((null != list) && (list.size() > pageSize)) {
			list.remove(pageSize); // 移除多余的一条
			dto.getMessage().setLastPage(0);
		} else {
			dto.getMessage().setLastPage(1);
		}
		dto.getMessage().setList(list);
		return dto;
	}

	@Override
	public void updateMessageRead(List<MessageEntity> list) {
		if (null != list) {
			for (MessageEntity messageEntity : list) {
				updateMessageRead(messageEntity);
			}
		}
	}

	public void updateMessageRead(MessageEntity messageEntity) {
		if (messageEntity.getIsread() != 0) {
			messageEntity.setIsread(0);
			dao.save(messageEntity);
		}
	}

	@Override
	public List<MessageEntity> getDoctorList(int userId, Date beginTime,
			Date createTime, int pageSize, String szSubject) {
		Parameter params = new Parameter();
		StringBuffer buf = new StringBuffer();
		buf.append("FROM DoctorMessageEntity WHERE doctor.id=:userId ");
		if (null != beginTime) {
			buf.append(" AND :beginTime < createTime ");
			params.put("beginTime", beginTime);
		}
		if (null != createTime) {
			buf.append(" AND :createTime > createTime ");
			params.put("createTime", createTime);
		}
		buf.append(" AND type=1 AND deleted = 0 ");
		buf.append("  AND szSubject=:szSubject ORDER BY createTime DESC");
		params.put("userId", userId);
		params.put("szSubject", szSubject);
		return dao.findByHql(buf.toString(), params, pageSize);
	}

	@Override
	public List<MessageEntity> getSickList(int userId, Date beginTime,
			Date createTime, int pageSize) {
		Parameter params = new Parameter();
		StringBuffer buf = new StringBuffer();
		buf.append("FROM SickMessageEntity WHERE sick.id=:userId ");
		if (null != beginTime) {
			buf.append(" AND :beginTime < createTime ");
			params.put("beginTime", beginTime);
		}
		if (null != createTime) {
			buf.append(" AND :createTime > createTime ");
			params.put("createTime", createTime);
		}
		buf.append(" AND type=1 AND deleted = 0 ");
		buf.append(" ORDER BY createTime DESC");
		params.put("userId", userId);
		return dao.findByHql(buf.toString(), params, pageSize);
	}

	@Override
	public List<MessageEntity> getSystemMessage(String appName, Date beginTime,
			Date createTime, int pageSize) {
		Parameter params = new Parameter();
		StringBuffer buf = new StringBuffer();
		buf.append("FROM SystemMessageEntity WHERE (appName IS NULL OR  appName=:appName)");
		if (null != beginTime) {
			buf.append(" AND :beginTime < createTime ");
			params.put("beginTime", beginTime);
		}
		if (null != createTime) {
			buf.append(" AND :createTime > createTime ");
			params.put("createTime", createTime);
		}
		params.put("appName", appName);
		buf.append(" AND type=2 AND deleted = 0 ");
		buf.append(" ORDER BY createTime DESC");

		return dao.findByHql(buf.toString(), params, pageSize);
	}

	@Override
	public BaseDto getUnreadCountForApi(int userId, String szSubject) {
		int count = 0;
		if ((userId > Constants.DOCTOR_USER_ID_START)
				&& (userId < Constants.DOCTOR_USER_ID_END)) {
			count = dao.getDoctorUnreadCount(userId, szSubject);
		} else if (userId > Constants.SICK_USER_ID_START) {
			count = dao.getSickUnreadCount(userId);
		}
		UnreadMessageCountDto dto = new UnreadMessageCountDto();
		dto.setUnread(count);
		return dto;
	}

	@Override
	public void addMessage(int userId, String title, String content,
			SzSubjectEntity szSubject) {
		addMessage(userId, title, content, szSubject.getId());

	}
}
