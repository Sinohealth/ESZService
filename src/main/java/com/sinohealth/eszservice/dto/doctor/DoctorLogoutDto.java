package com.sinohealth.eszservice.dto.doctor;

import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 资讯栏目列表dto
 * 
 * @author 黄世莲
 * 
 */
public class DoctorLogoutDto extends BaseDto {

	private static final long serialVersionUID = -7773849855852772570L;

	private int doctorId;

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

}
