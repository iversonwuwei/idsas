package com.zdtx.ifms.specific.model.monitor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 最后在线位置
 * @author Liu Jun
 * @since 2014年8月9日 上午11:23:44
 */
@Entity
@Table(name = "T_JK_LASTGPSDATA")
public class Last implements Serializable {
	
	private static final long serialVersionUID = -5778338378008728537L;
	
	private Long o_busno;
	private String o_busname;
	private Long o_timelabel;
	private String o_date;
	private String o_time;
	private Double o_longitude;
	private Double o_latitude;
	private Double o_speed;
	private Double o_direction;
	private Double o_height;

	@Id
	public Long getO_busno() {
		return o_busno;
	}

	public void setO_busno(Long o_busno) {
		this.o_busno = o_busno;
	}

	public String getO_busname() {
		return o_busname;
	}

	public void setO_busname(String o_busname) {
		this.o_busname = o_busname;
	}

	public Long getO_timelabel() {
		return o_timelabel;
	}

	public void setO_timelabel(Long o_timelabel) {
		this.o_timelabel = o_timelabel;
	}

	public String getO_date() {
		return o_date;
	}

	public void setO_date(String o_date) {
		this.o_date = o_date;
	}

	public String getO_time() {
		return o_time;
	}

	public void setO_time(String o_time) {
		this.o_time = o_time;
	}

	public Double getO_longitude() {
		return o_longitude;
	}

	public void setO_longitude(Double o_longitude) {
		this.o_longitude = o_longitude;
	}

	public Double getO_latitude() {
		return o_latitude;
	}

	public void setO_latitude(Double o_latitude) {
		this.o_latitude = o_latitude;
	}

	public Double getO_speed() {
		return o_speed;
	}

	public void setO_speed(Double o_speed) {
		this.o_speed = o_speed;
	}

	public Double getO_direction() {
		return o_direction;
	}

	public void setO_direction(Double o_direction) {
		this.o_direction = o_direction;
	}

	public Double getO_height() {
		return o_height;
	}

	public void setO_height(Double o_height) {
		this.o_height = o_height;
	}
}