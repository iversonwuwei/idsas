/*
 * @(#)Page.java  Thursday, November 25th, 2010
 * Copyright(c) 2010, Wiflg Goth. All rights reserved.
 */
package com.zdtx.ifms.common.utils;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * pageTotal pageSize First Previous10 Previous 1 2 3 4 5 6 7 8 9 Next Next10 Last
 * </pre>
 * 
 * @author Wiflg Goth
 * @since Thursday, November 25th, 2010
 */
public final class Page<E> {

	public static final String ORDER_DEFAULT = "DEFAULT";
	public static final String ORDER_ASC = "ASC";
	public static final String ORDER_DESC = "DESC";
//	public static final String ORDER_PINYIN = "PINYIN";
//	public static final String ORDER_RADICAL = "RADICAL";
//	public static final String ORDER_STROKE = "STROKE";

	/**
	 * The result order: DEFAULT, ASC, DESC 三种排序方式，点表头排序时用
	 */
	private static final String[] order = { ORDER_DEFAULT, ORDER_ASC, ORDER_DESC };
	/**
	 * The chinese result order: PINYIN, RADICAL, STROKE
	 */
//	private static final String[] order_ch = { ORDER_PINYIN, ORDER_RADICAL, ORDER_STROKE };
	/**
	 * 	每页显示数量的 数据集
	 */
	private static final int[] resultPerPage = {5, 25, 50, 100, 200 };
	/**
	 *  用于 操作 上十页 下十页
	 */
	private static final int BATCH_SIZE = 10;
	/**
	 * current page  当前页
	 */
	private int currentPage = 1;
	/**
	 * page size   每页显示数量
	 */
	private int pageSize = 25;
	/**
	 * total items count  总计数据条数，没用HibernateDao中getBatch()方法求page时   需要设置此变量
	 */
	private int totalCount = 0;
	/**
	 * the results in List 数据集合
	 */
	private List<E> result = Collections.emptyList();
	/**
	 * JPA entity name page中放的实体类的名字
	 */
	private String entityName;
	/**
	 * show links count    page中具体页的链接个数
	 */
	private int linkSize = 5;
	/**
	 * this current sort.  排序方式数组中位置数
	 */
	private int sortNum = 0;
	/**
	 * sort column  排序字段
	 */
	private String sortColumn;
	/**
	 * the sort is chinese or not.
	 */
	private boolean sortChinese;
	/**
	 * the sort by pk desc or not.是否根据主键排序
	 */
	private boolean pkDesc;
	/**
	 * The entity pk's name.
	 */
	private String pkName = "id";

	public Page(Class<E> clazz) {
		this.entityName = clazz.getName();
	}

	public Page(Class<E> clazz, int pageSize) {
		this.entityName = clazz.getName();
		this.pageSize = pageSize;
	}

