package com.zdtx.ifms.specific.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_REP_FUEL_MILEAGE")
public class Mileageoil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1840481796291052335L;
	private Long mile_id;
	private String riqi;
	private Long vehicleid;
	private String vehiclename;
	private Long typeid;
	private String typename;
	private Double mileage;
	private Double oilcost;
	private Double oil;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FUEL_MILEAGE_ID")
	@SequenceGenerator(name = "SEQ_FUEL_MILEAGE_ID", sequenceName = "SEQ_FUEL_MILEAGE_ID", allocationSize = 1)
	public Long getMile_id() {
		return mile_id;
	}
	public void setMile_id(Long mile_id) {
		this.mile_id = mile_id;
	}
	public String getRiqi() {
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}
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
	public Double getOilcost() {
		return oilcost;
	}
	public void setOilcost(Double oilcost) {
		this.oilcost = oilcost;
	}
	public Double getOil() {
		return oil;
	}
	public void setOil(Double oil) {
		this.oil = oil;
	}
	
	
}
