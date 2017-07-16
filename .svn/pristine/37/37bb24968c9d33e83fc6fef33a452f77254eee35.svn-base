package com.sinohealth.eszservice.dto.doctor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.base.DisciplineEntity;
import com.sinohealth.eszorm.entity.base.TitleEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

/**
 * 资讯栏目列表dto
 * 
 * @author 黄世莲
 * 
 */
public class DoctorProfileDto extends BaseDto {

	private static final long serialVersionUID = -4911000012335863840L;

	/**
	 * errCode：10006 无此医生
	 */
	public static final int ERRCODE_NO_THIS_DOCTOR = 10006;

	private DoctorEntity doctor;

	private int totalGrade;

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	public int getTotalGrade() {
		return totalGrade;
	}

	public void setTotalGrade(int totalGrade) {
		this.totalGrade = totalGrade;
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();

		try {
			jo.put("errCode", errCode);

			// userInfo
			JSONObject doctorObj = new JSONObject();
			if (null != doctor) {
				doctorObj.put("userId", doctor.getId());
				doctorObj.put("account",
						null != doctor.getMobile() ? doctor.getMobile()
								: doctor.getEmail());
				doctorObj.put("name",
						null != doctor.getName() ? doctor.getName() : "");

				// 专业信息
				DisciplineEntity discipline = doctor.getDiscipline();
				if (null != discipline) {
					doctorObj.put("disciplineId", discipline.getId());
					doctorObj.put("disciplineName",
							discipline.getDisciplineName());
				} else {
					doctorObj.put("disciplineId", 0);
					doctorObj.put("disciplineName", "");
				}
				// 返回省份
				String province = (doctor.getProvince() != null)
						&& (null != doctor.getProvince().getProvinceName()) ? doctor
						.getProvince().getProvinceName() : "";
				doctorObj.put("province", province);

				Object provinceId = (doctor.getProvince() != null)
						&& (null != doctor.getProvince().getId()) ? doctor
						.getProvince().getId() : 0;
				doctorObj.put("provinceId", provinceId);

				// 应该返回医院
				String hospitalName = (null != doctor.getHospital())
						&& (null != doctor.getHospital().getHospitalName()) ? doctor
						.getHospital().getHospitalName() : "";
				doctorObj.put("hospital", hospitalName);

				Object hospitalId = (null != doctor.getHospital())
						&& (null != doctor.getHospital().getId()) ? doctor
						.getHospital().getId() : 0;
				doctorObj.put("hospitalId", hospitalId);

				// 应该返回职称，而不是ID：主要有4种：住院医师，主治医师，副主任工程师，主任医师
				// doctorObj.put("title", "住院医师");
				TitleEntity title = doctor.getTitle();
				if (null != title) {
					doctorObj.put("title",
							null != title.getTitleName() ? title.getTitleName()
									: "");
				} else {
					doctorObj.put("title", "");
				}
				doctorObj.put("cert",
						null != doctor.getCert() ? doctor.getCert() : "");
				doctorObj.put("certdStatus", doctor.getCertdStatus());
				String url = null != doctor.getHeadShot() ? doctor
						.getHeadShot() : "";
				if (!"".equals(url)) {
					url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
				}

				doctorObj.put("recommendCode", null!=doctor.getRecommendCode()?doctor.getRecommendCode():"");
				doctorObj.put("qrCode", null!=doctor.getQrCode()?doctor.getQrCode():"");
				doctorObj.put("headshot", url);

				String smallHeadshotUrl = (null != doctor.getSmallHeadshot()) ? doctor
						.getSmallHeadshot() : "";
				if (!"".equals(smallHeadshotUrl)) {
					smallHeadshotUrl = QiniuService.getDownloadUrl(
							Space.PERSONAL, smallHeadshotUrl);
				}

				doctorObj.put("smallHeadshotUrl", smallHeadshotUrl);

				doctorObj.put("totalGrade", totalGrade);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				doctorObj.put(
						"lastLoginDate",
						null != doctor.getLastLoginDate() ? format
								.format(doctor.getLastLoginDate()) : "");
				jo.put("userInfo", doctorObj);
			}
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}
}
