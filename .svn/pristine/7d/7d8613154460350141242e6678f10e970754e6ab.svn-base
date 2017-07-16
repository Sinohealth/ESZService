package com.sinohealth.eszservice.common.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.ResultTransformer;

public interface IGenericDao<T, PK> {

	T save(T o);

	T get(PK id);

	T getByHql(String hql, Parameter params);

	void delete(T o);

	void update(T o);

	void remove(PK id);

	int deleteByHql(String hql, Parameter parameter);

	int updateByHql(String hql, Parameter parameter);

	List<T> getAll();

	T findUniqueBy(String propertyName, Object value);

	PaginationSupport findPageByHql(String hql, String vCountHql,
			int startIndex, int pageSize, Parameter params);

	PaginationSupport findPageByHql(String hql, int startIndex, int pageSize,
			Parameter params);

	PaginationSupport paginationByHql(String hql, String vCountHql, int pageNo,
			int pageSize, Parameter params);

	PaginationSupport paginationByHql(String hql, int pageNo, int pageSize,
			Parameter params);

	Criteria createCriteria(Criterion[] criterions);

	PaginationSupport findPageByCriteria(DetachedCriteria detachedCriteria,
			int startIndex);

	PaginationSupport findPageByCriteria(DetachedCriteria detachedCriteria,
			int startIndex, int pageSize);

	PaginationSupport findPageByCriteria(DetachedCriteria detachedCriteria,
			int startIndex, int pageSize, ResultTransformer resultTransformer);

	Session getSession() throws HibernateException;

	List<T> findByHql(String hql, Parameter params);

	List<T> findByHql(String hql, Parameter params, int limit, int startIndex);

	List<T> findByHql(String hql, Parameter params, int limit);

	Query prepareQuery(Session session, String hql, Parameter params);

	void flush();
}
