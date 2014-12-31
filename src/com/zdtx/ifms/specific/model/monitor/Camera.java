package com.zdtx.ifms.specific.model.monitor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.zdtx.ifms.specific.model.system.CamModel;

@Entity
@Table(name = "T_CORE_CAMERA")
public class Camera {
	private Long cameraID; // 主键
	private CamModel modelID; // 模块ID，外键关联摄像头模块基础表主键
	private String cameraName; // 摄像头名
	private String cameraCode; // 摄像头编号，不许重复
	private String mac; // 摄像头MAC地址，不许重复
	private String ipAddress; // 摄像头IP，不允许重复
	private String netmask; // 网络掩码
	private String gateway; // 网关
	private String adminName; // administrator用户名
	private String adminPass; // administrator密码
	private String creater; // 修改人
	private String creatime; // 修改时间
	private String isDelete; // 是否删除
	private String operatorName;	//operator用户名
	private String operatorPass;	//operator密码
	private String viewerName;	//viewer用户名
	private String viewerPass;	//viewer密码
	private Integer osd;	//是否显示OSD 0：不显示；1：显示
	private Long deptid;
	private String deptname;

	@Id
	@SequenceGenerator(name = "S_CORE_CAMERA", sequenceName = "S_CORE_CAMERA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_CAMERA")
	@Column(name = "CAMERAID", nullable = false)
	public Long getCameraID() {
		return cameraID;
	}

	public void setCameraID(Long cameraID) {
		this.cameraID = cameraID;
	}

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(targetEntity = CamModel.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "MODELID")
	public CamModel getModelID() {
		return modelID;
	}


	public void setModelID(CamModel modelID) {
		this.modelID = modelID;
	}

	@Column(name = "CAMERANAME")
	public String getCameraName() {
		return cameraName;
	}

	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}

	@Column(name = "CAMERACODE")
	public String getCameraCode() {
		return cameraCode;
	}

	public void setCameraCode(String cameraCode) {
		this.cameraCode = cameraCode;
	}

	@Column(name = "MAC")
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Column(name = "IPADDRESS")
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "NETMASK")
	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	@Column(name = "GATEWAY")
	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	@Column(name = "ADMINNAME")
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	@Column(name = "ADMINPASS")
	public String getAdminPass() {
		return adminPass;
	}

	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
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

	@Column(name = "ISDELETE")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "OPERATORNAME")
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Column(name = "OPERATORPASS")
	public String getOperatorPass() {
		return operatorPass;
	}

	public void setOperatorPass(String operatorPass) {
		this.operatorPass = operatorPass;
	}

	@Column(name = "VIEWERNAME")
	public String getViewerName() {
		return viewerName;
	}

	public void setViewerName(String viewerName) {
		this.viewerName = viewerName;
	}

	@Column(name = "VIEWERPASS")
	public String getViewerPass() {
		return viewerPass;
	}

	public void setViewerPass(String viewerPass) {
		this.viewerPass = viewerPass;
	}

	@Column(name = "OSD")
	public Integer getOsd() {
		return osd;
	}

	public void setOsd(Integer osd) {
		this.osd = osd;
	}

	@Override
	public String toString() {
		return "Camera [cameraID=" + cameraID + ", modelID=" + modelID
				+ ", cameraName=" + cameraName + ", cameraCode=" + cameraCode
				+ ", mac=" + mac + ", ipAddress=" + ipAddress + ", netmask="
				+ netmask + ", gateway=" + gateway + ", adminName=" + adminName
				+ ", adminPass=" + adminPass + ", creater=" + creater
				+ ", creatime=" + creatime + ", isDelete=" + isDelete
				+ ", operatorName=" + operatorName + ", operatorPass="
				+ operatorPass + ", viewerName=" + viewerName + ", viewerPass="
				+ viewerPass + ", osd=" + osd + "]";
	}

	public Long getDeptid() {
		return deptid;
	}

	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	
}