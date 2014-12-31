package com.zdtx.ifms.common.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zdtx.ifms.common.utils.OrderByChinese;
import com.zdtx.ifms.common.utils.Page;

/**
 * 通用类
 * hibernate 帮助类
 * @author Leon Liu
 * @since 2013-2-25 9:19:08
 */
@Repository
public class HibernateDao implements Serializable {

	private static final long serialVersionUID = -5368700292924817656L;

	protected final Logger log = Logger.getLogger(getClass());

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 *
	 * @param <E>
	 * @param page
	 * @param criteria
	 * @param orders
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Page<E> getBatch(Page<E> page, Criteria criteria, final List<Order> orders) {
		CriteriaImpl impl = (CriteriaImpl) criteria;
		Projection projection = impl.getProjection();
//		criteria.setCacheable(true);
		page.setTotalCount(((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
		criteria.setProjection(projection);
		criteria.setFirstResult(page.getFirst());
		criteria.setMaxResults(page.getPageSize());
		criteria = this.disposeSortCriteria(criteria, orders, page);
		page.setResult(criteria.list());
		return page;
	}

	/**
	 *
	 * @param <E>
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> getAll(final Criteria criteria) {
		return criteria.list();
	}


	/**
	 * 增加排序
	 * @param criteria
	 * @param orders
	 * @param page
	 * @return
	 */
	public Criteria disposeSortCriteria(Criteria criteria, final List<Order> orders, Page<?> page) {
		if(!page.isSortChinese()) {	//非中文查�?
			if (null != page.getSortColumn() && !Page.ORDER_DEFAULT.equals(page.getSortOrder())) {
				if (page.getSortOrder().equals(Page.ORDER_ASC)) {
					criteria.addOrder(Order.asc(page.getSortColumn()));
				} else if (page.getSortOrder().equals(Page.ORDER_DESC)) {
					criteria.addOrder(Order.desc(page.getSortColumn()));
				}
				if (page.isPkDesc()) {
					criteria.addOrder(Order.desc(page.getPkName()));
				}
			} else {
				if (null != orders) {
					for (Order order : orders) {
						criteria.addOrder(order);
					}
				}
			}
		} else {	//中文查询
			if (null != page.getSortColumn() && !Page.ORDER_DEFAULT.equals(page.getSortOrder())) {
				if (page.getSortOrder().equals(Page.ORDER_ASC)) {
					criteria.addOrder(OrderByChinese.asc(page.getSortColumn(), true));
				} else if (page.getSortOrder().equals(Page.ORDER_DESC)) {
					criteria.addOrder(OrderByChinese.desc(page.getSortColumn(), true));
				}
				if (page.isPkDesc()) {
					criteria.addOrder(Order.desc(page.getPkName()));
				}
			} else {
				if (null != orders) {
					for (Order order : orders) {
						criteria.addOrder(order);
					}
				}
			}
		}
		return criteria;
	}

	/**
	 *
	 * @return
	 */
	public Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 *
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}