package com.sinohealth.eszservice.dto.visit;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinohealth.eszorm.entity.visit.AppPastHistoryComponent;
import com.sinohealth.eszorm.entity.visit.ApplicationEntity;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitPrescriptionEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;

public class ArchiveListDto extends BaseDto {

	private static final long serialVersionUID = -1281565835630426075L;

	private ApplicationEntity application;

	private ApplicationEntity familyHistory;

	private VisitImgEntity cases;

	public VisitImgEntity getCases() {
		return cases;
	}

	public void setCases(VisitImgEntity cases) {
		this.cases = cases;
	}

	private VisitPrescriptionEntity prescription;

	private CheckItemValueEntity checkItem;

	private Date itemDate;

	private List img;

	private BodySignValueEntity bodySign;

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		JSONObject healthRecords = new JSONObject();

		String pastHistoryDate = "";
		try {
			jo.put("errCode", errCode);
			if (null != errMsg && (!"".equals(errMsg))) {
				jo.put("errMsg", errMsg);
			}
			// System.err.println("DTO Application: "+application);
			if (null != application) {
				AppPastHistoryComponent past = application.getAppPastHistory();
				// System.err.println("DTO 既往史：" + past);
				if (null != past) {
					String Allergy = past.getAllergyHistories();
					String Medical = past.getMedicalHistories();
					String Surgical = past.getSurgicalHistories();

					// System.err.println("DTO ArchiveList AppPastHistory:"
					// + application.getAppPastHistory() + " Allergy:"
					// + past.getAllergyHistories() + "" + " Medical:"
					// + past.getMedicalHistories() + " Surgical:"
					// +
					// past.getSurgicalHistories()+" applyTime:"+application.getApplyTime());
					if ((!"".equals(Allergy)) || (!"".equals(Medical))
							|| (!"".equals(Surgical))) {
						if (null != application.getApplyTime()) {
							pastHistoryDate = DateUtils.formatDate(
									application.getApplyTime(), "yyyy-MM-dd");
						}
					}
				}
			}
			healthRecords.put("pastHistoryDate", pastHistoryDate);

			String familyHistoryDate = "";
			if (null != familyHistory) {
				if (null != familyHistory.getFamilyHistory()
						|| !"[]".equals(familyHistory.getFamilyHistory())
						|| !"".equals(familyHistory.getFamilyHistory())) {
					if (null != familyHistory.getApplyTime()) {
						familyHistoryDate = DateUtils.formatDate(
								familyHistory.getApplyTime(), "yyyy-MM-dd");
					}
				}
			}
			healthRecords.put("familyHistoryDate", familyHistoryDate);

			if ((null != cases && !"".equals(cases.getImg()))
					|| (null != cases && !"".equals(cases.getThumb()))) {
				healthRecords
						.put("casesDate",
								(null != cases && null != cases.getPostTime()) ? DateUtils
										.formatDate(cases.getPostTime(),
												"yyyy-MM-dd") : "");
			} else {
				healthRecords.put("casesDate", "");
			}

			if (null != prescription && !"".equals(prescription.getMedPic())) {
				healthRecords.put(
						"prescribeDate",
						(null != prescription && null != prescription
								.getCreateDate()) ? DateUtils.formatDate(
								prescription.getCreateDate(), "yyyy-MM-dd")
								: "");
			} else {
				healthRecords.put("prescribeDate", "");
			}

			healthRecords.put(
					"checkItemDate",
					null != itemDate ? DateUtils.formatDate(itemDate,
							"yyyy-MM-dd") : "");

			// System.err.println("img:"+img+" getImg:"+img.getImg()+" getThumb:"+img.getThumb()+"itemID:"+img.getItemId());
			if (null != img) {
				List list = img;
				String postTime = (null != list.get(6)) ? list.get(6)
						.toString() : null;
				if (!"".equals(postTime) && null != postTime) {
					Date postDate = DateUtils.StrToDate(postTime);
					healthRecords.put("checkPicDate",
							DateUtils.formatDate(postDate, "yyyy-MM-dd"));
				} else {
					healthRecords.put("checkPicDate", "");
				}
			} else {
				healthRecords.put("checkPicDate", "");
			}

			/*
			 * if ((null != img && !"".equals(img.getImg()) || (null != img &&
			 * !"" .equals(img.getThumb())))) { healthRecords.put(
			 * "checkPicDate", (null != img && null != img.getPostTime()) ?
			 * DateUtils .formatDate(img.getPostTime(), "yyyy-MM-dd") : ""); }
			 * else { healthRecords.put("checkPicDate", ""); }
			 */

			if (null != bodySign && !"".equals(bodySign.getReportValue())) {
				healthRecords
						.put("bodySignDate",
								null != bodySign
										&& null != bodySign.getPostTime() ? DateUtils
										.formatDate(bodySign.getPostTime(),
												"yyyy-MM-dd") : "");
			} else {
				healthRecords.put("bodySignDate", "");
			}

			jo.put("healthRecords", healthRecords);

		} catch (JSONException e) {
			return "{}";
		}
		return jo.toString();
	}

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public VisitPrescriptionEntity getPrescription() {
		return prescription;
	}

	public void setPrescription(VisitPrescriptionEntity prescription) {
		this.prescription = prescription;
	}

	public CheckItemValueEntity getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(CheckItemValueEntity checkItem) {
		this.checkItem = checkItem;
	}

	public BodySignValueEntity getBodySign() {
		return bodySign;
	}

	public void setBodySign(BodySignValueEntity bodySign) {
		this.bodySign = bodySign;
	}

	public ApplicationEntity getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(ApplicationEntity familyHistory) {
		this.familyHistory = familyHistory;
	}

	public Date getItemDate() {
		return itemDate;
	}

	public void setItemDate(Date itemDate) {
		this.itemDate = itemDate;
	}

	public List getImg() {
		return img;
	}

	public void setImg(List img) {
		this.img = img;
	}

}
