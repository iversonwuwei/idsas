package com.zdtx.ifms.specific.model.query;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: Details
 * @Description: 不规范驾驶报告-不规范驾驶明细
 * @author JHQ
 * @date 2012年11月28日 16:28:27
 * @version V1.0
 */
@Entity
@Table(name = "T_BEH_NOT_STANDARD")
public class Details implements Serializable {

	private static final long serialVersionUID = 5459829412719975173L;

	private Long notId; // 主键
	private Long orgId; // 车队ID
	private String orgName; // 车队名称
	private Long vehId; // 车辆id
	private String vehcode; // 车牌照号
	private String riqi;// 日期
	private String timeBegin;// 开始时间
	private String timeEnd;// 结束时间
	private String timeCont;// 持续时间
	private Long behType;// 事件类型：取自数字字典
	private Long behValue;// 事件值
	private Long vehSpeed;// 车速
	private Long turnSpeed;// 转速
	private String addr;// 地理位置
	private String locus;// 历史轨迹
	private Long driver_id;
	private String driver_name;
	private Integer score;
	private Double longitude;
	private Double latitude;
	private String beh_name;
	private Long dept_id;
	private String dept_name;
	private Long geo_id;
	private String geo_caption;
	private String driverno;
	private String vehno;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "NOT_ID", columnDefinition = "主键", nullable = false)
	public Long getNotId() {
		return notId;
	}

	public void setNotId(Long notId) {
		this.notId = notId;
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

	public String getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getTimeCont() {
		return timeCont;
	}

	public void setTimeCont(String timeCont) {
		this.timeCont = timeCont;
	}

	public Long getBehType() {
		return behType;
	}

	public void setBehType(Long behType) {
		this.behType = behType;
	}

	public Long getBehValue() {
		return behValue;
	}

	public void setBehValue(Long behValue) {
		this.behValue = behValue;
	}

	public Long getVehSpeed() {
		return vehSpeed;
	}

	public void setVehSpeed(Long vehSpeed) {
		this.vehSpeed = vehSpeed;
	}

	public Long getTurnSpeed() {
		return turnSpeed;
	}

	public void setTurnSpeed(Long turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getLocus() {
		return locus;
	}

	public void setLocus(String locus) {
		this.locus = locus;
	}

	public Long getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(Long driver_id) {
		this.driver_id = driver_id;
	}

	public String getDriver_name() {
		return driver_name;
	}

	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	public String getBeh_name() {
		return beh_name;
	}

	public void setBeh_name(String beh_name) {
		this.beh_name = beh_name;
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

	public String getVehno() {
		return vehno;
	}

	public void setVehno(String vehno) {
		this.vehno = vehno;
	}

	@Override
	public String toString() {
		return "Details [notId=" + notId + ", orgId=" + orgId + ", orgName="
				+ orgName + ", vehId=" + vehId + ", vehcode=" + vehcode
				+ ", riqi=" + riqi + ", timeBegin=" + timeBegin + ", timeEnd="
				+ timeEnd + ", timeCont=" + timeCont + ", behType=" + behType
				+ ", behValue=" + behValue + ", vehSpeed=" + vehSpeed
				+ ", turnSpeed=" + turnSpeed + ", addr=" + addr + ", locus="
				+ locus + "]";
	}

}
