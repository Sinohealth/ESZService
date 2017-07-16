package com.sinohealth.eszservice.service.medicine;
/**
 * ATC类型
 * @author 黄洪根
 *
 */
public interface IAtcTypeService {
    /**
     * 获取ATC类型列表
     * @param parentId
     * @return
     */
	String getAtcTypeList(Integer parentId);
}
