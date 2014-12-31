package com.zdtx.ifms.specific.model.query;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: RepDaily
 * @Description: 不规范驾驶报告-不规范驾驶日报
 * @author JHQ
 * @date 2012年11月29日 11:03:47
 * @version V1.0
 */
@Entity
@Table(name = "T_BEH_NOT_DAY")
public class RepDaily implements Serializable {

	private static final long serialVersionUID = -2346269864032208935L;

	private Long dayId; // 主键
	private Long orgId; // 车队ID
	private String orgName; // 车队名称
	private Long vehId; // 车辆id
	private String vehcode; // 车牌照号
	private String riqi;// 日期
	private Long behType;// 事件类型：取自数字字典
	private String behName;// 事件类型名称
	private Integer behCount;// 事件次数
	private String behCont;// 事件持续时间
	private Long runScale;// 发动机转速绿区以外运行比例
	private Long driverId;// 司机ID
	private String driverName;// 司机名字
	private Integer score;
	private Long dept_id;
	private String dept_name;
	private Long geo_id;
	private String geo_caption;
	private String driverno;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DAY_ID", columnDefinition = "主键", nullable = false)
	public Long getDayId() {
		return dayId;
	}

	public void setDayId(Long dayId) {
		this.dayId = dayId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getVehId() {
		return vehId;
	}

	public void setVehId(Long vehId) {
		this.vehId = vehId;
	}

	public String getVehcode() {
		return vehcode;
	}

	public void setVehcode(String vehcode) {
		this.vehcode = vehcode;
	}

	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	public Long getBehType() {
		return behType;
	}

	public void setBehType(Long behType) {
		this.behType = behType;
	}

	public String getBehName() {
		return behName;
	}

	public void setBehName(String behName) {
		this.behName = behName;
	}

	public Integer getBehCount() {
		return behCount;
	}

	public void setBehCount(Integer behCount) {
		this.behCount = behCount;
	}

	public String getBehCont() {
		return behCont;
	}

	public void setBehCont(String behCont) {
		this.behCont = behCont;
	}

	public Long getRunScale() {
		return runScale;
	}

	public void setRunScale(Long runScale) {
		this.runScale = runScale;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getDept_id() {
		return dept_id;
	}

	public void setDept_id(Long dept_id) {
		this.dept_id = dept_id;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public Long getGeo_id() {
		return geo_id;
	}

	public void setGeo_id(Long geo_id) {
		this.geo_id = geo_id;
	}

	public String getGeo_caption() {
		return geo_caption;
	}

	public void setGeo_caption(String geo_caption) {
		this.geo_caption = geo_caption;
	}

	public String getDriverno() {
		return driverno;
	}

	public void setDriverno(String driverno) {
		this.driverno = driverno;
	}

	@Override
	public String toString() {
		return "RepDaily [dayId=" + dayId + ", orgId=" + orgId + ", orgName="
				+ orgName + ", vehId=" + vehId + ", vehcode=" + vehcode
				+ ", riqi=" + riqi + ", behType=" + behType + ", behName="
				+ behName + ", behCount=" + behCount + ", behCont=" + behCont
				+ ", runScale=" + runScale + ", driverId=" + driverId
				+ ", driverName=" + driverName + "]";
	}

}
