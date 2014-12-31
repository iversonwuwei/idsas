/**
 * @File: DisplayManager.java
 * @path: ifms - com.zdtx.ifms.specific.service.monitor
 */
package com.zdtx.ifms.specific.service.monitor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.model.query.Round;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.vo.monitor.DisplayVO;
import com.zdtx.ifms.specific.vo.monitor.GeoVO;

/**
 * @ClassName: DisplayManager
 * @Description: monitor-display-manager
 * @author: Leon Liu
 * @date: 2013-4-28 PM1:50:12
 * @version V1.0
 */
@Service
@Transactional
public class DisplayManager {

	@Autowired
	private BaseDao dao;

	public Page<VehcileView> getBatch(Page<VehcileView> page, DisplayVO vo) {
		Criteria criteria = dao.getSession().createCriteria(VehcileView.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.in("fleetid", (Long[])Struts2Util.getSession().getAttribute("userFleet")));	//add authority
		if (!Utils.isEmpty(vo.getFleetID()) && vo.getFleetID() != -1L) {
			criteria.add(Restrictions.eq("fleetid", vo.getFleetID()));
		}
		if (!Utils.isEmpty(vo.getVehID()) && vo.getVehID() != -1L) {
			criteria.add(Restrictions.eq("vehicleid", vo.getVehID()));
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("vehiclename"));
		return dao.getBatch(page, criteria, orders);
	}

	public List<KeyAndValue> getSchedule(Long id) {
		String sql = "";
		if (-1 == id) {
			sql = "SELECT T.SCHEDULEID AS KEY,T.STARTIME||','||T.ENDTIME AS VALUE FROM T_IFMS_SCHEDULE T WHERE T.ISDELETE = 'F' ORDER BY T.STARTIME DESC";
		} else {
			sql = "SELECT T.SCHEDULEID AS KEY,T.STARTIME||','||T.ENDTIME AS VALUE FROM T_IFMS_SCHEDULE T WHERE T.ISDELETE = 'F' AND T.VEHICLEID = "
					+ id + "  ORDER BY T.STARTIME DESC";
		}
		return dao.getKeyAndValueBySQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List<GeoVO> getGeo(String vehicleName, String beginDate,
			String endDate, String beginTime, String endTime) {
		String videoDate = beginDate.replaceAll("-", "");
		String tableName = "t_jk_fullgpsdata" + videoDate;
		String sql = "SELECT O_BUSNAME AS vehicleName, O_LONGITUDE AS longitude, O_LATITUDE AS latitude, TO_CHAR(O_GPSDATETIME, 'yyyy-mm-dd hh24:mi:ss') AS gpsDateTime FROM "
				+ tableName
				+ " WHERE O_BUSNAME = '"
				+ vehicleName
				+ "' AND TO_CHAR(O_GPSDATETIME, 'yyyy-mm-dd hh24:mi:ss') >= '"
				+ beginDate
				+ " "
				+ beginTime
				+ "' AND TO_CHAR(O_GPSDATETIME, 'yyyy-mm-dd hh24:mi:ss') <= '"
				+ beginDate
				+ " "
				+ endTime
				+ "'"
				+ " AND O_LONGITUDE <> 0 AND O_LATITUDE <> 0"
				+ " ORDER BY O_GPSDATETIME ASC";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("vehicleName", StringType.INSTANCE);
		query.addScalar("longitude", DoubleType.INSTANCE);
		query.addScalar("latitude", DoubleType.INSTANCE);
		query.addScalar("gpsDateTime", StringType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(GeoVO.class));
		List<GeoVO> list = (List<GeoVO>) query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Details> getEvents(Long vehID, String eventDate,
			String beginTime, String endTime) {
		Criteria criteria = dao.getSession().createCriteria(Details.class);
		criteria.add(Restrictions.eq("vehId", vehID));
		criteria.add(Restrictions.eq("riqi", eventDate));
		criteria.add(Restrictions.ge("timeBegin", beginTime));
		criteria.add(Restrictions.le("timeEnd", endTime));
		// criteria.setCacheable(true);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Round> getRound(String vehicleName, String beginDate,
			String endDate, String beginTime, String endTime, Long vehID,
			String eventDate) {
		String sql = "SELECT * FROM (SELECT LONGITUDE, LATITUDE, BEH_NAME, RIQI, TIME_BEGIN, TIME_END, BEH_TYPE, TIME_CONT, VEH_SPEED, TURN_SPEED, NOT_ID, RIQI || ' ' || TIME_END GPSDATETIME"
				+ " FROM T_BEH_NOT_STANDARD B WHERE"
				+ " B.VEH_ID = " + vehID
				+ " AND B.RIQI = '" + eventDate
				+ "' AND B.TIME_BEGIN >= '" + beginTime + "' AND B.TIME_END <= '" + endTime + "'";
		sql += " UNION ALL SELECT O_LONGITUDE AS LONGITUDE, O_LATITUDE AS LATITUDE, NULL AS BEH_NAME, O_DATE AS RIQI, O_TIME AS TIME_BEGIN, O_TIME AS TIME_END,NULL AS BEH_TYPE, NULL AS TIME_CONT, O_SPEED AS VEH_SPEED, NULL AS TURN_SPEED, NULL AS NOT_ID,TO_CHAR(O_GPSDATETIME, 'YYYY-MM-DD HH24:MI:SS') AS GPSDATETIME"
				+ " FROM T_JK_FULLGPSDATA" + beginDate.replaceAll("-", "") + " A"
				+	" WHERE A.O_BUSNAME = '" + vehicleName
				+ "' AND O_DATE='"+ beginDate +"' AND O_TIME >= '" + beginTime
				+ "' AND O_TIME<= '"  + endTime
				+ "' AND A.O_LONGITUDE <> 0 AND A.O_LATITUDE <> 0"
//				+ " AND A.O_LONGITUDE > 6200 AND A.O_LONGITUDE < 6300"	//过滤坐标到新加坡附近
//				+ " AND A.O_LATITUDE > 70 AND A.O_LATITUDE < 90"
				+ " ) T ORDER BY T.GPSDATETIME,T.BEH_NAME nulls first";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("longitude", DoubleType.INSTANCE);
		query.addScalar("latitude", DoubleType.INSTANCE);
		query.addScalar("gpsDateTime", StringType.INSTANCE);
		query.addScalar("beh_name", StringType.INSTANCE);
		query.addScalar("riqi", StringType.INSTANCE);
		query.addScalar("time_begin", StringType.INSTANCE);
		query.addScalar("time_end", StringType.INSTANCE);
		query.addScalar("beh_type", StringType.INSTANCE);
		query.addScalar("time_cont", StringType.INSTANCE);
		query.addScalar("veh_speed", LongType.INSTANCE);
		query.addScalar("turn_speed", LongType.INSTANCE);
		query.addScalar("not_id", LongType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(Round.class));
		return query.list();
	}

	/**
	 * 根据车辆ID获得视频ID
	 * 
	 * @param VehID
	 *            车辆ID
	 * @return null|视频ID
	 */
	public String getVideoIDByVehID(Long vehID) {
		String sql = "SELECT A.O_VIDEONAME AS KEY, 1 AS VALUE FROM T_CORE_UNITE A, T_CORE_VEHICLE B WHERE A.O_DEVICENO = B.DEVICEID AND B.VEHICLEID = "
				+ vehID;
		List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0).getKey();
		}
	}
}