package com.zdtx.ifms.specific.model.profile;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_IFMS_COUNSELING")
public class Counsell implements Serializable {

	private static final long serialVersionUID = -2242298594361735899L;
	
	private Long counseling;
	private Long userid;
	private Long driverid;
	private Long departid;
	private String departname;
	private String username;
	private String drivername;
	private String startime;
	private String endtime;
	private String remark;
	private String creater;
	private String creatime;
	private String isdelete;
	
	@Id
	@SequenceGenerator(name = "s_ifms_counseling", sequenceName = "s_ifms_counseling", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_counseling")
	public Long getCounseling() {
		return counseling;
	}
	public void setCounseling(Long counseling) {
		this.counseling = counseling;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Long getDriverid() {
		return driverid;
	}
	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDrivername() {
		return drivername;
	}
	public void setDrivername(String drivername) {
		this.drivername = drivername;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	public Long getDepartid() {
		return departid;
	}
	public void setDepartid(Long departid) {
		this.departid = departid;
	}
	public String getDepartname() {
		return departname;
	}
	public void setDepartname(String departname) {
		this.departname = departname;
	}

}
