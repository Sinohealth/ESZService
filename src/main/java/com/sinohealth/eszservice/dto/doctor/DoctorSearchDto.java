package com.sinohealth.eszservice.dto.doctor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinohealth.eszorm.entity.base.DisciplineEntity;
import com.sinohealth.eszorm.entity.doctor.DoctorEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;
import com.sinohealth.eszservice.service.visit.ISzSubjectService;

public class DoctorSearchDto extends BaseDto {

	private static final long serialVersionUID = -4818873673756824891L;

	private DoctorEntity doctor;

	private List<DoctorEntity> list;

	private Map<String, String> subMap;

	@Autowired
	private ISzSubjectService szSubjectService;

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	public List<DoctorEntity> getList() {
		return list;
	}

	public void setList(List<DoctorEntity> list) {
		this.list = list;
	}

	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}

			if (null != list) {
				jo.put("results", changeJsonList(list));
			}
		} catch (JSONException e) {
			{
			}
		}
		return jo.toString();
	}

	public JSONArray changeJsonList(List<DoctorEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		// System.out.println("found total count: " + list.size());
		for (int i = 0; i < list.size(); i++) {
			JSONObject jo = new JSONObject();
			String[] subArray = {};
			if (null != list.get(i).getSzSubjects()) {
				subArray = list.get(i).getSzSubjects().split(",");
			}

			List<String> subjects = Arrays.asList(subArray);
			int subSize = subjects.size();
			if (subSize > 1) {
				for (int j = 0; j < subSize; j++) {
					JSONObject jo2 = doctorJsonObject(i, j, subSize, subjects);
					ja.put(jo2);
				}
			} else {
				jo = doctorJsonObject(i, 0, subSize, subjects);
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONObject doctorJsonObject(int i, int j, int subjectSize,
			List<String> subjects) throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("doctorId", null != list.get(i).getId() ? list.get(i).getId()
				: 0);
		jo.put("doctorName", null != list.get(i).getName() ? list.get(i)
				.getName() : "");
		if (subjectSize > 1) {
			jo.put("szSubject", null != subjects.get(j) ? subjects.get(j) : "");
			// 查专科的增中文名
			// System.out.println("subject:"+subjects.get(j));
			if (null != subjects.get(j)) {
				String subjectName = subMap.get(subjects.get(j));
				jo.put("szSubjectName", null != subjectName ? subjectName : "");
			} else {
				jo.put("szSubjectName", "");
			}

		} else {
			jo.put("szSubject",
					null != list.get(i).getSzSubjects() ? list.get(i)
							.getSzSubjects() : "");
			// 查专科的增中文名
			if (null != subjects.get(j)) {
				String subjectName = subMap.get(subjects.get(j));
				jo.put("szSubjectName", null != subjectName ? subjectName : "");
			} else {
				jo.put("szSubjectName", "");
			}
		}

		jo.put("hospitalName", null != list.get(i).getHospital()
				.getHospitalName() ? list.get(i).getHospital()
				.getHospitalName() : "");
		jo.put("hositalAddr", null != list.get(i).getHospital()
				.getHospitalAddr() ? list.get(i).getHospital()
				.getHospitalAddr() : "");
		jo.put("hospitalId", null != list.get(i).getHospital().getId() ? list
				.get(i).getHospital().getId() : 0);
		jo.put("provinceId", null != list.get(i).getProvince().getId() ? list
				.get(i).getProvince().getId() : 0);
		DisciplineEntity disc = list.get(i).getDiscipline();
		jo.put("disciplineName", null != disc ? disc.getDisciplineName() : "");

		jo.put("recommendCode", null != list.get(i).getRecommendCode() ? list
				.get(i).getRecommendCode() : "");
		jo.put("qrCode", null != list.get(i).getQrCode() ? list.get(i)
				.getQrCode() : "");

		// 返回头像headShot，如果没有，则返回空字符串
		String url = (null != list.get(i).getHeadShot()) ? list.get(i)
				.getHeadShot() : "";

		if (!"".equals(url)) {
			// 带七牛url地址的原图url
			url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
		}
		jo.put("headshot", url);

		String samllHeadshotUrl = (null != list.get(i).getSmallHeadshot()) ? list
				.get(i).getSmallHeadshot() : "";
		if (!"".equals(samllHeadshotUrl)) {
			// 带七牛url地址的缩略图url
			samllHeadshotUrl = QiniuService.getDownloadUrl(Space.PERSONAL,
					samllHeadshotUrl);
		}
		jo.put("smallHeadshot", samllHeadshotUrl);
		return jo;
	}

	public Map<String, String> getSubMap() {
		return subMap;
	}

	public void setSubMap(Map<String, String> subMap) {
		this.subMap = subMap;
	}
}
