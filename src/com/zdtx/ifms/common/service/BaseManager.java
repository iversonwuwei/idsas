package com.zdtx.ifms.common.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.authority.Feat;
import com.zdtx.ifms.specific.model.authority.User;

/**
 * 通用类
 * 基础业务层，封装一系列通用查询方法
 * @author Leon Liu
 * @since 2013-2-25 9:22:40
 */
@Service
@Transactional
public class BaseManager {

	@Autowired
	private BaseDao baseDao;

	/**
	 * 保存
	 * @param <E>
	 * @param entity
	 */
	public <E> void save(E entity) {
		baseDao.save(entity);
	}

	/**
	 * 按ID删除
	 * @param <E>
	 * @param clazz
	 * @param id
	 */
	public <E> void delete(Class<E> clazz, Long id) {
		baseDao.delete(clazz, id);
	}

	/**
	 * 删除对象
	 * @param <E>
	 * @param entity
	 */
	public <E> void delete(E entity) {
		baseDao.delete(entity);
	}

	/**
	 * 按ID获取对象
	 * @param <E>
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <E> E get(Class<E> clazz, Long id) {
		return (E) baseDao.get(clazz, id);
	}

	/**
	 * 按类型获取所有对象
	 * @param <E>
	 * @param clazz
	 * @return
	 */
	public <E> List<E> getAll(Class<E> clazz) {
		return baseDao.getAll(clazz);
	}

	/**
	 * 按类型获取所有对象，并按字段排序
	 *
	 * @param <E>
	 * @param clazz
	 * @param orderColumn
	 * @param orderAsc
	 * @return
	 */
	public <E> List<E> getAll(Class<E> clazz, String orderColumn,
			String orderAsc) {
		return baseDao.getAll(clazz, orderColumn, orderAsc);
	}

	/**
	 * 判断角色是否具有所有功能权限(角色对应包含FEAT_ID = 0的数据关系)
	 * @param roleID 权限ID
	 * @return true:有所有功能权限;	false:没有所有功能权限.
	 */
	public boolean isAllFeats(Long roleID) {
		if (Utils.isEmpty(roleID)) {
			return false;
		}
		String sql = "SELECT 1 FROM T_CORE_ROLE_FEAT WHERE FEAT_ID = 0 AND ROLE_ID = " + roleID;
		List<?> countList = baseDao.getSession().createSQLQuery(sql).list();
		int countNum = 0;
		if (countList != null) {
			countNum = countList.size();
		}
		return countNum > 0;
	}
	
