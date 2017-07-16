package com.sinohealth.eszservice.dto.visit.sick;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class LoginBody implements Serializable {

	private static final long serialVersionUID = 6382639225641953769L;

	private String token;
	
	@JsonIgnoreProperties(value = { "age" })
	@JsonSerialize(using = SickSerializer.class)
	private SickEntity info;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public SickEntity getInfo() {
		return info;
	}

	public void setInfo(SickEntity info) {
		this.info = info;
	}

	public static class SickSerializer extends JsonSerializer<SickEntity> {

		@Override
		public void serialize(SickEntity value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			String url = (null != value.getHeadShot()) ? value.getHeadShot()
					: "";
			if (!"".equals(url)) {
				url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
			}
			value.setHeadShot(url);
			String smallHeadshotUrl = (null != value.getSmallHeadshot()) ? value
					.getSmallHeadshot() : "";
			if (!"".equals(smallHeadshotUrl)) {
				smallHeadshotUrl = QiniuService.getDownloadUrl(
						Space.PERSONAL, smallHeadshotUrl);
			}
			value.setSmallHeadshot(smallHeadshotUrl);
			String account = null != value.getEmail() ? value.getEmail()
					: value.getMobile();
			value.setAccount(account);
			int provinceId = (null != value.getProvince() && null != String
					.valueOf(value.getProvince().getId())) ? value
					.getProvince().getId() : 0;
			value.setProvinceId(provinceId);
			int cityId = (null != value.getCity() && null != String
					.valueOf(value.getCity().getId())) ? value.getCity()
					.getId() : 0;
			value.setCityId(cityId);
			jgen.writeObject(value);
		}
	}
}
