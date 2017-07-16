package com.sinohealth.eszservice.dto.visit.sick;

import com.sinohealth.eszservice.common.dto.BaseDto;

public class UpdateArchiveDto extends BaseDto {

	private static final long serialVersionUID = 720891179743443115L;

	private Result result = new Result();

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
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
