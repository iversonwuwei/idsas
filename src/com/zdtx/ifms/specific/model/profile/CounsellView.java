package com.zdtx.ifms.specific.model.profile;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_IFMS_COUNSELING")
public class CounsellView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3796847450063789659L;
	private Long counseling;
	private Long userid;
	private Long driverid;
	private Long departid;
	private String departname;
	private String usercode;
	private String username;
	private String drivername;
	private String drivernumber;
	private String startime;
	private String endtime;
	private String remark;
	private String creater;
	private String creatime;
	private String isdelete;
	
	@Id
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
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getDrivernumber() {
		return drivernumber;
	}
	public void setDrivernumber(String drivernumber) {
		this.drivernumber = drivernumber;
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
