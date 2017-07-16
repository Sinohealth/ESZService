package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sinohealth.eszorm.entity.visit.CheckItemValueEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dto.visit.elem.ItemValueDtoElem;

public class CheckItemValueDto extends BaseDto {

	private static final long serialVersionUID = -6063868833577466781L;

	@JsonIgnore
	private Set<CheckItemValueEntity> values = new HashSet<CheckItemValueEntity>();

	private List<ItemValueDtoElem> itemValues = new ArrayList<>();

	@JsonIgnore
	private int submitedValueCount;

	public List<ItemValueDtoElem> getItemValues() {
		if (null != values && values.size() > 0) {
			for (CheckItemValueEntity checkItem : values) {
				ItemValueDtoElem elem = new ItemValueDtoElem();
				elem.setItemId(checkItem.getVisitItem().getItemId());
				String reportDate = DateUtils.formatDate(checkItem.getReportTime());
				elem.setReportDate(reportDate);
				elem.setReportValue(checkItem.getReportValue());
				elem.setReportWarnLevel(checkItem.getReportWarnLevel());
				itemValues.add(elem);
			}
		}
		return itemValues;
	}

	public int getSubmitedValueCount() {
		return submitedValueCount;
	}

	public void setSubmitedValueCount(int submitedValueCount) {
		this.submitedValueCount = submitedValueCount;
	}

	public Set<CheckItemValueEntity> getValues() {
		return values;
	}

	public void setValues(Set<CheckItemValueEntity> values) {
		this.values = values;
	}
}
