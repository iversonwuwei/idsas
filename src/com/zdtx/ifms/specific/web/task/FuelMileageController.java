package com.zdtx.ifms.specific.web.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.task.FuelMileage;
import com.zdtx.ifms.specific.model.task.Mileageoil;
import com.zdtx.ifms.specific.service.system.VehicleTypeManager;
import com.zdtx.ifms.specific.service.task.FuelMileageManager;
import com.zdtx.ifms.specific.vo.task.FuelMileageVo;

/**
 * @ClassName: FuelMileageController
 * @Description: Task Management-Fuel Mileage-Controller
 * @author JiangHaiquan
 * @date 2013-05-03 10:09:47
 * @version V1.0
 */

public class FuelMileageController extends ReportBase<Mileageoil> {

	private static final long serialVersionUID = -2191420246684522359L;

	private FuelMileage fm = new FuelMileage();
	private FuelMileageVo fmvo = new FuelMileageVo();
	private List<KeyAndValue> typelist = new ArrayList<KeyAndValue>();

	@Autowired
	private FuelMileageManager fmMgr;
	@Autowired
	private VehicleTypeManager typeMgr;

	@Override
	public String index() {
		try {

			typelist = typeMgr.getVehicleTypeList(getCurrentUser());
			page = fmMgr.getBetch(page, fmvo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.index();
	}

	public String exportDetail(){
		try {
			List<Mileageoil> list = new ArrayList<Mileageoil>();
			String title  = "Fuel Mileage";
			xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
			if(fmvo.getVehicleid()!=null&&fmvo.getVehicleid()!=-1L){
				fmvo.setVehiclename(fmMgr.getVehicleNameByVehid(fmvo.getVehicleid()).get(0).getValue());
			}
			xlsStream = fmMgr.getData(page,list,fmvo);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	
		
	@Override
	public Mileageoil getModel() {
		return null;
	}

	public FuelMileage getFm() {
		return fm;
	}

	public void setFm(FuelMileage fm) {
		this.fm = fm;
	}

	public FuelMileageVo getFmvo() {
		return fmvo;
	}

	public void setFmvo(FuelMileageVo fmvo) {
		this.fmvo = fmvo;
	}

	public List<KeyAndValue> getTypelist() {
		return typelist;
	}

	public void setTypelist(List<KeyAndValue> typelist) {
		this.typelist = typelist;
	}



}