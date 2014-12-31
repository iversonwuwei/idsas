package com.zdtx.ifms.common.web;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;

/**
 * 多选车辆控件控制层
 * @author zdtx_liujun
 * @since 2013-3-8 9:59:59
 */
public class VehSelectController extends URLSupport<Object> {

	private static final long serialVersionUID = -476163397725797524L;

	@Autowired
	private BaseManager baseMgr;
	
	/**
	 * 根据输入内容，获得车辆自编号，取前10条
	 * @return JSON
	 */
	public String catchVehicles() {
		List<KeyAndValue> vehicleList = new ArrayList<KeyAndValue>();
		try {
			vehicleList = baseMgr.getVehicleByFilter(Utils.getParameter("text_veh"));
			if (0 != vehicleList.size()) {
				jsonArray = JSONArray.fromObject(vehicleList);
			} else {
				jsonObject.put("result", "empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json";
	}

	/**
	 * 根据车队，获得车辆
	 * @return JSON
	 */
	public String getVehicles() {
		List<KeyAndValue> vehicleList = new ArrayList<KeyAndValue>();
		try {
			vehicleList = baseMgr.getVehicleLicenseByFleet(Long.valueOf(Utils.getParameter("orgid")));
			if (0 != vehicleList.size()) {
				jsonArray = JSONArray.fromObject(vehicleList);
			} else {
				jsonObject.put("result", "empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json";
	}
	
	/**
	 * 获得权限下可控车队
	 * @return JSON
	 */
	public String getOrgs() {
		List<KeyAndValue> orgList = new ArrayList<KeyAndValue>();
		try {
			orgList = baseMgr.getFleetByAuthority(getCurrentUser().getUserID());
			if (0 != orgList.size()) {
				jsonArray = JSONArray.fromObject(orgList);
			} else {
				jsonObject.put("result", "empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json";
	}

	@Override
	public Object getModel() {
		return null;
	}
}