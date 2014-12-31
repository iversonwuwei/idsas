package com.zdtx.ifms.specific.web.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.system.ThresSet;
import com.zdtx.ifms.specific.service.system.ThresSetManager;
import com.zdtx.ifms.specific.service.system.VehicleTypeManager;
import com.zdtx.ifms.specific.vo.system.ThresSetVo;

/**
 * @ClassName: ThresSetController
 * @Description: System Settings-Threshold Setting-Controller
 * @author JiangHaiquan
 * @date 2013-05-03 21:05:47
 * @version V1.0
 */

public class ThresSetController extends URLSupport<ThresSet> {

	private static final long serialVersionUID = -2191420246684522359L;

	private ThresSet ts = new ThresSet();
	private ThresSetVo tsvo = new ThresSetVo();
	private List<KeyAndValue> typelist = new ArrayList<KeyAndValue>();
//	private List<KeyAndValue> typelist1 = new ArrayList<KeyAndValue>();
	private List<ThresSet> tslist = new ArrayList<ThresSet>();
	
	@Autowired
	private ThresSetManager tsMgr;
	@Autowired
	private VehicleTypeManager typeMgr;

	@Override
	public String index() {
		try {
			typelist = typeMgr.getVehicleTypeList(getCurrentUser());
//			typelist1 = tsMgr.getTypeList();
			tslist = tsMgr.getBetch(tsvo, typelist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.index();
	}
	
	public String create() throws IOException{
		Long vehTypeID = null;
		try {
			String command = "";
			String paramStr = "";
			for (int i = 0; i < tslist.size(); i++) {
				if(i == 7) {
					continue;
				}
				ThresSet thresSet = baseMgr.get(ThresSet.class, tslist.get(i).getThresholdid());
				thresSet.setIsdelete("F");
				thresSet.setCreater(getCurrentUser().getUserName());
				thresSet.setCreatime(DateUtil.formatLongTimeDate(new Date()));
				thresSet.setStartvalue(tslist.get(i).getStartvalue());
				thresSet.setEndvalue(tslist.get(i).getEndvalue());
				thresSet.setUsetime(tslist.get(i).getUsetime());
				thresSet.setType(tslist.get(i).getType());
				thresSet.setTypeid(tslist.get(i).getTypeid());
				thresSet.setVehicletype(tslist.get(i).getVehicletype());
				thresSet.setVehicletypeid(tslist.get(i).getVehicletypeid());
				vehTypeID = thresSet.getVehicletypeid();
				baseMgr.save(thresSet);
				paramStr = SocketUtil.createSettingParams(thresSet.getTypeid(), thresSet.getStartvalue(), thresSet.getEndvalue(), thresSet.getUsetime());
//				String temp = SocketUtil.setlist(t.getTypeid(), t.getStartvalue(), t.getEndvalue(), t.getUsetime());
				if (!Utils.isEmpty(paramStr)) {
					command += paramStr;
				}
			}
			if (vehTypeID != null) {
				List<KeyAndValue> vehicleList = baseMgr.getVehByType(vehTypeID);
				String thresholdStr = "";
				for (KeyAndValue vehicle : vehicleList) {	//按车辆类型批量修改报警阀值
//					String socketstr = SocketUtil.thresholdStr(String.valueOf((new Date()).getTime()).substring(0, 10), kav.getValue(), str);
					thresholdStr = SocketUtil.setThresholdStr(String.valueOf((new Date()).getTime()).substring(0, 10), vehicle.getValue(), command);
					SocketUtil.sendSocket(thresholdStr);
				}
			}
			Struts2Util.printSuccess("system/thres-set?editble=1", 0L);
		} catch (Exception e) {
			Struts2Util.printError(e);
		}
		   return this.index();
	    }
	
	@Override
	public ThresSet getModel() {
		return this.ts;
	}

	public List<KeyAndValue> getTypelist() {
		return typelist;
	}

	public void setTypelist(List<KeyAndValue> typelist) {
		this.typelist = typelist;
	}

	public ThresSetVo getTsvo() {
		return tsvo;
	}

	public void setTsvo(ThresSetVo tsvo) {
		this.tsvo = tsvo;
	}

//	public List<KeyAndValue> getTypelist1() {
//		return typelist1;
//	}
//
//	public void setTypelist1(List<KeyAndValue> typelist1) {
//		this.typelist1 = typelist1;
//	}

	public List<ThresSet> getTslist() {
		return tslist;
	}

	public void setTslist(List<ThresSet> tslist) {
		this.tslist = tslist;
	}
}