	/**
	 * 判断用户是否具有所有数据权限(用户所属部门的inlevel=0为拥有全部数据权限)
	 * @param userID	用户ID
	* @return true:有所有数据权限;	false:没有所有数据权限.
	 */
	public boolean isAllData(Long userID) {
		if (Utils.isEmpty(userID)) {
			return false;
		}
		User u=baseDao.get(User.class, userID);
		if (u != null) {
			if ((u.getUserRole().getAflag() != null) && "0".equals(u.getUserRole().getAflag())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * get companys by authority
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getComByAuthority(Long userID) {
		if (Utils.isEmpty(userID)) {
			return null;
		}
		User u=baseDao.get(User.class, userID);
		if(u.getUserRole().getAflag().equals("0")){
			String sql = "SELECT A.ORG_ID AS KEY, A.ORGNAME AS VALUE" +
			" FROM T_CORE_ORG A" +
			" WHERE A.INLEVEL = 1" +
			" AND A.ISDELETE = 'F'" +
			" ORDER BY A.ORGNAME ASC";
			return baseDao.getKeyAndValueBySQL(sql);
		}else{
			String sql = "SELECT A.ORG_ID AS KEY, A.ORGNAME AS VALUE" +
			" FROM T_CORE_ORG A, T_CORE_USER_DATA B" +
			" WHERE A.INLEVEL = 1" +
			" AND A.ISDELETE = 'F'" +
			" AND A.ORG_ID = B.ORG_ID AND B.USERID = " + userID + 
			" ORDER BY A.ORGNAME ASC";
			return baseDao.getKeyAndValueBySQL(sql);
		}
	}
	
	/**
	 * get departments by authority
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getDepartByAuthority(Long userID) {
		if (Utils.isEmpty(userID)) {
			return null;
		}
		User u=baseDao.get(User.class, userID);
		if(u.getUserRole().getAflag().equals("0")){
			String sql = "SELECT A.ORG_ID AS KEY, A.ORGNAME AS VALUE" +
			" FROM T_CORE_ORG A" +
			" WHERE A.INLEVEL = 2" +
			" AND A.ISDELETE = 'F'" +
			" ORDER BY A.ORGNAME ASC";
			return baseDao.getKeyAndValueBySQL(sql);
		}else{
			String sql = "SELECT A.ORG_ID AS KEY, A.ORGNAME AS VALUE" +
			" FROM T_CORE_ORG A, T_CORE_USER_DATA B" +
			" WHERE A.INLEVEL = 2" +
			" AND A.ISDELETE = 'F'" +
			" AND A.ORG_ID = B.ORG_ID AND B.USERID = " + userID + 
			" ORDER BY A.ORGNAME ASC";
			return baseDao.getKeyAndValueBySQL(sql);
		}
	}
	
	/**
	 * get fleets by authority
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getFleetByAuthority(Long userID) {
		if (Utils.isEmpty(userID)) {
			return null;
		}
		User u = baseDao.get(User.class, userID);
		if (u.getUserRole().getAflag().equals("0")) {
			String sql = "SELECT A.ORG_ID AS KEY, A.ORGNAME AS VALUE"
					+ " FROM T_CORE_ORG A WHERE A.INLEVEL = 3 and A.ISDELETE='F' ORDER BY A.ORGNAME ASC";
			return baseDao.getKeyAndValueBySQL(sql);
		} else {
			String sql = "SELECT A.ORG_ID AS KEY, A.ORGNAME AS VALUE"
					+ " FROM T_CORE_ORG A WHERE A.INLEVEL = 3 AND A.ISDELETE = 'F' AND A.PARENTID IN (SELECT B.ORG_ID FROM T_CORE_ORG B, T_CORE_USER_DATA C"
					+ " WHERE B.INLEVEL = 2 AND B.ISDELETE = 'F'"
					+ " AND B.ORG_ID = C.ORG_ID AND C.USERID = " + userID
					+ " ) ORDER BY A.ORGNAME ASC";
			return baseDao.getKeyAndValueBySQL(sql);
		}
	}
	
	/**
	 * get vehicle name by authority
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getVehicleNameByAuthority(Long userID) {
		if(Utils.isEmpty(userID)) {
			return null;
		}
		String sql = "";
		User user = baseDao.get(User.class, userID);
		if (user.getUserRole().getAflag().equals("0")) {	//super admin
			sql = "SELECT A.VEHICLEID AS KEY, A.VEHICLENAME AS VALUE" +
					" FROM T_CORE_VEHICLE A" +
					" WHERE A.ISDELETE = 'F'" +
					" ORDER BY A.VEHICLENAME ASC";
		} else {
			sql = "SELECT A.VEHICLEID AS KEY, A.VEHICLENAME AS VALUE" +
					" FROM T_CORE_VEHICLE A, T_CORE_ORG B, T_CORE_USER_DATA C" +
					" WHERE A.ISDELETE = 'F'" +
					" AND A.FLEETID = B.ORG_ID" +
					" AND B.PARENTID = C.ORG_ID" +
					" AND C.USERID = " + userID +
					" ORDER BY A.VEHICLENAME ASC";
		}
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * get vehicle's license plate number by authority
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getVehicleLicenseByAuthority(Long userID) {
		if(Utils.isEmpty(userID)) {
			return null;
		}
		String sql = "";
		User user = baseDao.get(User.class, userID);
		if (user.getUserRole().getAflag().equals("0")) {	//super admin
			sql = "SELECT A.VEHICLEID AS KEY, A.LICENSEPLATE AS VALUE" +
					" FROM T_CORE_VEHICLE A" +
					" WHERE A.ISDELETE = 'F'" +
					" ORDER BY A.LICENSEPLATE ASC";
		} else {
			sql = "SELECT A.VEHICLEID AS KEY, A.LICENSEPLATE AS VALUE" +
					" FROM T_CORE_VEHICLE A, T_CORE_ORG B, T_CORE_USER_DATA C" +
					" WHERE A.ISDELETE = 'F'" +
					" AND A.FLEETID = B.ORG_ID" +
					" AND B.PARENTID = C.ORG_ID" +
					" AND C.USERID = " + userID +
					" ORDER BY A.LICENSEPLATE ASC";
		}
		return baseDao.getKeyAndValueBySQL(sql);
	}

	/**
	 * get drivers by authority
	 * @param userID	operater's ID
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getDriverByAuthority(Long userID) {
		if (Utils.isEmpty(userID)) {
			return null;
		}
		String sql = "";
		User user = baseDao.get(User.class, userID);
		if (user.getUserRole().getAflag().equals("0")) {	//super admin
			sql = "SELECT A.DRIVERID AS KEY, A.DRIVERNAME AS VALUE" +
					" FROM T_CORE_DRIVER A" +
					" WHERE A.ISDELETE = 'F'" +
					" ORDER BY A.DRIVERNAME ASC";
		} else {
			sql = "SELECT A.DRIVERID AS KEY, A.DRIVERNAME AS VALUE" +
					" FROM T_CORE_DRIVER A, T_CORE_USER_DATA B" +
					" WHERE A.DEPARTMENTID = B.ORG_ID" +
					" AND A.ISDELETE = 'F'" +
					" AND B.USERID = " + userID + " ORDER BY A.DRIVERNAME ASC";
		}
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * get fleets by department's ID
	 * @return	List<KeyAndValue>
	 */
	public List<KeyAndValue> getFleetByDepartment(Long departmentID) {
		if(Utils.isEmpty(departmentID)) {
			return null;
		}
		String sql = "SELECT A.ORG_ID AS KEY, A.ORGNAME AS VALUE" +
				" FROM T_CORE_ORG A WHERE A.PARENTID =" + departmentID +
				" ORDER BY A.ORGNAME ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * get vehicle name by fleet
	 * @param fleetID
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getVehicleNameByFleet(Long fleetID) {
		if (Utils.isEmpty(fleetID)) {
			return null;
		}
		String sql = "SELECT VEHICLEID AS KEY, VEHICLENAME AS VALUE" +
				" FROM T_CORE_VEHICLE" +
				" WHERE ISDELETE = 'F'" +
				" AND FLEETID = "+ fleetID + " ORDER BY VEHICLENAME ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * get vehicle's license plate number by fleet
	 * @param fleetID
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getVehicleLicenseByFleet(Long fleetID) {
		if (Utils.isEmpty(fleetID)) {
			return null;
		}
		String sql = "SELECT VEHICLEID AS KEY, LICENSEPLATE AS VALUE" +
				" FROM T_CORE_VEHICLE" +
				" WHERE ISDELETE = 'F'" +
				" AND FLEETID = "+ fleetID + " ORDER BY LICENSEPLATE ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * get drivers by department
	 * @param departID
	 * @param userID	operater's ID
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getDriverByDepart(Long departID) {
		if (Utils.isEmpty(departID)) {
			return null;
		}
		String sql = "SELECT DRIVERID AS KEY, DRIVERNAME AS VALUE" +
				" FROM T_CORE_DRIVER" +
				" WHERE ISDELETE = 'F'" +
				" AND DEPARTMENTID = "+ departID + " ORDER BY DRIVERNAME ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	/**
	 * get vehcle's lisence plate number by filter text (top 10)
	 * @param text	filter context
	 * @return List<KeyAndValue>
	 */
	public List<KeyAndValue> getVehicleByFilter(String text) {
		String sql = "SELECT DISTINCT A.VEHICLEID AS KEY, A.LICENSEPLATE AS VALUE FROM T_CORE_VEHICLE A WHERE A.ISDELETE = 'F'"
			+ " AND A.LICENSEPLATE LIKE '%" + text + "%' AND ROWNUM < 11";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * get feat tree by authority(used by left feat tree)
	 * @param roleID operater's role ID
	 * @return List<Feat>
	 */
	@SuppressWarnings("unchecked")
	public List<Feat> getRoleTree(Long roleID) {
		String sql = "";
		if (isAllFeats(roleID)) { //true用户全部属性可见
			sql = "SELECT * FROM T_CORE_FEAT WHERE ISDELETE = 'F' ORDER BY TO_NUMBER(FATHERID), SORT";
		} else {
			sql = "SELECT * FROM T_CORE_FEAT A, T_CORE_ROLE_FEAT B WHERE A.ISDELETE = 'F' AND A.FEAT_ID = B.FEAT_ID AND B.ROLE_ID = "
					+ roleID + " ORDER BY TO_NUMBER(A.FATHERID), A.SORT";
		}
		SQLQuery query = baseDao.getSession().createSQLQuery(sql).addEntity(
				Feat.class);
		return (List<Feat>) query.list();
	}
	
	/**
	 * get vehicle name by authority
	 * @return List<KeyAndValue>
	 */
	public List<String> getVehicleNameOnlyByAuthority() {
		String sql = "SELECT A.VEHICLENAME " +
				"FROM T_CORE_VEHICLE A " +
				"WHERE A.ISDELETE = 'F' ORDER BY A.VEHICLENAME ASC";
		return baseDao.getStringBySQL(sql);
	}
	
	/***
	 * get vehicle's DeviceName
	 * @return
	 */
	public List<KeyAndValue> getVehicleAndDeviceNameByAuthority() {
		// String sql =
		// "SELECT DISTINCT A.VEHICLENAME AS KEY, b.devicekey AS VALUE "
		// +" FROM T_CORE_VEHICLE A ,t_core_device b "
		// +" WHERE A.ISDELETE = 'F' and a.deviceid=b.deviceid(+) ORDER BY A.VEHICLENAME ASC ";
		/***
		 * 获得视频设备号
		 */
//		String sql = "select a.vehiclename as key, b.o_videoname as value "
//				+ " from T_CORE_VEHICLE a, t_dm_videoid b "
//				+ " where a.vehiclename = b.o_busname(+) order by a.vehiclename";
		/***
		 * 获得车辆id 暂时用这个
		 */
		String sql = "select a.vehiclename as key, a.VEHICLEID as value  from T_CORE_VEHICLE a ";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	
	/***
	 * get vehicle's DeviceName (and police's name) Liu jun 修改于2014-6-20 13:47:35
	 * @return
	 */
	public List<KeyAndValue> getVehicleAndDeviceNameByAuthority2(Long userid) {
		String sql2 = "SELECT A.ORG_ID AS KEY " +
				" FROM T_CORE_ORG A WHERE A.ISDELETE = 'F' AND A.PARENTID IN (SELECT B.ORG_ID FROM T_CORE_ORG B, T_CORE_USER_DATA C" +
				" WHERE B.INLEVEL = 2" +
				" AND B.ISDELETE = 'F'" +
				" AND B.ORG_ID = C.ORG_ID AND C.USERID = " + userid +
				" ) ";
		// String sql =
		// "SELECT DISTINCT A.VEHICLENAME AS KEY, b.devicekey AS VALUE "
		// +" FROM T_CORE_VEHICLE A ,t_core_device b "
		// +" WHERE A.ISDELETE = 'F' and a.deviceid=b.deviceid(+) ORDER BY A.VEHICLENAME ASC ";
		/***
		 * 获得视频设备号
		 */
//		String sql = "select a.vehiclename as key, b.o_videoname as value "
//				+ " from T_CORE_VEHICLE a, t_dm_videoid b "
//				+ " where a.vehiclename = b.o_busname(+) order by a.vehiclename";
		/***
		 * 获得车辆id 暂时用这个
		 */
//		String sql = "select d.vehiclename as key, d.VEHICLEID as value  from T_CORE_VEHICLE d where d.ISDELETE='F' and d.FLEETID in ( "+sql2+" )";
		String sql = "SELECT D.VEHICLENAME AS KEY, D.VEHICLEID AS VALUE "
						+ "FROM T_CORE_VEHICLE D WHERE D.ISDELETE='F' AND D.FLEETID IN (" + sql2 + ") "
						+ "UNION (SELECT POLICENAME AS KEY, DEVICEID AS VALUE FROM T_CORE_POLICE WHERE FLEETID IN (" + sql2 + ") AND ISDELETE = 'F')";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * public do print pdf interface
	 */
	public void doPrintPdf(final String title, final Boolean hasSeq, final String headerArray, final String fieldsArray, final Map<String, String> convert, final Integer[] colWidth, final Integer pageSize, final String... paper) {
		DetachedCriteria criteria = (DetachedCriteria)Struts2Util.getSession().getAttribute("criteria_export");
		Criteria criteria2 = criteria.getExecutableCriteria(baseDao.getSession());
		criteria2.setFirstResult(0);
		criteria2.setMaxResults(1000);
		Utils.printPDF(title, hasSeq, criteria2.list(), headerArray, fieldsArray, convert, colWidth, pageSize, paper);
	}
	
	/**
	 * public do export excel interface
	 */
	public InputStream doExportExcel(final String title, final String headerArray, final String fieldsArray, final Map<String, String> convert) {
		DetachedCriteria detachedCriteria = (DetachedCriteria)Struts2Util.getSession().getAttribute("criteria_export");
		Criteria criteria = detachedCriteria.getExecutableCriteria(baseDao.getSession());
		criteria.setFirstResult(0);
		criteria.setMaxResults(1000);
		return Utils.exportExcel(criteria.list(), title, headerArray, fieldsArray, convert);
	}
	
	/***
	 * 通过车型获得车辆列表（id，name）
	 * @param typeid
	 * @return
	 */
	public List<KeyAndValue> getVehByType(Long typeid){
		String sql="select t.VEHICLEID as key ,t.VEHICLENAME as value from V_CORE_VEHICLE t where t.TYPEID="+typeid;
		return baseDao.getKeyAndValueBySQL(sql);
	}
}