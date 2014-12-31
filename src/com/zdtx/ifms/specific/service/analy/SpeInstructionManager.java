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
import com.zdtx.ifms.specific.model.analy.SpeInstruction;
import com.zdtx.ifms.specific.vo.profile.CounsellVo;

@Service
@Transactional
public class SpeInstructionManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<SpeInstruction> getBatch(Page<SpeInstruction> page, CounsellVo deVo) {
		Criteria criteria =  baseDao.getSession().createCriteria(SpeInstruction.class);

		if (!Utils.isEmpty(deVo.getUname())) { 
			criteria.add(Restrictions.like("licenseplate",
					"%" + deVo.getUname().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getCode())) { 
			criteria.add(Restrictions.eq("code",
					deVo.getCode() ));
		}
		if (!Utils.isEmpty(deVo.getUsername())) { 
			criteria.add(Restrictions.like("creater",
					"%" + deVo.getUsername().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getTimeMin())) {
			criteria.add(Restrictions.ge("creatime", deVo.getTimeMin()));
		}
		if (!Utils.isEmpty(deVo.getTimeMax())) {
			criteria.add(Restrictions.le("creatime", deVo.getTimeMax()));
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("creatime"));
		orders.add(Order.asc("licenseplate"));
		orders.add(Order.asc("code"));
		return baseDao.getBatch(page, criteria, orders);
	}
	public List<SpeInstruction>  getSpeInstruction(Long id,Long vehid,String code){
		if(null != id){
			return  baseDao.execute("FROM SpeInstruction WHERE vehicleid="+vehid+" and success not in ('T','K','P','L') and code='"+code+"' and  specialid !=" + id);
		} else {
			return  baseDao.execute("FROM SpeInstruction WHERE vehicleid="+vehid+" and code='"+code+"' and success not in ('T','K','P','L')");
		}
		
	}
	
}
