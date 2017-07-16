package com.sinohealth.eszservice.common.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 * 基于hibernate的数据操作类。
 * 
 */
public class SimpleBaseDao<T, PK extends Serializable> implements
		IGenericDao<T, PK> {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private Class<T> persistentClass;

	@Resource
	private SessionFactory sessionFactory;

	public SimpleBaseDao(final Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * 保存对象。
	 * 
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T save(T o) {
		return (T) getSession().merge(o);
	}

	/**
	 * 删除对象。
	 * 
	 * @param o
	 */
	@Override
	public void delete(T o) {
		getSession().delete(o);
	}

	/**
	 * 修改对象。
	 * 
	 * @param o
	 */
	@Override
	public void update(T o) {
		getSession().update(o);
	}

	@Override
	public void remove(PK id) {
		T o = this.get(id);
		getSession().delete(o);
	}

	/**
	 * 根据主键获取。
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T get(PK id) {
		Session sess = getSession();
		IdentifierLoadAccess byId = sess.byId(persistentClass);
		T entity = (T) byId.load(id);

		// if (entity == null) {
		// log.warn("Uh oh, '" + this.persistentClass + "' object with id '"
		// + id + "' not found...");
		// throw new ObjectRetrievalFailureException(this.persistentClass, id);
		// }

		return entity;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getSession().createCriteria(persistentClass).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByHql(final String hql, final Parameter params) {
		Query query = prepareQuery(getSession(), hql, params);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByHql(final String hql, final Parameter params,
			int limit, int startIndex) {
		Query query = prepareQuery(getSession(), hql, params);
		query.setMaxResults(limit);
		query.setFirstResult(startIndex);
		return query.list();
	}

	@Override
	public List<T> findByHql(final String hql, final Parameter params, int limit) {
		return findByHql(hql, params, limit, 0);
	}

	/**
	 * 根据某个有唯一值的字段获取。
	 * 
	 * @param persistentClass
	 * @param propertyName
	 * @param value
	 * @return the single result or null
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findUniqueBy(String propertyName, Object value) {
		Session sess = getSession();
		Criteria criteria = sess.createCriteria(persistentClass);
		criteria.add(Restrictions.eq(propertyName, value));
		return (T) criteria.uniqueResult();
	}

	/**
	 * 获取实体
	 * 
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getByHql(String qlString, Parameter parameter) {
		Query query = createQuery(qlString, parameter);
		return (T) query.uniqueResult();
	}

	/**
	 * 删除
	 * 
	 * @param qlString
	 * @param parameter
	 * @return 影响记录数
	 */
	public int deleteByHql(String qlString, Parameter parameter) {
		Query query = createQuery(qlString, parameter);
		return query.executeUpdate();
	}

	/**
	 * 创建 QL 查询对象
	 * 
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Parameter parameter) {
		Query query = getSession().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}

	/**
	 * 设置查询参数
	 * 
	 * @param query
	 * @param parameter
	 */
	private void setParameter(Query query, Parameter parameter) {
		if (parameter != null) {
			Set<String> keySet = parameter.keySet();
			for (String string : keySet) {
				Object value = parameter.get(string);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (value instanceof Collection<?>) {
					query.setParameterList(string, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(string, (Object[]) value);
				} else {
					query.setParameter(string, value);
				}
			}
		}
	}

	/**
	 * 根据hql进行获取分页对象。
	 * 
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	@Override
	public PaginationSupport paginationByHql(final String hql,
			final int pageNo, final int pageSize, final Parameter params) {
		int startIndex = (pageNo - 1) * pageSize;
		return findPageByHql(hql, null, startIndex, pageSize, params);
	}

	@Override
	public PaginationSupport paginationByHql(final String hql,
			final String vCountHql, final int pageNo, final int pageSize,
			final Parameter params) {
		int startIndex = (pageNo - 1) * pageSize;
		return findPageByHql(hql, vCountHql, startIndex, pageSize, params);
	}

	@Override
	public PaginationSupport findPageByHql(final String hql,
			final int startIndex, final int pageSize, final Parameter params) {
		return findPageByHql(hql, null, startIndex, pageSize, params);
	}

	/**
	 * 注意这里参数的意义，startIndex，是起始记录序号而不是页码。 注意代码里的countHql的处理，对于有些情况可能会不适用。
	 * 
	 * @param hql
	 * @param vCountHql
	 * @param startIndex
	 * @param pageSize
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PaginationSupport findPageByHql(final String hql,
			final String vCountHql, final int startIndex, final int pageSize,
			Parameter params) {
		Integer totalCount = 0;
		String countHql = vCountHql;
		if (countHql == null) {
			countHql = getCountHql(hql);
		}
		Number r = (Number) prepareQuery(getSession(), countHql, params)
				.uniqueResult();
		if (r != null)
			totalCount = r.intValue();
		if (totalCount == 0) {
			return new PaginationSupport(new ArrayList<T>(), 0, startIndex,
					pageSize);
		}
		Query query = prepareQuery(getSession(), hql, params);
		if (startIndex > 0) {
			query.setFirstResult(startIndex);
		}
		if (pageSize > 0) {
			query.setMaxResults(pageSize);
		}
		List<T> items = query.list();
		PaginationSupport ps = new PaginationSupport(items, totalCount,
				startIndex, pageSize);
		return ps;
	}

	@Override
	public Query prepareQuery(Session session, String hql, Parameter params) {
		Query query = session.createQuery(hql);

		if (null != params) {
			for (String s : params.keySet()) {
				query.setParameter(s, params.get(s));
			}
		}

		return query;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByPagesize(String hql, Integer pageSize, Parameter params) {
		Query query = prepareQuery(getSession(), hql, params);
		if (pageSize > 0) {
			query.setMaxResults(pageSize);
		}
		List<T> items = query.list();
		return items;
	}

	private String getCountHql(String hql) {
		int fromIndex = hql.toLowerCase().indexOf("from");
		String countHql = hql.substring(fromIndex);
		int orderByIndex = countHql.toLowerCase().indexOf("order by");
		if (orderByIndex != -1) {
			countHql = countHql.substring(0, orderByIndex);
		}
		return "select count(*) " + countHql;
	}

	/**
	 * QBC方式，并且分页。
	 * 
	 * @param detachedCriteria
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public Criteria createCriteria(Criterion[] criterions) {
		Criteria criteria = getSession().createCriteria(this.persistentClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	@Override
	public PaginationSupport findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int startIndex) {
		return findPageByCriteria(detachedCriteria, startIndex,
				PaginationSupport.PAGESIZE);
	}

	@Override
	public PaginationSupport findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int startIndex,
			final int pageSize) {
		return findPageByCriteria(detachedCriteria, startIndex, pageSize, null);
	}

	/**
	 * 注意这里参数的意义，startIndex，是起始记录序号而不是页码。
	 * 另外需要注意的是对于非lazy方式关联的对象使用Criteria执行的结果的内容。
	 * 
	 * @param detachedCriteria
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PaginationSupport findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int startIndex,
			final int pageSize, final ResultTransformer resultTransformer) {
		int start = startIndex;

		// DetachedCriteria query = DetachedCriteria.forClass(persistentClass);

		CriteriaImpl criteria = (CriteriaImpl) detachedCriteria
				.getExecutableCriteria(getSession());
		// http://jdkcn.com/entry/16.html
		List orderEntries = null;
		List newEntrys = new ArrayList();
		Field field = null;
		try {
			field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			// Get orders
			orderEntries = (List) field.get(criteria);
			// Remove orders
			field.set(criteria, newEntrys);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Integer totalCount = ((Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult());
		try {
			// Add orders return
			if (orderEntries != null)
				newEntrys.addAll(orderEntries);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (totalCount == null)
			totalCount = 0;
		criteria.setProjection(null);
		if (start > totalCount && totalCount > 0) {
			start = (totalCount - 1) / pageSize * pageSize;
		}
		if (resultTransformer != null)
			criteria.setResultTransformer(resultTransformer);
		List<T> items = criteria.setFirstResult(start).setMaxResults(pageSize)
				.list();
		PaginationSupport ps = new PaginationSupport(items, totalCount, start,
				pageSize);
		return ps;
	}

	@Override
	public Session getSession() throws HibernateException {
		Session sess = null;
		try {
			sess = getSessionFactory().getCurrentSession();
			return sess;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return sess;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public int updateByHql(String hql, Parameter parameter) {
		Query query = createQuery(hql, parameter);
		return query.executeUpdate();
	}

	@Override
	public void flush() {
		getSession().flush();
	}

}
