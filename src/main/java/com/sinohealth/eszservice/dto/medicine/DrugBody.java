package com.sinohealth.eszservice.dto.medicine;

import java.io.Serializable;

public class DrugBody implements Serializable{
	private static final long serialVersionUID = 1L;	

	private String ingredient;         /*成份*/
	private String indications;         /*适应症*/
	private String dosage;         /*用法用量*/
	private String adverseReactions;         /*不良反应*/
	private String avoid;         /*禁忌*/
	private String precautions;         /*注意事项*/
	private String specialPopulationsMedication;         /*特殊人群*/
	private String drugInteractions;         /*药物相互作用*/
	private String pharmacologicalEffects;         /*药理作用*/
	private String storage;         /*贮藏*/
	private String validity;         /*有效期*/
	private String approvalNumber;         /*批准文号*/
	private String manufacturer;         /*生产企业*/
	private String brandName;         /*商品名称*/
	private String commonName;         /*药品名称*/
	private String enName;         /*英文名称*/
	private String eDrug;         /*基药*/
	private String rank;         /*工信部排名*/
	
	private String atcType;         /*ATC类型*/
	private String dosageForm;      /*工信部排名*/
	private String hcType;          /*医保类型*/
	private String otcType;         /*OTC类型*/
	
	
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getIndications() {
		return indications;
	}
	public void setIndications(String indications) {
		this.indications = indications;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getAdverseReactions() {
		return adverseReactions;
	}
	public void setAdverseReactions(String adverseReactions) {
		this.adverseReactions = adverseReactions;
	}
	public String getAvoid() {
		return avoid;
	}
	public void setAvoid(String avoid) {
		this.avoid = avoid;
	}
	public String getPrecautions() {
		return precautions;
	}
	public void setPrecautions(String precautions) {
		this.precautions = precautions;
	}
	public String getSpecialPopulationsMedication() {
		return specialPopulationsMedication;
	}
	public void setSpecialPopulationsMedication(String specialPopulationsMedication) {
		this.specialPopulationsMedication = specialPopulationsMedication;
	}
	public String getDrugInteractions() {
		return drugInteractions;
	}
	public void setDrugInteractions(String drugInteractions) {
		this.drugInteractions = drugInteractions;
	}
	public String getPharmacologicalEffects() {
		return pharmacologicalEffects;
	}
	public void setPharmacologicalEffects(String pharmacologicalEffects) {
		this.pharmacologicalEffects = pharmacologicalEffects;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String geteDrug() {
		return eDrug;
	}
	public void seteDrug(String eDrug) {
		this.eDrug = eDrug;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getAtcType() {
		return atcType;
	}
	public void setAtcType(String atcType) {
		this.atcType = atcType;
	}
	public String getDosageForm() {
		return dosageForm;
	}
	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}
	public String getHcType() {
		return hcType;
	}
	public void setHcType(String hcType) {
		this.hcType = hcType;
	}
	public String getOtcType() {
		return otcType;
	}
	public void setOtcType(String otcType) {
		this.otcType = otcType;
	}
	
	
}
