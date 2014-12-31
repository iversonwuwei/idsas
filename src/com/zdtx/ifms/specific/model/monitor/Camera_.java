package com.zdtx.ifms.specific.model.monitor;

public class Camera_ {
	private String cameraid;		//主键
	private String cameraname;	//摄像头名
	private String cameracode;	//摄像头编号，不许重复	
		
	public String getCameraid() {
		return cameraid;
	}
	public void setCameraid(String cameraid) {
		this.cameraid = cameraid;
	}
	public String getCameraname() {
		return cameraname;
	}
	public void setCameraname(String cameraname) {
		this.cameraname = cameraname;
	}
	public String getCameracode() {
		return cameracode;
	}
	public void setCameracode(String cameracode) {
		this.cameracode = cameracode;
	}
}
