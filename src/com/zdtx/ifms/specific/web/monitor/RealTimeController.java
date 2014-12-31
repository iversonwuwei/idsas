package com.zdtx.ifms.specific.web.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.monitor.Last;
import com.zdtx.ifms.specific.model.system.PoiConf;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.service.system.PoiConfManager;
import com.zdtx.ifms.specific.service.vehicle.PoliceManager;

public class RealTimeController extends URLSupport<Object> {

	private static final long serialVersionUID = 6895924653048247542L;
	
	@Autowired
	private PoiConfManager pcMgr;
	@Autowired
	private PoliceManager policeMgr;
	private List<PoiConf> poilist=new ArrayList<PoiConf>();
	private List<KeyAndValue> vehDevlist=new ArrayList<KeyAndValue>();
	private List<KeyAndValue> policeDevList = new ArrayList<KeyAndValue>();
	private List<Last> lasts=new ArrayList<Last>();
	public String index() {
		lasts=baseMgr.getAll(Last.class);
		vehDevlist=baseMgr.getVehicleAndDeviceNameByAuthority2(getCurrentUser().getUserID());
		policeDevList = policeMgr.getPoliceDeviceNameList();
//		vehDevlist=baseMgr.getVehicleAndDeviceNameByAuthority();
		Map<String, String> busmap=new HashMap<String, String>();
		if(vehDevlist!=null && vehDevlist.size()!=0){
			for(KeyAndValue k:vehDevlist) {
				busmap.put(k.getKey(), k.getValue());
			}
		}
//		Struts2Util.getSession().setAttribute("busset", new HashSet<String>(baseMgr.getVehicleNameOnlyByAuthority()));
		Struts2Util.getSession().setAttribute("busmap", busmap);
		poilist=pcMgr.getAll(true, "T");
		return "index";
	}
	public String vehip(){
		if(id!=null){
			VehcileView tv=baseMgr.get(VehcileView.class, id);
			if(tv!=null && tv.getCctvip()!=null){
				Struts2Util.renderText(tv.getCctvip());
			}else{
				Struts2Util.renderText("noip");
			}
		}else{
			Struts2Util.renderText("noip");
		}
		return null;
	}
	@Override
	public Object getModel() {
		return null;
	}

	public List<PoiConf> getPoilist() {
		return poilist;
	}

	public void setPoilist(List<PoiConf> poilist) {
		this.poilist = poilist;
	}

	public List<KeyAndValue> getVehDevlist() {
		return vehDevlist;
	}

	public void setVehDevlist(List<KeyAndValue> vehDevlist) {
		this.vehDevlist = vehDevlist;
	}
	public List<Last> getLasts() {
		return lasts;
	}
	public void setLasts(List<Last> lasts) {
		this.lasts = lasts;
	}
	public List<KeyAndValue> getPoliceDevList() {
		return policeDevList;
	}
	public void setPoliceDevList(List<KeyAndValue> policeDevList) {
		this.policeDevList = policeDevList;
	}
	
}
