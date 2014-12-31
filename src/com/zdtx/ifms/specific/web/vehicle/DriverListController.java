package com.zdtx.ifms.specific.web.vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.vehicle.Driver;
import com.zdtx.ifms.specific.model.vehicle.DriverView;
import com.zdtx.ifms.specific.service.vehicle.DriverListManager;
import com.zdtx.ifms.specific.vo.vehicle.DriverVO;

/**
 * 车辆信息
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
public class DriverListController extends ReportBase<DriverView> {

	private static final long serialVersionUID = 1L;
	@Autowired
	private DriverListManager driverMgr;
	
	private Driver driver = new Driver();
	private DriverVO drivervo = new DriverVO();
	private List<Org> deptlist = new ArrayList<Org>();// 树
	private String deptname="";
	/***
	 * 导出
	 * @return
	 */
	public String exportDetail(){
		try {
			String title  = "Driver List";
			xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
			xlsStream = driverMgr.getExcel(title + DateUtil.formatDate(new Date()));
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String index() {
		page = driverMgr.getBatch(page, drivervo);
		return "index";
	}

	public String add() {
		return "add";
	}

	public String edit() {
		driver = baseMgr.get(Driver.class, id);
		return "add";
	}
	public String show() {
		driver = baseMgr.get(Driver.class, id);
		if(driver.getDepartmentid()!=null){
			Org o=baseMgr.get(Org.class, driver.getDepartmentid());
			if(o!=null){
				deptname=o.getOrgName();
			}
			
		}
		return "show";
	}

	public String save() {
		try {
			driver.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			driver.setCreater(getCurrentUser().getUserName());
			baseMgr.save(driver);
			highlight = driver.getDriverid();
			Utils.renderHtml(Constants.SUCCESS[0] + Utils.getBasePath() + "vehicle/driver-list?editble=1&highlight=" + highlight + Constants.SUCCESS[1]);
		} catch (Exception e) {
			Utils.renderHtml(Constants.ERROR);
		}
		return null;
	}

	public String check() {
		String drivername = Utils.getParameter("drivername");
		Utils.renderText(driverMgr.checkRepeat(drivername));
		return null;
	}

	public String checkNumber() {
		String drivernumber = Utils.getParameter("drivernumber");
		Utils.renderText(driverMgr.checkNumRepeat(drivernumber));
		return null;
	}

	public String delete() {
		driver = baseMgr.get(Driver.class, id);
		driver.setIsdelete("T");
		baseMgr.save(driver);
		return index();
	}

	@Override
	public DriverView getModel() {
		return null;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public List<Org> getDeptlist() {
		return deptlist;
	}

	public void setDeptlist(List<Org> deptlist) {
		this.deptlist = deptlist;
	}

	public DriverVO getDrivervo() {
		return drivervo;
	}

	public void setDrivervo(DriverVO drivervo) {
		this.drivervo = drivervo;
	}
}