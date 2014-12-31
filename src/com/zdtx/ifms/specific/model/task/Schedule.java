package com.zdtx.ifms.specific.model.task;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="T_IFMS_SCHEDULE")
public class Schedule implements Serializable{

	private static final long serialVersionUID = -8071312284808674846L;

	private Long scheduleid; //主键id
	private Long driverid; //司机id
	private String drivernumber; //司机编号
	private Long vehicleid; //车辆id
	private String vehiclenumber; //车辆编号
	private String startime; //开始时间
	private String endtime; //结束时间
	private Long routeid; //线路id
	private String routename;  //线路名称
	private String duty; //任务
	private String creater; //修改人
	private String creatime; //修改时间
	private String isdelete; //是否删除
	private String drivername;//司机姓名
	private String licenseplate;//车牌号
	
	private String planstartime;
	private String planendtime;
	private String plandrivernumber;
	private Long plandriverid;
	private String plandrivername;
	private Long planvehicleid;
	private String planvehiclenumber;
	private String planlicenseplate;
	private Long redriverid; //回车司机id
	private String redrivernumber; //回车司机编号
	private String redrivername;//回车司机姓名
	
	
	
	
	public Long getRedriverid() {
		return redriverid;
	}
	public void setRedriverid(Long redriverid) {
		this.redriverid = redriverid;
	}
	public String getRedrivernumber() {
		return redrivernumber;
	}
	public void setRedrivernumber(String redrivernumber) {
		this.redrivernumber = redrivernumber;
	}
	public String getRedrivername() {
		return redrivername;
	}
	public void setRedrivername(String redrivername) {
		this.redrivername = redrivername;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_IFMS_SCHEDULE")
	@SequenceGenerator(name = "S_IFMS_SCHEDULE", sequenceName = "S_IFMS_SCHEDULE", allocationSize = 1)
	@Column(name = "scheduleid", nullable = false)
	public Long getScheduleid() {
		return scheduleid;
	}
	public void setScheduleid(Long scheduleid) {
		this.scheduleid = scheduleid;
	}
	public String getDrivernumber() {
		return drivernumber;
	}
	public void setDrivernumber(String drivernumber) {
		this.drivernumber = drivernumber;
	}
	public String getVehiclenumber() {
		return vehiclenumber;
	}
	public void setVehiclenumber(String vehiclenumber) {
		this.vehiclenumber = vehiclenumber;
	}
	public String getStartime() {
		return startime;
	}
	public void setStartime(String startime) {
		this.startime = startime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getRoutename() {
		return routename;
	}
	public void setRoutename(String routename) {
		this.routename = routename;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
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
	public Long getDriverid() {
		return driverid;
	}
	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}
	public Long getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}
	public Long getRouteid() {
		return routeid;
	}
	public void setRouteid(Long routeid) {
		this.routeid = routeid;
	}
	
	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	public String getDrivername() {
		return drivername;
	}
	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}
	public String getLicenseplate() {
		return licenseplate;
	}
	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}
	
	
	public String getPlanstartime() {
		return planstartime;
	}
	public void setPlanstartime(String planstartime) {
		this.planstartime = planstartime;
	}
	public String getPlanendtime() {
		return planendtime;
	}
	public void setPlanendtime(String planendtime) {
		this.planendtime = planendtime;
	}
	public String getPlandrivernumber() {
		return plandrivernumber;
	}
	public void setPlandrivernumber(String plandrivernumber) {
		this.plandrivernumber = plandrivernumber;
	}
	public Long getPlandriverid() {
		return plandriverid;
	}
	public void setPlandriverid(Long plandriverid) {
		this.plandriverid = plandriverid;
	}
	public String getPlandrivername() {
		return plandrivername;
	}
	public void setPlandrivername(String plandrivername) {
		this.plandrivername = plandrivername;
	}
	public Long getPlanvehicleid() {
		return planvehicleid;
	}
	public void setPlanvehicleid(Long planvehicleid) {
		this.planvehicleid = planvehicleid;
	}
	public String getPlanvehiclenumber() {
		return planvehiclenumber;
	}
	public void setPlanvehiclenumber(String planvehiclenumber) {
		this.planvehiclenumber = planvehiclenumber;
	}
	public String getPlanlicenseplate() {
		return planlicenseplate;
	}
	public void setPlanlicenseplate(String planlicenseplate) {
		this.planlicenseplate = planlicenseplate;
	}
	@Override
	public String toString() {
		return "Schedule [scheduleid=" + scheduleid + ", driverid=" + driverid
				+ ", drivernumber=" + drivernumber + ", vehicleid=" + vehicleid
				+ ", vehiclenumber=" + vehiclenumber + ", startime=" + startime
				+ ", endtime=" + endtime + ", routeid=" + routeid
				+ ", routename=" + routename + ", duty=" + duty + ", creater="
				+ creater + ", creatime=" + creatime + ", isdelete=" + isdelete
				+ ", drivername=" + drivername + ", licenseplate="
				+ licenseplate + ", planstartime=" + planstartime
				+ ", planendtime=" + planendtime + ", plandrivernumber="
				+ plandrivernumber + ", plandriverid=" + plandriverid
				+ ", plandrivername=" + plandrivername + ", planvehicleid="
				+ planvehicleid + ", planvehiclenumber=" + planvehiclenumber
				+ ", planlicenseplate=" + planlicenseplate + "]";
	}
	
}
