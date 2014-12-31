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
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.task.Booking;
import com.zdtx.ifms.specific.model.task.BookingDetail;
import com.zdtx.ifms.specific.model.vehicle.Driver;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.service.query.VehicleTreeManager;
import com.zdtx.ifms.specific.service.system.VehicleTypeManager;
import com.zdtx.ifms.specific.service.task.BookingManager;
import com.zdtx.ifms.specific.service.task.FencingManager;
import com.zdtx.ifms.specific.service.task.ScheduleManager;
import com.zdtx.ifms.specific.service.vehicle.DriverListManager;
import com.zdtx.ifms.specific.service.vehicle.VehicleListManager;
import com.zdtx.ifms.specific.vo.task.BookingVO;

public class BookingController extends URLSupport<Booking>{

	private static final long serialVersionUID = -5891127278235241720L;

	private BookingVO bkVo = new BookingVO();
	private Booking bk = new Booking();
	
	
	private List<KeyAndValue> vehicleList = new ArrayList<KeyAndValue>(); //车辆List
	private List<KeyAndValue> driverList = new ArrayList<KeyAndValue>(); //司机List 
	private List<KeyAndValue> routeList = new ArrayList<KeyAndValue>(); //线路List
	
	@Autowired
	private DriverListManager drilistMgr; 
	@Autowired
	private VehicleListManager vehlistMgr;
	@Autowired
	private BookingManager bookingMgr;
	
	@Autowired
	private DriverListManager driverMgr;
	
	@Autowired
	private VehicleTypeManager vehMgr;
	
	@Autowired
	private FencingManager fenMgr;
	
	
	private List<KeyAndValue> listva;
	
	private List<KeyAndValue> listfenc;
	
	private List<KeyAndValue> listdriver;
	
	
	private List<Booking> listbing;
	
	
	private List<BookingDetail> listbookdeta;
	
	@Autowired
	private VehicleTreeManager vMgr;

    private List<KeyAndValue> list;
  
    private Long vehicleid;
    
    private Long driverid;
    
    private String dname;
    
    private String vehname;
 
	
	@Autowired
	private ScheduleManager schMgr;
	
	@Override
	public String index() {
		driverList = drilistMgr.getVehicleArratyList();
		vehicleList = vehlistMgr.getVehicleArratyList();
		page = bookingMgr.getPage(page, bkVo);
		listva = vehMgr.getVehicleTypeList(getCurrentUser());
		routeList = schMgr.getRouteList();
		return "index";
	}
	
	public String add() {
		driverList = drilistMgr.getVehicleArratyList();
		vehicleList = vehlistMgr.getVehicleArratyList();
		listva = vehMgr.getVehicleTypeList(getCurrentUser());
		listfenc = fenMgr.getOrgBySuper();
		return "edit";
	}
	
	public String bedit(){
		listdriver = driverMgr.getDriverList(dname);
		bk = baseMgr.get(Booking.class, id);
		listbing = bookingMgr.getBath();
		list = vMgr.getVehicleList1(vehname);
		listbookdeta = bookingMgr.getBath1(id);
		return "bedit";
	}
	
	
	public String edit() {
		driverList = drilistMgr.getVehicleArratyList();
		vehicleList = vehlistMgr.getVehicleArratyList();
		listva = vehMgr.getVehicleTypeList(getCurrentUser());
		listfenc = fenMgr.getOrgBySuper();
		bk = baseMgr.get(Booking.class, id);
		return "edit";
	}
	
	public String show() {
		bk = baseMgr.get(Booking.class, id);
		return "show";
	}
	
