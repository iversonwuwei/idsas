package com.zdtx.ifms.specific.model.vehicle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 车辆信息表
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@Entity
@Table(name = "V_CORE_VEHICLE")
public class VehcileView implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long vehicleid;
	private String vehiclename;
	private String licenseplate;
	private String description;
	private String cctvip;
	private String isdelete;
	private String creater;
	private String creatime;
	private Long fleetid;
	private Long deviceid;
	private Long typeid;
	private String fleetname;
	private String devicename;
	private String typename;
	private String path;
	private String icon;
	private String keycode;
	private Long deptID;
	private String deptname;
	private Long brandid;
	private String brandname;
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

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
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

	public String getCctvip() {
		return cctvip;
	}

	public void setCctvip(String cctvip) {
		this.cctvip = cctvip;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreatime() {
		return creatime;
	}

	public void setCreatime(String creatime) {
		this.creatime = creatime;
	}

	public String getFleetname() {
		return fleetname;
	}

	public void setFleetname(String fleetname) {
		this.fleetname = fleetname;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public Long getBrandid() {
		return brandid;
	}

	public void setBrandid(Long brandid) {
		this.brandid = brandid;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public Long getDeptID() {
		return deptID;
	}

	public void setDeptID(Long deptID) {
		this.deptID = deptID;
	}

	@Override
	public String toString() {
		return "VehcileView [vehicleid=" + vehicleid + ", vehiclename="
				+ vehiclename + ", licenseplate=" + licenseplate
				+ ", description=" + description + ", cctvip=" + cctvip
				+ ", isdelete=" + isdelete + ", creater=" + creater
				+ ", creatime=" + creatime + ", fleetid=" + fleetid
				+ ", deviceid=" + deviceid + ", typeid=" + typeid
				+ ", fleetname=" + fleetname + ", devicename=" + devicename
				+ ", typename=" + typename + ", path=" + path + ", icon="
				+ icon + ", keycode=" + keycode + ", deptID=" + deptID
				+ ", deptname=" + deptname + ", brandid=" + brandid
				+ ", brandname=" + brandname + "]";
	}
}