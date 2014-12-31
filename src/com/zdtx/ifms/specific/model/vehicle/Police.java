/**
 * @path com.zdtx.ifms.specific.model.vehicle
 * @file Police.java
 */
package com.zdtx.ifms.specific.model.vehicle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @description Orgnization management-Police-entity
 * @author Liu Jun
 * @since 2014年6月10日 上午10:40:32
 */
@Entity
@Table(name = "T_CORE_POLICE")
public class Police implements Serializable {

	private static final long serialVersionUID = 3366607119976985387L;

	private Long policeID; // 主键ID 序列：S_CORE_POLICE
	private String policeName; // 持有者姓名
	private Long deptID; // 部门ID
	private String deptName; // 部门名称
	private Long fleetID; // 车队ID
	private String fleetName; // 车队名称
	private Long deviceID; // 设备ID
	private String deviceName; // 设备名称
	private Integer gender; // 性别，0：不显示；1：男；2：女;
	private String email; // email
	private String phone; // 电话
	private String description; // 描述
	private String isDelete; // 是否删除
	private String creater;
	private String createTime;

	@Id
	@SequenceGenerator(name = "S_CORE_POLICE", sequenceName = "S_CORE_POLICE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_POLICE")
	@Column(name = "POLICEID", nullable = false, unique = true)
	public Long getPoliceID() {
		return policeID;
	}

	public void setPoliceID(Long policeID) {
		this.policeID = policeID;
	}

	@Column(name = "POLICENAME")
	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	@Column(name = "DEPARTMENTID")
	public Long getDeptID() {
		return deptID;
	}

	public void setDeptID(Long deptID) {
		this.deptID = deptID;
	}

	@Column(name = "DEPARTMENTNAME")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "FLEETID")
	public Long getFleetID() {
		return fleetID;
	}

	public void setFleetID(Long fleetID) {
		this.fleetID = fleetID;
	}

	@Column(name = "FLEETNAME")
	public String getFleetName() {
		return fleetName;
	}

	public void setFleetName(String fleetName) {
		this.fleetName = fleetName;
	}

	@Column(name = "DEVICEID")
	public Long getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(Long deviceID) {
		this.deviceID = deviceID;
	}

	@Column(name = "DEVICENAME")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Column(name = "GENDER")
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "ISDELETE")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "CREATER")
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Column(name = "CREATETIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Police [policeID=" + policeID + ", policeName=" + policeName
				+ ", deptID=" + deptID + ", deptName=" + deptName
				+ ", fleetID=" + fleetID + ", fleetName=" + fleetName
				+ ", deviceID=" + deviceID + ", deviceName=" + deviceName
				+ ", gender=" + gender + ", email=" + email + ", phone="
				+ phone + ", description=" + description + ", isDelete="
				+ isDelete + ", creater=" + creater + ", createTime="
				+ createTime + "]";
	}
}