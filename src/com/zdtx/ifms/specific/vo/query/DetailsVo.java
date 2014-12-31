package com.zdtx.ifms.specific.vo.query;

import com.zdtx.ifms.common.utils.DateUtil;

public class DetailsVo {

	private Long notId; // 主键
	private Long orgId; // 车队ID
	private Long deptId; // 车队ID
	private String orgName; // 车队名称
	private Long vehId; // 车辆id
	private String vehcode; // 车牌照号
	private Long driverId; // 车辆id
	private String driverName;// 日期
	private String riqi;// 日期
	private String timeBegin;// 开始时间
	private String timeEnd;// 结束时间
	private String timeCont;// 持续时间
	private Long behType;// 事件类型：取自数字字典
	private Long behValue;// 事件值
	private Long vehSpead;// 车速
	private Long turnSpead;// 转速
	private String addr;// 地理位置
	private String locus;// 历史轨迹
    private String moth=DateUtil.getNextMonth();
    private String behCont;// 事件持续时间
	private String date1;// 以下是四个类型用于传值的name
	private String date2;
	private String date3;
	private String date4;
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

	public Long getVehSpead() {
		return vehSpead;
	}

	public void setVehSpead(Long vehSpead) {
		this.vehSpead = vehSpead;
	}

	public Long getTurnSpead() {
		return turnSpead;
	}

	public void setTurnSpead(Long turnSpead) {
		this.turnSpead = turnSpead;
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

	public String getMoth() {
		return moth;
	}

	public void setMoth(String moth) {
		this.moth = moth;
	}

	public String getBehCont() {
		return behCont;
	}

	public void setBehCont(String behCont) {
		this.behCont = behCont;
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

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Override
	public String toString() {
		return "DetailsVo [notId=" + notId + ", orgId=" + orgId + ", deptId="
				+ deptId + ", orgName=" + orgName + ", vehId=" + vehId
				+ ", vehcode=" + vehcode + ", driverId=" + driverId + ", riqi="
				+ riqi + ", timeBegin=" + timeBegin + ", timeEnd=" + timeEnd
				+ ", timeCont=" + timeCont + ", behType=" + behType
				+ ", behValue=" + behValue + ", vehSpead=" + vehSpead
				+ ", turnSpead=" + turnSpead + ", addr=" + addr + ", locus="
				+ locus + ", moth=" + moth + ", behCont=" + behCont
				+ ", date1=" + date1 + ", date2=" + date2 + ", date3=" + date3
				+ ", date4=" + date4 + "]";
	}
}