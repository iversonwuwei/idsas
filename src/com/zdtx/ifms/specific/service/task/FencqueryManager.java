package com.zdtx.ifms.specific.service.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.specific.model.task.FenceDetail;
import com.zdtx.ifms.specific.model.task.FenceVehicle;

@Service
@Transactional
public class FencqueryManager {
	@Autowired
	private BaseDao baseDao;
	
	public List<FenceDetail>  getFenceDetailByVehid(Long id){
		return  baseDao.execute("FROM FenceDetail f WHERE  f.geofencingid in (Select b.geofencingid From FenceVehicle b where b.vehicleid="+id+")  order by f.latiid asc");
	}
	public List<FenceVehicle>  getFenceVehicleByVehid(Long id){
		return  baseDao.execute("From FenceVehicle b where b.vehicleid="+id+"");
	}
	
	public List<FenceDetail>  getFenceDetailById(Long id){
		String hql="FROM FenceDetail f WHERE  f.geofencingid = "+id+"  order by f.latiid asc";
		return  baseDao.execute(hql);
	}
	
}
