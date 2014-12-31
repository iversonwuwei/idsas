package com.zdtx.ifms.specific.vo.analy;

public class PhenomenonVO {

	private String vehiclename;
	private String drivername;
	private String isremove;
	
	public String getVehiclename() {
		return vehiclename;
	}
	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}
	public String getDrivername() {
		return drivername;
	}
	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}
	public String getIsremove() {
		return isremove;
	}
	public void setIsremove(String isremove) {
		this.isremove = isremove;
	}
	@Override
	public String toString() {
		return "PhenomenonVO [vehiclename=" + vehiclename + ", drivername="
				+ drivername + ", isremove=" + isremove + "]";
	}
	
}