	public Page(final int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public String getEntityName() {
		return entityName;
	}

	/**
	 * 当前页第一条数据的位数
	 * @return
	 */
	public int getFirst() {

		if (2 <= this.currentPage && this.getTotalCount() == (this.currentPage - 1) * this.pageSize) {
			this.currentPage--;
		}
		return (this.currentPage - 1) * this.pageSize;
	}

	/**
	 * generate links with {@link #setLinkSize(int)}
	 * 
	 * @return
	 */
	public int[] getLinks() {

		int links = linkSize / 2;

		int beginLink = currentPage - links;
		int endLink = currentPage + links;

		if (beginLink < 1) {
			beginLink = 1;
			endLink = linkSize;
		}

		if (endLink > this.getTotalPage()) {
			endLink = this.getTotalPage();
			beginLink = this.getTotalPage() - linkSize + 1;
			if (beginLink < 1) {
				beginLink = 1;
			}
		}

		int[] arr = new int[endLink - beginLink + 1];
		for (int i = beginLink; i <= endLink; i++) {
			arr[i - beginLink] = i;
		}
		return arr;
	}
	public String getNewLinks() {
		
		int links = linkSize / 2;
		
		int beginLink = currentPage - links;
		int endLink = currentPage + links;
		
		if (beginLink < 1) {
			beginLink = 1;
			endLink = linkSize;
		}
		
		if (endLink > this.getTotalPage()) {
			endLink = this.getTotalPage();
			beginLink = this.getTotalPage() - linkSize + 1;
			if (beginLink < 1) {
				beginLink = 1;
			}
		}
		
		int[] arr = new int[endLink - beginLink + 1];
		for (int i = beginLink; i <= endLink; i++) {
			arr[i - beginLink] = i;
		}
		String s="";
		for (int i = 0; i < arr.length; i++) {
			s+=arr[i]+":";
		}
		return s.substring(0,s.length()-1);
	}

	/**
	 * 
	 * @return
	 */
	public int getPrevious() {

		if (currentPage - 1 <= 1) {
			return 1;
		}
		return currentPage - 1;
	}

	/**
	 * 
	 * @return
	 */
	public int getNext() {

		if (currentPage + 1 >= this.getTotalPage()) {
			return this.getTotalPage();
		}
		return currentPage + 1;
	}

	/**
	 * 
	 * @return
	 */
	public int getPreBatch() {

		if (currentPage - BATCH_SIZE <= 1) {
			return 1;
		}
		return currentPage - BATCH_SIZE;
	}

	/**
	 * 
	 * @return
	 */
	public int getNextBatch() {

		if (currentPage + BATCH_SIZE >= this.getTotalPage()) {
			return this.getTotalPage();
		}
		return currentPage + BATCH_SIZE;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<E> getResult() {
		return result;
	}

	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 
	 * @return
	 */
	public int getTotalPage() {
		return ((totalCount - 1) / pageSize) + 1;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasPrevious() {

		if (currentPage <= 1) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {

		if (currentPage >= this.getTotalPage()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasPreBatch() {

		if (currentPage - BATCH_SIZE <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasNextBatch() {

		if (currentPage + BATCH_SIZE > this.getTotalPage()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param currentPage
	 */
	public void setCurrentPage(final int currentPage) {

		this.currentPage = currentPage;
		if (currentPage < 1) {
			this.currentPage = 1;
		}
	}

	public void setEntityName(final String entityName) {
		this.entityName = entityName;
	}

	/**
	 * set links count
	 * 
	 * @param linkSize
	 */
	public void setLinkSize(final int linkSize) {
		this.linkSize = linkSize;
	}

	public void setResult(final List<E> result) {
		this.result = result;
	}

	public void setTotalCount(final int totalCount) {
		this.totalCount = totalCount;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(final int sortNum) {
		this.sortNum = sortNum;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(final String sortColumn) {
		this.sortColumn = sortColumn;
	}

	/**
	 * 
	 * @return
	 */
	public String getSortOrder() {
		return order[this.getSortNum() % 3];
	}
	
	/**
	 * 
	 * @return
	 */
//	public String getChSortOrder() {
//		return order_ch[this.getSortNum() % 3];
//	}

	/**
	 * 
	 * @return
	 */
	public int[] getResultperpage() {
		return resultPerPage;
	}
	public String getNewResultperpage() {
		return "5:25:50:100:200";
	}

	public boolean isPkDesc() {
		return pkDesc;
	}

	public void setSortChinese(boolean sortChinese) {
		this.sortChinese = sortChinese;
	}
	
	public boolean isSortChinese() {
		return sortChinese;
	}
	
	public void setPkDesc(boolean pkDesc) {
		this.pkDesc = pkDesc;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	@Override
	public String toString() {
//		return "Page [currentPage=" + currentPage + ", entityName="
//				+ entityName + ", linkSize=" + linkSize + ", pageSize="
//				+ pageSize + ", pkDesc=" + pkDesc + ", pkName=" + pkName
//				+ ", result=" + result + ", sortColumn=" + sortColumn + ", sortChinese=" + sortChinese
//				+ ", sortNum=" + sortNum + ", totalCount=" + totalCount + "]";
		return currentPage+","+sortColumn+","+sortNum+","+sortChinese+","+getTotalPage()+","+totalCount
				+","+getNewResultperpage()+","+hasPrevious()+","+hasPreBatch()+","+getPreBatch()
				+","+getPrevious()+","+hasNext()+","+hasNextBatch()+","+getNext()+","+getNextBatch()+","+getNewLinks()+","+pageSize;
	}

}