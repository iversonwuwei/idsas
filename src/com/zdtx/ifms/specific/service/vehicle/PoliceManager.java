/**
 * @path com.zdtx.ifms.specific.service.vehicle
 * @file PoliceManager.java
 */
package com.zdtx.ifms.specific.service.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.vehicle.Police;
import com.zdtx.ifms.specific.vo.monitor.TargetVO;
import com.zdtx.ifms.specific.vo.vehicle.PoliceVO;

/**
 * @description Orgnization management-Police-manager
 * @author Liu Jun
 * @since 2014年6月10日 上午10:52:05
 */
@Service
@Transactional
public class PoliceManager {
	
	@Autowired
	private BaseDao dao;

	public Page<Police> getBatch(Page<Police> page, PoliceVO vo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Police.class);
		criteria.add(Restrictions.eq("isDelete", "F"));
		criteria.add(Restrictions.in("fleetID", (Long[])Struts2Util.getSession().getAttribute("userFleet")));	//add authority
		if (null != vo.getDeptID() && -1L != vo.getDeptID()) {
			criteria.add(Restrictions.eq("deptID", vo.getDeptID()));
		}
		if (null != vo.getFleetID() && -1L != vo.getFleetID()) {
			criteria.add(Restrictions.eq("fleetID", vo.getFleetID()));
		}
		if (!Utils.isEmpty(vo.getDeviceName())) {	// driverName
			criteria.add(Restrictions.ilike("deviceName", "%" + vo.getDeviceName() + "%"));
		}
		if (!Utils.isEmpty(vo.getPoliceName())) {
			criteria.add(Restrictions.ilike("policeName", "%" + vo.getPoliceName() + "%"));
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("deptID"));
		orders.add(Order.asc("fleetID"));
		orders.add(Order.asc("policeName"));
		Page<Police> pageResult =  dao.getBatch(page, criteria.getExecutableCriteria(dao.getSession()), orders);
		Struts2Util.getSession().setAttribute("criteria_export", criteria);
		Struts2Util.getSession().setAttribute("page_export", page);
		Struts2Util.getSession().setAttribute("order_export", orders);
		return pageResult;
	}

	/**
	 * check if police name is duplicate 
	 * @return true duplicate; false not duplicate
	 */
	@SuppressWarnings("rawtypes")
	public Boolean isDuplicate(Long feetID, Long policeID, String policeName) {
		String sql = "";
		if(Utils.isEmpty(policeID)) {
			sql = "SELECT 1 FROM T_CORE_POLICE WHERE ISDELETE='F' AND FLEETID = " + feetID + " AND POLICENAME='" + policeName + "'";
		} else {
			sql = "SELECT 1 FROM T_CORE_POLICE WHERE ISDELETE='F' AND POLICEID <> " + policeID + " AND FLEETID = " + feetID + " AND POLICENAME = '" + policeName + "'";
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
	 * 获取所有单兵设备
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getPoliceDeviceList() {
		String sql = "SELECT T.O_DEVICENO AS KEY, T.O_DEVICENAME AS VALUE FROM T_CORE_UNITE T WHERE T.O_UNITTYPE = '2'"
				+ " AND T.O_DEVICENO NOT IN (SELECT DISTINCT A.DEVICEID FROM T_CORE_POLICE A WHERE A.ISDELETE = 'F')";
		return dao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * 根据部门获取未绑定的单兵设备
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getUnboundedPoliceDeviceList(Long deptID) {
		String sql = "SELECT T.O_DEVICENO AS KEY, T.O_DEVICENAME AS VALUE FROM T_CORE_UNITE T WHERE T.O_UNITTYPE = '2'"
				+ " AND T.O_DEVICENO NOT IN (SELECT DISTINCT A.DEVICEID FROM T_CORE_POLICE A WHERE A.ISDELETE = 'F' AND A.DEVICEID IS NOT NULL) AND T.DEPTID = " + deptID;
		return dao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * 获取所有单兵持有人名称和单兵设备名称组
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getPoliceDeviceNameList() {
		String sql = "SELECT T.DEVICENAME AS KEY, T.POLICENAME AS VALUE FROM T_CORE_POLICE T WHERE T.ISDELETE = 'F'"
				+ " AND T.DEVICENAME IS NOT NULL";
		return dao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * 获取real-time 页左侧菜单数据
	 * @param fleetStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TargetVO> getTargets(String fleetStr) {
		if(Utils.isEmpty(fleetStr)) {
			return null;
		}
		String sql = "SELECT 2 AS TARGETTYPE, B.FLEETID AS FATHERID, B.VEHICLEID||'88000' AS TARGETID, B.VEHICLENAME AS TARGETNAME "
				+ "FROM T_CORE_VEHICLE B "
				+ "WHERE B.ISDELETE = 'F' "
				+ "AND B.FLEETID IN (" + fleetStr + ") "
				+ "UNION ALL "
				+ "SELECT 3 AS TARGETTYPE, A.FLEETID AS FATHERID, A.POLICEID||'88000' AS TARGETID, A.POLICENAME AS TARGETNAME "
				+ "FROM T_CORE_POLICE A "
				+ "WHERE A.ISDELETE = 'F' "
				+ "AND A.FLEETID IN (" + fleetStr + ") "
				+ "UNION ALL "
				+ "SELECT 1 AS TARGETTYPE,  0 AS FATHERID, C.ORG_ID||'' AS TARGETID, C.ORGNAME AS TARGETNAME "
				+ "FROM T_CORE_ORG C "
				+ "WHERE C.ISDELETE = 'F' "
				+ "AND C.ORG_ID IN (" + fleetStr + ") "
				+ "ORDER BY TARGETTYPE, FATHERID, TARGETNAME";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("targetID", StringType.INSTANCE);
		query.addScalar("fatherID", StringType.INSTANCE);
		query.addScalar("targetName", StringType.INSTANCE);
		query.addScalar("targetType", StringType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(TargetVO.class));
		return (List<TargetVO>) query.list();
	}
}