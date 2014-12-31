package com.zdtx.ifms.specific.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 电子围栏表
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@Entity
@Table(name = "t_ifms_booking")
public class Booking implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long bookingid;	//	id ： s_ifms_booking
	private String bookingdate;	//			booking date
	private String starttime;//		start time
	private String endtime;	//			end time
	private Long vehicletypeid;//		vehicle typeid
	private String vehicletype;//		vehicle type
	private String destination;	//		destination
	private String route;	//		route
	private String duty;	//		duty
	private String isdelete="F";//	'f'		isdelete
	private String creater;//		creater
	private String creatime;//		create time

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_booking")
	@SequenceGenerator(name = "s_ifms_booking", sequenceName = "s_ifms_booking", allocationSize = 1)
	public Long getBookingid() {
		return bookingid;
	}
	public void setBookingid(Long bookingid) {
		this.bookingid = bookingid;
	}
	public String getBookingdate() {
		return bookingdate;
	}
	public void setBookingdate(String bookingdate) {
		this.bookingdate = bookingdate;
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
	public Long getVehicletypeid() {
		return vehicletypeid;
	}
	public void setVehicletypeid(Long vehicletypeid) {
		this.vehicletypeid = vehicletypeid;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
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
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreatime() {
		return creatime;
	}
	public void setCreatime(String creatime) {
		this.creatime = creatime;
	}	 

}
