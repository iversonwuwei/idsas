package com.zdtx.ifms.specific.model.analy;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "t_ifms_special")
public class SpeInstruction implements Serializable{

	private static final long serialVersionUID = 2831908543267937910L;
	
	private Long specialid;//		id
	private Long vehicleid;	//		vehicleid
	private String vehiclename;	//		vehiclename
	private String licenseplate;//		license plate
	private String code;	//		code
	private String chndefinition;//	chn definition
	private String engdefinition;	//		eng definition
	private String creater;	//		creater
	private String creatime;//			create time
	private String success="F";	//	success
	private Double longitude;	//		longitude
	private Double latitude;//			latitude
	
	@Id
	@SequenceGenerator(name = "s_ifms_special", sequenceName = "s_ifms_special", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_special")
	public Long getSpecialid() {
		return specialid;
	}
	public void setSpecialid(Long specialid) {
		this.specialid = specialid;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getChndefinition() {
		return chndefinition;
	}
	public void setChndefinition(String chndefinition) {
		this.chndefinition = chndefinition;
	}
	public String getEngdefinition() {
		return engdefinition;
	}
	public void setEngdefinition(String engdefinition) {
		this.engdefinition = engdefinition;
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
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
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
	 
}
