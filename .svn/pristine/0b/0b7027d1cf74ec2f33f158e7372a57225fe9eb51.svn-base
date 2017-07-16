package com.sinohealth.eszservice.vo.visit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 医生定制模板/计划的内容值
 * 
 * @author lianhuang
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplContentVo implements Serializable {

	private static final long serialVersionUID = -5039089366601698346L;

	private List<BodySignVo> bodySigns;

	private DailynoteVo dailynote;

	private int cycleUnit;

	private VisitPointsVo visitPoints;
	
	/**
	 * 抗凝指标
	 */
	private List<BodySignVo> anticoag;

	public List<BodySignVo> getAnticoag() {
		if (null == anticoag) {
			anticoag = new ArrayList<>();
		}
		return anticoag;
	}

	public void setAnticoag(List<BodySignVo> anticoag) {
		this.anticoag = anticoag;
	}

	public List<BodySignVo> getBodySigns() {
		if (null == bodySigns) {
			bodySigns = new ArrayList<>();
		}
		return bodySigns;
	}

	public void setBodySigns(List<BodySignVo> bodySigns) {
		this.bodySigns = bodySigns;
	}

	public DailynoteVo getDailynote() {
		if (null == dailynote) {
			dailynote = new DailynoteVo();
		}
		return dailynote;
	}

	public void setDailynote(DailynoteVo dailynote) {
		this.dailynote = dailynote;
	}

	public int getCycleUnit() {
		return cycleUnit;
	}

	public void setCycleUnit(int cycleUnit) {
		this.cycleUnit = cycleUnit;
	}

	public VisitPointsVo getVisitPoints() {
		if (null == visitPoints) {
			visitPoints = new VisitPointsVo();
		}
		return visitPoints;
	}

	public void setVisitPoints(VisitPointsVo visitPoints) {
		this.visitPoints = visitPoints;
	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class BodySignVo implements Serializable {

		private static final long serialVersionUID = -948886096781675529L;

		private int freq;
		private int itemId;

		public int getFreq() {
			return freq;
		}

		public void setFreq(int freq) {
			this.freq = freq;
		}

		public int getItemId() {
			return itemId;
		}

		public void setItemId(int itemId) {
			this.itemId = itemId;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class DailynoteVo implements Serializable {
		private static final long serialVersionUID = 6839468640310326860L;
		private List<Integer> noteIds;
		private String otherNote;

		public List<Integer> getNoteIds() {
			if (null == noteIds) {
				noteIds = new ArrayList<Integer>();
			}
			return noteIds;
		}

		public void setNoteIds(List<Integer> noteIds) {
			this.noteIds = noteIds;
		}

		public String getOtherNote() {
			return otherNote;
		}

		public void setOtherNote(String otherNote) {
			this.otherNote = otherNote;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class VisitPointsVo implements Serializable {
		private static final long serialVersionUID = -5950289391103934294L;
		private int cycleUnit;
		private List<PhaseVo> phases;

		public int getCycleUnit() {
			return cycleUnit;
		}

		public void setCycleUnit(int cycleUnit) {
			this.cycleUnit = cycleUnit;
		}

		public List<PhaseVo> getPhases() {
			if (null == phases) {
				phases = new ArrayList<PhaseVo>();
			}
			return phases;
		}

		public void setPhases(List<PhaseVo> phases) {
			this.phases = phases;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class PhaseVo implements Serializable {
		private static final long serialVersionUID = 7293259453724802526L;
		private Date visitTime;
		private List<Integer> itemIds;
		private int isFuzhenItem;
		private int phaseId;
		private int selected=1;
		private int timePoint;
		private int editable;

		@JsonFormat(pattern = "yyyy-MM-dd")
		public Date getVisitTime() {
			return visitTime;
		}

		public void setVisitTime(Date visitTime) {
			this.visitTime = visitTime;
		}

		public List<Integer> getItemIds() {
			if (null == itemIds) {
				itemIds = new ArrayList<Integer>();
			}
			return itemIds;
		}

		public void setItemIds(List<Integer> itemIds) {
			this.itemIds = itemIds;
		}

		public int getIsFuzhenItem() {
			return isFuzhenItem;
		}

		public void setIsFuzhenItem(int isFuzhenItem) {
			this.isFuzhenItem = isFuzhenItem;
		}

		public int getPhaseId() {
			return phaseId;
		}

		public void setPhaseId(int phaseId) {
			this.phaseId = phaseId;
		}

		public int getSelected() {
			return selected;
		}

		public void setSelected(int selected) {
			this.selected = selected;
		}

		public int getTimePoint() {
			return timePoint;
		}

		public void setTimePoint(int timePoint) {
			this.timePoint = timePoint;
		}

		public int getEditable() {
			return editable;
		}

		public void setEditable(int editable) {
			this.editable = editable;
		}

		@Override
		public int hashCode() {
			final int prime = 131;
			int result = 1;
			result = prime * result + timePoint;
			return result;
		}

		/**
		 * timePoint相同，就表示两个对象相等
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof PhaseVo))
				return false;
			PhaseVo other = (PhaseVo) obj;
			if (timePoint != other.timePoint)
				return false;
			return true;
		}

	}

}
