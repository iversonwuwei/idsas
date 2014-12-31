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
import com.zdtx.ifms.specific.model.authority.User;
import com.zdtx.ifms.specific.model.system.VehBrand;

@Service
@Transactional
public class VehicleBrandManager {
	@Autowired
	private BaseDao baseDao;

	public Page<VehBrand> getBatch(Page<VehBrand> page,Long[] coms) {
		Criteria criteria = baseDao.getSession().createCriteria(VehBrand.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.in("comid", coms));
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("name"));// name
		orders.add(Order.asc("memo"));// memo
		return baseDao.getBatch(page, criteria, orders);
	}

	public String checkRepeat(String name,Long id) {
		List<?> list = baseDao.execute("FROM VehBrand V WHERE V.isdelete = 'F' AND V.comid="+id+" AND V.name='" + name + "'");
		return String.valueOf(list.size());
	}

	public List<KeyAndValue> getVehicleTypeList(User u) {
		//comid
		if(u.getUserRole().getAflag().equals("0")){
			String sql = "SELECT T.ID AS KEY, T.name AS VALUE FROM T_CORE_VEH_brand T WHERE     T.ISDELETE = 'F' ORDER BY T.TYPE";
			return baseDao.getKeyAndValueBySQL(sql);
		}else{
			String sql = "SELECT T.ID AS KEY, T.name AS VALUE FROM T_CORE_VEH_brand T WHERE  t.comid="+u.getUserOrg().getOrgID()+"  and    T.ISDELETE = 'F' ORDER BY T.TYPE";
			return baseDao.getKeyAndValueBySQL(sql);
			
		}
	}

	
	/**
	 * 验证车辆类型是否在用
	 * @param typeid
	 * @return
	 */
	public Integer checkTypeUsed(Long typeid){
		return baseDao.execute("FROM Vehcile T WHERE T.isdelete = 'F' AND T.brandid = " + typeid).size();
	}
}
