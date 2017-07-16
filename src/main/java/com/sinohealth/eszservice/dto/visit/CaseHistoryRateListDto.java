package com.sinohealth.eszservice.dto.visit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sinohealth.eszservice.dto.visit.elem.CaseHistoryRateElem;

/**
 * 病历完整性列表
 * 
 * @author 黄世莲
 * 
 */
public class CaseHistoryRateListDto implements Serializable {

	private static final long serialVersionUID = 8910324614571221073L;

	private int passed;

	private int total;

	private List<CaseHistoryRateElem> items;

	public void addItem(String description, int itemCount, int submitedCount) {
		items.add(new CaseHistoryRateElem(description, itemCount, submitedCount));
	}

	public CaseHistoryRateListDto() {
		super();
		items = new ArrayList<>();
	}

	public List<CaseHistoryRateElem> getItems() {
		return items;
	}

	public void setItems(List<CaseHistoryRateElem> items) {
		this.items = items;
	}

	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
