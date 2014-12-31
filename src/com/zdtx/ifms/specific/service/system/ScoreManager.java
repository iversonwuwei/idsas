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
import com.zdtx.ifms.specific.model.authority.User;
import com.zdtx.ifms.specific.model.system.Score;
import com.zdtx.ifms.specific.vo.system.ScoreVO;

@Service
@Transactional
public class ScoreManager {

	@Autowired
	private BaseDao baseDao;
	
	@Autowired
	private VehicleTypeManager vehtypMgr;
	
	public Page<Score> getPage(Page<Score> page, Long vehicleType) {
		List<Order> orderList = new ArrayList<Order>();
		Criteria criteria = baseDao.getSession().createCriteria(Score.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		if (!Utils.isEmpty(vehicleType) && -1 != vehicleType) {
			criteria.add(Restrictions.eq("vehicletypeid", vehicleType));
		}
		orderList.add(Order.asc("vehicletype"));
		orderList.add(Order.asc("typeid"));
		return baseDao.getBatch(page, criteria, orderList);
	}
	
	public List<KeyAndValue> getSortList() {
		String sql = "SELECT T.DICT_ID AS KEY, T.DICTNAME AS VALUE FROM T_CORE_DICT T";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public Boolean checkTypeAndSort(String sortId, String vehTypeId) {
		String hql = "From Score s where s.isdelete = 'F' and s.typeid = " + sortId + " and s.vehicletypeid = " + vehTypeId;
		return baseDao.execute(hql).size() > 0 ? true : false;
	}

	public String saveSco(List<Score> scoreList, User currentUser, Boolean istrue) {
		for (int i = 0; i < scoreList.size(); i++) {
			Score sco = baseDao.get(Score.class, scoreList.get(i).getScoreid());
			sco.setMaxcount(scoreList.get(i).getMaxcount());
			sco.setDeductpoints(scoreList.get(i).getDeductpoints());
			sco.setMaxtime(scoreList.get(i).getMaxtime());
			sco.setDeductime(scoreList.get(i).getDeductime());
			if (istrue == true) { //当权重合计等于100时才保存权重的值
				sco.setWeight(scoreList.get(i).getWeight());
			}
			baseDao.save(sco);
		}
	/*	int a = 0;
		Connection conn = baseDao.getSession().connection();
		CallableStatement cs = null;
		try {
			conn.setAutoCommit(false);
			cs = conn.prepareCall("{call UP_TYPE_SCORE.P_SCORE_MAIN(?,?,?,?)}");
			cs.setLong(1, sco.getVehicletypeid());
			cs.setLong(2, sco.getTypeid());
			if ("".equals(sco.getWeight()) || null == sco.getWeight()) {
				cs.setLong(3, 0);
			} else {
				cs.setLong(3, sco.getWeight());
			}
			cs.registerOutParameter(4, OracleTypes.INTEGER);
			cs.executeQuery();
			a = cs.getInt(4);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				cs.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (1 == a) {
			sco.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			sco.setCreater(currentUser.getUserName());
			sco.setIsdelete("F");
			baseDao.save(sco);
		}*/
		return null;
	}

	public List<Score> getScoreList(ScoreVO scoVo,List<KeyAndValue> vehtype) {
		Criteria criteria = baseDao.getSession().createCriteria(Score.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		if (Utils.isEmpty(scoVo.getVehId())  && (vehtype!=null && vehtype.size()!=0)) {
			scoVo.setVehId(Long.parseLong(vehtype.get(0).getKey()));
		}
		criteria.add(Restrictions.eq("vehicletypeid", scoVo.getVehId()));
		criteria.add(Restrictions.ne("typeid", 18L));
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.asc("typeid"));
		if (null != orderList) {
			for (Order order : orderList) {
				criteria.addOrder(order);
			}
		}
		return baseDao.getAll(criteria);
	}
}