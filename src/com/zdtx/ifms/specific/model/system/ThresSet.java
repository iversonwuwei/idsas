package com.zdtx.ifms.specific.model.system;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: ThresSet
 * @Description: Threshold Setting
 * @author JiangHaiquan
 * @date 2013-05-03 20:26:14
 * @version V1.0
 */

@Entity
@Table(name = "T_CORE_THRESHOLD")
public class ThresSet implements Serializable {
	
	private static final long serialVersionUID = -130382574152939155L;
	
	private Long thresholdid;	
	private Long typeid;	
	private String type;	
	private Long vehicletypeid;	
	private String vehicletype;	
	private Long startvalue;	//number(15,6)
	private Long endvalue;	//number(15,6)
	private Long usetime;	// default  s
	private String creater;
	private String creatime;
	private String isdelete;//default  'F'
	
	@Id
	@SequenceGenerator(name = "S_CORE_THRESHOLD", sequenceName = "S_CORE_THRESHOLD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_THRESHOLD")
	public Long getThresholdid() {
		return thresholdid;
	}
	public void setThresholdid(Long thresholdid) {
		this.thresholdid = thresholdid;
	}
	public Long getTypeid() {
		return typeid;
	}
	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getVehicletypeid() {
		return vehicletypeid;
	}
	public void setVehicletypeid(Long vehicletypeid) {
		this.vehicletypeid = vehicletypeid;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public Long getStartvalue() {
		return startvalue;
	}
	public void setStartvalue(Long startvalue) {
		this.startvalue = startvalue;
	}
	public Long getEndvalue() {
		return endvalue;
	}
	public void setEndvalue(Long endvalue) {
		this.endvalue = endvalue;
	}
	public Long getUsetime() {
		return usetime;
	}
	public void setUsetime(Long usetime) {
		this.usetime = usetime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreatime() {
		return creatime;
	}
	public void setCreatime(String creatime) {
		this.creatime = creatime;
	}
	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	@Override
	public String toString() {
		return "ThresSet [thresholdid=" + thresholdid + ", typeid=" + typeid
				+ ", type=" + type + ", vehicletypeid=" + vehicletypeid
				+ ", vehicletype=" + vehicletype + ", startvalue=" + startvalue
				+ ", endvalue=" + endvalue + ", usetime=" + usetime
				+ ", creater=" + creater + ", creatime=" + creatime
				+ ", isdelete=" + isdelete + "]";
	}
	
}