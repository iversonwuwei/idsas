/**
 * @path com.zdtx.ifms.specific.vo.monitor
 * @file CamDeviceVO.java
 */
package com.zdtx.ifms.specific.vo.monitor;

import java.io.Serializable;

/**
 * @description
 * @author Liu Jun
 * @since 2014年8月15日 下午3:05:19
 */
public class CamDeviceVO implements Serializable {

	private static final long serialVersionUID = 1720314216892889767L;
	
	private Long deviceID;	//设备ID
	private Long camID;		//摄像头ID
	private String camIP;	//摄像头IP
	private Long camPort;		//摄像头端口号
	private Integer channel;		//摄像头通道号
	private String userName;		//摄像头用户名
	private String password;		//摄像头密码
	private Integer camType;		//摄像头类型	1 ipcamera,2 video server
	
	public Long getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(Long deviceID) {
		this.deviceID = deviceID;
	}

	public Long getCamID() {
		return camID;
	}

	public void setCamID(Long camID) {
		this.camID = camID;
	}

	public String getCamIP() {
		return camIP;
	}

	public void setCamIP(String camIP) {
		this.camIP = camIP;
	}

	public Long getCamPort() {
		return camPort;
	}

	public void setCamPort(Long camPort) {
		this.camPort = camPort;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCamType() {
		return camType;
	}

	public void setCamType(Integer camType) {
		this.camType = camType;
	}

	@Override
	public String toString() {
		return "CamDeviceVO [deviceID=" + deviceID + ", camID=" + camID
				+ ", camIP=" + camIP + ", camPort=" + camPort + ", channel="
				+ channel + ", userName=" + userName + ", password=" + password
				+ ", camType=" + camType + "]";
	}
}