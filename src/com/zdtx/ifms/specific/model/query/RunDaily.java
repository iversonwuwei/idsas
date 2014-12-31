package com.zdtx.ifms.specific.model.query;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: RunDaily
 * @Description: 车辆运行报告-车辆运行日报、月报、汇总报告
 * @author JHQ
 * @date 2012年12月4日 10:09:32
 * @version V1.0
 */
@Entity
@Table(name = "T_BEH_VEH_RUN_DAY")
public class RunDaily {

	private Long day_id;// 主键id: 主键序列:S_BEH_VEH_RUN_DAY
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
	private Float km_run;// 运行里程(km)
	private Float km_begin;// 开始里程(km)
	private Float km_end;// 结束里程(km)
	private Float spead_avg;// 平均速度（km/h）
	private Float fuel;// 燃料消耗（L）
	private Float fuel_avg;// 百公里平均油耗
	private Long driver_id;// 司机id
	private String driver_name;// 司机名称

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_BEH_VEH_RUN_DAY")
	@SequenceGenerator(name = "S_BEH_VEH_RUN_DAY", sequenceName = "S_BEH_VEH_RUN_DAY", allocationSize = 1)
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

	public Float getKm_run() {
		return km_run;
	}

	public void setKm_run(Float km_run) {
		this.km_run = km_run;
	}

	public Float getKm_begin() {
		return km_begin;
	}

	public void setKm_begin(Float km_begin) {
		this.km_begin = km_begin;
	}

	public Float getKm_end() {
		return km_end;
	}

	public void setKm_end(Float km_end) {
		this.km_end = km_end;
	}

	public Float getSpead_avg() {
		return spead_avg;
	}

	public void setSpead_avg(Float spead_avg) {
		this.spead_avg = spead_avg;
	}

	public Float getFuel() {
		return fuel;
	}

	public void setFuel(Float fuel) {
		this.fuel = fuel;
	}

	public Float getFuel_avg() {
		return fuel_avg;
	}

	public void setFuel_avg(Float fuel_avg) {
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

	@Override
	public String toString() {
		return "RunDaily [day_id=" + day_id + ", org_id=" + org_id
				+ ", org_name=" + org_name + ", veh_id=" + veh_id
				+ ", vehcode=" + vehcode + ", riqi=" + riqi + ", time_start="
				+ time_start + ", time_stop=" + time_stop + ", period_run="
				+ period_run + ", period_drive=" + period_drive
				+ ", period_idle=" + period_idle + ", period_air=" + period_air
				+ ", period_hot=" + period_hot + ", km_run=" + km_run
				+ ", km_begin=" + km_begin + ", km_end=" + km_end
				+ ", spead_avg=" + spead_avg + ", fuel=" + fuel + ", fuel_avg="
				+ fuel_avg + ", driver_id=" + driver_id + ", driver_name="
				+ driver_name + "]";
	}
}
