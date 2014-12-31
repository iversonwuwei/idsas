/**
 * @File: IpCamManager.java
 * @path: idsas - com.zdtx.ifms.specific.service.monitor
 */
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
import com.zdtx.ifms.specific.model.system.CamModel;

/**
 * @ClassName: CamModelManager
 * @Description: system-IP Camera Model-manager
 * @author: Leon Liu
 * @date: 2013-7-24 下午2:37:49
 * @version V1.0
 */
@Service
@Transactional
public class CamModelManager {

	@Autowired
	private BaseDao dao;
	
	public Page<CamModel> getBatch(Page<CamModel> page, String modelName) {
		Criteria criteria = dao.getSession().createCriteria(CamModel.class);
		criteria.add(Restrictions.eq("isDelete", "F"));
		if (!Utils.isEmpty(modelName)) {
			criteria.add(Restrictions.ilike("modelName", "%" + modelName + "%"));
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("modelName"));
		return dao.getBatch(page, criteria, orders);
	}
	
	/**
	 * check modelName duplicate
	 * @param modelID
	 * @param modelName
	 * @return	true: duplicate;	false:unique;
	 */
	public boolean isDuplicate(Long modelID, String modelName) {
		String sql = "";
		if(modelID == null) {
			sql = "SELECT 1 AS KEY, 1 AS VALUE FROM T_CORE_CAMERA_MODEL WHERE ISDELETE = 'F' AND MODEL = '" + modelName + "'";
		} else {
			sql = "SELECT 1 AS KEY, 1 AS VALUE FROM T_CORE_CAMERA_MODEL WHERE ISDELETE = 'F' AND MODELID <> " + modelID + " AND MODEL = '" + modelName + "'";
		}
		List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
		if(list != null && list.size() != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * check if data can be deleted
	 * @return		true: can;	false:can not;
	 */
	public boolean checkDelete(Long modelID) {
		String sql = "SELECT 1 AS KEY, 1 AS VALUE FROM T_CORE_CAMERA WHERE ISDELETE = 'F' AND MODELID = " + modelID;
		List<KeyAndValue> list = dao.getKeyAndValueBySQL(sql);
		if(list != null && list.size() != 0) {
			return false;
		} else {
			return true;
		}
	}
}