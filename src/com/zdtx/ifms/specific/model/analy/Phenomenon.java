package com.zdtx.ifms.specific.model.analy;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "T_IFMS_FAULT")
public class Phenomenon implements Serializable{

	private static final long serialVersionUID = -821793707937177063L;

	private Long faultid; //id
	private Long vehicleid; //vehicleid
	private String vehiclename; //vehicle name
	private String licenseplate; //license plate
	private String faulttime; //fault time
	private FaultDict faultcode; //fault code
	private Double longitude; //longitude
	private Double latitude; //latitude
	private Long driverid; //driverid
	private String drivernumber; //driver number
	private String drivername; //driver name
	private String isremove; //default is N ： N Y 
	private String removetime; //remove time
	private String remover; //remover
	private String isdelete; //isdelete
	private String creater; //creater
	private String creatime; //create time
	
	@Id
	public Long getFaultid() {
		return faultid;
	}
	public void setFaultid(Long faultid) {
		this.faultid = faultid;
	}
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
	public String getFaulttime() {
		return faulttime;
	}
	public void setFaulttime(String faulttime) {
		this.faulttime = faulttime;
	}
	
	@ManyToOne(targetEntity = FaultDict.class, fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@NotFound(action = NotFoundAction.IGNORE)
	public FaultDict getFaultcode() {
		return faultcode;
	}
	public void setFaultcode(FaultDict faultcode) {
		this.faultcode = faultcode;
	}
	
	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}
	public String getDrivernumber() {
		return drivernumber;
	}
	public void setDrivernumber(String drivernumber) {
		this.drivernumber = drivernumber;
	}
	public String getDrivername() {
		return drivername;
	}
	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}
	public String getIsremove() {
		return isremove;
	}
	public void setIsremove(String isremove) {
		this.isremove = isremove;
	}
	public String getRemovetime() {
		return removetime;
	}
	public void setRemovetime(String removetime) {
		this.removetime = removetime;
	}
	public String getRemover() {
		return remover;
	}
	public void setRemover(String remover) {
		this.remover = remover;
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
	
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Long getDriverid() {
		return driverid;
	}
	@Override
	public String toString() {
		return "Phenomenon [faultid=" + faultid + ", vehicleid=" + vehicleid
				+ ", vehiclename=" + vehiclename + ", licenseplate="
				+ licenseplate + ", faulttime=" + faulttime + ", faultcode="
				+ faultcode + ", longitude=" + longitude + ", latitude="
				+ latitude + ", driverid=" + driverid + ", drivernumber="
				+ drivernumber + ", drivername=" + drivername + ", isremove="
				+ isremove + ", removetime=" + removetime + ", remover="
				+ remover + ", isdelete=" + isdelete + ", creater=" + creater
				+ ", creatime=" + creatime + "]";
	}
	
}
