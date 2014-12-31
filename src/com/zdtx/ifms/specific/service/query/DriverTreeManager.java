package com.zdtx.ifms.specific.service.query;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.specific.model.query.DriverTree;



@Service
@Transactional
public class DriverTreeManager {
	@Autowired
	private BaseDao baseDao;

	public List<DriverTree> getTreeList(Long id) {
		List<DriverTree> treelist = new ArrayList<DriverTree>();
		if(id == 1L) {
			treelist = baseDao.execute("FROM DriverTree WHERE departmentid = 1 and driverid in ("+ Struts2Util.getSession().getAttribute("userComStr") + ")");
		} else {
			treelist = baseDao.execute("FROM DriverTree WHERE departmentid =" + id);
		}
		return treelist;
	}

	/**
	 * @title 搜索功能返回的特殊格式的字符串
	 * @param name
	 * @return
	 */
	public List<String> search(String name) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT DISTINCT T.driverid FROM V_TREE_DRIVER T WHERE 1=1 START WITH T.driverid IN "
				+ "(SELECT R.driverid FROM V_TREE_DRIVER R WHERE R.drivername LIKE '%" + name.trim()
				+ "%') CONNECT BY PRIOR T.departmentid = T.driverid ";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		for (Object obj : query.list()) {
			list.add("#" + obj.toString());
		}
		return list;
	}
	
}
