package com.zdtx.ifms.specific.vo.vehicle;

import java.io.Serializable;

/**
 * Vehicle list搜索用VO
 * 
 * @author Lonn
 * 
 */
public class VehicleVO implements Serializable {
	
	private static final long serialVersionUID = -5156981492391103839L;
	
	private String vehiclename;
	private String licenseplate;
	private String cctvip;
	private String description;
	private Long typeid;
	private Long fleetid;
	private Long deviceid;
	private String brand;
	private String keyCode;
	private Long deptID;

	public String getVehiclename() {
		return vehiclename;
	}

	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}

	public String getCctvip() {
		return cctvip;
	}

	public void setCctvip(String cctvip) {
		this.cctvip = cctvip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTypeid() {
		return typeid;
	}

	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	public Long getFleetid() {
		return fleetid;
	}

	public void setFleetid(Long fleetid) {
		this.fleetid = fleetid;
	}

	public Long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public Long getDeptID() {
		return deptID;
	}

	public void setDeptID(Long deptID) {
		this.deptID = deptID;
	}

	@Override
	public String toString() {
		return "VehicleVO [vehiclename=" + vehiclename + ", licenseplate="
				+ licenseplate + ", cctvip=" + cctvip + ", description="
				+ description + ", typeid=" + typeid + ", fleetid=" + fleetid
				+ ", deviceid=" + deviceid + ", brand=" + brand + ", keyCode="
				+ keyCode + ", deptID=" + deptID + "]";
	}
}