/**
 * @path com.zdtx.ifms.specific.vo.monitor
 * @file MediaVO.java
 */
package com.zdtx.ifms.specific.vo.monitor;

import java.io.Serializable;

/**
 * @description Monitor-Video Download-VO
 * @author Liu Jun
 * @since 2014年9月10日 上午11:34:31
 */
public class MediaVO implements Serializable {

	private static final long serialVersionUID = -1101054383234161061L;

	private Long label; // Primary Key:A unique integer
	private String triggerType; // Indicate the event trigger type
	private String mediaType; // Indicate the file media type
	private String destPath; // Indicate the file location in camera
	private String resolution; // Indicate the media file resolution
	private Boolean isLocked; // Indicate if the file is locked or not
	private String triggerTime; // Indicate the event trigger time. Format is “YYYY-MM-DD HH:MM:SS”
	private String beginTime;		//Format is “YYYY-MM-DD HH:MM:SS”
	private String endTime;		//Format is “YYYY-MM-DD HH:MM:SS”
	private Boolean backup; // Indicate if the file is generated when network loss

	public Long getLabel() {
		return label;
	}

	public void setLabel(Long label) {
		this.label = label;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}

	public Boolean getBackup() {
		return backup;
	}

	public void setBackup(Boolean backup) {
		this.backup = backup;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "MediaVO [label=" + label + ", triggerType=" + triggerType
				+ ", mediaType=" + mediaType + ", destPath=" + destPath
				+ ", resolution=" + resolution + ", isLocked=" + isLocked
				+ ", triggerTime=" + triggerTime + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + ", backup=" + backup + "]";
	}
}