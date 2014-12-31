/**
 * @path com.zdtx.ifms.specific.web.vehicle
 * @file PoliceController.java
 */
package com.zdtx.ifms.specific.web.vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.system.DeviceList;
import com.zdtx.ifms.specific.model.vehicle.Police;
import com.zdtx.ifms.specific.service.vehicle.PoliceManager;
import com.zdtx.ifms.specific.vo.vehicle.PoliceVO;

/**
 * @description Orgnization management-Police-controller
 * @author Liu Jun
 * @since 2014年6月10日 上午11:06:56
 */
public class PoliceController extends ReportBase<Police> {

	private static final long serialVersionUID = 7156762886417770813L;
	
	@Autowired
	private PoliceManager manager;
	
	private Police police = new Police();
	private PoliceVO vo = new PoliceVO();
	
	private List<KeyAndValue> deviceList = new ArrayList<KeyAndValue>();
	
	public String index() {
		page = manager.getBatch(page, vo);
		return "index";
	}

	public String editNew() {
		return "edit";
	}
	
	public String edit() {
		police = baseMgr.get(Police.class, id);
		deviceList = manager.getUnboundedPoliceDeviceList(police.getDeptID());
		if(police.getDeviceID() != null) {
			deviceList.add(new KeyAndValue(police.getDeviceID() + "", police.getDeviceName()));
		}
		return "edit";
	}
	
	public String create() {
		try {
			police.setIsDelete("F");
			police.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
			police.setCreater(getCurrentUser().getUserName());
			if(police.getDeviceID() == -1L) {	//页面下拉菜单选择Please select项，解除绑定关系
				police.setDeviceID(null);
				police.setDeviceName(null);
			}
			boolean isNew = (police.getPoliceID() == null);	//判断是否新增
			String oldName = "";
			boolean isBounded = false;
			if(!isNew) {
				oldName = baseMgr.get(Police.class, police.getPoliceID()).getPoliceName();		//取旧名称
				isBounded = (baseMgr.get(Police.class, police.getPoliceID()).getDeviceID() != null);	 //旧单兵数据是否曾绑设备
			}
			baseMgr.save(police);
			if(!isNew && isBounded) {	//编辑且曾被绑定
				SocketUtil.sendSocket(SocketUtil.setVehicle(oldName, "1"));	//删除原有数据
//				SocketUtil.sendSocket(SocketUtil.vehSocket(oldName, "1"));		//删除原有数据
			}
			if(police != null && police.getDeviceID() != null) {	//绑定设备
				SocketUtil.sendSocket(SocketUtil.setVehicle(police.getPoliceName(), "0"));	//发送新增串
//				SocketUtil.sendSocket(SocketUtil.vehSocket(police.getPoliceName(), "0"));	//发送新增串
			}
			DeviceList device = null;
			if(!Utils.isEmpty(vo.getDeviceID())) {
				device = baseMgr.get(DeviceList.class, vo.getDeviceID());	//remove old device
				device.setO_busid(null);
				device.setO_busname(null);
				baseMgr.save(device);
			}
			if(police.getDeviceID() != null) {	//绑定新设备
				device = baseMgr.get(DeviceList.class, police.getDeviceID());		//update new device
				device.setO_busid(police.getPoliceID());
				device.setO_busname(police.getPoliceName());
				baseMgr.save(device);
			}
			highlight = police.getPoliceID();
			Utils.renderHtml(Constants.SUCCESS[0] + Utils.getBasePath() + "vehicle/police?editble=1&highlight=" + highlight + Constants.SUCCESS[1]);
		} catch (Exception e) {
			Utils.renderHtml(Constants.ERROR);
		}
		return null;
	}
	
	public String show() {
		police = baseMgr.get(Police.class, id);
		return super.show();
	}
	
	public String destory() {
		police = baseMgr.get(Police.class, id);
		police.setIsDelete("T");
		baseMgr.save(police);
		return index();
	}
	
	public String checkDuplicate() {
		if(manager.isDuplicate(vo.getFleetID(), vo.getPoliceID(), vo.getPoliceName())) {
			jsonObject.put("result", "duplicate");
		} else {
			jsonObject.put("result", "pass");
		}
		return "jsonObject";
	}
	
	/**
	 * Get device by department'id
	 * @return JSON
	 */
	public String getDeviceByDept() {
		deviceList = manager.getUnboundedPoliceDeviceList(vo.getDeptID());
		jsonArray = JSONArray.fromObject(deviceList);
		return "jsonArray";
	}
	
	public String doExport() {
		Map<String, String> convert = new HashMap<String, String>();
		convert.put("0", "hidden");
		convert.put("1", "male");
		convert.put("2", "famale");
		this.xlsFileName = disposeXlsName("Police_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()));
		this.xlsStream = baseMgr.doExportExcel("Police", "No., Police Name, Department, Fleet, Device, Gender[cv], Email, Phone, Description",
						"policeName, deptName, fleetName, deviceName, gender, email, phone, description", convert);
//		Integer[] colWidth = new Integer[]{12, 12, 12, 12, 6, 14, 12, 20};
//		baseMgr.doPrintPdf("Police", false, "Police Name, Department, Fleet, Device, Gender[cv], Email, Phone, Description",
//						"policeName, deptName, fleetName, deviceName, gender, email, phone, description", convert, colWidth, 25);
		return "xls";
	}
	
	@Override
	public Police getModel() {
		return police;
	}

	public PoliceVO getVo() {
		return vo;
	}

	public void setVo(PoliceVO vo) {
		this.vo = vo;
	}

	public List<KeyAndValue> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<KeyAndValue> deviceList) {
		this.deviceList = deviceList;
	}

}