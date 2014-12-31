package com.zdtx.ifms.specific.model.query;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_TREE_DRIVER")
public class DriverTree implements Serializable{

	private static final long serialVersionUID = 7797291564297104586L;

	private Long driverid;
	private String drivernumber;
	private String drivername;
	private String gender;
	private String email;
	private Long departmentid;
	private String description;
	
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
	@Override
	public String toString() {
		return "DriverTree [driverid=" + driverid + ", drivernumber="
				+ drivernumber + ", drivername=" + drivername + ", gender="
				+ gender + ", email=" + email + ", departmentid="
				+ departmentid + ", description=" + description + "]";
	}
	
}
