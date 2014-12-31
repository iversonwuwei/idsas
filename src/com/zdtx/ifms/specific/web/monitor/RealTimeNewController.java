package com.zdtx.ifms.specific.web.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.monitor.Last;
import com.zdtx.ifms.specific.model.system.PoiConf;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.service.monitor.RealTimeNewManager;
import com.zdtx.ifms.specific.service.system.PoiConfManager;
import com.zdtx.ifms.specific.service.vehicle.PoliceManager;
import com.zdtx.ifms.specific.vo.monitor.CamDeviceVO;
import com.zdtx.ifms.specific.vo.monitor.TargetVO;

public class RealTimeNewController extends URLSupport<Object> {

	private static final long serialVersionUID = 6895924653048247542L;

	@Autowired
	private RealTimeNewManager manager;
	@Autowired
	private PoiConfManager pcMgr;
	@Autowired
	private PoliceManager policeMgr;

	private List<PoiConf> poiList = new ArrayList<PoiConf>();
	private List<KeyAndValue> vehDevlist = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> policeDevList = new ArrayList<KeyAndValue>();
	private List<Last> lastPositionList = new ArrayList<Last>();
	private List<TargetVO> targetList = new ArrayList<TargetVO>();
	private  List<CamDeviceVO> camList = new ArrayList<CamDeviceVO>();
	private CamDeviceVO vo = new CamDeviceVO();
	
	private Long targetID;
	private String deviceName;

	public String index() {
		poiList = pcMgr.getAll(true, "T");	//获取所有当前公司权限下的POI，只取isVisible==true的数据
		lastPositionList = baseMgr.getAll(Last.class);	//获取所有vehicle/police最后下线前数据
		vehDevlist = baseMgr.getVehicleAndDeviceNameByAuthority2(getCurrentUser().getUserID());
		policeDevList = policeMgr.getPoliceDeviceNameList();
		Map<String, String> busmap = new HashMap<String, String>();
		if (vehDevlist != null && vehDevlist.size() != 0) {
			for (KeyAndValue k : vehDevlist) {
				busmap.put(k.getKey(), k.getValue());
			}
		}
		Struts2Util.getSession().setAttribute("busmap", busmap);
		String fleetStr = Utils.keysToString(baseMgr
				.getFleetByAuthority(getCurrentUser().getUserID()));
		targetList = policeMgr.getTargets(fleetStr);
		return "index";
	}

	public String vehip() {
		if (id != null) {
			VehcileView tv = baseMgr.get(VehcileView.class, id);
			if (tv != null && tv.getCctvip() != null) {
				Struts2Util.renderText(tv.getCctvip());
			} else {
				Struts2Util.renderText("noip");
			}
		} else {
			Struts2Util.renderText("noip");
		}
		return null;
	}

	public String openVehCamWin() {
		camList = manager.getVehCamList(targetID);
		int camCount = 0;
		if(camList != null) {
			camCount = camList.size();
		}
		if(camCount == 0) {
			Struts2Util.renderText("<script type='text/JavaScript'>alert('This vehicle has no camera!');</script>");
			return null;
		} else if(camCount == 1) {
			return "open1";
		} else if(camCount == 2) {
			return  "open2";
		} else if(camCount == 3 || camCount == 4) {
			return  "open4";
		} else {
			return  "open9";
		}
	}
	
	public String openPoliceCamWin() {
		return "open0";
	}
	
	@Override
	public Object getModel() {
		return null;
	}

	public List<PoiConf> getPoiList() {
		return poiList;
	}

	public void setPoiList(List<PoiConf> poiList) {
		this.poiList = poiList;
	}

	public List<Last> getLastPositionList() {
		return lastPositionList;
	}

	public void setLastPositionList(List<Last> lastPositionList) {
		this.lastPositionList = lastPositionList;
	}

	public List<KeyAndValue> getVehDevlist() {
		return vehDevlist;
	}

	public void setVehDevlist(List<KeyAndValue> vehDevlist) {
		this.vehDevlist = vehDevlist;
	}

	public List<KeyAndValue> getPoliceDevList() {
		return policeDevList;
	}

	public void setPoliceDevList(List<KeyAndValue> policeDevList) {
		this.policeDevList = policeDevList;
	}

	public List<TargetVO> getTargetList() {
		return targetList;
	}

	public void setTargetList(List<TargetVO> targetList) {
		this.targetList = targetList;
	}

	public List<CamDeviceVO> getCamList() {
		return camList;
	}

	public void setCamList(List<CamDeviceVO> camList) {
		this.camList = camList;
	}

	public Long getTargetID() {
		return targetID;
	}

	public void setTargetID(Long targetID) {
		this.targetID = targetID;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public CamDeviceVO getVo() {
		return vo;
	}

	public void setVo(CamDeviceVO vo) {
		this.vo = vo;
	}
}