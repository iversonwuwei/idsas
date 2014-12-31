package com.zdtx.ifms.specific.vo.vehicle;

import java.io.Serializable;

/**
 * 司机VO类
 * 
 * @author LiuGuilong
 * @since 2012-04-27
 */
public class DriverVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String description;
	private String drivernumber;
	private Long driverid;
	private String drivername;
	private Long departmentid;

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

	public Long getDriverid() {
		return driverid;
	}

	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}

	@Override
	public String toString() {
		return "DriverVO [description=" + description + ", drivernumber=" + drivernumber + ", driverid=" + driverid + ", drivername=" + drivername + ", departmentid=" + departmentid + "]";
	}

}
