package com.sinohealth.eszservice.service.medicine;
/**
 * 药品
 * @author 黄洪根
 *
 */
public interface IDrugShareService {

	/**
	 * 医生对患者进行用药分享
	 * @param doctorId
	 * @param sickIds
	 * @param drugId
	 * @return
	 */
	String drugShare(Integer doctorId,String sickIds,Integer drugId);

}
