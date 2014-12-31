/**
 * @File: DriverScorecardVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.comparison
 */
package com.zdtx.ifms.specific.vo.comparison;

import java.io.Serializable;

/**
 * @ClassName: DriverScorecardVO
 * @Description: comparison-Driver Scorecard-value object
 * @author: Leon Liu
 * @date: 2013-9-26 下午4:55:38
 * @version V1.0
 */
public class DriverScorecardVO implements Serializable {

	private static final long serialVersionUID = -900072996611456527L;

	private Double score;
	private Long driverID;
	private String driverName;
	private String riqi;

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
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
		return "DriverScorecardVO [score=" + score + ", driverID=" + driverID
				+ ", driverName=" + driverName + ", riqi=" + riqi + "]";
	}
}