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
@Table(name = "t_ifms_geofencing")
public class Fence implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long geofencingid;	// s_fence
	private String caption;	//			caption
	private String creater;	//		creater
	private String creatime;	//	create time  yyyy-mm-dd hh24:mi:ss
	private String isdelete="F";	//		isdelete :  f   t 

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_geofencing")
	@SequenceGenerator(name = "s_ifms_geofencing", sequenceName = "s_ifms_geofencing", allocationSize = 1)
	public Long getGeofencingid() {
		return geofencingid;
	}
	public void setGeofencingid(Long geofencingid) {
		this.geofencingid = geofencingid;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
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
	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	@Override
	public String toString() {
		return "Fence [geofencingid=" + geofencingid + ", caption=" + caption
				+ ", creater=" + creater + ", creatime=" + creatime
				+ ", isdelete=" + isdelete + "]";
	}
	 
	
	
}
