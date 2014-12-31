/**
 * @path com.zdtx.ifms.specific.web.monitor
 * @file VideoDownloadController.java
 */
package com.zdtx.ifms.specific.web.monitor;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.service.monitor.VideoDownloadManager;
import com.zdtx.ifms.specific.vo.monitor.MediaVO;

/**
 * @description Monitor-Video Download-controller
 * @author Liu Jun
 * @since 2014年9月10日 上午11:30:25
 */
public class VideoDownloadController extends URLSupport<MediaVO> {

	private static final long serialVersionUID = -1812477715178741781L;
	
	@Autowired
	private VideoDownloadManager manager;

	private MediaVO vo = new MediaVO();
	private Long fleetID;				//for search
	private Long vehicleID;			//for search
	private String vehicle_name;	//for search
	private String search_date;	//for search
	private String begin_time;		//for search
	private String end_time;		//for search
	private String deviceIP;		//for download
	
	public String index() {
		if(!Utils.isEmpty(vehicle_name) && !Utils.isEmpty(search_date)) {
			begin_time = Utils.isEmpty(begin_time) ? "00:00:00" : begin_time;
			end_time = Utils.isEmpty(end_time) ? "23:59:59" : end_time;
			deviceIP = manager.getIPByVehicle(vehicle_name);		//获取查询车辆对应IP
			if(!Utils.isEmpty(deviceIP)) {
				page = manager.getData(deviceIP, search_date, begin_time, end_time);
			}
		}
		return "index";
	}
	
	@Override
	public MediaVO getModel() {
		return vo;
	}

	public Long getFleetID() {
		return fleetID;
	}

	public void setFleetID(Long fleetID) {
		this.fleetID = fleetID;
	}

	public Long getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Long vehicleID) {
		this.vehicleID = vehicleID;
	}

	public String getVehicle_name() {
		return vehicle_name;
	}

	public void setVehicle_name(String vehicle_name) {
		this.vehicle_name = vehicle_name;
	}

	public String getSearch_date() {
		return search_date;
	}

	public void setSearch_date(String search_date) {
		this.search_date = search_date;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getDeviceIP() {
		return deviceIP;
	}

	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}
}