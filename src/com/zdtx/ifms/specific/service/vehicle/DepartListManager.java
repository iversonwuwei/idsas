package com.zdtx.ifms.specific.service.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.vo.vehicle.DepartListVO;

/**
 * @ClassName: DepartListManager
 * @Description: Vehicle-Department List-Manager
 * @author Leon Liu
 * @date 2013-4-27 10:12:21
 * @version V1.0
 */
@Service
@Transactional
public class DepartListManager {

	@Autowired
	private BaseDao dao;

	/**
	 * get grid data
	 * 
	 * @param page
	 * @param vo
	 *            query criteria
	 * @return Page<Org>
	 */
	public Page<Org> getBatch(Page<Org> page, DepartListVO vo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Org.class);
		List<Order> orderList = new ArrayList<Order>();
		try {
			criteria.add(Restrictions.in("orgID", (Long[]) Struts2Util.getSession().getAttribute("userDepartment"))); // add
			criteria.add(Restrictions.eq("isDelete", "F"));
			criteria.add(Restrictions.eq("flag", 2));
			criteria.add(Restrictions.eq("inLevel", 2L));
			if (!Utils.isEmpty(vo.getDepartID()) && -1 != vo.getDepartID()) { // by
				criteria.add(Restrictions.eq("orgID", vo.getDepartID()));
			}
			if (!Utils.isEmpty(vo.getDepartName())) {
				criteria.add(Restrictions.like("parentName", "%" + vo.getDepartName() + "%").ignoreCase());
			}
			orderList.add(Order.asc("orgName")); // add default sort
		} catch (Exception e) {
			e.printStackTrace();
		}
		Page<Org> pageResult = dao.getBatch(page, criteria.getExecutableCriteria(dao.getSession()), orderList);
		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;
	}

	/**
	 * get grid data
	 * 
	 * @param page
	 * @param vo
	 *            query criteria
	 * @return Page<Org>
	 */
	public Page<Org> getoBatch(Page<Org> page, DepartListVO vo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Org.class);
		List<Order> orderList = new ArrayList<Order>();
		try {
			criteria.add(Restrictions.in("orgID", (Long[]) Struts2Util
					.getSession().getAttribute("userCom"))); // add authority
			criteria.add(Restrictions.eq("isDelete", "F"));
			criteria.add(Restrictions.eq("flag", 2));
			criteria.add(Restrictions.eq("inLevel", 1L));
			criteria.add(Restrictions.ne("parentID", -1L));
			if (!Utils.isEmpty(vo.getDepartName())) {
				criteria.add(Restrictions.like("orgName", "%" + vo.getDepartName() + "%").ignoreCase());
			}
			if (vo.getDepartID() != null && vo.getDepartID() != 3l) {
				criteria.add(Restrictions.eq("lineno", vo.getDepartID()));
			}
			orderList.add(Order.asc("orgName")); // add default sort
		} catch (Exception e) {
			e.printStackTrace();
		}
		Page<Org> pageResult = dao.getBatch(page, criteria.getExecutableCriteria(dao.getSession()), orderList);
		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;
	}

	/**
	 * get department tree list(public departments tree interface)
	 * 
	 * @param userID
	 *            operater id
	 * @return List<Org> department list
	 */
	public List<Org> getDepartTree(Long userID) {
		String hql = "From Org where isDelete='F' and inLevel < 2 order by orgName";
		return dao.execute(hql);
	}

	/**
	 * check if department is duplicate
	 * 
	 * @return true duplicate; false not duplicate
	 */
	@SuppressWarnings("rawtypes")
	public Boolean isDuplicate(Long departID, Long parentID, String departName) {
		String sql = "";
		if (Utils.isEmpty(departID)) {
			sql = "SELECT 1 FROM T_CORE_ORG WHERE ISDELETE='F' AND PARENTID = "
					+ parentID + " AND ORGNAME='" + departName + "'";
		} else {
			sql = "SELECT 1 FROM T_CORE_ORG WHERE ISDELETE='F' AND ORG_ID <> "
					+ departID + " AND PARENTID=" + parentID + " AND ORGNAME='"
					+ departName + "'";
		}
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		List list = query.list();
		if (list == null || list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * check if department is duplicate
	 * 
	 * @return true duplicate; false not duplicate
	 */
	@SuppressWarnings("rawtypes")
	public Boolean hasSubOrg(Long orgID) {
		if (Utils.isEmpty(orgID)) {
			return false;
		}
		String sql = "SELECT 1 FROM T_CORE_ORG WHERE PARENTID = " + orgID
				+ " AND ISDELETE='F'";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		List list = query.list();
		if (list == null || list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * check if department has subordinate fleet or driver or device or camera
	 * @return OK/fleet/driver/device/camera
	 */
	@SuppressWarnings("rawtypes")
	public String checkDelDepartment(Long orgID) {
		if (Utils.isEmpty(orgID)) {
			return null;
		}
		String sql = "SELECT 1 FROM T_CORE_ORG WHERE PARENTID = " + orgID + " AND ISDELETE='F'";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		List list = query.list();
		if (list != null && list.size() != 0) {
			return "fleet";
		}
		sql = "SELECT 1 FROM T_CORE_DRIVER WHERE DEPARTMENTID = " + orgID + " AND ISDELETE='F'";
		query = dao.getSession().createSQLQuery(sql);
		list = query.list();
		if (list != null && list.size() != 0) {
			return "driver";
		}
		sql = "SELECT 1 FROM T_CORE_UNITE WHERE DEPTID = " + orgID;
		query = dao.getSession().createSQLQuery(sql);
		list = query.list();
		if (list != null && list.size() != 0) {
			return "device";
		}
		sql = "SELECT 1 FROM T_CORE_CAMERA WHERE DEPTID = " + orgID + " AND ISDELETE='F'";
		query = dao.getSession().createSQLQuery(sql);
		list = query.list();
		if (list != null && list.size() != 0) {
			return "camera";
		}
		return "OK";
	}
}