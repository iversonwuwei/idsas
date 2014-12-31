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
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.system.DeviceStatus;
import com.zdtx.ifms.specific.vo.system.DeviceVo;

@Service
@Transactional
public class DeviceStatusManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<DeviceStatus> getBatch(Page<DeviceStatus> page, DeviceVo deVo) {
		Criteria criteria =  baseDao.getSession().createCriteria(DeviceStatus.class);
		if (!Utils.isEmpty(deVo.getName())) { 
			criteria.add(Restrictions.like("statusname",
					"%" + deVo.getName().trim() + "%").ignoreCase());
		}
		criteria.add(Restrictions.eq("isdelete","F"));
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("statusname"));
		return baseDao.getBatch(page, criteria, orders);
	}
	
	public Boolean checkAdd(String name) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM DeviceStatus WHERE statusname = '" + name
							+ "' AND isdelete='F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}
	public Boolean checkEdit( String userID,String name) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM DeviceStatus WHERE statusname = '" + name
							+ "' and statusid <> "+ userID +" AND isdelete='F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}
	
	public List<KeyAndValue> getList(){
		String sql="select o.statusid as key,o.statusname as value from T_CORE_DEVICESTATUS o  " +
				"where o.isdelete='F' ";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	public Boolean checkDel(Long id){//true 设备在车上
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM DeviceList WHERE statusid = " + id).size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}
}
