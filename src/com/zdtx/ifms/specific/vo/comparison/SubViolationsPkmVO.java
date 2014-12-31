/**
 * @File: SubViolationsPkmVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.comparison
 */
package com.zdtx.ifms.specific.vo.comparison;

import java.io.Serializable;

/**
 * @ClassName: SubViolationsPkmVO
 * @Description: comparison-Sub-violations Per KM-value object
 * @author: Leon Liu
 * @date: 2013-9-25 11:01:13
 * @version V1.0
 */
public class SubViolationsPkmVO implements Serializable {

	private static final long serialVersionUID = -5294640151904733400L;

	private Double behSA;
	private Double behSB;
	private Double behSL;
	private Double behSR;
	private Long driverID;
	private String driverName;
	private String riqi;

	public Double getBehSA() {
		return behSA;
	}

	public void setBehSA(Double behSA) {
		this.behSA = behSA;
	}

	public Double getBehSB() {
		return behSB;
	}

	public void setBehSB(Double behSB) {
		this.behSB = behSB;
	}

	public Double getBehSL() {
		return behSL;
	}

	public void setBehSL(Double behSL) {
		this.behSL = behSL;
	}

	public Double getBehSR() {
		return behSR;
	}

	public void setBehSR(Double behSR) {
		this.behSR = behSR;
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
		return "SubViolationsPKMVO [behSA=" + behSA + ", behSB=" + behSB
				+ ", behSL=" + behSL + ", behSR=" + behSR + ", driverID="
				+ driverID + ", driverName=" + driverName + ", riqi=" + riqi
				+ "]";
	}
}