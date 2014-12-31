/**
 * @path com.zdtx.ifms.specific.service.monitor
 * @file PolicePlaybackManager.java
 */
package com.zdtx.ifms.specific.service.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;

/**
 * @description monitor-police playback-manager
 * @author Liu Jun
 * @since 2014年6月19日 上午10:23:57
 */
@Service
@Transactional
public class PolicePlaybackManager {

	@Autowired
	private BaseDao dao;
	
	/**
	 * 获取权限内的已绑定单兵设备的police
	 * @return List<KeyAndValue> key：deviceName；value：policeName
	 */
	public List<KeyAndValue> getDeviceBoundedPolice() {
		String sql = "SELECT B.O_DEVICENAME AS KEY, A.POLICENAME AS VALUE"
							+ " FROM T_CORE_POLICE A, T_CORE_UNITE B"
							+ "  WHERE A.ISDELETE = 'F'"
							+ " AND A.POLICEID = B.O_BUSID"
							+ " AND B.O_UNITTYPE = '2'"
							+ " AND A.FLEETID IN (" + Struts2Util.getSession().getAttribute("userFleetStr") + ")";
		return dao.getKeyAndValueBySQL(sql);
	}
}
