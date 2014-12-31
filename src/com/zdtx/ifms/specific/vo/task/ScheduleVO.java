package com.zdtx.ifms.specific.vo.task;

public class ScheduleVO {

	private Long vehicleid;
	private String driverid;
	private String redriverid;
	private Long routeid;
	private String duty;
	private String timeMin;
	private String timeMax;
	private Long returntype;
	
	public Long getReturntype() {
		return returntype;
	}
	public void setReturntype(Long returntype) {
		this.returntype = returntype;
	}
	public Long getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}
	public String getDriverid() {
		return driverid;
	}
	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}
	public Long getRouteid() {
		return routeid;
	}
	public void setRouteid(Long routeid) {
		this.routeid = routeid;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getTimeMin() {
		return timeMin;
	}
	public void setTimeMin(String timeMin) {
		this.timeMin = timeMin;
	}
	public String getTimeMax() {
		return timeMax;
	}
	public void setTimeMax(String timeMax) {
		this.timeMax = timeMax;
	}
	@Override
	public String toString() {
		return "ScheduleVO [vehicleid=" + vehicleid + ", driverid=" + driverid
				+ ", routeid=" + routeid + ", duty=" + duty + ", timeMin="
				+ timeMin + ", timeMax=" + timeMax + "]";
	}
	public String getRedriverid() {
		return redriverid;
	}
	public void setRedriverid(String redriverid) {
		this.redriverid = redriverid;
	}
	
	
}
