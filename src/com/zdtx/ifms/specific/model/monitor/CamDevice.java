package com.zdtx.ifms.specific.model.monitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_CORE_CAMERA_DEVICE")
public class CamDevice {

	private Long cdID; // 主键
	private Long deviceID; // 设备ID
	private Long camID; // 摄像头ID
	private Long channel; // 通道号	cameraid+channel为唯一
	private String creater; // 修改人
	private String creatime; // 修改时间

	@Id
	@SequenceGenerator(name = "S_CAMERA_DEVICE", sequenceName = "S_CAMERA_DEVICE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CAMERA_DEVICE")
	@Column(name = "COREID", nullable = false, unique = true)
	public Long getCdID() {
		return cdID;
	}

	public void setCdID(Long cdID) {
		this.cdID = cdID;
	}

	@Column(name = "DEVICEID")
	public Long getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(Long deviceID) {
		this.deviceID = deviceID;
	}

	@Column(name = "CAMERAID")
	public Long getCamID() {
		return camID;
	}

	public void setCamID(Long camID) {
		this.camID = camID;
	}

	@Column(name = "CHANNEL")
	public Long getChannel() {
		return channel;
	}

	public void setChannel(Long channel) {
		this.channel = channel;
	}

	@Column(name = "CREATER")
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Column(name = "CREATIME")
	public String getCreatime() {
		return creatime;
	}

	public void setCreatime(String creatime) {
		this.creatime = creatime;
	}

	@Override
	public String toString() {
		return "CamDevice [cdID=" + cdID + ", deviceID=" + deviceID
				+ ", camID=" + camID + ", channel=" + channel + ", creater="
				+ creater + ", creatime=" + creatime + "]";
	}
}