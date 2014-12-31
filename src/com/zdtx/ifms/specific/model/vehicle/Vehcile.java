package com.zdtx.ifms.specific.model.vehicle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 车辆信息表
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@Entity
@Table(name = "T_CORE_VEHICLE")
public class Vehcile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long vehicleid;
	private String vehiclename;
	private String licenseplate;
	private Long fleetid;
	private Long deviceid;
	private Long typeid;
	private String description;
	private String cctvip;
	private String isdelete;
	private String creater;
	private String creatime;
	private String keycode;
	private Long brandid;
	private String brandname;
	private String fleetname;
	private String deptname;
	private Long deptid;


	@Id
	@SequenceGenerator(name = "S_CORE_VEHICLE", sequenceName = "s_core_org", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_VEHICLE")
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

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	@Override
	public String toString() {
		return "Vehcile [vehicleid=" + vehicleid + ", vehiclename="
				+ vehiclename + ", licenseplate=" + licenseplate + ", fleetid="
				+ fleetid + ", deviceid=" + deviceid + ", typeid=" + typeid
				+ ", description=" + description + ", cctvip=" + cctvip
				+ ", isdelete=" + isdelete + ", creater=" + creater
				+ ", creatime=" + creatime + ", keycode=" + keycode + "]";
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

	public String getFleetname() {
		return fleetname;
	}

	public void setFleetname(String fleetname) {
		this.fleetname = fleetname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public Long getDeptid() {
		return deptid;
	}

	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}

	
}
