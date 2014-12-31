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
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.analy.FaultDict;
import com.zdtx.ifms.specific.vo.profile.CounsellVo;

@Service
@Transactional
public class FaultDictManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<FaultDict> getBatch(Page<FaultDict> page, CounsellVo deVo) {
		Criteria criteria =  baseDao.getSession().createCriteria(FaultDict.class);

		if (!Utils.isEmpty(deVo.getUname())) { 
			criteria.add(Restrictions.like("code",
					"%" + deVo.getUname().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getCode())) { 
			criteria.add(Restrictions.like("engdefinition",
					"%" + deVo.getCode().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getDname())) { 
			criteria.add(Restrictions.like("engscope",
					"%" + deVo.getDname().trim() + "%").ignoreCase());
		}
		criteria.add(Restrictions.eq("isdelete","F"));
		
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("code"));
		return baseDao.getBatch(page, criteria, orders);
	}
	public Boolean checkAdd(String name) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM FaultDict WHERE code = '" + name
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
					"FROM FaultDict WHERE code = '" + name
							+ "' and faultcodeid <> "+ userID +" AND isdelete='F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	} 
 
	
	/***
	 *  根据车辆id获得实体类list
	 * @param id
	 * @return
	 */
	public List<KeyAndValue> getDitList() {
			String sql = "SELECT code AS KEY,engdefinition AS VALUE FROM t_core_faultcode where isdelete='F' and code in ('1001','1002') ";
			return baseDao.getKeyAndValueBySQL(sql);
		
	}
	
	/***
	 *  根据车辆id获得实体类list
	 * @param id
	 * @return
	 */
	public List<KeyAndValue> getDitList1() {
			String sql = "SELECT code AS KEY,engdefinition AS VALUE FROM t_core_faultcode where isdelete='F' and code in ('1001') ";
			return baseDao.getKeyAndValueBySQL(sql);
		
	}
	
	public List<FaultDict> getBatch() {
		Criteria criteria =  baseDao.getSession().createCriteria(FaultDict.class);
		criteria.add(Restrictions.eq("code","1002"));
		criteria.add(Restrictions.eq("isdelete","F"));
		return baseDao.getAll(criteria);
	}
	
	
}
