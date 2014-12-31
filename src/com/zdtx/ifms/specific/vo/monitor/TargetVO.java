package com.zdtx.ifms.specific.vo.monitor;

import java.io.Serializable;

/**
 * @description real-time 左侧菜单值对象
 * @author Liu Jun
 * @since 2014年8月8日 下午4:13:56
 */
public class TargetVO implements Serializable {

	private static final long serialVersionUID = 1151505642888356992L;

	private String targetID;
	private String fatherID;
	private String targetName;
	private String targetType;

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public String getFatherID() {
		return fatherID;
	}

	public void setFatherID(String fatherID) {
		this.fatherID = fatherID;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	@Override
	public String toString() {
		return "TargetVO [targetID=" + targetID + ", fatherID=" + fatherID
				+ ", targetName=" + targetName + ", targetType=" + targetType
				+ "]";
	}
}