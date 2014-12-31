package com.zdtx.ifms.specific.vo.query;

public class TrackVO {
	private Long busid;
	private Long fleetid;
	private  String datemin;
	private String datemax;
	public Long getBusid() {
		return busid;
	}
	public void setBusid(Long busid) {
		this.busid = busid;
	}
	public Long getFleetid() {
		return fleetid;
	}
	public void setFleetid(Long fleetid) {
		this.fleetid = fleetid;
	}
	public String getDatemin() {
		return datemin;
	}
	public void setDatemin(String datemin) {
		this.datemin = datemin;
	}
	public String getDatemax() {
		return datemax;
	}
	public void setDatemax(String datemax) {
		this.datemax = datemax;
	}
	
}
