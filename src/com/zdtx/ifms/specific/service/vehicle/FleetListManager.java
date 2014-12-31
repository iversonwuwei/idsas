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
import com.zdtx.ifms.specific.vo.vehicle.FleetListVO;

/**
 * @ClassName: FleetListController
 * @Description: Vehicle-Fleet List-Manager
 * @author Leon Liu
 * @date 2013-4-26 19:52:26
 * @version V1.0
 */
@Service
@Transactional
public class FleetListManager {
	@Autowired
	private BaseDao dao;

	/**
	 * get grid data
	 * @param page
	 * @param vo	query criteria
	 * @return Page<Org>
	 */
	public Page<Org> getBatch(Page<Org> page, FleetListVO vo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Org.class);
		List<Order> orderList = new ArrayList<Order>();
		try {
			criteria.add(Restrictions.eq("isDelete", "F"));
			criteria.add(Restrictions.eq("flag", 3));
			
			criteria.add(Restrictions.in("parentID", (Long[])Struts2Util.getSession().getAttribute("userDepartment")));	//add authority
//			criteria.add(Restrictions.in("orgID", (Long[])Struts2Util.getSession().getAttribute("userFleet")));	//add authority
			if (!Utils.isEmpty(vo.getParentID()) && -1 != vo.getParentID()) {	// by department name
				criteria.add(Restrictions.eq("parentID", vo.getParentID()));
			}
			if (!Utils.isEmpty(vo.getFleetID()) && -1 != vo.getFleetID()) { // by fleet name
				criteria.add(Restrictions.eq("orgID", vo.getFleetID()));
			}
			orderList.add(Order.asc("orgName"));	// order by fleet name
		} catch (Exception e) {
			e.printStackTrace();
		}
		Page<Org> pageResult=dao.getBatch(page,
				criteria.getExecutableCriteria(dao.getSession()),
				orderList);

		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;
	}
	
	/**
	 * check if fleet is duplicate 
	 * @return true duplicate; false not duplicate
	 */
	@SuppressWarnings("rawtypes")
	public Boolean isDuplicate(Long feetID, Long parentID, String fleetName) {
		String sql = "";
		if(Utils.isEmpty(feetID)) {
			sql = "SELECT 1 FROM T_CORE_ORG WHERE ISDELETE='F' AND PARENTID = " + parentID + " AND ORGNAME='" + fleetName + "'";
		} else {
			sql = "SELECT 1 FROM T_CORE_ORG WHERE ISDELETE='F' AND ORG_ID <> " + feetID + " AND PARENTID=" + parentID + " AND ORGNAME='" + fleetName + "'";
		}
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * check if fleet has subordinate vehicle
	 * @return OK/vehicle
	 */
	@SuppressWarnings("rawtypes")
	public String checkDelFleet(Long orgID) {
		if (Utils.isEmpty(orgID)) {
			return null;
		}
		String sql = "SELECT 1 FROM T_CORE_VEHICLE WHERE FLEETID = " + orgID + " AND ISDELETE='F'";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		List list = query.list();
		if (list != null && list.size() != 0) {
			return "vehicle";
		}
//		sql = "SELECT 1 FROM T_CORE_DRIVER WHERE DEPARTMENTID = " + orgID + " AND ISDELETE='F'";
//		query = dao.getSession().createSQLQuery(sql);
//		list = query.list();
//		if (list != null && list.size() != 0) {
//			return "driver";
//		}
//		sql = "SELECT 1 FROM T_CORE_UNITE WHERE DEPTID = " + orgID;
//		query = dao.getSession().createSQLQuery(sql);
//		list = query.list();
//		if (list != null && list.size() != 0) {
//			return "device";
//		}
//		sql = "SELECT 1 FROM T_CORE_CAMERA WHERE DEPTID = " + orgID + " AND ISDELETE='F'";
//		query = dao.getSession().createSQLQuery(sql);
//		list = query.list();
//		if (list != null && list.size() != 0) {
//			return "camera";
//		}
		return "OK";
	}
}