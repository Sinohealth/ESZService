package com.sinohealth.eszservice.service.visit.report.chart;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.service.visit.report.chart.InspectionChart.Data;
import com.sinohealth.eszservice.service.visit.report.chart.InspectionChart.Option;

public class InspectionChart implements Chart<Option, Data> {

	protected Option option;

	protected Data data;

	@Override
	public String getType() {
		return "inspetion";
	}

	@Override
	public String getZhTitle() {
		return "检验";
	}

	@Override
	public String getEhTitle() {
		return "";
	}

	@Override
	public Option getOption() {
		if (null == option) {
			option = new Option();
		}
		return option;
	}

	@Override
	public Data getData() {
		if (null == data) {
			data = new Data();
		}
		return data;
	}

	public static class Option {
		private final String head = "项目名称";
		private String[] list;

		public String getHead() {
			return head;
		}

		public String[] getList() {
			return list;
		}

		public void setList(String[] list) {
			this.list = list;
		}
	}

	public static class Data {
		@JsonIgnoreProperties(value = { "type", "problem", "options", "op1",
				"sex1", "minValue1", "maxValue1", "warnLevel1", "op2", "sex2",
				"minValue2", "maxValue2", "warnLevel2", "op3", "sex3",
				"minValue3", "maxValue3", "warnLevel3", "op4", "sex4",
				"minValue4", "maxValue4", "warnLevel4", "op5", "sex5",
				"minValue5", "maxValue5", "warnLevel5", "phases",
				"szsubjectsVersion", "diseaseEntitys", "max", "min", "tips",
				"defaultVal", "required", "cat", "sex" })
		private Set<VisitItemEntity> headList;
		private Object[][][] valueList;

		public Set<VisitItemEntity> getHeadList() {
			return headList;
		}

		public void setHeadList(Set<VisitItemEntity> headList) {
			this.headList = headList;
		}

		public Object[][][] getValueList() {
			return valueList;
		}

		public void setValueList(Object[][][] valueList) {
			this.valueList = valueList;
		}

	}
}