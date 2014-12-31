/**
 * @Title: OrderByPinyin.java
 * @Package package com.zdtx.utils;
 * @Description: 扩展order 支持oracle 按笔划，部首，拼音排序
 * @author Liu Jun  
 * @Date 2011-11-21
 * @Version V1.0
 * @Company: dlzd
 * @Copyright 2011
 */
package com.zdtx.ifms.common.utils;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * 注掉部分为多种中文方式
 * @Title: OrderByPinyin
 * @Author Liu Jun
 * @Time 2011-11-21 下午03:11:47
 */
public class OrderByChinese extends Order implements Serializable {

	private static final long serialVersionUID = -6902133146217767062L;

//	private static int pinyin = 0; // 'SCHINESE_PINYIN_M'
//	private static int radical = 1; // SCHINESE_RADICAL_M
//	private static int stroke = 2; // SCHINESE_STROKE_M
	private String propertyName;	//排序字段名
//	private int ascending = 0;	//排序方式选择器
	private boolean ascending;	//true:asc; false:desc
	private boolean nulls; // 如果排序字段为空的话，空排最前。否则最后 //true: first; false: last

	/**
	 * Constructor for OrderByPinyin.
	 */
	protected OrderByChinese(String propertyName, boolean ascending, boolean nulls) {
		super(propertyName, ascending);
		this.propertyName = propertyName;
		this.ascending = ascending;
		this.nulls = nulls;
	}

	/**
	 * 只对准排序字段只有一个的情况，多个未考虑。
	 */
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
		StringBuffer fragment = new StringBuffer();
		fragment.append(" nlssort(");
		fragment.append( columns[0] );
		fragment.append(",'NLS_SORT=SCHINESE_PINYIN_M')");
		fragment.append( this.ascending ? " asc" : " desc" );
		fragment.append(" nulls");
		fragment.append( this.nulls ? " first" : " last" );
//		if (this.ascending == 0) {
//			fragment = " nlssort(" + columns[0]
//					+ ",'NLS_SORT=SCHINESE_PINYIN_M') nulls "
//					+ (this.nulls == 0 ? "first" : "last");
//		} else if (this.ascending == 1) {
//			fragment = " nlssort(" + columns[0]
//					+ ",'NLS_SORT=SCHINESE_RADICAL_M') nulls "
//					+ (this.nulls == 0 ? "first" : "last");
//		} else {
//			fragment = " nlssort(" + columns[0]
//					+ ",'NLS_SORT=SCHINESE_STROKE_M') nulls "
//					+ (this.nulls == 0 ? "first" : "last");
//		}
		return fragment.toString();
	}

	/**
	 * 按拼音正排序
	 * @param propertyName	排序字段名
	 * @param nulls	null值处理	true:最前面;	false:最后面
	 * @return	OrderByChinese
	 */
	public static OrderByChinese asc(String propertyName, boolean nulls) {
		return new OrderByChinese(propertyName, true, nulls);
	}

	/**
	 * 按拼音倒排序
	 * @param propertyName	排序字段名
	 * @param nulls	null值处理	true:最前面;	false:最后面
	 * @return	OrderByChinese
	 */
	public static OrderByChinese desc(String propertyName, boolean nulls) {
		return new OrderByChinese(propertyName, false, nulls);
	}
	
	/**
	 * 按部首排序
	 * @param propertyName	排序字段名
	 * @param nulls	null值处理	0:最前面;1:最后面
	 * @return	OrderByChinese
	 */
//	public static OrderByChinese byRadical(String propertyName, int nulls) {
//		return new OrderByChinese(propertyName, OrderByChinese.radical, nulls);
//	}

	/**
	 * 按比划排序
	 * @param propertyName	排序字段名
	 * @param nulls	null值处理	0:最前面;1:最后面
	 * @return	OrderByChinese
	 */
//	public static OrderByChinese byStroke(String propertyName, int nulls) {
//		return new OrderByChinese(propertyName, OrderByChinese.stroke, nulls);
//	}
}