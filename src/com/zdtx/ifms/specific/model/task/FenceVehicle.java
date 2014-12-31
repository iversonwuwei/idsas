package com.zdtx.ifms.specific.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 电子围栏车辆表
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@Entity
@Table(name = "t_ifms_geo_veh")
public class FenceVehicle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long geovehid;	//		id: s_ifms_geo_veh
	private Long geofencingid;	//		t_ifms_geofencing.geofencingid
	private Long vehicleid;	//		vehicleid
	private String vehiclename;//		vehicle name
	private String licenseplate;//		license plate
	
	
 
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
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_geo_veh")
	@SequenceGenerator(name = "s_ifms_geo_veh", sequenceName = "s_ifms_geo_veh", allocationSize = 1)
	public Long getGeovehid() {
		return geovehid;
	}
	public void setGeovehid(Long geovehid) {
		this.geovehid = geovehid;
	}
	public Long getGeofencingid() {
		return geofencingid;
	}
	public void setGeofencingid(Long geofencingid) {
		this.geofencingid = geofencingid;
	}
	@Override
	public String toString() {
		return "FenceVehicle [geovehid=" + geovehid + ", geofencingid="
				+ geofencingid + ", vehicleid=" + vehicleid + ", vehiclename="
				+ vehiclename + ", licenseplate=" + licenseplate + "]";
	}
 


	 
}
