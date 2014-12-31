package com.zdtx.ifms.specific.web.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.JSTree;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.task.Fence;
import com.zdtx.ifms.specific.model.task.FenceDetail;
import com.zdtx.ifms.specific.model.task.FenceVehicle;
import com.zdtx.ifms.specific.model.task.Geoveh;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.service.query.VehicleTreeManager;
import com.zdtx.ifms.specific.service.task.FencingManager;

public class GoogleFencingController extends URLSupport<Fence>{

	private static final long serialVersionUID = 9173702565438285389L;

	private Fence f= new Fence();
	
	@Autowired
	private FencingManager fenMgr;
	
	@Autowired
	private VehicleTreeManager vMgr;
	
	private String fenname;
	
	private Long vehicleid;
	
	private String vehname;
	
    private List<KeyAndValue> list;
    
    private List<Object[]> listobj;
	
	public String getTree() {
		 
		List<JSTree> jsonList = new ArrayList<JSTree>();
		try {
			List<Geoveh> list = fenMgr.getTreeList(Long.parseLong(Struts2Util.getParameter("id")));
			for (Geoveh t : list) {
				JSTree node = new JSTree();
				Map<String, String> attr = new HashMap<String, String>();
				attr.put("id", t.getTree_id().toString());
				attr.put("sort", t.getLicenseplate());
				// data为节点名称
				node.setData(t.getTree_name());
				// 将属性集合放入一开始遍历新建的node里
				node.setAttr(attr);
				// 根节点
				if (0L == t.getParentid()) {
					attr.put("rel", t.getParentid().toString());
				} else if(-1L == t.getParentid()){
					attr.put("rel", t.getParentid().toString());
				}else {
					if (2L == t.getTree_level()) {
						// 这里默认打开三级
						// 根据业务不同,这里可以自行更改
						// 设置其state属性为空字符串意味着其为末节点
						attr.put("rel", "last");
						node.setState("");
					}
				}
				jsonList.add(node);
			}
			JSONArray json = JSONArray.fromObject(jsonList);
			Utils.getResponse().getWriter().print(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public String catchArch() throws IOException{
		 List<KeyAndValue>  list = vMgr.getVehicleList(vehname,id);
		 JSONArray json = JSONArray.fromObject(list);
		 Utils.getResponse().getWriter().print(json.toString());
		 return null;
	}
	
	public  String create() throws IOException{
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
		f.setCreater(getCurrentUser().getUserName());
		f.setCreatime(DateUtil.formatDate(new Date()));
		baseMgr.save(f);
		String[] ids= fenname.split(";");
		for (int i = 0; i < ids.length; i++) {
			FenceDetail detail = new FenceDetail();
			detail.setOrdno(i+1);
			detail.setGeofencingid(f.getGeofencingid());
			detail.setLatitude(Double.parseDouble(ids[i].split(",")[0]));
			detail.setLongitude(Double.parseDouble(ids[i].split(",")[1]));
			baseMgr.save(detail);
		}
		if(null != vehicleid){
			Vehcile v = baseMgr.get(Vehcile.class, vehicleid);
			
			FenceVehicle  veh = new FenceVehicle();
			veh.setGeofencingid(f.getGeofencingid());
			veh.setLicenseplate(v.getLicenseplate());
			veh.setVehicleid(vehicleid);
			veh.setVehiclename(v.getVehiclename());
			baseMgr.save(veh);
		}
		out.print(Constants.SUCCESS[0]+"google-fencing!index?highlight=" + f.getGeofencingid()
				+ "'</script>");
	} catch (Exception e) {
		out.print(Constants.ERROR);
		e.printStackTrace();
	} finally {
		out.close();
	}
	
		return "index";
	}
	
	public  String create1() throws IOException{
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
		f.setCreater(getCurrentUser().getUserName());
		f.setCreatime(DateUtil.formatDate(new Date()));
		baseMgr.save(f);
		String[] ids= fenname.split(";");
		fenMgr.deleteDetail(f.getGeofencingid());
		for (int i = 0; i < ids.length; i++) {
			FenceDetail detail = new FenceDetail();
			detail.setOrdno(i+1);
			detail.setGeofencingid(f.getGeofencingid());
			detail.setLatitude(Double.parseDouble(ids[i].split(",")[0]));
			detail.setLongitude(Double.parseDouble(ids[i].split(",")[1]));
			baseMgr.save(detail);
		}
		if(null != vehicleid){
			Vehcile v = baseMgr.get(Vehcile.class, vehicleid);
		    FenceVehicle  veh = new FenceVehicle();
		    veh.setGeofencingid(f.getGeofencingid());
		    veh.setLicenseplate(v.getLicenseplate());
		    veh.setVehicleid(vehicleid);
		    veh.setVehiclename(v.getVehiclename());
		    baseMgr.save(veh);
		}
		out.print(Constants.SUCCESS[0]+"google-fencing!index?highlight=" + f.getGeofencingid()
				+ "'</script>");
	} catch (Exception e) {
		out.print(Constants.ERROR);
		e.printStackTrace();
	} finally {
		out.close();
	}
	
		return "index";
	} 
	public String getveh() throws IOException{
		List<FenceVehicle> list1= fenMgr.FenceVehicle(vehicleid);
		if(null != list1 && list1.size()>0){
			 JSONArray json = JSONArray.fromObject(false);
			 Utils.getResponse().getWriter().print(json.toString());
		 } else {
			 JSONArray json = JSONArray.fromObject(true);
			 Utils.getResponse().getWriter().print(json.toString());
		 }
	   return null;
	}
	
	public String getveh1() throws IOException{
		List<FenceVehicle> list1= fenMgr.FenceVehicle1(vehicleid,id);
		if(null != list1 && list1.size()>0){
			 JSONArray json = JSONArray.fromObject(false);
			 Utils.getResponse().getWriter().print(json.toString());
		 } else {
			 JSONArray json = JSONArray.fromObject(true);
			 Utils.getResponse().getWriter().print(json.toString());
		 }
	   return null;
	}
	
	
	public String edit(){
		list = vMgr.getVehicleList(null,Long.parseLong(Struts2Util.getParameter("id")));
		f = baseMgr.get(Fence.class, Long.parseLong(Struts2Util.getParameter("id")));
		return "edit";
	}
	public String edit1() throws IOException{
		List<FenceDetail> fenlist = fenMgr.getFenceDetail(Long.parseLong(Struts2Util.getParameter("id")));
		listobj = new ArrayList<Object[]>();
		for (int i = 0; i <fenlist.size(); i++) {
			Object[] obj =new Object[2];
			obj[0] = fenlist.get(i).getLatitude();
			obj[1] = fenlist.get(i).getLongitude();
			listobj.add(obj);
		}
		if(null != listobj && listobj.size()>0){
			Object[] obj1 =new Object[2];
			obj1[0] = fenlist.get(0).getLatitude();
			obj1[1] = fenlist.get(0).getLongitude();
			listobj.add(obj1);
		}
	   JSONArray json = JSONArray.fromObject(listobj);
	   Utils.getResponse().getWriter().print(json.toString());
		return null;
	}
	public String check2() throws IOException{
		 List<Fence>  list1 = fenMgr.getFence(f.getCaption(), id);
		 if(null != list1 && list1.size()>0){
			 JSONArray json = JSONArray.fromObject(false);
			 Utils.getResponse().getWriter().print(json.toString());
		 } else {
			 JSONArray json = JSONArray.fromObject(true);
			 Utils.getResponse().getWriter().print(json.toString());
		 }
		 
		return null;
	}
	/**
	 * @title 删除节点
	 * @return
	 */
	public String remove() {
		try {
			Long strLong=Long.parseLong(Struts2Util.getParameter("id"));
			if(Long.parseLong(Struts2Util.getParameter("id"))>10000000000L){
				strLong = Long.parseLong(Struts2Util.getParameter("id"))-10000000000L;
				baseMgr.delete(FenceVehicle.class,strLong);
			} else {
				fenMgr.delete(strLong);
				fenMgr.update(strLong);
			}
			Struts2Util.renderJson("{\"status\" : \"success\"}");
		} catch (Exception e) {
			Struts2Util.renderJson("{\"status\" : \"error\"}");
			e.printStackTrace();
		}
		return null;
	}

	public String add(){
		list = vMgr.getVehicleList(null,id);
		return "add";
	}
	
	@Override
	public Fence getModel() {
		return f;
	}

	public Fence getF() {
		return f;
	}

	public void setF(Fence f) {
		this.f = f;
	}

 
	public String getFenname() {
		return fenname;
	}

	public void setFenname(String fenname) {
		this.fenname = fenname;
	}

	public Long getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}

	public String getVehname() {
		return vehname;
	}

	public void setVehname(String vehname) {
		this.vehname = vehname;
	}

	 
	public List<KeyAndValue> getList() {
		return list;
	}

	public void setList(List<KeyAndValue> list) {
		this.list = list;
	}

	public List<Object[]> getListobj() {
		return listobj;
	}

	public void setListobj(List<Object[]> listobj) {
		this.listobj = listobj;
	}
	
	

}
