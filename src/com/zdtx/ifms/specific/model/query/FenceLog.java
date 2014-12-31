package com.zdtx.ifms.specific.model.query;

import java.io.Serializable;

public class FenceLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9007639942606307704L;
	/** 车辆名称 */
	private Long o_busno;
	/** 状态 */
	private Long o_curstate;
	/** 区域ID */
	private Long o_elecfenceid;
	/** 时间戳 */
	private Long o_timelabel;
	/** 操作日期 */
	private String o_date;
	/** 操作时间 */
	private String o_time;
	/** 经度/秒 */
	private Long o_ptx;
	/** 纬度/秒 */
	private Long o_pty;
	/** 线路 */
	private Long o_lineno;
	/** 运行方向 */
	private Long o_yxfx;
	/** 站点 */
	private Long o_stationno;

	public FenceLog() {
	}
	public Long getO_busno() {
		return o_busno;
	}

	public void setO_busno(Long o_busno) {
		this.o_busno = o_busno;
	}

	public Long getO_curstate() {
		return o_curstate;
	}

	public void setO_curstate(Long o_curstate) {
		this.o_curstate = o_curstate;
	}

	public Long getO_elecfenceid() {
		return o_elecfenceid;
	}

	public void setO_elecfenceid(Long o_elecfenceid) {
		this.o_elecfenceid = o_elecfenceid;
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

	public Long getO_ptx() {
		return o_ptx;
	}

	public void setO_ptx(Long o_ptx) {
		this.o_ptx = o_ptx;
	}

	public Long getO_pty() {
		return o_pty;
	}

	public void setO_pty(Long o_pty) {
		this.o_pty = o_pty;
	}

	public Long getO_lineno() {
		return o_lineno;
	}

	public void setO_lineno(Long o_lineno) {
		this.o_lineno = o_lineno;
	}

	public Long getO_yxfx() {
		return o_yxfx;
	}

	public void setO_yxfx(Long o_yxfx) {
		this.o_yxfx = o_yxfx;
	}

	public Long getO_stationno() {
		return o_stationno;
	}

	public void setO_stationno(Long o_stationno) {
		this.o_stationno = o_stationno;
	}
	@Override
	public String toString() {
		return "FenceLog [o_busno=" + o_busno + ", o_curstate=" + o_curstate
				+ ", o_elecfenceid=" + o_elecfenceid + ", o_timelabel="
				+ o_timelabel + ", o_date=" + o_date + ", o_time=" + o_time
				+ ", o_ptx=" + o_ptx + ", o_pty=" + o_pty + ", o_lineno="
				+ o_lineno + ", o_yxfx=" + o_yxfx + ", o_stationno="
				+ o_stationno + "]";
	}
	
}
