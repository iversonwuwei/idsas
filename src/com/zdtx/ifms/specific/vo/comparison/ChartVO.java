/**
 * @File: ChartVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.comparison
 */
package com.zdtx.ifms.specific.vo.comparison;

import java.util.Date;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Struts2Util;

 

/**
 * @ClassName: ChartVO
 * @Description: comparision-value object
 * @author: Leon Liu
 * @date: 2013-9-24 下午1:04:44
 * @version V1.0
 */
public class ChartVO {
	private Long DepartmentID = ((Long[])Struts2Util.getSession().getAttribute("userDepartment"))[0];	//默认选择权限下第一个部门
	private String driverIDs;
	private String beginDate = DateUtil.formatDate(new Date());
	private String endDate = DateUtil.formatDate(new Date());

	public Long getDepartmentID() {
		return DepartmentID;
	}

	public void setDepartmentID(Long departmentID) {
		DepartmentID = departmentID;
	}

	public String getDriverIDs() {
		return driverIDs;
	}

	public void setDriverIDs(String driverIDs) {
		this.driverIDs = driverIDs;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ChartVO [DepartmentID=" + DepartmentID + ", driverIDs="
				+ driverIDs + ", beginDate=" + beginDate + ", endDate="
				+ endDate + "]";
	}
}