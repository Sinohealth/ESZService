package com.sinohealth.eszservice.dto.medicine;

import java.io.Serializable;

public class AtcTypeDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String pinyin;		  /*分类第一个字的首字母*/
	private String name;		  /*名称*/
	private String commonDrug;	  /*该类型下的常见药品*/	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCommonDrug() {
		return commonDrug;
	}
	public void setCommonDrug(String commonDrug) {
		this.commonDrug = commonDrug;
	}
	
}
