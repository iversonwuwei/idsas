package com.zdtx.ifms.specific.model.system;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: DeviceStatus
 * @Description: 设备状态
 * @author yqs
 * @date 2012-9-5 下午05:17:43
 * @version V1.0
 */

@Entity
@Table(name = "T_CORE_DEVICESTATUS")
public class DeviceStatus implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3926636969108714959L;
	private Long statusid;	
	private String statusname;	
	private String memo;	
	private String isdelete;	
	private String creater;	
	private String creatime;
	
	@Id
	@SequenceGenerator(name = "s_core_devicestatus", sequenceName = "s_core_devicestatus", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_core_devicestatus")
	public Long getStatusid() {
		return statusid;
	}
	public void setStatusid(Long statusid) {
		this.statusid = statusid;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
}