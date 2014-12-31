/**
 * @File: PolicePlaybackController.java
 * @path: ifms - com.zdtx.ifms.specific.web.monitor
 */
package com.zdtx.ifms.specific.web.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.service.monitor.PolicePlaybackManager;

/**
 * @ClassName: DisplayController
 * @Description: monitor-Police playback-contrller
 * @author: Leon Liu
 * @date: 2014-6-19 AM10:02:15
 * @version V1.0
 */
public class PolicePlaybackController extends URLSupport<Object> {

	private static final long serialVersionUID = 5582443000563070962L;

	@Autowired
	private PolicePlaybackManager manager;
	
	VehcileView vehcileView = new VehcileView();
	
	List<KeyAndValue> policeList = new ArrayList<KeyAndValue>();

	public String index() {
		policeList = manager.getDeviceBoundedPolice();
		return "index";
	}
	
	@Override
	public VehcileView getModel() {
		return null;
	}
	
	public List<KeyAndValue> getPoliceList() {
		return policeList;
	}

	public void setPoliceList(List<KeyAndValue> policeList) {
		this.policeList = policeList;
	}
}