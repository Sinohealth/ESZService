package com.sinohealth.eszservice.dto.visit;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszorm.entity.visit.CommentEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

public class GetCommentDto extends BaseDto {

	private static final long serialVersionUID = 3211506602491916448L;

	@JsonIgnore
	private List<CommentEntity> list;

	@JsonIgnore
	private SickEntity sick;

	@JsonIgnore
	private DoctorEntity doctor;

	public CommentElem getComments() {
		CommentElem elem = new CommentElem();
		for (CommentEntity commentEntity : list) {
			if (null != list) {
				if (commentEntity.getIsSick() == 0) {
					elem.setDoctorComm(commentEntity);
				} else if (commentEntity.getIsSick() == 1) {
					elem.setSickComm(commentEntity);
				}
			}
		}
		elem.setSickName(sick.getName());
		elem.setDoctorName(doctor.getName());
		return elem;
	}

	public SickEntity getSick() {
		return sick;
	}

	public void setSick(SickEntity sick) {
		this.sick = sick;
	}

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	public List<CommentEntity> getList() {
		return list;
	}

	public void setList(List<CommentEntity> list) {
		this.list = list;
	}

	public class CommentElem {

		private CommentEntity doctorComm = new CommentEntity();

		private CommentEntity sickComm = new CommentEntity();

		private String sickName;

		private String doctorName;

		public CommentEntity getDoctorComm() {
			doctorComm.setIsSick(0);
			return doctorComm;
		}

		public void setDoctorComm(CommentEntity doctorComm) {
			this.doctorComm = doctorComm;
		}

		public CommentEntity getSickComm() {
			sickComm.setIsSick(1);
			return sickComm;
		}

		public void setSickComm(CommentEntity sickComm) {
			this.sickComm = sickComm;
		}

		public String getSickName() {
			return sickName;
		}

		public void setSickName(String sickName) {
			this.sickName = sickName;
		}

		public String getDoctorName() {
			return doctorName;
		}

		public void setDoctorName(String doctorName) {
			this.doctorName = doctorName;
		}

	}

}
