package com.zdtx.ifms.specific.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="T_CORE_SCORE")
public class Score implements Serializable{

	private static final long serialVersionUID = -6554961862978446680L;

	private Long scoreid;
	private Long typeid;
	private String type;
	private Long vehicletypeid;
	private String vehicletype;
	private Long maxcount;
	private Long deductpoints;
	private Long maxtime;
	private Long deductime;
	private Long weight;
	private Long ratio;
	private String isdelete;
	private String creater;
	private String creatime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_SCORE")
	@SequenceGenerator(name = "S_CORE_SCORE", sequenceName = "S_CORE_SCORE", allocationSize = 1)
	public Long getScoreid() {
		return scoreid;
	}
	public void setScoreid(Long scoreid) {
		this.scoreid = scoreid;
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
	public Long getMaxcount() {
		return maxcount;
	}
	public void setMaxcount(Long maxcount) {
		this.maxcount = maxcount;
	}
	public Long getDeductpoints() {
		return deductpoints;
	}
	public void setDeductpoints(Long deductpoints) {
		this.deductpoints = deductpoints;
	}
	public Long getMaxtime() {
		return maxtime;
	}
	public void setMaxtime(Long maxtime) {
		this.maxtime = maxtime;
	}
	public Long getDeductime() {
		return deductime;
	}
	public void setDeductime(Long deductime) {
		this.deductime = deductime;
	}
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public Long getRatio() {
		return ratio;
	}
	public void setRatio(Long ratio) {
		this.ratio = ratio;
	}
	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
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
	@Override
	public String toString() {
		return "Score [scoreid=" + scoreid + ", typeid=" + typeid + ", type="
				+ type + ", vehicletypeid=" + vehicletypeid + ", vehicletype="
				+ vehicletype + ", maxcount=" + maxcount + ", deductpoints="
				+ deductpoints + ", maxtime=" + maxtime + ", deductime="
				+ deductime + ", weight=" + weight + ", ratio=" + ratio
				+ ", isdelete=" + isdelete + ", creater=" + creater
				+ ", creatime=" + creatime + "]";
	}
}
