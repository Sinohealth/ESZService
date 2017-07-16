package com.sinohealth.eszservice.dao.visit.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.sinohealth.eszorm.entity.visit.VisitSubjectsVersionEntity;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.common.persistence.SimpleBaseDao;
import com.sinohealth.eszservice.dao.visit.IVisitSubjectsVersionDao;
import com.sinohealth.eszservice.dto.visit.elem.SzSubjectVersionItemElem;

@Repository("visitSubjectsVersionDao")
public class VisitSubjectsVersionDaoImpl extends SimpleBaseDao<VisitSubjectsVersionEntity, Integer>
		implements IVisitSubjectsVersionDao {

	public VisitSubjectsVersionDaoImpl() {
		super(VisitSubjectsVersionEntity.class);
	}

	@Override
	public List<VisitSubjectsVersionEntity> getByIdAndVersion(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity) {
		Parameter params = new Parameter();
		params.put("szsubjectId", visitSubjectsVersionEntity.getSzsubjectId());
		params.put("versionValue", visitSubjectsVersionEntity.getVersionValue());
		
        List<VisitSubjectsVersionEntity> list=this.findByHql(
        		"from VisitSubjectsVersionEntity where szsubjectId=:szsubjectId and versionValue=:versionValue", params);
		return list;
	}

	@Override
	public int getMaxVersion(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity) {
		StringBuffer buf = new StringBuffer("select max(versionValue) from VisitSubjectsVersionEntity where szsubjectId=:szsubjectId");
		
		Query query = getSession().createQuery(buf.toString());
		query.setParameter("szsubjectId", visitSubjectsVersionEntity.getSzsubjectId());
		Number r = (Number) query.uniqueResult();
		return r != null ? r.intValue() : 0;
	}

	@Override
	public List<SzSubjectVersionItemElem> getByVersionValue(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity,
			int beginVal) {
		String szsubject=visitSubjectsVersionEntity.getSzsubjectId();

		String hql="select d.disease_id as diseaseId,v.cat,v.item_id itemId,v.zh_name zhName,v.en_name enName,v.type,v.problem,v.options ,"+
                    "v.unit,v.sex,v.min,v.max,v.tips,v.default_val defaultVal,v.required  "+
					"from visit_disease_item_map d left join visit_items v on(d.item_id=v.item_id) where d.disease_id in( "+
					"select id from visit_base_diseases where sz_subject=?) and d.item_id in(select item_id from visit_szsubject_version_item_map "+
					" where version_id in(select id from visit_szsubject_versions where version_value > ? ) )";
		Query query = this.getSession().createSQLQuery(hql);
		query.setString(0, szsubject);
		query.setInteger(1, beginVal);
		query.setResultTransformer(Transformers.TO_LIST);
		List<List<Object>> list = query.list();
		
		ArrayList<SzSubjectVersionItemElem> elemList=new ArrayList<SzSubjectVersionItemElem>();
		for(List<Object> mapItem:list){
			SzSubjectVersionItemElem elem=new SzSubjectVersionItemElem();
			elem.setSubjectId(szsubject);
			elem.setDiseaseId((String)mapItem.get(0));
			elem.setCat((int)mapItem.get(1));
			elem.setItemId((int)mapItem.get(2));
			elem.setZhName((String)mapItem.get(3));
			elem.setEnName((String)mapItem.get(4));
			elem.setType((int)mapItem.get(5));		
			elem.setProblem((String)mapItem.get(6));			
			elem.setOptions((String)mapItem.get(7));			
			elem.setUnit((String)mapItem.get(8));			
			elem.setSex((int)mapItem.get(9));			
			elem.setMin((int)mapItem.get(10));			
			elem.setMax((int)mapItem.get(11));			
			elem.setTips((String)mapItem.get(12));			
			elem.setDefaultVal((String)mapItem.get(13));
			elem.setRequired((int)mapItem.get(14));
			elemList.add(elem);	
		}
		
		return elemList;
	}

	@Override
	public List<SzSubjectVersionItemElem> getAllBySzSubject(
			VisitSubjectsVersionEntity visitSubjectsVersionEntity) {
		String szsubject=visitSubjectsVersionEntity.getSzsubjectId();

		StringBuffer hql=new StringBuffer("");
		hql.append("select d.disease_id as diseaseId,v.cat,v.item_id itemId,v.zh_name zhName,v.en_name enName,v.type,v.problem,v.options ,");
		hql.append("v.unit,v.sex,v.min,v.max,v.tips,v.default_val defaultVal,v.required ,dis.sz_subject szSubject ");
		hql.append("from visit_disease_item_map d left join visit_items v on(d.item_id=v.item_id) left join visit_base_diseases dis on(d.disease_id=dis.id) where d.disease_id in( ");
		hql.append("select id from visit_base_diseases ");
		if("_".equals(szsubject)){
			hql.append(" )");
		}else{
			hql.append(" where sz_subject=?) ");
		}
		
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		
		if(!"_".equals(szsubject)){
		 query.setString(0, szsubject);
		}
		
		query.setResultTransformer(Transformers.TO_LIST);
		List<List<Object>> list = query.list();//第一步获取visit_items表数据
		
		ArrayList<SzSubjectVersionItemElem> elemList=switchVisitItemList(list);	
		
		//select note_id,sz_subject,disease_id,content from visit_templ_dailynotes where sz_subject=? and other_note=0
		StringBuffer dailynotes_sql=new StringBuffer("select note_id,sz_subject,disease_id,content from visit_templ_dailynotes where 1=1 ");
		if("_".equals(szsubject)){
			dailynotes_sql.append(" and other_note=0 ");
		}else{
			dailynotes_sql.append(" and sz_subject=? and other_note=0 ");
		}
			
		Query query_note = this.getSession().createSQLQuery(dailynotes_sql.toString());
		
		if(!"_".equals(szsubject)){
			query_note.setString(0, szsubject);
		}

		query_note.setResultTransformer(Transformers.TO_LIST);
		List<List<Object>> list_note = query_note.list();//第二步获取visit_templ_dailynotes表数据
		elemList=switchNoteList(list_note, elemList);
		return elemList;
	}
	
	public ArrayList<SzSubjectVersionItemElem> switchVisitItemList(List<List<Object>> list){
		ArrayList<SzSubjectVersionItemElem> elemList=new ArrayList<SzSubjectVersionItemElem>();
		for(List<Object> mapItem:list){
			SzSubjectVersionItemElem elem=new SzSubjectVersionItemElem();			
			elem.setDiseaseId((String)mapItem.get(0));
			elem.setCat((int)mapItem.get(1));
			elem.setItemId((int)mapItem.get(2));
			elem.setZhName((String)mapItem.get(3));
			elem.setEnName((String)mapItem.get(4));
			elem.setType((int)mapItem.get(5));		
			elem.setProblem((String)mapItem.get(6));			
			elem.setOptions((String)mapItem.get(7));			
			elem.setUnit((String)mapItem.get(8));			
			elem.setSex((int)mapItem.get(9));			
			elem.setMin((int)mapItem.get(10));			
			elem.setMax((int)mapItem.get(11));			
			elem.setTips((String)mapItem.get(12));			
			elem.setDefaultVal((String)mapItem.get(13));
			elem.setRequired((int)mapItem.get(14));
			elem.setSubjectId((String)mapItem.get(15));
			elemList.add(elem);	
		}
		
		return elemList;
	}
	
	public ArrayList<SzSubjectVersionItemElem> switchNoteList(List<List<Object>> noteList,ArrayList<SzSubjectVersionItemElem> elemList){
		if(elemList==null){
			elemList=new ArrayList<SzSubjectVersionItemElem>();
		}
		for(List<Object> mapItem:noteList){
			SzSubjectVersionItemElem elem=new SzSubjectVersionItemElem();
			elem.setItemId((int)mapItem.get(0));
			elem.setSubjectId((String)mapItem.get(1));
			elem.setDiseaseId((String)mapItem.get(2));
			elem.setZhName((String)mapItem.get(3));
			elem.setCat(9);
			elemList.add(elem);	
		}	
		return elemList;
	}

	
}
