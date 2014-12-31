/**
 * @File: IpCamVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.monitor
 */
package com.zdtx.ifms.specific.vo.monitor;

import java.io.Serializable;

/**
 * @ClassName: IpCamVO
 * @Description: IP Camera's value object
 * @author: Leon Liu
 * @date: 2013-7-8 下午2:51:18
 * @version V1.0
 */
public class IpCamVO implements Serializable {

	private static final long serialVersionUID = -148062449668100770L;
	
	private Long modelID;
	private String camCode;
	private String camID;
	private String camName;
	private String camIP;
	private String newName;
	private String newIP;
	private String date;
	private String time;
	private String etime;
	private String drivername;
	private Long departmentid;
	public String getCamID() {
		return camID;
	}

	public void setCamID(String camID) {
		this.camID = camID;
	}
	
	public String getCamName() {
		return camName;
	}

	public void setCamName(String camName) {
		this.camName = camName;
	}

	public String getCamIP() {
		return camIP;
	}

	public void setCamIP(String camIP) {
		this.camIP = camIP;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public String getNewIP() {
		return newIP;
	}
	
	public void setNewIP(String newIP) {
		this.newIP = newIP;
	}

	public Long getModelID() {
		return modelID;
	}
	
	public void setModelID(Long modelID) {
		this.modelID = modelID;
	}

	public String getCamCode() {
		return camCode;
	}

	public void setCamCode(String camCode) {
		this.camCode = camCode;
	}
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	@Override
	public String toString() {
		return "IpCamVO [modelID=" + modelID + ", camCode=" + camCode
				+ ", camID=" + camID + ", camName=" + camName + ", camIP="
				+ camIP + ", newName=" + newName + ", newIP=" + newIP + "]";
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
	
}