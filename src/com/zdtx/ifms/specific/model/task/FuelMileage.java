package com.zdtx.ifms.specific.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Fuel Mileage
 * 
 * @author JiangHaiquan
 * @since 2013-05-03 09:40:15
 */
@Entity
@Table(name = "t_tmp_fuel_mileage")
public class FuelMileage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long vehicleid; // vehicle id
	private String vehiclename; // vehicle name
	private Long typeid; // type id
	private String typename; // type
	private Double mileage; // mileage
	private Double cost; // cost
	private String startdate; // start date
	private String enddate; // end date

	@Id
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

	@Override
	public String toString() {
		return "FuelMileage [vehicleid=" + vehicleid + ", vehiclename="
				+ vehiclename + ", typeid=" + typeid + ", typename=" + typename
				+ ", mileage=" + mileage + ", cost=" + cost + ", startdate="
				+ startdate + ", enddate=" + enddate + "]";
	}

}
