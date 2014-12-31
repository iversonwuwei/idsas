package com.zdtx.ifms.specific.web.vehicle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.monitor.CamDevice;
import com.zdtx.ifms.specific.model.monitor.Camera;
import com.zdtx.ifms.specific.model.system.ThresSet;
import com.zdtx.ifms.specific.model.system.VehBrand;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.specific.service.system.DeviceListManager;
import com.zdtx.ifms.specific.service.system.ThresSetManager;
import com.zdtx.ifms.specific.service.system.VehicleTypeManager;
import com.zdtx.ifms.specific.service.vehicle.VehicleListManager;
import com.zdtx.ifms.specific.vo.vehicle.VehicleVO;

/**
 * 车辆信息
 * 
 * @author LiuGuilong
 * 
 */
public class VehicleListController extends ReportBase<VehcileView> {

	private static final long serialVersionUID = -4790489179151473218L;
	
	@Autowired
	private VehicleListManager vehMgr;
	@Autowired
	private VehicleTypeManager typeMgr;
	@Autowired
	private DeviceListManager dlMgr;
	@Autowired
	private ThresSetManager tsMgr;
	
	private Long cdid;
	private Long soid;
	private Long rootid;// 树的根节点的ID
	private Vehcile veh;
	private VehicleVO vehvo = new VehicleVO();
	private List<KeyAndValue> typelist = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> devicelist = new ArrayList<KeyAndValue>();
	private VehcileView vehv=new VehcileView();
	private Camera[] cameras = new Camera[16];
	private List<CamDevice> channllist=new ArrayList<CamDevice>();
	private List<KeyAndValue> depts=new ArrayList<KeyAndValue>();
	private List<KeyAndValue> fleets=new ArrayList<KeyAndValue>();
	private List<KeyAndValue> brands=new ArrayList<KeyAndValue>();
	
