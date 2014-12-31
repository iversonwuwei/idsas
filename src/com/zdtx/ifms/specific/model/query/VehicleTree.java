package com.zdtx.ifms.specific.model.query;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: VehicleTree
 * @author Zhang Yi
 * @date 2013-4-27 10:55:40
 * @version V1.0
 */
@Entity
@Table(name="V_TREE_VEHICLE")
public class VehicleTree implements Serializable{

	private static final long serialVersionUID = 5546768998290035550L;

	private Long vehicleid;
	private String vehiclename;
	private Long fleetid;
	private String licenseplate;
	private String description;
	private String cctvip;
	private Long deviceid;
	private Long typeid;
	private String devicename;
	private String typename;
	
	@Id
	public Long getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}
	public String getVehiclename() {
		return vehiclename;
	}
	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}
	public Long getFleetid() {
		return fleetid;
	}
	public void setFleetid(Long fleetid) {
		this.fleetid = fleetid;
	}
	public String getLicenseplate() {
		return licenseplate;
	}
	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCctvip() {
		return cctvip;
	}
	public void setCctvip(String cctvip) {
		this.cctvip = cctvip;
	}
	public Long getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}
	public Long getTypeid() {
		return typeid;
	}
	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	@Override
	public String toString() {
		return "VehicleTree [vehicleid=" + vehicleid + ", vehiclename="
				+ vehiclename + ", fleetid=" + fleetid + ", licenseplate="
				+ licenseplate + ", description=" + description + ", cctvip="
				+ cctvip + ", deviceid=" + deviceid + ", typeid=" + typeid
				+ ", devicename=" + devicename + ", typename=" + typename + "]";
	}
	
	
}
