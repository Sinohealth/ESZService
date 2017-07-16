package com.sinohealth.eszservice.dao.visit.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.TemplatePhaseEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IPhaseDao;

@Repository("phaseDao")
public class PhaseDaoImpl extends SimpleBaseDao<TemplatePhaseEntity, Integer>
		implements IPhaseDao {

	public PhaseDaoImpl() {
		super(TemplatePhaseEntity.class);
	}

	@Override
	public int getValuesCount(int phaseId) {
		StringBuffer buf = new StringBuffer(
				"SELECT COUNT(itemValues.*) FROM TemplatePhaseEntity  WHERE phaseId=:phaseId ");

		Parameter params = new Parameter();
		params.put("phaseId", phaseId);

		Query query = getSession().createQuery(buf.toString());
		if (null != params) {
			for (String s : params.keySet()) {
				query.setParameter(s, params.get(s));
			}
		}
		Number r = (Number) query.uniqueResult();

		return r != null ? r.intValue() : 0;
	}

}
