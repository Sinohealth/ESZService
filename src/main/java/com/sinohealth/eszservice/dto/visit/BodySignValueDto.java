package com.sinohealth.eszservice.dto.visit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.dto.visit.elem.ItemValueDtoElem;

public class BodySignValueDto extends BaseDto {

	private static final long serialVersionUID = -6063868833577466781L;

	@JsonIgnore
	private List<BodySignValueEntity> values = new ArrayList<>();

	private List<ItemValueDtoElem> itemValues = new ArrayList<>();

	public List<BodySignValueEntity> getValues() {
		return values;
	}

	public void setValues(List<BodySignValueEntity> values) {
		this.values = values;
	}

	public List<ItemValueDtoElem> getItemValues() {
		if (null != values && values.size() > 0) {
			for (BodySignValueEntity bodySign : values) {
				ItemValueDtoElem elem = new ItemValueDtoElem();
				elem.setItemId(bodySign.getItem().getItemId());
				String reportDate = DateUtils.formatDate(bodySign
						.getReportDate());
				elem.setReportDate(reportDate);
				elem.setReportValue(bodySign.getReportValue());  
				elem.setReportWarnLevel(bodySign.getReportWarnLevel());
				itemValues.add(elem);
			}
		}
		return itemValues;
	}

	public void setItemValues(List<ItemValueDtoElem> itemValues) {
		this.itemValues = itemValues;
	}

}
