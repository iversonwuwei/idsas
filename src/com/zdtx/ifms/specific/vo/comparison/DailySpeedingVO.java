/**
 * @File: DailySpeedingVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.comparison
 */
package com.zdtx.ifms.specific.vo.comparison;

import java.io.Serializable;

/**
 * @ClassName: DailySpeedingVO
 * @Description: comparison-Daily Speeding Violations Per KM-value object
 * @author: Leon Liu
 * @date: 2013-9-26 下午3:38:02
 * @version V1.0
 */
public class DailySpeedingVO implements Serializable {

	private static final long serialVersionUID = -1182381106119871655L;

	private Double behSP;
	private Long driverID;
	private String driverName;
	private String riqi;

	public Double getBehSP() {
		return behSP;
	}

	public void setBehSP(Double behSP) {
		this.behSP = behSP;
	}

	public Long getDriverID() {
		return driverID;
	}

	public void setDriverID(Long driverID) {
		this.driverID = driverID;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	@Override
	public String toString() {
		return "DailySpeedingVO [behSP=" + behSP + ", driverID=" + driverID
				+ ", driverName=" + driverName + ", riqi=" + riqi + "]";
	}
}