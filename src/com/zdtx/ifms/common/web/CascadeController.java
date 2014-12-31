package com.zdtx.ifms.common.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;

/**
 * 级联菜单控制层
 * @author zdtx_liujun
 * @since 2013-3-22 15:56:55
 */
public class CascadeController extends URLSupport<Object> {

	private static final long serialVersionUID = -476163397725797524L;

	@Autowired
	private BaseManager baseMgr;
	private List<KeyAndValue> dataList = new ArrayList<KeyAndValue>();
	private String comID;
	private String teamID;
	private String lineID;
	
	/**
	 * 获得权限下机构
	 * @return JSON
	 */
	public String initCom() {
		dataList = baseMgr.getDepartByAuthority(getCurrentUser().getUserID());
		return setJSON(dataList);
	}
	
	/**
	 * 根据机构获得车队
	 * @return JSON
	 */
	public String getTeamByCom() {
		if(Long.valueOf(teamID) == -1L) {
			dataList = baseMgr.getFleetByAuthority(getCurrentUser().getUserID());	
		} else {
			dataList = baseMgr.getFleetByDepartment(Long.valueOf(teamID));
		}
		return setJSON(dataList);
	}
	
	/**
	 * 根据机构获得车辆牌照号
	 * @return JSON
	 */
	public String getVehByOrg() {
		if(Long.valueOf(teamID) == -1L) {
			dataList = baseMgr.getVehicleLicenseByAuthority(getCurrentUser().getUserID());
		} else {
			dataList = baseMgr.getVehicleLicenseByFleet(Long.valueOf(teamID));
		}
		return setJSON(dataList);
	}
	
	/**
	 * 根据机构获得车辆名称（自编号）
	 * @return JSON
	 */
	public String getVehNameByOrg() {
		if(Long.valueOf(teamID) == -1L) {
			dataList = baseMgr.getVehicleNameByAuthority(getCurrentUser().getUserID());
		} else {
			dataList = baseMgr.getVehicleNameByFleet(Long.valueOf(teamID));
		}
		return setJSON(dataList);
	}
	
	/**
	 * 根据机构获得司机
	 * @return JSON
	 */
	public String getDriverByOrg() {
		if(Long.valueOf(comID) == -1L) {
			dataList = baseMgr.getDriverByAuthority(getCurrentUser().getUserID());
		} else {
			dataList = baseMgr.getDriverByDepart(Long.valueOf(comID));	//根据department获得driver
		}
		return setJSON(dataList);
	}
	
	/**
	 * 根据输入内容，获得车辆自编号，取前10条
	 * @return JSON
	 */
	public String catchVehicles() {
		List<KeyAndValue> dataList = new ArrayList<KeyAndValue>();
		dataList = baseMgr.getVehicleByFilter(Utils.getParameter("text_veh"));
		return setJSON(dataList);
	}

	@Override
	public Object getModel() {
		return null;
	}

	public String getComID() {
		return comID;
	}

	public void setComID(String comID) {
		this.comID = comID;
	}

	public String getTeamID() {
		return teamID;
	}

	public void setTeamID(String teamID) {
		this.teamID = teamID;
	}

	public String getLineID() {
		return lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}
}