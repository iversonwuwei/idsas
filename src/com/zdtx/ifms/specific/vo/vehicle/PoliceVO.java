/**
 * @path com.zdtx.ifms.specific.vo.vehicle
 * @file PoliceVO.java
 */
package com.zdtx.ifms.specific.vo.vehicle;

import java.io.Serializable;

/**
 * @description Orgnization management-Police-VO
 * @author Liu Jun
 * @since 2014年6月10日 上午10:55:56
 */
public class PoliceVO implements Serializable {

	private static final long serialVersionUID = -9208637565949719837L;

	private Long deptID;
	private String deptName;
	private Long fleetID;
	private String fleetName;
	private Long deviceID;
	private String deviceName;
	private Long policeID;
	private String policeName;

	public Long getDeptID() {
		return deptID;
	}

	public void setDeptID(Long deptID) {
		this.deptID = deptID;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getFleetID() {
		return fleetID;
	}

	public void setFleetID(Long fleetID) {
		this.fleetID = fleetID;
	}

	public String getFleetName() {
		return fleetName;
	}

	public void setFleetName(String fleetName) {
		this.fleetName = fleetName;
	}

	public Long getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(Long deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Long getPoliceID() {
		return policeID;
	}

	public void setPoliceID(Long policeID) {
		this.policeID = policeID;
	}

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	@Override
	public String toString() {
		return "PoliceVO [deptID=" + deptID + ", deptName=" + deptName
				+ ", fleetID=" + fleetID + ", fleetName=" + fleetName
				+ ", deviceID=" + deviceID + ", deviceName=" + deviceName
				+ ", policeID=" + policeID + ", policeName=" + policeName + "]";
	}
}