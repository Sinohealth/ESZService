package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.List;

import com.sinohealth.eszorm.entity.visit.DailynoteEntity;
import com.sinohealth.eszorm.entity.visit.base.DiseaseEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class DailyNoteDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private List<DailyNoteElem> dailyNotes = new ArrayList<>();

	public List<DailyNoteElem> getDailyNotes() {
		return dailyNotes;
	}

	public void setDailyNotes(List<DailyNoteElem> dailyNotes) {
		this.dailyNotes = dailyNotes;
	}

	public static class DailyNoteElem {
		private DiseaseEntity disease;
		private List<DailynoteEntity> notes = new ArrayList<>();

		/**
		 * 因为接口写的是返回String类型的disease，所以要调整一下返回，否则，其实可以票@jsonunwrapped全部显示
		 * 
		 * @return
		 */
		public String getDisease() {
			return disease.getId();
		}

		public void setDisease(DiseaseEntity disease) {
			this.disease = disease;
		}

		public List<DailynoteEntity> getNotes() {
			return notes;
		}

		public void setNotes(List<DailynoteEntity> notes) {
			this.notes = notes;
		}

	}
}
