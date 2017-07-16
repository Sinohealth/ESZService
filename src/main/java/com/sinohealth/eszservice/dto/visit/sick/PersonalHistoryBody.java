package com.sinohealth.eszservice.dto.visit.sick;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class PersonalHistoryBody implements Serializable {

	private static final long serialVersionUID = -6948044377054343139L;

	private Set<PersonalHis> personalHis = new HashSet<>();

	public Set<PersonalHis> getPersonalHis() {
		return personalHis;
	}

	public void setPersonalHis(Set<PersonalHis> personalHis) {
		this.personalHis = personalHis;
	}
	
}