	public String index() {
		typelist = typeMgr.getVehicleTypeList(getCurrentUser());
		page = vehMgr.getBatch(page, vehvo);
		return "index";
	}
	/***
	 * 导出
	 * @return
	 */
	public String exportDetail(){
		try {
			String title  = "Vehicle List";
			xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
			xlsStream = vehMgr.getExcel(title + DateUtil.formatDate(new Date()));
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}

	public String add() {
		depts=baseMgr.getDepartByAuthority(getCurrentUser().getUserID());
		return "add";
	}
	
	public String getftbd(){
		Object[] ary=vehMgr.getftbd(id,veh.getVehicleid());
		Struts2Util.renderJson(JSONArray.fromObject(ary).toString());
		return null;
	}

	public String edit() {
		depts=baseMgr.getDepartByAuthority(getCurrentUser().getUserID());
		veh = baseMgr.get(Vehcile.class, id);
		return "add";
	}
	public String show() {
		veh = baseMgr.get(Vehcile.class, id);
		vehv= baseMgr.get(VehcileView.class, id);
		return "show";
	}

	public String showlive(){
		vehv = baseMgr.get(VehcileView.class, id);
		getCamlist(vehv.getDeviceid());
		return "show44";
	}
	public String showliveo(){
		vehv = baseMgr.get(VehcileView.class, id);
		getCamlist(vehv.getDeviceid());
		return "show12";
	}
	public String showliveone(){
		vehv = baseMgr.get(VehcileView.class, id);
		getCamlist(vehv.getDeviceid());
		return "show11";
	}
	public String showliveonly(){
		
		vehv = baseMgr.get(VehcileView.class, id);
//		DeviceList dl=baseMgr.get(DeviceList.class, vehv.getDeviceid());
		channllist = dlMgr.getChannel(vehv.getDeviceid());
	
		return "showliveonly";
	}
	public String getchannel(){
		vehv = baseMgr.get(VehcileView.class, id);
		channllist = dlMgr.getChannel(vehv.getDeviceid());
		
		Struts2Util.renderJson(JSONArray.fromObject(channllist).toString());
		return null;
	}
	
	public String showonly(){
		vehv = baseMgr.get(VehcileView.class, id);
		CamDevice cdt=baseMgr.get(CamDevice.class, cdid);
		Camera camt=baseMgr.get(Camera.class, cdt.getCamID());
//		DeviceListCamtype dlc=baseMgr.get(DeviceListCamtype.class, vehv.getDeviceid());
//		getCamlist(vehv.getDeviceid());
		cameras[0]=new Camera();
		cameras[0].setIpAddress(vehv.getCctvip());
		cameras[0].setAdminName(camt.getAdminName());
		cameras[0].setAdminPass(camt.getAdminPass());
		cameras[0].setCreater(""+soid);//暂时用来记录 插件类型 1 vlc 2 原厂
		cameras[0].setMac(camt.getModelID().getType());//临时记录类型 video server|| ipcam 
		int port=(int) (554+(cdt.getChannel()-1));
		cameras[0].setGateway(port+"");//临时记录端口
//		if(dlc.getType()!=null){
//			cameras[0].setMac(dlc.getType());
//		}
		if(cameras[0]!=null){
		}
		
		return "showonly";
	}
	public String showlivet(){
		vehv = baseMgr.get(VehcileView.class, id);
		getCamlist(vehv.getDeviceid());
		return "show22";
	}
	public String showlivef(){
		vehv = baseMgr.get(VehcileView.class, id);
		getCamlist(vehv.getDeviceid());
		return "show24";
	}
	
	public void getCamlist(Long deviceid){
		List<CamDevice> clist = dlMgr.getChannel(deviceid);
//		for(int i = 0; i<16;i++){
//			cameras[i] = new Camera();
//		}
//		for(int i = 0; i<clist.size();i++){
//			CamDevice cd = clist.get(i);
//			Camera c = baseMgr.get(Camera.class,cd.getCameraid());
//			cameras[Integer.parseInt(cd.getChannel()+"")-1]=c;
//		}
		if(clist.size()>0){
			CamDevice cd = clist.get(0);
			Camera c = baseMgr.get(Camera.class,cd.getCamID());
			cameras[0]=c;
		}
	}
	
	public String save() {
		try {
			veh.setLicenseplate(veh.getVehiclename());//编号和车牌号相同
			if(veh.getFleetid()!=null){
				Org o=baseMgr.get(Org.class, veh.getFleetid());
				veh.setFleetname(o.getOrgName());
			}else{
				veh.setFleetname(null);
			}
			if(veh.getDeptid()!=null){
				Org o=baseMgr.get(Org.class, veh.getDeptid());
				veh.setDeptname(o.getOrgName());
			}else{
				veh.setDeptname(null);
			}
			if(veh.getBrandid()!=null){
				VehBrand vb=baseMgr.get(VehBrand.class, veh.getBrandid());
				veh.setBrandname(vb.getName());
			}else{
				veh.setBrandname(null);
			}
			
			boolean isNew = (veh.getVehicleid() == null);	//判断是否新增
			String oldname = "";
			boolean isBounded = false;
			if(!isNew) {
				oldname = baseMgr.get(Vehcile.class, veh.getVehicleid()).getVehiclename();	//取旧车牌号
				isBounded = (baseMgr.get(Vehcile.class, veh.getVehicleid()).getDeviceid() != null);	 //车绑设备是否为空
			}
			veh.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			veh.setCreater(getCurrentUser().getUserName());
			baseMgr.save(veh);
			highlight = veh.getVehicleid();
			if(!isNew && isBounded) {	//编辑旧数据且被绑定
				SocketUtil.sendSocket(SocketUtil.setVehicle(oldname, "1"));		//删除原有数据
			}
			if(veh != null && veh.getDeviceid() != null) {	//新数据有绑定设备
				SocketUtil.sendSocket(SocketUtil.setVehicle(veh.getVehiclename(), "0"));	//发送新增串
			}
//			if(isNew) {	//新增
//				if(veh != null && veh.getDeviceid() != null) {	//绑定设备
//					SocketUtil.sendSocket(SocketUtil.setVehicle(veh.getVehiclename(), "0"));	//发送新增串
////					SocketUtil.sendSocket(SocketUtil.vehSocket(veh.getVehiclename(), "0"));		//发送新增串
//				}
//			} else {	//编辑
//				if(isBounded) {	//有车绑设备
//					SocketUtil.sendSocket(SocketUtil.setVehicle(oldname, "1"));		//删除原有数据
////					SocketUtil.sendSocket(SocketUtil.vehSocket(oldname, "1"));			//删除原有数据
//				}
//				if(veh != null && veh.getDeviceid() != null) {
//					SocketUtil.sendSocket(SocketUtil.setVehicle(veh.getVehiclename(), "0"));	//发送新增串
////					SocketUtil.sendSocket(SocketUtil.vehSocket(veh.getVehiclename(), "0"));	//发送新增串
//				}
//			}
			List<ThresSet> thresSetList = tsMgr.findtset(veh.getTypeid());	//获取报警阀值
			if (thresSetList != null) {
				String command = "";
				String paramStr = "";
				for (ThresSet thresSet : thresSetList) {
					paramStr = SocketUtil.createSettingParams(thresSet.getTypeid(), thresSet.getStartvalue(), thresSet.getEndvalue(), thresSet.getUsetime());
//					String temp = SocketUtil.setlist(t.getTypeid(), t.getStartvalue(), t.getEndvalue(), t.getUsetime());
					if (!Utils.isEmpty(paramStr)) {
						command += paramStr;
					}
				}
//			 	String socketstr=SocketUtil.thresholdStr(String.valueOf((new Date()).getTime()).substring(0, 10), veh.getVehiclename(), str);
				String thresholdStr = SocketUtil.setThresholdStr(String.valueOf((new Date()).getTime()).substring(0, 10), veh.getVehiclename(), command);
				SocketUtil.sendSocket(thresholdStr);	//设置报警阀值
			}
			Utils.renderHtml(Constants.SUCCESS[0] + Utils.getBasePath() + "vehicle/vehicle-list?editble=1&highlight=" + highlight + Constants.SUCCESS[1]);
		} catch (Exception e) {
			Utils.renderHtml(Constants.ERROR);
		}
		return null;
	}

	public String check() {
		String vehiclename = Utils.getParameter("vehiclename");
		Utils.renderText(vehMgr.checkRepeat(vehiclename));
		return null;
	}
	public String checkkey() {
		String vehiclename = Utils.getParameter("keycode");
		Utils.renderText(vehMgr.checkKey(vehiclename,id));
		return null;
	}
	
	public String getvehbyfleet(){
		List<KeyAndValue> vehs=vehMgr.getVehbyFleet(id);
		Struts2Util.renderJson(JSONArray.fromObject(vehs).toString());
		return null;
	}

	public String checkLicenseplate() {
		String licenseplate = Utils.getParameter("licenseplate");
		Utils.renderText(vehMgr.checkLicenseplateRepeat(licenseplate));
		return null;
	}
	public String del2(){
		veh = baseMgr.get(Vehcile.class, id);
		if(veh!=null && veh.getDeviceid()!=null ){
			Struts2Util.renderText("false");
		}else{
			Struts2Util.renderText("true");
		}
		return null;
	}
	public String delete() {
		veh = baseMgr.get(Vehcile.class, id);
		veh.setIsdelete("T");
		baseMgr.save(veh);
		return index();
	}

	@Override
	public VehcileView getModel() {
		return null;
	}

	public Vehcile getVeh() {
		return veh;
	}

	public void setVeh(Vehcile veh) {
		this.veh = veh;
	}

	public List<KeyAndValue> getTypelist() {
		return typelist;
	}

	public void setTypelist(List<KeyAndValue> typelist) {
		this.typelist = typelist;
	}

	public List<KeyAndValue> getDevicelist() {
		return devicelist;
	}

	public void setDevicelist(List<KeyAndValue> devicelist) {
		this.devicelist = devicelist;
	}

	public VehicleVO getVehvo() {
		return vehvo;
	}

	public void setVehvo(VehicleVO vehvo) {
		this.vehvo = vehvo;
	}

	public Long getRootid() {
		return rootid;
	}

	public void setRootid(Long rootid) {
		this.rootid = rootid;
	}

	public String test() {
		return "test";
	}

	public VehcileView getVehv() {
		return vehv;
	}

	public void setVehv(VehcileView vehv) {
		this.vehv = vehv;
	}

	public Camera[] getCameras() {
		return cameras;
	}

	public void setCameras(Camera[] cameras) {
		this.cameras = cameras;
	}

	public List<CamDevice> getChannllist() {
		return channllist;
	}

	public void setChannllist(List<CamDevice> channllist) {
		this.channllist = channllist;
	}

	public String getData() throws IOException {
		PrintWriter out = Utils.getResponse().getWriter();
		List<KeyAndValue> list = new ArrayList<KeyAndValue>();
		list = vehMgr.getDatas(Utils.getParameter("search_text"), Utils.getParameter("search_value"));
		JSONArray json = JSONArray.fromObject(list);
		out.print(json.toString());
		return null;
	}

	public Long getCdid() {
		return cdid;
	}

	public void setCdid(Long cdid) {
		this.cdid = cdid;
	}

	public Long getSoid() {
		return soid;
	}

	public void setSoid(Long soid) {
		this.soid = soid;
	}

	public List<KeyAndValue> getDepts() {
		return depts;
	}

	public void setDepts(List<KeyAndValue> depts) {
		this.depts = depts;
	}

	public List<KeyAndValue> getFleets() {
		return fleets;
	}

	public void setFleets(List<KeyAndValue> fleets) {
		this.fleets = fleets;
	}

	public List<KeyAndValue> getBrands() {
		return brands;
	}

	public void setBrands(List<KeyAndValue> brands) {
		this.brands = brands;
	}
}