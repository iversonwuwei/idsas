package com.zdtx.ifms.specific.service.system;

import java.io.File;
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
import com.zdtx.ifms.specific.model.system.VehType;

@Service
@Transactional
public class VehicleTypeManager {
	@Autowired
	private BaseDao baseDao;

	public Page<VehType> getBatch(Page<VehType> page,Long[] coms) {
		Criteria criteria = baseDao.getSession().createCriteria(VehType.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.in("comid", coms));
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("type"));// typeName
		orders.add(Order.asc("description"));// typeMemo
		return baseDao.getBatch(page, criteria, orders);
	}

	public String checkRepeat(String name,Long id) {
		List<?> list = baseDao.execute("FROM VehType V WHERE V.isdelete = 'F' AND V.comid="+id+" AND V.type='" + name + "'");
		return String.valueOf(list.size());
	}

	public List<KeyAndValue> getVehicleTypeList(User u) {
		//comid
		if(u.getUserRole().getAflag().equals("0")){
			String sql = "SELECT T.TYPEID AS KEY, T.TYPE AS VALUE FROM T_CORE_VEH_TYPE T WHERE T.ISDELETE = 'F' ORDER BY T.TYPE";
			return baseDao.getKeyAndValueBySQL(sql);
		}else{
			String sql = "SELECT T.TYPEID AS KEY, T.TYPE AS VALUE FROM T_CORE_VEH_TYPE T WHERE  t.comid = "+u.getUserOrg().getOrgID()+" and T.ISDELETE = 'F' ORDER BY T.TYPE";
			return baseDao.getKeyAndValueBySQL(sql);
		}
	}

	/**
	 * 删除没用的图片
	 * 
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	public void deleteOldImages(String path) {
		// 获得表中全部的图片名
		List<String> iconList = baseDao.getSession().createSQLQuery("SELECT T.ICON FROM T_CORE_VEH_TYPE T WHERE T.ICON IS NOT NULL").list();
		// 获得图片保存路径
		File dir = new File(path);
		if (null != dir) {
			// 获得里面所有文件
			File fileList[] = dir.listFiles();
			for (File file : fileList) {
				// 如果文件不被包含于表中，视为没用的文件，删除
				if (!iconList.contains(file.getName())) {
					file.delete();
				}
			}
		}
	}
	/**
	 * 验证车辆类型是否在用
	 * @param typeid
	 * @return
	 */
	public Integer checkTypeUsed(Long typeid){
		return baseDao.execute("FROM Vehcile T WHERE T.isdelete = 'F' AND T.typeid = " + typeid).size();
	}
}