	public String save() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			bk.setCreater(getCurrentUser().getUserName());
			bk.setCreatime(DateUtil.formatDate(new Date()));
			baseMgr.save(bk);
			out.print(Constants.SUCCESS[0]+"booking!index?highlight=" + bk.getBookingid() + "&editble=1" + Constants.SUCCESS[1]);
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}
	public String deletebook() throws IOException{
		BookingDetail bde = baseMgr.get(BookingDetail.class, id);
		bde.setIsdelete("T");
		baseMgr.save(bde);
		JSONArray json = JSONArray.fromObject(true);
		Utils.getResponse().getWriter().print(json.toString());
		return null;
		
	}
	
	public String catchArch() throws IOException{
		 List<KeyAndValue>  list = drilistMgr.getDriverList(dname);
		 JSONArray json = JSONArray.fromObject(list);
		 Utils.getResponse().getWriter().print(json.toString());
		 return null;
	}
	
	public String catchArch1() throws IOException{
		 List<KeyAndValue>  list = vMgr.getVehicleList1(vehname);
		 JSONArray json = JSONArray.fromObject(list);
		 Utils.getResponse().getWriter().print(json.toString());
		 return null;
	}
	public String check() throws IOException{
		 List<Booking> list = bookingMgr.getBath( bk.getBookingdate(), id);
		 if(null != list && list.size()>0){
			 JSONArray json = JSONArray.fromObject(false);
			 Utils.getResponse().getWriter().print(json.toString());
		 } else {
			 JSONArray json = JSONArray.fromObject(true);
			 Utils.getResponse().getWriter().print(json.toString());
		 }
		   return null;
	}
	public String create1() throws IOException{
		BookingDetail bookde = new BookingDetail();
		Driver driver=baseMgr.get(Driver.class, driverid);
		Vehcile vehcile=baseMgr.get(Vehcile.class, vehicleid);
		bookde.setBookingid(bk.getBookingid());
		bookde.setCreater(getCurrentUser().getUserName());
		bookde.setCreatime(DateUtil.formatDate(new Date()));
		bookde.setDriverid(driverid);
		bookde.setVehicleid(vehicleid);
		bookde.setDrivername(driver.getDrivername());
		bookde.setDrivernumber(driver.getDrivernumber());
		bookde.setIsdelete("F");
		bookde.setLicenseplate(vehcile.getLicenseplate());
		bookde.setVehiclename(vehcile.getVehiclename());
		baseMgr.save(bookde);
		List<BookingDetail> list= new ArrayList<BookingDetail>();
		list.add(bookde);
		JSONArray json = JSONArray.fromObject(list);
		Utils.getResponse().getWriter().print(json.toString());
		return null;
	}
	
	public String delete() {
		bk = baseMgr.get(Booking.class, id);
		bk.setIsdelete("T");
		baseMgr.save(bk);
		return this.index();
	}
 

	public Booking getModel() {
		return bk;
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

	public BookingVO getBkVo() {
		return bkVo;
	}

	public void setBkVo(BookingVO bkVo) {
		this.bkVo = bkVo;
	}

	public Booking getBk() {
		return bk;
	}

	public void setBk(Booking bk) {
		this.bk = bk;
	}

	public List<KeyAndValue> getListva() {
		return listva;
	}

	public void setListva(List<KeyAndValue> listva) {
		this.listva = listva;
	}

	public List<KeyAndValue> getListfenc() {
		return listfenc;
	}

	public void setListfenc(List<KeyAndValue> listfenc) {
		this.listfenc = listfenc;
	}

	public List<KeyAndValue> getListdriver() {
		return listdriver;
	}

	public void setListdriver(List<KeyAndValue> listdriver) {
		this.listdriver = listdriver;
	}

	public List<Booking> getListbing() {
		return listbing;
	}

	public void setListbing(List<Booking> listbing) {
		this.listbing = listbing;
	}

	public List<BookingDetail> getListbookdeta() {
		return listbookdeta;
	}

	public void setListbookdeta(List<BookingDetail> listbookdeta) {
		this.listbookdeta = listbookdeta;
	}

	public List<KeyAndValue> getList() {
		return list;
	}

	public void setList(List<KeyAndValue> list) {
		this.list = list;
	}

	public Long getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}

	public Long getDriverid() {
		return driverid;
	}

	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getVehname() {
		return vehname;
	}

	public void setVehname(String vehname) {
		this.vehname = vehname;
	}
	
	
 
}
