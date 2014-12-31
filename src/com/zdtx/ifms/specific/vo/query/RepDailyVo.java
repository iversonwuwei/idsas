package com.zdtx.ifms.specific.vo.query;

public class RepDailyVo {

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
	private String date1;// 以下是四个类型用于传值的name
	private String date2;
	private String date3;
	private String date4;
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

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getDate3() {
		return date3;
	}

	public void setDate3(String date3) {
		this.date3 = date3;
	}

	public String getDate4() {
		return date4;
	}

	public void setDate4(String date4) {
		this.date4 = date4;
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
