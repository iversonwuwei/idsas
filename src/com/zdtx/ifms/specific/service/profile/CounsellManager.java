package com.zdtx.ifms.specific.service.profile;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.profile.CounsellView;
import com.zdtx.ifms.specific.vo.profile.CounsellVo;

@Service
@Transactional
public class CounsellManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<CounsellView> getBatch(Page<CounsellView> page, CounsellVo deVo) {
		Criteria criteria =  baseDao.getSession().createCriteria(CounsellView.class);
		criteria.add(Restrictions.in("departid", (Long[])Struts2Util.getSession().getAttribute("userDepartment")));	//add authority
		if (!Utils.isEmpty(deVo.getDerid())&&-1L != deVo.getDerid()) { 
			criteria.add(Restrictions.eq("driverid",deVo.getDerid()));
		}
		if (!Utils.isEmpty(deVo.getUid())&&-1L != deVo.getUid()) { 
			criteria.add(Restrictions.eq("departid",deVo.getUid()));
		}
		if (!Utils.isEmpty(deVo.getOid())&&-1L != deVo.getOid()) { 
			criteria.add(Restrictions.eq("userid",deVo.getOid()));
		}
		if (!Utils.isEmpty(deVo.getUname())) { 
			criteria.add(Restrictions.like("username",
					"%" + deVo.getUname().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getDname())) { 
			criteria.add(Restrictions.like("drivername",
					"%" + deVo.getDname() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getStarttime())) {
			criteria.add(Restrictions.ge("startime",
					deVo.getStarttime()));
		}
		if (!Utils.isEmpty(deVo.getEndtime())) {
			criteria.add(Restrictions.le("endtime",
					deVo.getEndtime()));
		}
		criteria.add(Restrictions.eq("isdelete","F"));
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("startime"));
		orders.add(Order.asc("username"));
		orders.add(Order.asc("drivername"));
		return baseDao.getBatch(page, criteria, orders);
	}

	/**
	 * 根据司机ID获取所属部门ID和名称
	 * @param driverID	司机ID
	 * @return	KeyAndValue key:部门ID,value:部门名称
	 */
	public KeyAndValue getDepartmentByDriver(Long driverID) {
		String sql = "SELECT ORG_ID AS KEY, ORGNAME AS VALUE FROM T_CORE_ORG A WHERE A.ORG_ID = (SELECT B.DEPARTMENTID FROM T_CORE_DRIVER B WHERE DRIVERID = " + driverID + ")";
		List<KeyAndValue> dataList = baseDao.getKeyAndValueBySQL(sql);
		if(dataList != null && dataList.size() > 0) {
			return dataList.get(0);
		} else {
			return null;
		}
	}
	
}