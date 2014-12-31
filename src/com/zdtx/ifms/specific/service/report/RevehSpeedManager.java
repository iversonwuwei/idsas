package com.zdtx.ifms.specific.service.report;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.vo.query.DetailsVo;

@Service
@Transactional
public class RevehSpeedManager {
	@Autowired
	public BaseDao baseDao;
	@Autowired
	public BaseManager baseMgr;
	
	@SuppressWarnings("unchecked")
	public List<List<String>> getAll(DetailsVo dvo){
		
		String tablename = "t_jk_fullgpsdata"+dvo.getRiqi().replace("-","");
		String sql = "select TO_CHAR(O_GPSDATETIME, 'yyyy-mm-dd hh24:mi:ss'),o_speed from "+tablename+" where 1=1 ";
		sql+=" and o_date = '"+dvo.getRiqi()+"'";
		sql+=" and o_time <= '"+ dvo.getTimeEnd()+"'";
		sql+=" and o_time >= '"+ dvo.getTimeBegin()+"'";
		sql+=" and o_busname = '"+dvo.getVehcode()+"'";
		sql+=" order by o_time";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		List<List<String>> list = new ArrayList<List<String>>();
		list=query.list();
		return list;
	}
	public List<Details> getDets(DetailsVo dvo){
		Long[] types = {12L,11L,19L,13L,14L};
		Criteria criteria =  baseDao.getSession().createCriteria(Details.class);
		criteria.add(Restrictions.eq("vehId",dvo.getVehId()));
		criteria.add(Restrictions.eq("riqi",dvo.getRiqi()));
		criteria.add(Restrictions.ge("timeEnd",dvo.getTimeBegin()));
		criteria.add(Restrictions.le("timeEnd",dvo.getTimeEnd()));
		criteria.add(Restrictions.in("behType",types));
		criteria.addOrder(Order.asc("timeEnd"));
		return baseDao.getAll(criteria);
	}
}
