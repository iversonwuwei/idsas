package com.zdtx.ifms.specific.model.query;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_BEH_VEH_RUN")
public class RunDetails {
	private Long run_id;
	private Long org_id;
	/** 车队名称 */
	private String org_name;
	/** 车辆id */
	private Long veh_id;
	/** 车牌号 */
	private String vehcode;
	/** 日期: yyyy-mm-dd */
	private String riqi;
	/** 启动时间:hh24:mi:ss */
	private String time_start;
	/** 起步时间 */
	private String time_begin;
	/** 停止时间 */
	private String time_end;
	/** 熄火时间 */
	private String time_stop;
	/** 运行时长 */
	private String period_run;
	/** 驾驶时长 */
	private String period_drive;
	/** 怠速时长 */
	private String period_idle;
	/** 空调开启时长 */
	private String period_air;
	/** 加热器开启时长 */
	private String period_hot;
	/** 运行里程(km) */
	private Double km_run;
	/** 开始里程(km) */
	private Double km_begin;
	/** 结束里程(km) */
	private Double km_end;
	/** 平均速度（km/h） */
	private Double spead_avg;
	/** 燃料消耗（L） */
	private Double fuel;
	/** 百公里平均油耗 */
	private Double fuel_avg;
	/** 司机id */
	private Long driver_id;
	/** 司机名称 */
	private String driver_name;

	@Id
	public Long getRun_id() {
		return run_id;
	}

	public void setRun_id(Long run_id) {
		this.run_id = run_id;
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

	public String getTime_begin() {
		return time_begin;
	}

	public void setTime_begin(String time_begin) {
		this.time_begin = time_begin;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
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

	public Double getKm_run() {
		return km_run;
	}

	public void setKm_run(Double km_run) {
		this.km_run = km_run;
	}

	public Double getKm_begin() {
		return km_begin;
	}

	public void setKm_begin(Double km_begin) {
		this.km_begin = km_begin;
	}

	public Double getKm_end() {
		return km_end;
	}

	public void setKm_end(Double km_end) {
		this.km_end = km_end;
	}

	public Double getSpead_avg() {
		return spead_avg;
	}

	public void setSpead_avg(Double spead_avg) {
		this.spead_avg = spead_avg;
	}

	public Double getFuel() {
		return fuel;
	}

	public void setFuel(Double fuel) {
		this.fuel = fuel;
	}

	public Double getFuel_avg() {
		return fuel_avg;
	}

	public void setFuel_avg(Double fuel_avg) {
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
		return "RunDetails [run_id=" + run_id + ", org_id=" + org_id + ", org_name=" + org_name + ", veh_id=" + veh_id + ", vehcode=" + vehcode + ", riqi=" + riqi + ", time_start=" + time_start
				+ ", time_begin=" + time_begin + ", time_end=" + time_end + ", time_stop=" + time_stop + ", period_run=" + period_run + ", period_drive=" + period_drive + ", period_idle="
				+ period_idle + ", period_air=" + period_air + ", period_hot=" + period_hot + ", km_run=" + km_run + ", km_begin=" + km_begin + ", km_end=" + km_end + ", spead_avg=" + spead_avg
				+ ", fuel=" + fuel + ", fuel_avg=" + fuel_avg + ", driver_id=" + driver_id + ", driver_name=" + driver_name + "]";
	}

}
