package com.zdtx.ifms.specific.web.task;

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
import com.zdtx.ifms.specific.model.task.Fence;
import com.zdtx.ifms.specific.model.task.Schedule;
import com.zdtx.ifms.specific.model.vehicle.Driver;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.service.task.ScheduleManager;
import com.zdtx.ifms.specific.service.vehicle.DriverListManager;
import com.zdtx.ifms.specific.service.vehicle.VehicleListManager;
import com.zdtx.ifms.specific.vo.task.ScheduleVO;

public class ScheduleController extends ReportBase<Schedule>{

	private static final long serialVersionUID = -5891127278235241720L;

	private ScheduleVO schVo = new ScheduleVO();
	private Schedule sch = new Schedule();
	private List<KeyAndValue> vehicleList = new ArrayList<KeyAndValue>(); //车辆List
	private List<KeyAndValue> driverList = new ArrayList<KeyAndValue>(); //司机List 
	private List<KeyAndValue> routeList = new ArrayList<KeyAndValue>(); //线路List
	private String cardid;
	@Autowired
	private DriverListManager drilistMgr; 
	@Autowired
	private VehicleListManager vehlistMgr;
	@Autowired
	private ScheduleManager schMgr;
	
	private String code1;
	private String code2;
	private String stime;
	
	@Override
	public String index() {
		driverList = drilistMgr.getVehicleArratyList();
		vehicleList = vehlistMgr.getVehicleArratyList();
		routeList = schMgr.getRouteList();
		page = schMgr.getPage(page, schVo);
		return "index";
	}
	public String rindex() {
		vehicleList = vehlistMgr.getVehicleArratyList();
		routeList = schMgr.getRouteList();
		page = schMgr.getPage2(page, schVo);
		return "rindex";
	}
	
	public String add() {
		driverList = drilistMgr.getVehicleArratyList();
		vehicleList = vehlistMgr.getVehicleArratyList();
		routeList = schMgr.getRouteList();
		return "add";
	}
	
	public String edit() {
		driverList = drilistMgr.getVehicleArratyList();
		vehicleList = vehlistMgr.getVehicleArratyList();
		routeList = schMgr.getRouteList();
		sch = baseMgr.get(Schedule.class, id);
		return "add";
	}
	
	public String show() {
		sch = baseMgr.get(Schedule.class, id);
		return "show";
	}
	
