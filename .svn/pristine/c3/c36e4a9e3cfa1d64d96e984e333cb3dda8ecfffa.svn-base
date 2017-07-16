package com.sinohealth.eszservice.service.visit.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinohealth.eszorm.VisitStatus;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.TemplateEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IDailynoteDao;
import com.sinohealth.eszservice.dto.visit.DailyNoteDto;
import com.sinohealth.eszservice.dto.visit.DailyNoteDto.DailyNoteElem;
import com.sinohealth.eszservice.service.visit.IApplicationService;
import com.sinohealth.eszservice.service.visit.IDailynoteService;

@Service("dailynoteService")
public class DailynoteServiceImpl implements IDailynoteService {

	@Autowired
	IDailynoteDao dailynoteDao;

	@Autowired
	IApplicationService applicationService;

	public IDailynoteDao getDailynoteDao() {
		return dailynoteDao;
	}

	public void setDailynoteDao(IDailynoteDao dailynoteDao) {
		this.dailynoteDao = dailynoteDao;
	}

	@Override
	public DailynoteEntity addNew(String content, String szSubject,
			boolean isOtherNote) {
		DailynoteEntity ent = new DailynoteEntity();
		ent.setContent(content);
		ent.setSzSubject(szSubject);
		ent.setOtherNote(isOtherNote);
		return dailynoteDao.save(ent);
	}

	@Override
	public List<DailynoteEntity> getDailynotes(int templId) {
		Parameter params = new Parameter(templId);

		return dailynoteDao
				.findByHql(
						"SELECT dailynote FROM DailynoteEntity dailynote JOIN dailynote.templates template  WHERE template.templId=:p1",
						params);
	}

	@Override
	public DailynoteEntity get(int id) {
		return dailynoteDao.get(id);
	}

	@Override
	public DailyNoteDto getDailyNotes(int sickId) {
		DailyNoteDto dto = new DailyNoteDto();
		List<ApplicationEntity> applications = applicationService
				.getListBySick(sickId, VisitStatus.APPLY_VISITING);

		if (null != applications && applications.size() > 0) {
			for (ApplicationEntity applicationEntity : applications) {
				DailyNoteElem e = new DailyNoteDto.DailyNoteElem();
				e.setDisease(applicationEntity.getDisease());
				TemplateEntity template = applicationEntity.getTemplate();
				if (null != template) {
					Set<DailynoteEntity> notes = template.getDailynotes();

					if (null != notes) {
						e.getNotes().addAll(notes);
					}
					dto.getDailyNotes().add(e);
				}
			}
		}
		return dto;
	}

	@Override
	public void delete(DailynoteEntity e) {
		dailynoteDao.delete(e);
	}

	@Override
	public DailynoteEntity add(DailynoteEntity e) {
		return dailynoteDao.save(e);
	}
}
