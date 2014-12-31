package com.zdtx.ifms.specific.web.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.query.FenceLog;
import com.zdtx.ifms.specific.model.task.FenceDetail;
import com.zdtx.ifms.specific.model.task.FenceVehicle;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.service.task.FencingManager;
import com.zdtx.ifms.specific.service.task.FencqueryManager;
import com.zdtx.ifms.specific.vo.query.FencqueryVo;

public class FencqueryController extends URLSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5836315865229474434L;
	private VehcileView vehcileView = new VehcileView();
	private FencqueryVo fenVo=new  FencqueryVo();
	private List<KeyAndValue> vehlist=new ArrayList<KeyAndValue>();
	private List<FenceDetail> fenlist=new ArrayList<FenceDetail>();
	 private List<Object[]> listobj;
	@Autowired
	private FencingManager fenMgr;
	@Autowired
	private FencqueryManager fqMgr;
	public String index(){
		vehlist=baseMgr.getVehicleLicenseByAuthority(getCurrentUser().getUserID());
		return "index";
	}
	
	public String getFence() throws IOException {
		try {
			
	
		List<FenceVehicle> fvlist=fqMgr.getFenceVehicleByVehid(id);
		List<ArrayList<Object[]>> objlists=new ArrayList<ArrayList<Object[]>>();
		for(int j=0;j<fvlist.size();j++){
			List<FenceDetail> tempfds=fqMgr.getFenceDetailById(fvlist.get(j).getGeofencingid());
			ArrayList<Object[]> listobj2 = new ArrayList<Object[]>();
			for (int i = 0; i < tempfds.size(); i++) {
				Object[] obj = new Object[2];
				obj[0] = tempfds.get(i).getLatitude();
				obj[1] = tempfds.get(i).getLongitude();
				listobj2.add(obj);
			}
			if (null != listobj2 && listobj2.size() > 0) {
				Object[] obj1 = new Object[2];
				obj1[0] = tempfds.get(0).getLatitude();
				obj1[1] = tempfds.get(0).getLongitude();
				listobj2.add(obj1);
			}
			objlists.add(listobj2);
		}
		
	//	List<FenceDetail> fenlist = fqMgr.getFenceDetailByVehid(id);
//		listobj = new ArrayList<Object[]>();
//		for (int i = 0; i < fenlist.size(); i++) {
//			Object[] obj = new Object[2];
//			obj[0] = fenlist.get(i).getLatitude();
//			obj[1] = fenlist.get(i).getLongitude();
//			listobj.add(obj);
//		}
//		if (null != listobj && listobj.size() > 0) {
//			Object[] obj1 = new Object[2];
//			obj1[0] = fenlist.get(0).getLatitude();
//			obj1[1] = fenlist.get(0).getLongitude();
//			listobj.add(obj1);
//		}
		List<FenceLog> fencelog=fenMgr.getFenceLog(id,fenVo);
		Object[] objary=new Object[4];
		objary[0]=fencelog;
		objary[1]=objlists;
		objary[2]=getLine(fencelog);
		objary[3]=fenMgr.getGpsline(baseMgr.get(VehcileView.class, id).getVehiclename(),fenVo);
		JSONArray json = JSONArray.fromObject(objary);
		Utils.getResponse().getWriter().print(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<ArrayList<String[]>> getLine(List<FenceLog> fencelog){
		ArrayList<ArrayList<String[]>> list=new ArrayList<ArrayList<String[]>>();
		for(int i=0;i<fencelog.size();i++){
			String[] xy=new String[]{""+fencelog.get(i).getO_ptx(),""+fencelog.get(i).getO_pty()};
			if(fencelog.get(i).getO_curstate()==1){
				list.add(new ArrayList<String[]>());
			}
			if(list.size()==0){
				list.add(new ArrayList<String[]>());
			}
			if(fencelog.get(i).getO_curstate()==2){
				list.get(list.size()-1).add(xy);
			}
		}
		return list;
	}
	
	
	
	@Override
	public Object getModel() {
		return null;
	}
	public VehcileView getVehcileView() {
		return vehcileView;
	}
	public void setVehcileView(VehcileView vehcileView) {
		this.vehcileView = vehcileView;
	}
	public FencqueryVo getFenVo() {
		return fenVo;
	}
	public void setFenVo(FencqueryVo fenVo) {
		this.fenVo = fenVo;
	}
	public List<KeyAndValue> getVehlist() {
		return vehlist;
	}
	public void setVehlist(List<KeyAndValue> vehlist) {
		this.vehlist = vehlist;
	}


	public List<FenceDetail> getFenlist() {
		return fenlist;
	}


	public void setFenlist(List<FenceDetail> fenlist) {
		this.fenlist = fenlist;
	}

	public List<Object[]> getListobj() {
		return listobj;
	}

	public void setListobj(List<Object[]> listobj) {
		this.listobj = listobj;
	}
	
}
