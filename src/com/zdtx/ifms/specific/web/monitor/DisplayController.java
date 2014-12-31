/**
 * @File: DisplayController.java
 * @path: ifms - com.zdtx.ifms.specific.web.monitor
 */
package com.zdtx.ifms.specific.web.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.model.query.Round;
import com.zdtx.ifms.specific.model.system.PoiConf;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.service.monitor.DisplayManager;
import com.zdtx.ifms.specific.service.monitor.SocketManager;
import com.zdtx.ifms.specific.service.system.PoiConfManager;
import com.zdtx.ifms.specific.vo.monitor.DisplayVO;

/**
 * @ClassName: DisplayController
 * @Description: monitor-display-contrller
 * @author: Leon Liu
 * @date: 2013-4-28 PM1:39:15
 * @version V1.0
 */
public class DisplayController extends URLSupport<VehcileView> {

	private static final long serialVersionUID = 5582443000563070962L;

	@Autowired
	private DisplayManager manager;
	@Autowired
	private PoiConfManager poiConfManager;
	@Autowired
	private SocketManager socketManager;
	
	VehcileView vehcileView = new VehcileView();
	private DisplayVO vo = new DisplayVO();
	private List<KeyAndValue> scheduleList = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> fleets=new ArrayList<KeyAndValue>();
	private Long scheduleID;	//任务ID
	
	public String index() {
	//	page = manager.getBatch(page, vo);
	//	fleets=baseMgr.getFleetByAuthority(getCurrentUser().getUserID());
		return "edit";
	}
	
	public String show() {
		vehcileView = baseMgr.get(VehcileView.class, id);
		return "edit";
	}
	public String mod(){
		fleets=baseMgr.getFleetByAuthority(getCurrentUser().getUserID());
		return "mod";
	}
	
	/**
	 * get map data
	 * @return JSON
	 */
	public String getGeo() {
		String plate = vo.getPlateNumber();
		String beginDate = vo.getBeginDate();
		String endDate = vo.getEndDate();
		String beginTime = vo.getBeginTime();
		String endTime = vo.getEndTime();
		if(Utils.isEmpty(beginDate)) {
			if(Utils.isEmpty(endDate)) {
				endDate = DateUtil.getToDay();
			}
			beginDate = endDate;
		} else if(Utils.isEmpty(endDate)) {
			endDate = beginDate;
		}
		beginTime = Utils.isEmpty(beginTime) ? "00:00:00" : beginTime;
		endTime = Utils.isEmpty(endTime) ? "23:59:59" : endTime;
//		List<GeoVO> list = manager.getGeo(plate, beginDate, endDate, beginTime, endTime);
		List<Round> list =manager.getRound(plate, beginDate, endDate, beginTime, endTime,  vo.getVehID(), beginDate);
		if(list == null || list.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = JSONArray.fromObject(list);
			return "jsonArray";
		}
	}
	
	/**
	 * Get event data
	 * @return JSON
	 */
	public String getEvents() {
		Long vehID = vo.getVehID();
		String beginDate = vo.getBeginDate();
		String beginTime = vo.getBeginTime();
		String endTime = vo.getEndTime();
		List<Details> eventList =  manager.getEvents(vehID, beginDate, beginTime, endTime);
		if(eventList == null || eventList.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = JSONArray.fromObject(eventList);
			return "jsonArray";
		}
	}
	
	/**
	 * get schedule by vehicle
	 * @return JSON
	 */
	public String getSchedule() {
		scheduleList = manager.getSchedule(vo.getVehID());
		jsonObject = new JSONObject();
		if(scheduleList == null || scheduleList.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = JSONArray.fromObject(scheduleList);
			return "jsonArray";
		}
	}
	
	/**
	 * Get poi data
	 * @return JSON
	 */
	public String getPOI() {
		List<PoiConf> poiList = poiConfManager.getAll(true, "T");
		if(poiList == null || poiList.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = JSONArray.fromObject(poiList);
			return "jsonArray";
		}
	}
	
	public String closeSocket() {
		socketManager.closeSocket();
		return null;
	}
	
