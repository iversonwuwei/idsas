/**
 * @File: Geo.java
 * @path: ifms - com.zdtx.ifms.specific.model.monitor
 */
package com.zdtx.ifms.specific.vo.monitor;

import java.io.Serializable;

/**
 * @ClassName: Geo
 * @Description: monitor-Geo-Model
 * @author: Leon Liu
 * @date: 2013-4-28 AM11:16:35
 * @version V1.0
 */
public class GeoVO implements Serializable {

	private static final long serialVersionUID = 313059443105363107L;

	private Long fleetID;
	private String fleetName;
	private String terminalNo;
	private String vehicleName;
	private String gpsDateTime;
	private String gpsDate;
	private String gpsTime;
	private Double longitude;
	private Double latitude;
	private Long speed;
	private Long direction;
	private Long height;
	private Long status;
	private Long midDoor;
	private Long reaDoor;
	private Long frontDoor;
	private Long up;
	private Long run;
	private Long nextStation;
	private Long motorTemp;
	private Long inDoorTemp;
	private Long distance;
	private Long history;
	private Long reserved;
	private Long sendTime;
	private Long receiveTime;
	private Long totalMile;

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

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getGpsDateTime() {
		return gpsDateTime;
	}

	public void setGpsDateTime(String gpsDateTime) {
		this.gpsDateTime = gpsDateTime;
	}

	public String getGpsDate() {
		return gpsDate;
	}

	public void setGpsDate(String gpsDate) {
		this.gpsDate = gpsDate;
	}

	public String getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Long getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed = speed;
	}

	public Long getDirection() {
		return direction;
	}

	public void setDirection(Long direction) {
		this.direction = direction;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getMidDoor() {
		return midDoor;
	}

	public void setMidDoor(Long midDoor) {
		this.midDoor = midDoor;
	}

	public Long getReaDoor() {
		return reaDoor;
	}

	public void setReaDoor(Long reaDoor) {
		this.reaDoor = reaDoor;
	}

	public Long getFrontDoor() {
		return frontDoor;
	}

	public void setFrontDoor(Long frontDoor) {
		this.frontDoor = frontDoor;
	}

	public Long getUp() {
		return up;
	}

	public void setUp(Long up) {
		this.up = up;
	}

	public Long getRun() {
		return run;
	}

	public void setRun(Long run) {
		this.run = run;
	}

	public Long getNextStation() {
		return nextStation;
	}

	public void setNextStation(Long nextStation) {
		this.nextStation = nextStation;
	}

	public Long getMotorTemp() {
		return motorTemp;
	}

	public void setMotorTemp(Long motorTemp) {
		this.motorTemp = motorTemp;
	}

	public Long getInDoorTemp() {
		return inDoorTemp;
	}

	public void setInDoorTemp(Long inDoorTemp) {
		this.inDoorTemp = inDoorTemp;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Long getHistory() {
		return history;
	}

	public void setHistory(Long history) {
		this.history = history;
	}

	public Long getReserved() {
		return reserved;
	}

	public void setReserved(Long reserved) {
		this.reserved = reserved;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public Long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Long getTotalMile() {
		return totalMile;
	}

	public void setTotalMile(Long totalMile) {
		this.totalMile = totalMile;
	}

	@Override
	public String toString() {
		return "GeoVO [fleetID=" + fleetID + ", fleetName=" + fleetName
				+ ", terminalNo=" + terminalNo + ", vehicleName="
				+ vehicleName + ", gpsDateTime=" + gpsDateTime + ", gpsDate="
				+ gpsDate + ", gpsTime=" + gpsTime + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", speed=" + speed
				+ ", direction=" + direction + ", height=" + height
				+ ", status=" + status + ", midDoor=" + midDoor + ", reaDoor="
				+ reaDoor + ", frontDoor=" + frontDoor + ", up=" + up
				+ ", run=" + run + ", nextStation=" + nextStation
				+ ", motorTemp=" + motorTemp + ", inDoorTemp=" + inDoorTemp
				+ ", distance=" + distance + ", history=" + history
				+ ", reserved=" + reserved + ", sendTime=" + sendTime
				+ ", receiveTime=" + receiveTime + ", totalMile=" + totalMile
				+ "]";
	}
}