package com.zdtx.ifms.specific.vo.system;

import java.io.Serializable;


/**
 * @ClassName: PoiConfVO
 * @Description: System Settings-POI-VO
 * @author Liu Jun
 * @date 2014-7-16 09:47:34
 * @version V1.1
 */
public class PoiConfVO implements Serializable {

	private static final long serialVersionUID = 3500667994202610255L;
	
	private Long poiID; // id
	private String caption="";// caption
	private Double longitude; // longitude
	private Double latitude; // latitude
	private String icon; // iconurl
	private String isVisible; // isvisible default 'F'
	private String description;// description
	private String isDelete;// isdelete default 'F'
	private String creater;// creater
	private String creatime;// create time
	private Long comID;

	public Long getPoiID() {
		return poiID;
	}

	public void setPoiID(Long poiID) {
		this.poiID = poiID;
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

	public String getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
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

	public Long getComID() {
		return comID;
	}

	public void setComID(Long comID) {
		this.comID = comID;
	}

	@Override
	public String toString() {
		return "PoiConfVo [poiID=" + poiID + ", caption=" + caption
				+ ", longitude=" + longitude + ", latitude=" + latitude
				+ ", icon=" + icon + ", isVisible=" + isVisible
				+ ", description=" + description + ", isDelete=" + isDelete
				+ ", creater=" + creater + ", creatime=" + creatime
				+ ", comID=" + comID + "]";
	}
}