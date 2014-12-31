package com.zdtx.ifms.specific.service.system;

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
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.system.ThresSet;
import com.zdtx.ifms.specific.vo.system.ThresSetVo;

/**
 * @ClassName: ThresSetManager
 * @Description: System Settings-Threshold Setting-Manager
 * @author JiangHaiquan
 * @date 2013-05-03 20:58:40
 * @version V1.0
 */

@Service
@Transactional
public class ThresSetManager {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private VehicleTypeManager typeMgr;

	public List<ThresSet> getBetch(ThresSetVo tsvo, List<KeyAndValue> vehtype) {
		Criteria criteria = baseDao.getSession().createCriteria(ThresSet.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		try {
			if (Utils.isEmpty(tsvo.getVehicletypeid())
					&& (vehtype != null && vehtype.size() != 0)) {
				tsvo.setVehicletypeid(Long.valueOf(vehtype.get(0).getKey()));
			}
			criteria.add(Restrictions.eq("vehicletypeid",
					tsvo.getVehicletypeid()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("typeid"));
		if (null != orders) {
			for (Order order : orders) {
				criteria.addOrder(order);
			}
		}
		return baseDao.getAll(criteria);
	}

	public List<KeyAndValue> getTypeList() {
		String sql = "SELECT DICT_ID AS KEY,DICTNAME AS VALUE FROM T_CORE_DICT WHERE ISDELETE='F' AND SORT_ID=201000";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public List<ThresSet> findtset(Long typeid) {
		if (typeid == null) {
			return null;
		}
		Criteria criteria = baseDao.getSession().createCriteria(ThresSet.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.eq("vehicletypeid", typeid));
		return baseDao.getAll(criteria);
	}
}
