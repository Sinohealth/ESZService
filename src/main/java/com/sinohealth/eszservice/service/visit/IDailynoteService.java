package com.sinohealth.eszservice.service.visit;

import java.util.List;

import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszservice.dto.visit.DailyNoteDto;

public interface IDailynoteService {

	/**
	 * 新增日常注意事项
	 * 
	 * @param content
	 * @param szSubject
	 * @param isOtherNote
	 * @return
	 */
	DailynoteEntity addNew(String content, String szSubject, boolean isOtherNote);

	/**
	 * 
	 * 根据模板ID，获取日常注意事项
	 * 
	 * @param templId
	 * @return
	 */
	List<DailynoteEntity> getDailynotes(int templId);

	DailynoteEntity get(int id);

	/**
	 * 3.5.23 获取日常注意事项
	 * 
	 * @param sickId
	 * @return
	 */
	DailyNoteDto getDailyNotes(int sickId);

	void delete(DailynoteEntity e);

	DailynoteEntity add(DailynoteEntity e);
}
