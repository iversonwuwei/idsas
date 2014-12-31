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
import com.zdtx.ifms.specific.service.system.PoiConfManager;
import com.zdtx.ifms.specific.service.vehicle.PoliceManager;

public class RealTimegController extends URLSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8909191174630924420L;
	private List<Last> lasts=new ArrayList<Last>();
	@Autowired
	private PoiConfManager pcMgr;
	@Autowired
	private PoliceManager policeMgr;
	private List<PoiConf> poilist=new ArrayList<PoiConf>();
	private List<KeyAndValue> vehDevlist=new ArrayList<KeyAndValue>();
	private List<KeyAndValue> policeDevList = new ArrayList<KeyAndValue>();
	public String index(){
		lasts=baseMgr.getAll(Last.class);
		vehDevlist=baseMgr.getVehicleAndDeviceNameByAuthority2(getCurrentUser().getUserID());
		policeDevList = policeMgr.getPoliceDeviceNameList();
		Map<String, String> busmap=new HashMap<String, String>();
		if(vehDevlist!=null && vehDevlist.size()!=0){
			for(KeyAndValue k:vehDevlist){
				busmap.put(k.getKey(), k.getValue());
			}
		}
//		Struts2Util.getSession().setAttribute("busset", new HashSet<String>(baseMgr.getVehicleNameOnlyByAuthority()));
		Struts2Util.getSession().setAttribute("busmap", busmap);
		poilist=pcMgr.getAll(true, "T");
		return "index";
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
