package com.sinohealth.eszservice.dto.visit;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class ApplyDtoV103 extends BaseDto {

	private static final long serialVersionUID = -1281565835630426075L;

	private Result body = new Result();

	public Result getBody() {
		return body;
	}

	public void setBody(Result body) {
		this.body = body;
	}

	public class Result {

		private int applyId;

		public int getApplyId() {
			return applyId;
		}

		public void setApplyId(int applyId) {
			this.applyId = applyId;
		}

	}

}
