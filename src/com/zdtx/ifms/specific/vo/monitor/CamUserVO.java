/**
 * @File: IpCamVO.java
 * @path: idsas - com.zdtx.ifms.specific.vo.monitor
 */
package com.zdtx.ifms.specific.vo.monitor;

import java.io.Serializable;

/**
 * @ClassName: CamUserVO
 * @Description: IP Camera's user value object
 * @author: Leon Liu
 * @date: 2013-7-25 上午11:03:44
 * @version V1.0
 */
public class CamUserVO implements Serializable {

	private static final long serialVersionUID = -6155012545081874359L;

	private String adminName;
	private String adminPass;
	private String operatorName;
	private String operatorPass;
	private String viewerName;
	private String viewerPass;

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminPass() {
		return adminPass;
	}

	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorPass() {
		return operatorPass;
	}

	public void setOperatorPass(String operatorPass) {
		this.operatorPass = operatorPass;
	}

	public String getViewerName() {
		return viewerName;
	}

	public void setViewerName(String viewerName) {
		this.viewerName = viewerName;
	}

	public String getViewerPass() {
		return viewerPass;
	}

	public void setViewerPass(String viewerPass) {
		this.viewerPass = viewerPass;
	}

	@Override
	public String toString() {
		return "CamUserVO [adminName=" + adminName + ", adminPass=" + adminPass
				+ ", operatorName=" + operatorName + ", operatorPass="
				+ operatorPass + ", viewerName=" + viewerName + ", viewerPass="
				+ viewerPass + "]";
	}
}