	public String subcard(){
		if(code1.equals("zhengchang")){
			Schedule ts=baseMgr.get(Schedule.class, sch.getScheduleid());
			ts.setStartime(sch.getStartime());
			ts.setDriverid(ts.getPlandriverid());
			ts.setDrivername(ts.getPlandrivername());
			ts.setDrivernumber(ts.getPlandrivernumber());
			ts.setVehicleid(ts.getPlanvehicleid());
			ts.setVehiclenumber(ts.getPlanvehiclenumber());
			ts.setLicenseplate(ts.getPlanlicenseplate());
			baseMgr.save(ts);
			Struts2Util.renderJson("{\"result\":\"ok\"}");
			return null;
		}else if(code1.equals("huiche")){
			Schedule ts=baseMgr.get(Schedule.class, sch.getScheduleid());
			ts.setEndtime(sch.getEndtime());
			Driver td=baseMgr.get(Driver.class, sch.getDriverid());
			ts.setRedriverid(td.getDriverid());
			ts.setRedrivername(td.getDrivername());
			ts.setRedrivernumber(td.getDrivernumber());
			baseMgr.save(ts);
			Struts2Util.renderJson("{\"result\":\"ok\"}");
			return null;
		}else if(code1.equals("linshika")){
			Schedule ts=baseMgr.get(Schedule.class, sch.getScheduleid());
			ts.setStartime(sch.getStartime());
			ts.setVehicleid(ts.getPlanvehicleid());
			ts.setVehiclenumber(ts.getPlanvehiclenumber());
			ts.setLicenseplate(ts.getPlanlicenseplate());
			Driver td=baseMgr.get(Driver.class, sch.getDriverid());
			ts.setDriverid(td.getDriverid());
			ts.setDrivername(td.getDrivername());
			ts.setDrivernumber(td.getDrivernumber());
			ts.setDuty(sch.getDuty());
			baseMgr.save(ts);
			Struts2Util.renderJson("{\"result\":\"ok\"}");
		}else if(code1.equals("linshiche")){
			Schedule ts=baseMgr.get(Schedule.class, sch.getScheduleid());
			ts.setStartime(sch.getStartime());
			ts.setDriverid(ts.getPlandriverid());
			ts.setDrivername(ts.getPlandrivername());
			ts.setDrivernumber(ts.getPlandrivernumber());
			VehcileView tvv=baseMgr.get(VehcileView.class, sch.getVehicleid());
			ts.setVehicleid(tvv.getVehicleid());
			ts.setVehiclenumber(tvv.getVehiclename());
			ts.setLicenseplate(tvv.getLicenseplate());
			ts.setDuty(sch.getDuty());
			baseMgr.save(ts);
			Struts2Util.renderJson("{\"result\":\"ok\"}");
		}else if(code1.equals("linrenwu")){
			Schedule ts=new Schedule();
			ts.setStartime(sch.getStartime());
			VehcileView tvv=baseMgr.get(VehcileView.class, sch.getVehicleid());
			ts.setVehicleid(tvv.getVehicleid());
			ts.setVehiclenumber(tvv.getVehiclename());
			ts.setLicenseplate(tvv.getLicenseplate());
			Driver td=baseMgr.get(Driver.class, sch.getDriverid());
			ts.setDriverid(td.getDriverid());
			ts.setDrivername(td.getDrivername());
			ts.setDrivernumber(td.getDrivernumber());
			ts.setIsdelete("F");
			baseMgr.save(ts);
			Struts2Util.renderJson("{\"result\":\"ok\"}");
		}
		
		
		return null;
	}
	
	public String shua(){
			Vehcile tv= null;
			Driver td=null;
			tv=schMgr.getVehByCode(code1);
			if(tv==null){
				tv=schMgr.getVehByCode(code2);
				td=schMgr.getDriverByCode(code1);
			}else{
				td=schMgr.getDriverByCode(code2);
			}
			if(tv==null || td==null ){
				Struts2Util.renderJson("{\"result\":\"false\"}");
				return null;
			}
			
			
			Date ti=new Date(Long.valueOf(stime));
			Object[] obj=new Object[2];
			
			sch= schMgr.getHuiche(tv);
			if(sch!=null){
				sch.setRedriverid(td.getDriverid());
				sch.setRedrivername(td.getDrivername());
				sch.setRedrivernumber(td.getDrivernumber());
				sch.setEndtime(DateUtil.formatLongTimeDate(ti));
				obj[0]="huiche";
				obj[1]=sch;
				
				JSONArray js=JSONArray.fromObject(obj);
				Struts2Util.renderJson(js.toString());
				return null;
			}
			
			
			sch= schMgr.getZhengchang(tv,td,ti);
			if(sch!=null){
				sch.setStartime(DateUtil.formatLongTimeDate(ti));
				obj[0]="zhengchang";
				obj[1]=sch;
				JSONArray js=JSONArray.fromObject(obj);
				Struts2Util.renderJson(js.toString());
				return null;
			}
			
			if(td.getType()!=null && td.getType().equals("1")){
				sch=schMgr.getLinshika(tv,ti);
			}
			if(sch!=null){
				sch.setStartime(DateUtil.formatLongTimeDate(ti));
				sch.setDriverid(td.getDriverid());
				sch.setDrivername(td.getDrivername());
				obj[0]="linshika";
				obj[1]=sch;
				JSONArray js=JSONArray.fromObject(obj);
				Struts2Util.renderJson(js.toString());
				return null;
			}
			sch=schMgr.getLinshiche(td,ti);
			if(sch!=null){
				sch.setStartime(DateUtil.formatLongTimeDate(ti));
				sch.setVehicleid(tv.getVehicleid());
				sch.setVehiclenumber(tv.getVehiclename());
				obj[0]="linshiche";
				obj[1]=sch;
				JSONArray js=JSONArray.fromObject(obj);
				Struts2Util.renderJson(js.toString());
				return null;
			}
			
			if(sch==null){
				sch=new Schedule();
				sch.setStartime(DateUtil.formatLongTimeDate(ti));
				sch.setDrivername(td.getDrivername());
				sch.setDriverid(td.getDriverid());
				sch.setVehiclenumber(tv.getVehiclename());
				sch.setVehicleid(tv.getVehicleid());
				obj[0]="linrenwu";
				obj[1]=sch;
				JSONArray js=JSONArray.fromObject(obj);
				Struts2Util.renderJson(js.toString());
				return null;
			}
		return null;
	}
	
