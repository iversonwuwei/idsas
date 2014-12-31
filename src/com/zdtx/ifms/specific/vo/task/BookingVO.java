package com.zdtx.ifms.specific.vo.task;

public class BookingVO {

	private Long vehicleid;
	private String driverid;
	private String route;
	private String duty;
	private String startTimeMin;
	private String startTimeMax;
	private String endTimeMin;
	private String endTimeMax;
	private String bookingdate;
	private String destination;
 
	
	
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
 
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getStartTimeMin() {
		return startTimeMin;
	}
	public void setStartTimeMin(String startTimeMin) {
		this.startTimeMin = startTimeMin;
	}
	public String getStartTimeMax() {
		return startTimeMax;
	}
	public void setStartTimeMax(String startTimeMax) {
		this.startTimeMax = startTimeMax;
	}
	public String getEndTimeMin() {
		return endTimeMin;
	}
	public void setEndTimeMin(String endTimeMin) {
		this.endTimeMin = endTimeMin;
	}
	public String getEndTimeMax() {
		return endTimeMax;
	}
	public void setEndTimeMax(String endTimeMax) {
		this.endTimeMax = endTimeMax;
	}
	
	
	public String getBookingdate() {
		return bookingdate;
	}
	public void setBookingdate(String bookingdate) {
		this.bookingdate = bookingdate;
	}
	
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
 
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	 
	
}
