/**
 * @path com.zdtx.ifms.specific.service.monitor
 * @file RealTimeNewManager.java
 */
package com.zdtx.ifms.specific.service.monitor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.monitor.CamDeviceVO;

/**
 * @description 
 * @author Liu Jun
 * @since 2014年8月15日 上午11:50:41
 */
@Service
@Transactional
public class RealTimeNewManager {

	@Autowired
	private BaseDao dao;
	
	/**
	 * 根据设备ID，获取设备通道数
	 * @param deviceID
	 * @return
	 */
	public Integer getDeviceChannelCount(Long deviceID) {
		if(Utils.isEmpty(deviceID)) {
			return null;
		}
		String sql = "SELECT COUNT(1) AS KEY, 1 AS VALUE FROM T_CORE_CAMERA_DEVICE "
				+ "WHERE DEVICEID = " + deviceID;
		List<KeyAndValue> dataList = dao.getKeyAndValueBySQL(sql);
		if(dataList == null || dataList.size() == 0) {
			return 0;
		} else {
			return Integer.valueOf(dataList.get(0).getKey());
		}
	}
	
	/**
	 * 根据车辆ID，获取设备对应摄像头以及通道号
	 * @param vehicleID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CamDeviceVO> getVehCamList(Long vehicleID) {
		if (null == vehicleID) {
			return null;
		}
		String sql = "SELECT A.CCTVIP AS CAMIP, A.DEVICEID AS DEVICEID, B.CAMERAID AS CAMID, B.CHANNEL AS CHANNEL, "
							+ " (554 + B.CHANNEL - 1) AS CAMPORT, D.TYPE AS CAMTYPE, "
							+ "C.ADMINNAME AS USERNAME, C.ADMINPASS AS PASSWORD "
							+ "FROM T_CORE_VEHICLE A "
							+ "LEFT JOIN T_CORE_CAMERA_DEVICE B ON B.DEVICEID = A.DEVICEID "
							+ "LEFT JOIN T_CORE_CAMERA C ON C.CAMERAID = B.CAMERAID "
							+ "LEFT JOIN T_CORE_CAMERA_MODEL D ON C.MODELID = D.MODELID "
							+ "WHERE VEHICLEID = " + vehicleID;
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("deviceID", LongType.INSTANCE);
		query.addScalar("camID", LongType.INSTANCE);
		query.addScalar("camIP", StringType.INSTANCE);
		query.addScalar("channel", IntegerType.INSTANCE);
		query.addScalar("camPort", LongType.INSTANCE);
		query.addScalar("camType", IntegerType.INSTANCE);
		query.addScalar("userName", StringType.INSTANCE);
		query.addScalar("password", StringType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(CamDeviceVO.class));
		return (List<CamDeviceVO>) query.list();
	}
	
	/**
	 * 
	 * @param policeID
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public CamDeviceVO getPoliceCam(Long policeID) {
		if (null == policeID) {
			return null;
		}
		String sql = "SELECT A.CCTVIP AS CAMIP, A.DEVICEID AS DEVICEID, B.CAMERAID AS CAMID, B.CHANNEL AS CHANNEL, "
				+ " (554 + B.CHANNEL - 1) AS CAMPORT, D.TYPE AS CAMTYPE, "
				+ "C.ADMINNAME AS USERNAME, C.ADMINPASS AS PASSWORD "
				+ "FROM T_CORE_VEHICLE A "
				+ "LEFT JOIN T_CORE_CAMERA_DEVICE B ON B.DEVICEID = A.DEVICEID "
				+ "LEFT JOIN T_CORE_CAMERA C ON C.CAMERAID = B.CAMERAID "
				+ "LEFT JOIN T_CORE_CAMERA_MODEL D ON C.MODELID = D.MODELID "
				+ "WHERE VEHICLEID = " + policeID;
		List<CamDeviceVO> list = new ArrayList<CamDeviceVO>();
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("deviceID", LongType.INSTANCE);
		query.addScalar("camID", LongType.INSTANCE);
		query.addScalar("camIP", StringType.INSTANCE);
		query.addScalar("channel", IntegerType.INSTANCE);
		query.addScalar("camPort", LongType.INSTANCE);
		query.addScalar("camType", IntegerType.INSTANCE);
		query.addScalar("userName", StringType.INSTANCE);
		query.addScalar("password", StringType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(CamDeviceVO.class));
		list = (List<CamDeviceVO>) query.list();
		if(list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}
}
