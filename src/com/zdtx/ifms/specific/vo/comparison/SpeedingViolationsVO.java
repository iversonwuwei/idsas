/**
 * @File: SpeedingViolationsVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.comparison
 */
package com.zdtx.ifms.specific.vo.comparison;

import java.io.Serializable;

/**
 * @ClassName: SpeedingViolationsVO
 * @Description: comparison-Speeding Violations-value object
 * @author: Leon Liu
 * @date: 2013-9-27 下午1:05:11
 * @version V1.0
 */
public class SpeedingViolationsVO implements Serializable {

	private static final long serialVersionUID = 7854944662998999615L;

	private String week;
	private Double sp1;
	private Double sp2;
	private Double sp3;
	private Double spv;

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Double getSp1() {
		return sp1;
	}

	public void setSp1(Double sp1) {
		this.sp1 = sp1;
	}

	public Double getSp2() {
		return sp2;
	}

	public void setSp2(Double sp2) {
		this.sp2 = sp2;
	}

	public Double getSp3() {
		return sp3;
	}

	public void setSp3(Double sp3) {
		this.sp3 = sp3;
	}

	public Double getSpv() {
		return spv;
	}

	public void setSpv(Double spv) {
		this.spv = spv;
	}

	@Override
	public String toString() {
		return "SpeedingViolationsVO [week=" + week + ", sp1=" + sp1 + ", sp2="
				+ sp2 + ", sp3=" + sp3 + ", spv=" + spv + "]";
	}
}