package com.sinohealth.eszservice.dto.visit.elem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class SickDetailElem implements Serializable {

	private static final long serialVersionUID = 7548131088297457334L;

	private int sickId;

	private String sickName;

	private int sex;

	private int age;

	private int height;

	private float weight;

	private float bmi;

	private String headshot;

	private String smallHeadshot;

	public int getSickId() {
		return sickId;
	}

	public void setSickId(int sickId) {
		this.sickId = sickId;
	}

	public String getSickName() {
		return sickName;
	}

	public void setSickName(String sickName) {
		this.sickName = sickName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getHeadshot() {
		if (null != headshot && !"".equals(headshot)) {
			// 带七牛url地址的原图url
			headshot = QiniuService.getDownloadUrl(Space.PERSONAL, headshot);
		}
		return headshot;
	}

	public void setHeadshot(String headshot) {
		this.headshot = headshot;
	}

	public String getSmallHeadshot() {
		if (null != smallHeadshot && !"".equals(smallHeadshot)) {
			// 带七牛url地址的原图url
			smallHeadshot = QiniuService.getDownloadUrl(Space.PERSONAL, smallHeadshot);
		}
		return smallHeadshot;
	}

	public void setSmallHeadshot(String smallHeadshot) {
		this.smallHeadshot = smallHeadshot;
	}

	@Transient
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Transient
	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getBmi() {
		return bmi;
	}

	public void setBmi(float bmi) {
		this.bmi = bmi;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}
