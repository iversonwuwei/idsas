/**
 * @File: ViolationsPkmVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.comparison
 */
package com.zdtx.ifms.specific.vo.comparison;

import java.io.Serializable;

/**
 * @ClassName: ViolationsPkmVO
 * @Description: comparison-Violations Per KM-value object
 * @author: Leon Liu
 * @date: 2013-9-26 16:18:32
 * @version V1.0
 */
public class ViolationsPkmVO implements Serializable {

	private static final long serialVersionUID = 2523057761971363227L;
	
	private Double behCount;
	private Long driverID;
	private String driverName;
	private String riqi;

	public Double getBehCount() {
		return behCount;
	}

	public void setBehCount(Double behCount) {
		this.behCount = behCount;
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
		return "ViolationsPkmVO [behCount=" + behCount + ", driverID="
				+ driverID + ", driverName=" + driverName + ", riqi=" + riqi
				+ "]";
	}
}