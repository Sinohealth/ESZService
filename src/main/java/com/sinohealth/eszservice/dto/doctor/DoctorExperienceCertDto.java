package com.sinohealth.eszservice.dto.doctor;

import java.io.Serializable;

import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 医生资质验证目列表dto
 * 
 * @author 陈学宏
 * 
 */
public class DoctorExperienceCertDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -8991070532662964748L;

	/**
	 * 认证状态说明：0-未认证 1-已认证 2-认证中，3-认证失败
	 */
	public static final String[] certStateMsg = { "未认证", "已认证", "认证中", "认证失败" };

	private DoctorEntity doctor;

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{\"errCode\":");
		buf.append(errCode);
		
		if (null != errMsg && (!"".equals(errMsg))) {
			buf.append(",\"errMsg\":\"");
			buf.append(errMsg);
			buf.append("\"");
		}

		if (errCode == DoctorExperienceCertDto.ERRCODE_SUCCESS) { // 如果成功，返回certStatus数据
			buf.append(",certdStatusInfo:{\"certdStatus\":");
			buf.append(doctor.getCertdStatus());
			buf.append(",\"certdMsg\":\"");
			buf.append(certStateMsg[doctor.getCertdStatus()]);
			buf.append("\"}");
		}
		buf.append("}");

		return buf.toString();

	}

}
