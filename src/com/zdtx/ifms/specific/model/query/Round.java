package com.zdtx.ifms.specific.model.query;

import java.io.Serializable;

public class Round  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6266422794262520943L;
	private Double longitude;
	private Double latitude;
	private String beh_name;
	private String riqi;
	private String time_begin;
	private String time_end;
	private String beh_type;
	private String time_cont;
	private Long veh_speed;
	private Long turn_speed;
	private Long not_id;
	private String gpsDateTime;
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
	public String getBeh_name() {
		return beh_name;
	}
	public void setBeh_name(String beh_name) {
		this.beh_name = beh_name;
	}
	public String getRiqi() {
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}
	public String getTime_begin() {
		return time_begin;
	}
	public void setTime_begin(String time_begin) {
		this.time_begin = time_begin;
	}
	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	public String getBeh_type() {
		return beh_type;
	}
	public void setBeh_type(String beh_type) {
		this.beh_type = beh_type;
	}
	public String getTime_cont() {
		return time_cont;
	}
	public void setTime_cont(String time_cont) {
		this.time_cont = time_cont;
	}
	public Long getVeh_speed() {
		return veh_speed;
	}
	public void setVeh_speed(Long veh_speed) {
		this.veh_speed = veh_speed;
	}
	public Long getTurn_speed() {
		return turn_speed;
	}
	public void setTurn_speed(Long turn_speed) {
		this.turn_speed = turn_speed;
	}
	public Long getNot_id() {
		return not_id;
	}
	public void setNot_id(Long not_id) {
		this.not_id = not_id;
	}
	public String getGpsDateTime() {
		return gpsDateTime;
	}
	public void setGpsDateTime(String gpsDateTime) {
		this.gpsDateTime = gpsDateTime;
	}
	@Override
	public String toString() {
		return "Round [longitude=" + longitude + ", latitude=" + latitude
				+ ", beh_name=" + beh_name + ", riqi=" + riqi + ", time_begin="
				+ time_begin + ", time_end=" + time_end + ", beh_type="
				+ beh_type + ", time_cont=" + time_cont + ", veh_speed="
				+ veh_speed + ", turn_speed=" + turn_speed + ", not_id="
				+ not_id + ", gpsDateTime=" + gpsDateTime + "]";
	}
	
	
}
