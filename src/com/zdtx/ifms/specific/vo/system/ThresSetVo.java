package com.zdtx.ifms.specific.vo.system;

public class ThresSetVo {
	
	private Long threshold;	
	private Long typeid;	
	private String type;	
	private Long vehicletypeid;	
	private String vehicletype;	
	private Double startvalue;	//number(15,6)
	private Double endvalue;	//number(15,6)
	private Long usetime;	// default  s
	private String creater;
	private String creatime;
	private String isdelete;//default  'F'
	
	public Long getThreshold() {
		return threshold;
	}
	public void setThreshold(Long threshold) {
		this.threshold = threshold;
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
	public Double getStartvalue() {
		return startvalue;
	}
	public void setStartvalue(Double startvalue) {
		this.startvalue = startvalue;
	}
	public Double getEndvalue() {
		return endvalue;
	}
	public void setEndvalue(Double endvalue) {
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
		return "ThresSet [threshold=" + threshold + ", typeid=" + typeid
				+ ", type=" + type + ", vehicletypeid=" + vehicletypeid
				+ ", vehicletype=" + vehicletype + ", startvalue=" + startvalue
				+ ", endvalue=" + endvalue + ", usetime=" + usetime
				+ ", creater=" + creater + ", creatime=" + creatime
				+ ", isdelete=" + isdelete + "]";
	}
	
}