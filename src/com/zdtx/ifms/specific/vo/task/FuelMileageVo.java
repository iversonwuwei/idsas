package com.zdtx.ifms.specific.vo.task;

public class FuelMileageVo {

	private Long vehicleid; // vehicle id
	private String vehiclename; // vehicle name
	private Long typeid; // type id
	private String typename; // type
	private Double mileage; // mileage
	private Double cost; // cost
	private String startdate; // start date
	private String enddate; // end date
	private String starttime; // start time
	private String endtime; // end time

	public Long getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}

	public String getVehiclename() {
		return vehiclename;
	}

	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}

	public Long getTypeid() {
		return typeid;
	}

	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@Override
	public String toString() {
		return "FuelMileageVo [vehicleid=" + vehicleid + ", vehiclename="
				+ vehiclename + ", typeid=" + typeid + ", typename=" + typename
				+ ", mileage=" + mileage + ", cost=" + cost + ", startdate="
				+ startdate + ", enddate=" + enddate + ", starttime="
				+ starttime + ", endtime=" + endtime + "]";
	}


}
