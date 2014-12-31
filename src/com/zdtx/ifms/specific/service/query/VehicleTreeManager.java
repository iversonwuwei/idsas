package com.zdtx.ifms.specific.service.query;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.specific.model.query.VehicleTree;



@Service
@Transactional
public class VehicleTreeManager {
	@Autowired
	private BaseDao baseDao;

	public List<VehicleTree> getTreeList(Long id) {
		List<VehicleTree> treelist = new ArrayList<VehicleTree>();
		if(id == 1L) {
			treelist = baseDao.execute("FROM VehicleTree WHERE fleetid = 1 and vehicleid in ("+ Struts2Util.getSession().getAttribute("userComStr") + ")");
		} else {
			treelist = baseDao.execute("FROM VehicleTree WHERE fleetid =" + id);
		}
		return treelist;
	}

/*	*//**
	 * 通过用户id获得可见公司列表
	 *
	 * @param userID
	 *            登录用户id
	 * @return 可见的机构列表
	 *//*
	public List<KeyAndValue> getOrgBySuper(Long userID) {
		if (Struts2Util.isEmpty(userID)) { // 验证入参是否为空
			return null;
		}
		String sql = "SELECT ORG_ID AS KEY,USERID AS VALUE FROM T_CORE_USER_DATA "
				+ " WHERE USERID =  " + userID;
		return baseDao.getKeyAndValueBySQL(sql);
	}*/

/*	*//**
	 * 列表封装成 list<org>
	 *
	 * @param user
	 * @return list<org>
	 *//*
	@SuppressWarnings("unchecked")
	public List<VehicleTree> getAll(User user) {
		String hql = "";
		List<KeyAndValue> orgList = null;
		orgList = getOrgBySuper(user.getUserID());
		Long[] orgArr = null;
		orgArr = Struts2Util.keysToArray(orgList);// 可见的机构列表封装成long[]
		if (0 == user.getUserRole().getInLevel()) { // 超级管理员全部可见
			hql = "FROM Org o where o.isDelete = 'F'";
			return baseDao.execute(hql);
		} else {
			hql = "FROM Org o where o.isDelete = 'F' and o.orgID in (:orgArr)";
			Query query = baseDao.getSession().createQuery(hql);
			query.setParameterList("orgArr", orgArr);
			return query.list();
		}
	}

	public Feat getFeatById(long id) {
		String hql = "FROM Feat f where f.isDelete = 'F' and f.featID = " + id;
		return (Feat) baseDao.execute(hql).get(0);
	}

	*//**
	 * @param 根据机构名称搜索
	 * @param name
	 * @return
	 *//*
	public List<KeyAndValue> searchOrg(String name) {
		List<KeyAndValue> list = new ArrayList<KeyAndValue>();
		list = baseDao.getKeyAndValueBySQL("SELECT ORG_ID AS KEY, ORGNAME AS VALUE "
				+ " FROM T_CORE_ORG WHERE ISDELETE = 'F' START WITH ORG_ID IN "
				+ " ( SELECT T.ORG_ID FROM T_CORE_ORG T WHERE T.ORGNAME LIKE '%" + name
				+ "%') CONNECT BY PRIOR FATHERORG = ORG_ID");
		return list;
	}

	*//**
	 * 取新显示顺序号
	 *//*
	public String getNewDisplayNo(Long parentID) {
		if (Struts2Utils.isEmpty(parentID)) {
			return null;
		}
		String sql = "SELECT MAX(ORGSORT) AS KEY,MAX(ORGSORT) AS VALUE FROM T_CORE_ORG WHERE FATHERORG = "
				+ parentID;
		return baseDao.getKeyAndValueBySQL(sql).get(0).getKey() == null ? "0"
				: baseDao.getKeyAndValueBySQL(sql).get(0).getKey();
	}
	*/
	/**
	 * @title 搜索功能返回的特殊格式的字符串
	 * @param name
	 * @return
	 */
	public List<String> search(String name) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT T.VEHICLEID FROM V_TREE_VEHICLE T WHERE 1=1 START WITH T.vehicleid IN "
				+ "(SELECT R.VEHICLEID FROM V_TREE_VEHICLE R WHERE R.LICENSEPLATE LIKE '%" + name.trim()
				+ "%') CONNECT BY PRIOR T.FLEETID = T.VEHICLEID ";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		for (Object obj : query.list()) {
			list.add("#" + obj.toString());
		}
		return list;
	}
	
	/***
	 *  根据车辆id获得实体类list
	 * @param id
	 * @return
	 */
	public List<KeyAndValue> getVehicleList(String name,Long id) {
		if(null != id){
			if (null == name || "".equals(name)) { // 验证入参是否为空
				String sql = "SELECT vehicleid AS KEY,licenseplate AS VALUE FROM T_CORE_VEHICLE "
						+ " WHERE ISDELETE='F' and vehicleid not in (select max(vehicleid) from t_ifms_geo_veh  where geofencingid="+id+" group by  vehicleid) and  rownum<10";
				return baseDao.getKeyAndValueBySQL(sql);
			} else {
				String sql = "SELECT vehicleid AS KEY,licenseplate AS VALUE FROM T_CORE_VEHICLE "
						+ " WHERE ISDELETE='F' and vehicleid not in (select max(vehicleid) from t_ifms_geo_veh where geofencingid="+id+"   group by  vehicleid) and  licenseplate like '" + name+"%' and  rownum<10 ";
				return baseDao.getKeyAndValueBySQL(sql);
			}
		} else {
			if (null == name || "".equals(name)) { // 验证入参是否为空
				String sql = "SELECT vehicleid AS KEY,licenseplate AS VALUE FROM T_CORE_VEHICLE "
						+ " WHERE ISDELETE='F' and    rownum<10";
				return baseDao.getKeyAndValueBySQL(sql);
			} else {
				String sql = "SELECT vehicleid AS KEY,licenseplate AS VALUE FROM T_CORE_VEHICLE "
						+ " WHERE ISDELETE='F' and   licenseplate like '" + name+"%' and  rownum<10 ";
				return baseDao.getKeyAndValueBySQL(sql);
			}
		}
		 
		
		
	}
	
	public List<KeyAndValue> getVehicleList1(String name) {
		if (null == name || "".equals(name)) { // 验证入参是否为空
			String sql = "SELECT vehicleid AS KEY,licenseplate AS VALUE FROM T_CORE_VEHICLE "
					+ " WHERE ISDELETE='F' and vehicleid not in (select max(vehicleid) from t_ifms_booking_detail where isdelete='F' group by  vehicleid)  and rownum<10";
			return baseDao.getKeyAndValueBySQL(sql);
		} else {
			String sql = "SELECT vehicleid AS KEY,licenseplate AS VALUE FROM T_CORE_VEHICLE "
					+ " WHERE ISDELETE='F' and vehicleid not in (select max(vehicleid) from t_ifms_booking_detail where isdelete='F'  group by  vehicleid)  and licenseplate like '" + name+"%' and  rownum<10 ";
			return baseDao.getKeyAndValueBySQL(sql);
		}
		
		
	}
	
}
