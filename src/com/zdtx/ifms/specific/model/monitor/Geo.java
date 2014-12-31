/**
 * @File: Geo.java
 * @path: ifms - com.zdtx.ifms.specific.model.monitor
 */
package com.zdtx.ifms.specific.model.monitor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @ClassName: Geo
 * @Description: monitor-Geo-Model
 * @author: Leon Liu
 * @date: 2013-4-28 AM11:16:35
 * @version V1.0
 */
@Entity
@Table(name = "T_JK_GEO")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Geo implements Serializable {

	private static final long serialVersionUID = 313059443105363107L;

	private Long fleetID;
	private String fleetName;
	private String terminalNo;
	private String lisencePlate;
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

	@Column(name = "O_LINENO", nullable = false)
	public Long getFleetID() {
		return fleetID;
	}

	public void setFleetID(Long fleetID) {
		this.fleetID = fleetID;
	}

	@Column(name = "O_LINENAME")
	public String getFleetName() {
		return fleetName;
	}

	public void setFleetName(String fleetName) {
		this.fleetName = fleetName;
	}

	@Column(name = "O_TERMINALNO")
	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	@Column(name = "O_BUSNAME")
	public String getLisencePlate() {
		return lisencePlate;
	}

	public void setLisencePlate(String lisencePlate) {
		this.lisencePlate = lisencePlate;
	}

	@Column(name = "O_GPSDATETIME")
	public String getGpsDateTime() {
		return gpsDateTime;
	}

	public void setGpsDateTime(String gpsDateTime) {
		this.gpsDateTime = gpsDateTime;
	}

	@Column(name = "O_DATE")
	public String getGpsDate() {
		return gpsDate;
	}

	public void setGpsDate(String gpsDate) {
		this.gpsDate = gpsDate;
	}

	@Column(name = "O_TIME")
	public String getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}

	@Column(name = "O_LONGITUDE", precision = 6)
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "O_LATITUDE", precision = 6)
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "O_SPEED")
	public Long getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed = speed;
	}

	@Column(name = "O_DIRECTION")
	public Long getDirection() {
		return direction;
	}

	public void setDirection(Long direction) {
		this.direction = direction;
	}

	@Column(name = "O_HEIGHT")
	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	@Column(name = "O_RUNSTATUS")
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Column(name = "O_MIDDOOR")
	public Long getMidDoor() {
		return midDoor;
	}

	public void setMidDoor(Long midDoor) {
		this.midDoor = midDoor;
	}

	@Column(name = "O_REARDOOR")
	public Long getReaDoor() {
		return reaDoor;
	}

	public void setReaDoor(Long reaDoor) {
		this.reaDoor = reaDoor;
	}

	@Column(name = "O_FRONTDOOR")
	public Long getFrontDoor() {
		return frontDoor;
	}

	public void setFrontDoor(Long frontDoor) {
		this.frontDoor = frontDoor;
	}

	@Column(name = "O_UP")
	public Long getUp() {
		return up;
	}

	public void setUp(Long up) {
		this.up = up;
	}

	@Column(name = "O_RUN")
	public Long getRun() {
		return run;
	}

	public void setRun(Long run) {
		this.run = run;
	}

	@Column(name = "O_NEXTSTATIONNO")
	public Long getNextStation() {
		return nextStation;
	}

	public void setNextStation(Long nextStation) {
		this.nextStation = nextStation;
	}

	@Column(name = "O_MOTORTEMP")
	public Long getMotorTemp() {
		return motorTemp;
	}

	public void setMotorTemp(Long motorTemp) {
		this.motorTemp = motorTemp;
	}

	@Column(name = "O_INDOORTEMP")
	public Long getInDoorTemp() {
		return inDoorTemp;
	}

	public void setInDoorTemp(Long inDoorTemp) {
		this.inDoorTemp = inDoorTemp;
	}

	@Column(name = "O_DISTANCE")
	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	@Column(name = "O_HISTORYDATA")
	public Long getHistory() {
		return history;
	}

	public void setHistory(Long history) {
		this.history = history;
	}

	@Column(name = "O_RESERVED")
	public Long getReserved() {
		return reserved;
	}

	public void setReserved(Long reserved) {
		this.reserved = reserved;
	}

	@Column(name = "O_SENDTIME")
	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	@Id
	@Column(name = "O_RECEIVETIME")
	public Long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Long receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "O_TOTALMILE")
	public Long getTotalMile() {
		return totalMile;
	}

	public void setTotalMile(Long totalMile) {
		this.totalMile = totalMile;
	}

	@Override
	public String toString() {
		return "Geo [fleetID=" + fleetID + ", fleetName=" + fleetName
				+ ", terminalNo=" + terminalNo + ", lisencePlate="
				+ lisencePlate + ", gpsDateTime=" + gpsDateTime + ", gpsDate="
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