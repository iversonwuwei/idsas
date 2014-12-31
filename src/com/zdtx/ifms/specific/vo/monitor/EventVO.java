/**
 * @File: Event.java
 * @path: idsas - com.zdtx.ifms.specific.model.monitor
 */
package com.zdtx.ifms.specific.vo.monitor;

import java.io.Serializable;

/**
 * @ClassName: Event
 * @Description: Event value object
 * @author: Leon Liu
 * @date: 2013-5-23 下午1:51:06
 * @version V1.0
 */
public class EventVO implements Serializable {

	private static final long serialVersionUID = 5529285559171673619L;

	private String vehicleName;
	private Double longitude;
	private Double latitude;
	private String eventDate;
	private String eventTime;
	private String dateTime;
	private Integer speed;
	private Integer upDown;

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
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

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Integer getUpDown() {
		return upDown;
	}

	public void setUpDown(Integer upDown) {
		this.upDown = upDown;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "EventVO [vehicleName=" + vehicleName + ", longitude="
				+ longitude + ", latitude=" + latitude + ", eventDate="
				+ eventDate + ", eventTime=" + eventTime + ", dateTime="
				+ dateTime + ", speed=" + speed + ", upDown=" + upDown + "]";
	}
}