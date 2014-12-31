package com.zdtx.ifms.specific.service.analy;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.analy.Phenomenon;
import com.zdtx.ifms.specific.vo.analy.PhenomenonVO;

@Service
@Transactional
public class PhenomenonManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<Phenomenon> getPage(Page<Phenomenon> page, PhenomenonVO pheVo) {
		List<Order> orderList = new ArrayList<Order>();
		Criteria criteria = baseDao.getSession().createCriteria(Phenomenon.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		if (!Utils.isEmpty(pheVo.getDrivername())) {
			criteria.add(Restrictions.ilike("drivername", "%" + pheVo.getDrivername().trim() + "%"));
		}
		if (!Utils.isEmpty(pheVo.getVehiclename())) {
			criteria.add(Restrictions.ilike("licenseplate", "%" + pheVo.getVehiclename().trim() + "%"));
		}
		if (!Utils.isEmpty(pheVo.getIsremove()) && !"-1".equals(pheVo.getIsremove())) {
			criteria.add(Restrictions.eq("isremove", pheVo.getIsremove()));
		}
		orderList.add(Order.asc("isremove"));
		orderList.add(Order.asc("licenseplate"));
		return baseDao.getBatch(page, criteria, orderList);
	}
}













