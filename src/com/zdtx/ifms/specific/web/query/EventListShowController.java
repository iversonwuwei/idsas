package com.zdtx.ifms.specific.web.query;

import com.zdtx.ifms.common.web.URLSupport;

public class EventListShowController  extends URLSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3702751923296023893L;
	private String lng;
	private String lat;
	@Override
	public Object getModel() {
		return null;
	}
	public String index(){
		return "index";
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	

}
