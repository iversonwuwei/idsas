package com.zdtx.ifms.common.dao;

import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.KeyAndValues;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据提取类- 新架构直接从Solr数据中心取得*/

/**
 * 通用类
 * 基础DAO
 * @author Leon Liu
 * @since 2013-2-25 9:18:12
 */
@Repository
public class BaseDao extends HibernateDao {

	private static final long serialVersionUID = -6315619117127424199L;

	/**
	 *
	 * @param <E>
	 * @param entity
	 */
	public <E> void save(final E entity) {
		Session session = super.getSession();
		session.saveOrUpdate(entity);
		session.flush();
	}

	/**
	 *
	 * @param <E>
	 * @param clazz
	 * @param id
	 */
	public <E> void delete(final Class<E> clazz, final Long id) {
		Session session = super.getSession();
		session.delete(session.get(clazz, id));
	}

	/**
	 *
	 * @param <E>
	 * @param entity
	 */
	public <E> void delete(final E entity) {
		Session session = super.getSession();
		session.delete(entity);
	}

	/**
	 *
	 * @param <E>
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E get(final Class<E> clazz, final Long id) {
		Session session = super.getSession();
		return (E) session.get(clazz, id);
	}

	/**
	 *
	 * @param <E>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> getAll(final Class<E> clazz) {
		Session session = super.getSession();
		String hql = "SELECT o FROM " + clazz.getName() + " o";
		Query query = session.createQuery(hql);
		return query.list();
	}

	/**
	 *
	 * @param <E>
	 * @param clazz
	 * @param orderColumn
	 *            column name
	 * @param order
	 *            ASC or DESC
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> getAll(final Class<E> clazz, String orderColumn, String order) {
		Session session = super.getSession();
		String hql = "SELECT o FROM " + clazz.getName() + " o ORDER BY o." + orderColumn + order;
		Query query = session.createQuery(hql);
		return query.list();
	}

	/**
	 *
	 * @param <E>
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> execute(final String hql) {
		Session session = super.getSession();
		Query query = session.createQuery(hql);
		return query.list();
	}

	/**
	 *
	 * @param hql
	 */
	public int executeUpdate(final String hql) {
		Session session = super.getSession();
		return session.createQuery(hql).executeUpdate();
	}

	/**
	 * 根据SQL获取KeyAndValue(用于下拉菜单)
	 *
	 * @param sql	查询语句
	 * @return	List<KeyAndValue>
	 */
	@SuppressWarnings("unchecked")
	public List<KeyAndValue> getKeyAndValueBySQL(String sql) {
		if (null == sql || "".equals(sql)) {
			return null;
		}
		List<KeyAndValue> list = new ArrayList<KeyAndValue>();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("key", StringType.INSTANCE);
		query.addScalar("value", StringType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(KeyAndValue.class));
//		query.setCacheable(true);
		list = (List<KeyAndValue>) query.list();
		return list;
	}
	/**
	 * 根据SQL获取KeyAndValues(用于下拉菜单)
	 *
	 * @param sql	查询语句
	 * @return	List<KeyAndValue>
	 */
	@SuppressWarnings("unchecked")
	public List<KeyAndValues> getKeyAndValuesBySQL(String sql,int i) {
		if (null == sql || "".equals(sql)) {
			return null;
		}
		List<KeyAndValues> list = new ArrayList<KeyAndValues>();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("key", StringType.INSTANCE);
		query.addScalar("value", StringType.INSTANCE);
		query.addScalar("value1", StringType.INSTANCE);
		if(i==2){
			query.addScalar("value2", StringType.INSTANCE);
		}
		if(i==3){
			query.addScalar("value2", StringType.INSTANCE);
			query.addScalar("value3", StringType.INSTANCE);
		}
		query.setResultTransformer(Transformers.aliasToBean(KeyAndValues.class));
		query.setCacheable(true);
		list = (List<KeyAndValues>) query.list();
		return list;
	}
	/**
	 * 根据SQL获取as Value(用于下拉菜单)
	 *
	 * @param sql	查询语句
	 * @return	List<KeyAndValue>
	 */
	@SuppressWarnings("unchecked")
	public List<String> getStringBySQL(String sql) {
		if (null == sql || "".equals(sql)) {
			return null;
		}
		SQLQuery query = this.getSession().createSQLQuery(sql);
		return query.list();
	}
}