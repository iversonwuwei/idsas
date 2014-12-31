/**
 * @File: FleetListVO.java
 * @path: ifms - com.zdtx.ifms.specific.vo.vehicle
 */
package com.zdtx.ifms.specific.vo.vehicle;

/**
 * @ClassName: FleetListVO
 * @Description: 
 * @author: Leon Liu
 * @date: 2013-4-26 下午4:44:06
 * @version V1.1
 * @modify:add parent params at 2013-9-17 上午9:27:33 by Leon Liu
 */
public class FleetListVO {
	private Long parentID;
	private String parentName;
	private Long fleetID;
	private String fleetName;
	private Long placeNo;

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
	
	public Long getFleetID() {
		return fleetID;
	}
	
	public void setFleetID(Long fleetID) {
		this.fleetID = fleetID;
	}
	
	public String getFleetName() {
		return fleetName;
	}
	
	public void setFleetName(String fleetName) {
		this.fleetName = fleetName;
	}
	
	public Long getPlaceNo() {
		return placeNo;
	}
	
	public void setPlaceNo(Long placeNo) {
		this.placeNo = placeNo;
	}

	@Override
	public String toString() {
		return "FleetListVO [parentID=" + parentID + ", parentName="
				+ parentName + ", fleetID=" + fleetID + ", fleetName="
				+ fleetName + ", placeNo=" + placeNo + "]";
	}
}