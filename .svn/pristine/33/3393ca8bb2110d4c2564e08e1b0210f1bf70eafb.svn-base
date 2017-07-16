package com.sinohealth.eszservice.dto.visit;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionItemEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class PrescriptionDto extends BaseDto {

	private static final long serialVersionUID = 8615972279741095494L;

	private List<VisitPrescriptionEntity> list;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		JSONObject prescriptions = new JSONObject();
		try {
			jo.put("errCode", errCode);
			System.err.print("errMsg: " + errMsg);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			prescriptions.put("pics", convertJsonList(list));
			prescriptions.put("text", converItemJsonList(list));
			jo.put("prescriptions", prescriptions);
		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public JSONArray convertJsonList(List<VisitPrescriptionEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo = new JSONObject();
				jo.put("imgId", list.get(i).getId());
				// jo.put("itemId", null != Integer.valueOf(list.get(i)
				// .getItemId()) ? list.get(i).getItemId() : 0);

				// 返回头像img，如果没有，则返回空字符串
				String url = (null != list.get(i).getMedPic()) ? list.get(i)
						.getMedPic() : "";
				String samllHeadshotUrl = (null != list.get(i).getSmallMedPic()) ? list
						.get(i).getSmallMedPic() : "";
				// 带七牛url地址的原图url
				String img = QiniuService.getDownloadUrl(Space.PRESCRIPTION,
						url);
				// 带七牛url地址的缩略图url
				String thumb = QiniuService.getDownloadUrl(Space.PRESCRIPTION,
						samllHeadshotUrl);

				jo.put("img", img);
				jo.put("thumb", thumb);

				jo.put("status", null != Integer.valueOf(list.get(i)
						.getDoctorMarkStatus()) ? list.get(i)
						.getDoctorMarkStatus() : "");
				jo.put("remarks",
						null != list.get(i).getDoctorMarkRemarks() ? list
								.get(i).getDoctorMarkRemarks() : "");
				jo.put("postTime",
						null != list.get(i).getCreateDate() ? DateUtils
								.formatDate(list.get(i).getCreateDate(),
										"yyyy-MM-dd") : "");
				ja.put(jo);
			}
		}
		return ja;
	}

	public JSONArray converItemJsonList(List<VisitPrescriptionEntity> list)
			throws JSONException {
		JSONArray ja = new JSONArray();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Set<VisitPrescriptionItemEntity> set = list.get(i).getItems();
				if (null != set && set.size() > 0) {
					for (VisitPrescriptionItemEntity item : set) {
						JSONObject jo = new JSONObject();
						jo.put("imgId",
								null != item.getPrescription().getId() ? item
										.getPrescription().getId() : 0);
						jo.put("name", item.getName());
						jo.put("standard", item.getStandard());
						jo.put("num", item.getNum());
						jo.put("dosage_form", item.getDosageForm().getName());
						jo.put("dosage", item.getDosage());
						ja.put(jo);
					}
				}
			}
		}
		return ja;
	}

	public List<VisitPrescriptionEntity> getList() {
		return list;
	}

	public void setList(List<VisitPrescriptionEntity> list) {
		this.list = list;
	}

}
