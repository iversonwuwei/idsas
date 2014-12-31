package com.zdtx.ifms.specific.model.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: PoiConf
 * @Description: System Settings-POI-PO Class
 * @author jhq
 * @date 2013-04-27 10:03:12
 * @version V1.0
 */

@Entity
@Table(name = "T_CORE_POI")
public class PoiConf implements Serializable {

	private static final long serialVersionUID = -130382574152939155L;

	private Long poiid; // id
	private String caption;// caption
	private Double longitude; // longitude
	private Double latitude; // latitude
	private String icon; // iconurl
	private String isvisible; // isvisible default 'F'
	private String description;// description
	private String isdelete;// isdelete default 'F'
	private String creater;// creater
	private String creatime;// create time
	private Long comid;
	private String comname;

	@Id
	@SequenceGenerator(name = "S_CORE_POI", sequenceName = "S_CORE_POI", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_POI")
	@Column(name = "POIID", nullable = false, columnDefinition = "PrimaryKey")
	public Long getPoiid() {
		return poiid;
	}

	public void setPoiid(Long poiid) {
		this.poiid = poiid;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIsvisible() {
		return isvisible;
	}

	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public String toString() {
		return "PoiConf [poiid=" + poiid + ", caption=" + caption
				+ ", longitude=" + longitude + ", latitude=" + latitude
				+ ", icon=" + icon + ", isvisible=" + isvisible
				+ ", description=" + description + ", isdelete=" + isdelete
				+ ", creater=" + creater + ", creatime=" + creatime + "]";
	}

	public Long getComid() {
		return comid;
	}

	public void setComid(Long comid) {
		this.comid = comid;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

}