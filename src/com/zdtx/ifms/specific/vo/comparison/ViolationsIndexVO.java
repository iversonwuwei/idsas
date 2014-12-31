/**
 * @File: ViolationsIndexVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.comparison
 */
package com.zdtx.ifms.specific.vo.comparison;

import java.io.Serializable;

/**
 * @ClassName: ViolationsIndexVO
 * @Description: comparison-Violations Index-value object
 * @author: Leon Liu
 * @date: 2013-9-27 上午9:58:08
 * @version V1.0
 */
public class ViolationsIndexVO implements Serializable {
	
	private static final long serialVersionUID = 1791391517817277477L;
	
	public String week;
	public Double behIndex;
	public Double behSA;
	public Double behSB;
	public Double behSL;
	public Double behSR;

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Double getBehIndex() {
		return behIndex;
	}

	public void setBehIndex(Double behIndex) {
		this.behIndex = behIndex;
	}

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

	@Override
	public String toString() {
		return "ViolationsIndexVO [week=" + week + ", behIndex=" + behIndex
				+ ", behSA=" + behSA + ", behSB=" + behSB + ", behSL=" + behSL
				+ ", behSR=" + behSR + "]";
	}
}