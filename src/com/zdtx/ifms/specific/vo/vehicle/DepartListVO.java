/**
 * @File: FleetListVO.java
 * @path: ifms - com.zdtx.ifms.specific.vo.vehicle
 */
package com.zdtx.ifms.specific.vo.vehicle;

/**
 * @ClassName: DepartListVO
 * @Description: Vehicle-Department List-Value Object
 * @author: Leon Liu
 * @date: 2013-4-27 10:05:45
 * @version V1.0
 */
public class DepartListVO {
	private Long departID;
	private String departName;
	private Long parentID;
	private String parentName;
	
	public Long getDepartID() {
		return departID;
	}

	public void setDepartID(Long departID) {
		this.departID = departID;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public Long getParentID() {
		return parentID;
	}

	public void setParentID(Long parentID) {
		this.parentID = parentID;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Override
	public String toString() {
		return "DepartListVO [departID=" + departID + ", departName="
				+ departName + ", parentID=" + parentID + ", parentName="
				+ parentName + "]";
	}
}