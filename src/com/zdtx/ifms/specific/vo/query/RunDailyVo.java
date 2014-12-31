package com.zdtx.ifms.specific.vo.query;

import com.zdtx.ifms.common.utils.DateUtil;

/**
 * @ClassName: RunDailyVo
 * @Description: 车辆运行报告-车辆运行日报、月报、汇总报告-VO类
 * @author JHQ
 * @date 2012年12月4日 10:18:56
 * @version V1.0
 */
public class RunDailyVo {

	private Long day_id;// 主键id
	private Long org_id;// 车队id
	private String org_name;// 车队名称
	private Long veh_id;// 车辆id
	private String vehcode;// 车牌号
	private String riqi;// 日期: yyyy-mm-dd
	private String time_start;// 开始时间:hh24:mi:ss
	private String time_stop;// 停止时间 :hh24:mi:ss
	private String period_run;// 运行时长
	private String period_drive;// 驾驶时长
	private String period_idle;// 怠速时长
	private String period_air;// 空调开启时长
	private String period_hot;// 加热器开启时长
	private Long km_run;// 运行里程(km)
	private Long km_begin;// 开始里程(km)
	private Long km_end;// 结束里程(km)
	private Long spead_avg;// 平均速度（km/h）
	private Long fuel;// 燃料消耗（L）
	private Long fuel_avg;// 百公里平均油耗
	private Long driver_id;// 司机id
	private String driver_name;// 司机名称
	private String moth = DateUtil.getNextMonth(); // 月
	private Long daytype;// 时间类型
	private String date1;// 以下是四个类型用于传值的name
	private String date2;
	private String date3;
	private String date4;

	public Long getDay_id() {
		return day_id;
	}

	public void setDay_id(Long day_id) {
		this.day_id = day_id;
	}

	public Long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public Long getVeh_id() {
		return veh_id;
	}

	public void setVeh_id(Long veh_id) {
		this.veh_id = veh_id;
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

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_stop() {
		return time_stop;
	}

	public void setTime_stop(String time_stop) {
		this.time_stop = time_stop;
	}

	public String getPeriod_run() {
		return period_run;
	}

	public void setPeriod_run(String period_run) {
		this.period_run = period_run;
	}

	public String getPeriod_drive() {
		return period_drive;
	}

	public void setPeriod_drive(String period_drive) {
		this.period_drive = period_drive;
	}

	public String getPeriod_idle() {
		return period_idle;
	}

	public void setPeriod_idle(String period_idle) {
		this.period_idle = period_idle;
	}

	public String getPeriod_air() {
		return period_air;
	}

	public void setPeriod_air(String period_air) {
		this.period_air = period_air;
	}

	public String getPeriod_hot() {
		return period_hot;
	}

	public void setPeriod_hot(String period_hot) {
		this.period_hot = period_hot;
	}

	public Long getKm_run() {
		return km_run;
	}

	public void setKm_run(Long km_run) {
		this.km_run = km_run;
	}

	public Long getKm_begin() {
		return km_begin;
	}

	public void setKm_begin(Long km_begin) {
		this.km_begin = km_begin;
	}

	public Long getKm_end() {
		return km_end;
	}

	public void setKm_end(Long km_end) {
		this.km_end = km_end;
	}

	public Long getSpead_avg() {
		return spead_avg;
	}

	public void setSpead_avg(Long spead_avg) {
		this.spead_avg = spead_avg;
	}

	public Long getFuel() {
		return fuel;
	}

	public void setFuel(Long fuel) {
		this.fuel = fuel;
	}

	public Long getFuel_avg() {
		return fuel_avg;
	}

	public void setFuel_avg(Long fuel_avg) {
		this.fuel_avg = fuel_avg;
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

	public String getMoth() {
		return moth;
	}

	public void setMoth(String moth) {
		this.moth = moth;
	}

	public Long getDaytype() {
		return daytype;
	}

	public void setDaytype(Long daytype) {
		this.daytype = daytype;
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
		return "RunDailyVo [day_id=" + day_id + ", org_id=" + org_id + ", org_name=" + org_name + ", veh_id=" + veh_id + ", vehcode=" + vehcode + ", riqi=" + riqi + ", time_start=" + time_start
				+ ", time_stop=" + time_stop + ", period_run=" + period_run + ", period_drive=" + period_drive + ", period_idle=" + period_idle + ", period_air=" + period_air + ", period_hot="
				+ period_hot + ", km_run=" + km_run + ", km_begin=" + km_begin + ", km_end=" + km_end + ", spead_avg=" + spead_avg + ", fuel=" + fuel + ", fuel_avg=" + fuel_avg + ", driver_id="
				+ driver_id + ", driver_name=" + driver_name + ", moth=" + moth + ", daytype=" + daytype + ", date1=" + date1 + ", date2=" + date2 + ", date3=" + date3 + ", date4=" + date4 + "]";
	}

}
