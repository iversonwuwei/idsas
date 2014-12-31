/**
 * @File: GeoVO.java
 * @path: ifms - com.zdtx.ifms.specific.vo.monitor
 */
package com.zdtx.ifms.specific.vo.monitor;

/**
 * @ClassName: GeoVO
 * @Description: monitor-display-value object
 * @author: Leon Liu
 * @date: 2013-4-28 15:10:07
 * @version V1.0
 */
public class DisplayVO {
	
	private Long vehID;
	private Long fleetID;
	private String vehicleName;
	private String plateNumber;
	private String beginDate;
	private String endDate;
	private String beginTime;
	private String endTime;
	private String fileName;
	private String channelID;

	public Long getVehID() {
		return vehID;
	}

	public void setVehID(Long vehID) {
		this.vehID = vehID;
	}

	public Long getFleetID() {
		return fleetID;
	}

	public void setFleetID(Long fleetID) {
		this.fleetID = fleetID;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	@Override
	public String toString() {
		return "DisplayVO [vehID=" + vehID + ", fleetID=" + fleetID
				+ ", vehicleName=" + vehicleName + ", plateNumber="
				+ plateNumber + ", beginDate=" + beginDate + ", endDate="
				+ endDate + ", beginTime=" + beginTime + ", endTime=" + endTime
				+ ", fileName=" + fileName + ", channelID=" + channelID + "]";
	}
}