package com.sinohealth.eszservice.dto.sick;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinohealth.eszorm.entity.sick.SickEntity;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.service.qiniu.QiniuService;
import com.sinohealth.eszservice.service.qiniu.Space;

public class SickLoginDtoV103 extends BaseDto implements Serializable {

	private static final long serialVersionUID = -1175628491947956594L;

	private SickInfo body = new SickInfo();

	public SickInfo getBody() {
		return body;
	}

	public void setBody(SickInfo body) {
		this.body = body;
	}

	public class SickInfo {
		private String token;

		@JsonIgnoreProperties(value = { "age" })
		private SickEntity info = new SickEntity();

		private Server server = new Server();

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public SickEntity getInfo() {
			System.out.println("sick: " + info);
			if (null != info) {
				String url = (null != info.getHeadShot()) ? info.getHeadShot()
						: "";
				if (!"".equals(url)) {
					url = QiniuService.getDownloadUrl(Space.PERSONAL, url);
				}
				info.setHeadShot(url);
				String smallHeadshotUrl = (null != info.getSmallHeadshot()) ? info
						.getSmallHeadshot() : "";
				if (!"".equals(smallHeadshotUrl)) {
					smallHeadshotUrl = QiniuService.getDownloadUrl(
							Space.PERSONAL, smallHeadshotUrl);
				}
				info.setSmallHeadshot(smallHeadshotUrl);
				String account = null != info.getEmail() ? info.getEmail()
						: info.getMobile();
				info.setAccount(account);
				int provinceId = (null != info.getProvince() && null != String
						.valueOf(info.getProvince().getId())) ? info
						.getProvince().getId() : 0;
				info.setProvinceId(provinceId);
				int cityId = (null != info.getCity() && null != String
						.valueOf(info.getCity().getId())) ? info.getCity()
						.getId() : 0;
				info.setCityId(cityId);
			}
			return info;
		}

		public void setInfo(SickEntity info) {
			this.info = info;
		}

		public Server getServer() {
			return server;
		}

		public void setServer(Server server) {
			this.server = server;
		}

	}

	public class Server {
		private String main;

		private String backup;

		public String getMain() {
			main = Global.getConfig("server.main");
			return main;
		}

		public void setMain(String main) {
			this.main = main;
		}

		public String getBackup() {
			backup = Global.getConfig("server.backup");
			return backup;
		}

		public void setBackup(String backup) {
			this.backup = backup;
		}
	}
}
