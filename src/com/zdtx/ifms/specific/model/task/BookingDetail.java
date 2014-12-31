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
@Table(name = "t_ifms_booking_detail")
public class BookingDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long detailid;	//		id : s_ifms_booking_detail
	private Long bookingid;	//		mainid
	private Long vehicleid;	//			vehicleid
	private String vehiclename;	//			vehicle name
	private String licenseplate;//		license plate
	private Long driverid;	//		driverid
	private String drivernumber;	//			driver number
	private String drivername;	//		driver name
	private String isdelete;	//'f'		isdelete
	private String creater;	//		creater
	private String creatime;//			create time

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_booking_detail")
	@SequenceGenerator(name = "s_ifms_booking_detail", sequenceName = "s_ifms_booking_detail", allocationSize = 1)

	public Long getDetailid() {
		return detailid;
	}
	public void setDetailid(Long detailid) {
		this.detailid = detailid;
	}
	public Long getBookingid() {
		return bookingid;
	}
	public void setBookingid(Long bookingid) {
		this.bookingid = bookingid;
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
	public String getLicenseplate() {
		return licenseplate;
	}
	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}
	public Long getDriverid() {
		return driverid;
	}
	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}
	public String getDrivernumber() {
		return drivernumber;
	}
	public void setDrivernumber(String drivernumber) {
		this.drivernumber = drivernumber;
	}
	public String getDrivername() {
		return drivername;
	}
	public void setDrivername(String drivername) {
		this.drivername = drivername;
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