	/**
	 * Check whether the vehicle is online
	 * @return JSON
	 */
	public String checkVehIsOnLine() {
		Long vehID = vo.getVehID();
		String videoID = manager.getVideoIDByVehID(vehID);	//Get device id
		int checkResult = socketManager.checkVehIsOnLine(getCurrentUser().getLoginName(), getCurrentUser().getPassword(), videoID);	//check vehicle status
		if(checkResult == 1){
			jsonObject.put("result", "The vehicle you selected does not exist!");
		} else if(checkResult == 2){
			jsonObject.put("result", "The vehicle you selected is not online!");
		} else if(checkResult == 3){
			jsonObject.put("result", "Vehicle response has timed out!");
		} else {
			jsonObject.put("result", "pass");
		}
		jsonObject.put("videoID", videoID);
		return "jsonObject";
	}
	
	/**
	 * Check the existence of the playback data
	 * @return
	 */
	public String checkVideoExist() {
		Long vehID = vo.getVehID();
		String videoID = manager.getVideoIDByVehID(vehID);	//Get device id
		String videoDate = vo.getBeginDate().replaceAll("-", "");
		String begin_time = vo.getBeginTime().replaceAll(":", "");
		String end_time = vo.getEndTime().replaceAll(":", "");
		String channelID = vo.getChannelID();
		List<KeyAndValue> playList = socketManager.checkVideoExist(getCurrentUser().getLoginName(), videoID, channelID, videoDate, begin_time, end_time);
		if(playList == null || playList.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			//jsonArray = JSONArray.fromObject(playList);
			//return "jsonArray";
			jsonObject.put("result", videoID);
			return "jsonObject";
		}
	}
	
	/**
	 * Get download video
	 * @return JSON
	 */
	public String downloadVideo() {
		String videoFileName = vo.getFileName();
		Long vehID = vo.getVehID();
		String videoID = manager.getVideoIDByVehID(vehID);	//Get device id
		String resStr = socketManager.downloadServer(getCurrentUser().getLoginName(), videoID, videoFileName);
		if(resStr.equals("4")) {
			jsonObject.put("error", "The vehicle you selected is not online!");
		} else if(resStr.equals("5")) {
			jsonObject.put("error", "The vehicle you selected does not exist!");
		} else if(resStr.equals("6")) {
			jsonObject.put("error", "Vehicle response has timed out!");
		} else if(resStr.equals("7")) {
			jsonObject.put("error", "The video you selected does not exist!");
		} else if(resStr.equals("131096")) {
			jsonObject.put("error", "Limit download tasks!");
		} else if(resStr.equals("131090")) {
			jsonObject.put("error", "The video you selected is recording!");
		} else if(resStr.equals("131095")) {
			jsonObject.put("error", "Get the download file's task number failed!");
		} else {
			jsonObject.put("fileName", resStr.split(":")[0]);
		}
		return "jsonObject";
	}
	
	/**
	 * 下载录像到本地
	 * @param fileName	录像在服务端路径
	 * @return
	 */
	@SuppressWarnings("unused")
	public String downloadLocal() {
		String fileName = vo.getFileName();
		File file = new File(fileName);
		String downloadFileName = fileName.split("/")[7];
		if(!file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		OutputStream os = null;
		try {
			fis = new FileInputStream(file);
			os = Struts2Util.getOutputStreamDL(downloadFileName);
			byte[] b = new byte[4096];
			int i = 0;
			while ((i = fis.read(b)) > 0) {
				os.write(b);
			}
			os.flush();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
				fis = null;
			}
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
				os = null;
			}
		}
		return null;
	}
	
	@Override
	public VehcileView getModel() {
		return vehcileView;
	}

	public DisplayVO getVo() {
		return vo;
	}

	public void setVo(DisplayVO vo) {
		this.vo = vo;
	}

	public List<KeyAndValue> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<KeyAndValue> scheduleList) {
		this.scheduleList = scheduleList;
	}

	public Long getScheduleID() {
		return scheduleID;
	}

	public void setScheduleID(Long scheduleID) {
		this.scheduleID = scheduleID;
	}

	public List<KeyAndValue> getFleets() {
		return fleets;
	}

	public void setFleets(List<KeyAndValue> fleets) {
		this.fleets = fleets;
	}
	
}