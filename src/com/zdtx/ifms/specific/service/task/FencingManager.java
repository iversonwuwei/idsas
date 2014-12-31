package com.zdtx.ifms.specific.service.task;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.query.FenceLog;
import com.zdtx.ifms.specific.model.query.Round;
import com.zdtx.ifms.specific.model.task.Fence;
import com.zdtx.ifms.specific.model.task.FenceDetail;
import com.zdtx.ifms.specific.model.task.FenceVehicle;
import com.zdtx.ifms.specific.model.task.Geoveh;
import com.zdtx.ifms.specific.vo.query.FencqueryVo;

@Service
@Transactional
public class FencingManager {

	@Autowired
	private BaseDao baseDao;

	/**
	 * @title 根据父节点ID获得下一级所有子节点
	 * @param id
	 * @return
	 */
	public List<Geoveh> getTreeList(Long id) {
		List<Geoveh> treelist = new ArrayList<Geoveh>();
		treelist = baseDao.execute("FROM Geoveh WHERE parentid =" + id
				+ " ORDER BY tree_name");
		return treelist;
	}

	@SuppressWarnings("unchecked")
	public List<FenceLog> getFenceLog(Long name, FencqueryVo fenVo) {
		String where = "";
		String date = "";
		if (!Utils.isEmpty(fenVo.getBdate())) {
			date = DateUtil.yyyy_MM_ddTOyyyyMMdd(fenVo.getBdate().trim());
		}
		String order = " order by  o_date,o_time,o_timelabel ";

		String sql = "select o_busno,o_curstate,o_elecfenceid,o_timelabel,o_date,o_time,o_ptx,o_pty from T_LOG_BUSELECFENCES"
				+ date + " where o_busno=" + name + " " + where + order;
		List<FenceLog> list = null;
		try {
			list = new ArrayList<FenceLog>();
			SQLQuery query = baseDao.getSession().createSQLQuery(sql);
			query.addScalar("o_busno", LongType.INSTANCE);
			query.addScalar("o_curstate", LongType.INSTANCE);
			query.addScalar("o_elecfenceid", LongType.INSTANCE);
			query.addScalar("o_timelabel", LongType.INSTANCE);
			query.addScalar("o_date", StringType.INSTANCE);
			query.addScalar("o_time", StringType.INSTANCE);
			query.addScalar("o_ptx", LongType.INSTANCE);
			query.addScalar("o_pty", LongType.INSTANCE);
			query.setResultTransformer(Transformers.aliasToBean(FenceLog.class));
			query.setCacheable(true);
			list = (List<FenceLog>) query.list();
		} catch (Exception e) {
		}
		return list;
	}

	public List<KeyAndValue> getOrgBySuper(String caption) {
		String sql = " SELECT geofencingid AS KEY,caption AS VALUE FROM t_ifms_geofencing "
				+ " WHERE isdelete='F' and caption='" + caption + "'";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public List<KeyAndValue> getOrgBySuper() {
		String sql = " SELECT geofencingid AS KEY,caption AS VALUE FROM t_ifms_geofencing "
				+ " WHERE isdelete='F' order by caption";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public List<FenceDetail> getFenceDetail(Long id) {
		return baseDao.execute("FROM FenceDetail WHERE  geofencingid =" + id
				+ " order by latiid asc");
	}

	public List<FenceDetail> getFenceDetailByVehid(Long id) {
		return baseDao
				.execute("FROM FenceDetail f WHERE  f.geofencingid in (Select b.geofencingid From FenceVehicle b where b.vehicleid="
						+ id + ")  order by f.latiid asc");
	}

	public List<FenceVehicle> FenceVehicle(Long id) {
		return baseDao.execute("FROM FenceVehicle WHERE vehicleid =" + id);
	}

	public List<FenceVehicle> FenceVehicle1(Long id, Long geofencingid) {
		return baseDao.execute("FROM FenceVehicle WHERE vehicleid =" + id
				+ " and geofencingid=" + geofencingid);
	}

	public List<Fence> getFence(String caption, Long id) {
		Criteria criteria = baseDao.getSession().createCriteria(Fence.class);
		if (null != caption && !"".equals(caption)) {
			criteria.add(Restrictions.eq("caption", caption));
		}
		if (null != id) {
			criteria.add(Restrictions.ne("geofencingid", id));
		}
		criteria.add(Restrictions.eq("isdelete", "F"));
		return baseDao.getAll(criteria);
	}

	public void deleteDetail(Long id) {
		String hql = "delete from t_ifms_geo_lati where geofencingid=" + id;
		baseDao.getSession().createSQLQuery(hql).executeUpdate();
	}

	public void delete(Long id) {
		String hql = "delete from t_ifms_geo_veh where geofencingid=" + id;
		baseDao.getSession().createSQLQuery(hql).executeUpdate();
	}

	public void update(Long id) {
		String hql = "update Fence  set isdelete='T' where geofencingid = "
				+ id;
		baseDao.executeUpdate(hql);
	}

	@SuppressWarnings("unchecked")
	public List<Round> getGpsline(String name, FencqueryVo fenVo) {
		String sql = " SELECT O_LONGITUDE AS longitude, O_LATITUDE AS latitude, null as beh_name, null as riqi, null as time_begin, null as time_end,null as beh_type, null as time_cont, null as veh_speed, null as turn_speed, null as not_id,TO_CHAR(O_GPSDATETIME, 'yyyy-mm-dd hh24:mi:ss') AS gpsDateTime "
				+ " FROM t_jk_fullgpsdata"
				+ fenVo.getBdate().replaceAll("-", "")
				+ "  a where a.o_busname='"
				+ name
				+ "' AND a.O_LONGITUDE <> 0 AND a.O_LATITUDE <> 0  order by a.O_GPSDATETIME ";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
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
}