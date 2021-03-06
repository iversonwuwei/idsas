package com.zdtx.ifms.specific.model.vehicle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 司机表视图类
 * 
 * @author LiuGuilong
 * @since 2012-04-27
 */
@Entity
@Table(name = "V_CORE_DRIVER")
public class DriverView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long driverid;
	private String drivernumber;
	private String drivername;
	private String gender;
	private String email;
	private Long departmentid;
	private String description;
	private String isdelete;
	private String creater;
	private String creatime;
	private String departmentname;
	private String type;//0 Normal 1 Temporary
	private String phone;

	@Id
	public Long getDriverid() {
		return driverid;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(Long departmentid) {
		this.departmentid = departmentid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getCreatime() {
		return creatime;
	}

	public void setCreatime(String creatime) {
		this.creatime = creatime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DriverView [driverid=" + driverid + ", drivernumber=" + drivernumber + ", drivername=" + drivername + ", gender=" + gender + ", email=" + email + ", departmentid=" + departmentid
				+ ", description=" + description + ", isdelete=" + isdelete + ", creater=" + creater + ", creatime=" + creatime + ", departmentname=" + departmentname + "]";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
