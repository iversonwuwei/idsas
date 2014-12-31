package com.zdtx.ifms.specific.service.system;

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
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.system.PoiConf;
import com.zdtx.ifms.specific.vo.system.PoiConfVO;

@Service
@Transactional
public class PoiConfManager {
	
	@Autowired
	private BaseDao dao;

	public Page<PoiConf> getBatch(Page<PoiConf> page, PoiConfVO pcvo, Long[] coms) {
		Criteria criteria = dao.getSession().createCriteria(PoiConf.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.in("comid", coms));
		try {
			if (!Utils.isEmpty(pcvo.getCaption())) {
				criteria.add(Restrictions.ilike("caption", "%" + pcvo.getCaption().trim() + "%"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("caption"));
		orders.add(Order.asc("isvisible"));
		return dao.getBatch(page, criteria, orders);
	}
	
	/**
	 * 获取POI
	 * @param isFliter	是否增加可见过滤
	 * @param isVisible	是否可见（isFliter == true时有效）
	 * @return
	 */
	public List<PoiConf> getAll(boolean isFliter, String isVisible) {
		Criteria criteria = dao.getSession().createCriteria(PoiConf.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.in("comid", (Long[]) Struts2Util.getSession().getAttribute("userCom")));
		if (isFliter) {
			criteria.add(Restrictions.eq("isvisible", isVisible));
		}
		return dao.getAll(criteria);
	}
	
	public List<PoiConf> getAll(boolean b, String tem, Long id) {
		Criteria criteria = dao.getSession().createCriteria(PoiConf.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.not(Restrictions.eq("poiid", id)));
		if(b){
			criteria.add(Restrictions.eq("isvisible", tem));
		}
		return dao.getAll(criteria);
	}
	
	/**
	 * 验证数据是否重复
	 * @param comID 公司ID
	 * @param poiID		POI's ID
	 * @param poiName	POI's name
	 */
	@SuppressWarnings("rawtypes")
	public Boolean isDuplicate(Long comID, Long poiID, String poiName) {
		String sql = "";
		if(Utils.isEmpty(poiID)) {
			sql = "SELECT 1 FROM T_CORE_POI WHERE ISDELETE='F' AND COMID = " + comID + " AND CAPTION='" + poiName + "'";
		} else {
			sql = "SELECT 1 FROM T_CORE_POI WHERE ISDELETE='F' AND POIID <> " + poiID + " AND COMID = " + comID + " AND CAPTION = '" + poiName + "'";
		}
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
}