	public String save() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
//			sch.setCreater(getCurrentUser().getUserName());
//			sch.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			Schedule ts=new Schedule();
			ts.setCreater(getCurrentUser().getUserName());
			ts.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			
			ts.setPlandriverid(sch.getDriverid());
			ts.setPlandrivernumber(baseMgr.get(Driver.class, sch.getDriverid()).getDrivernumber());
			ts.setPlandrivername(baseMgr.get(Driver.class, sch.getDriverid()).getDrivername());
			ts.setPlanvehicleid(sch.getVehicleid());
			ts.setPlanvehiclenumber(baseMgr.get(VehcileView.class, sch.getVehicleid()).getVehiclename());
			ts.setPlanstartime(sch.getStartime());
			ts.setPlanendtime(sch.getEndtime());
			ts.setPlanlicenseplate(baseMgr.get(VehcileView.class, sch.getVehicleid()).getLicenseplate());
			
			ts.setDuty(sch.getDuty());
			ts.setRouteid(sch.getRouteid());
			ts.setRoutename(baseMgr.get(Fence.class, sch.getRouteid()).getCaption());
			ts.setIsdelete("F");
			
			
			
			baseMgr.save(ts);
			out.print(Constants.SUCCESS[0]+"schedule!index?highlight=" + ts.getScheduleid() + "&editble=1" + Constants.SUCCESS[1]);
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}
	
	public String delete() {
		sch = baseMgr.get(Schedule.class, id);
		sch.setIsdelete("T");
		baseMgr.save(sch);
		return this.index();
	}
	
	public String checkoutTime() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			String startime = Utils.getParameter("startime");
			String endtime = Utils.getParameter("endtime");
			Long vehicleid = Long.parseLong(Utils.getParameter("vehicleid"));
			
			Long scheduleid = -1l;
			if (!"".equals(Utils.getParameter("scheduleid"))) {
				scheduleid = Long.parseLong(Utils.getParameter("scheduleid"));
			}
			out.print(schMgr.getCheckOutTime(scheduleid, vehicleid, startime, endtime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String checkDriverTime() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			String startime = Utils.getParameter("startime");
			String endtime = Utils.getParameter("endtime");
			Long driverid = Long.parseLong(Utils.getParameter("driverid"));
			
			Long scheduleid = -1l;
			if (!"".equals(Utils.getParameter("scheduleid"))) {
				scheduleid = Long.parseLong(Utils.getParameter("scheduleid"));
			}
			out.print(schMgr.getCheckDriverTime(scheduleid, driverid, startime, endtime));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String exportDetail(){
		try {
			String title  = "Schedule List";
			xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
			xlsStream = schMgr.getExcel(title + DateUtil.formatDate(new Date()));
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	
	public ScheduleVO getSchVo() {
		return schVo;
	}

	public void setSchVo(ScheduleVO schVo) {
		this.schVo = schVo;
	}

	public Schedule getModel() {
		return null;
	}

	public List<KeyAndValue> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<KeyAndValue> vehicleList) {
		this.vehicleList = vehicleList;
	}

	public List<KeyAndValue> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<KeyAndValue> driverList) {
		this.driverList = driverList;
	}

	public List<KeyAndValue> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<KeyAndValue> routeList) {
		this.routeList = routeList;
	}
	public Schedule getSch() {
		return sch;
	}

	public void setSch(Schedule sch) {
		this.sch = sch;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}
	
}
