package com.zdtx.ifms.specific.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * 电子围栏经纬度表
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@Entity
@Table(name = "t_ifms_geo_lati")
public class FenceDetail implements Serializable {
	 
	private static final long serialVersionUID = 1L;

	private Long latiid;	// s_fence_detail
	private Long geofencingid;	//	t_fence.id
	private Double longitude;	//		经度
	private Double latitude;//	纬度
	private Integer ordno;//排序字段


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_geo_lati")
	@SequenceGenerator(name = "s_ifms_geo_lati", sequenceName = "s_ifms_geo_lati", allocationSize = 1)
	public Long getLatiid() {
		return latiid;
	}
	public void setLatiid(Long latiid) {
		this.latiid = latiid;
	}
	public Long getGeofencingid() {
		return geofencingid;
	}
	public void setGeofencingid(Long geofencingid) {
		this.geofencingid = geofencingid;
	}
 
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
	
	
 
	public Integer getOrdno() {
		return ordno;
	}
	public void setOrdno(Integer ordno) {
		this.ordno = ordno;
	}
	@Override
	public String toString() {
		return "FenceDetail [latiid=" + latiid + ", geofencingid="
				+ geofencingid + ", longitude=" + longitude + ", latitude="
				+ latitude + ", ordno=" + ordno + "]";
	}
	 
	 
}
