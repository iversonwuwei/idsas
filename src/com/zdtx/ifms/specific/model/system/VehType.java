package com.zdtx.ifms.specific.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 车辆类型表
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@Entity
@Table(name = "T_CORE_VEH_TYPE")
public class VehType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long typeid;
	private String type;
	private String isdelete;
	private String description;
	private String icon;
	private String creater;
	private String creatime;
	private Long comid;
	private String comname;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_VEH_TYPE")
	@SequenceGenerator(name = "S_CORE_VEH_TYPE", sequenceName = "S_CORE_VEH_TYPE", allocationSize = 1)
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

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "VehType [typeid=" + typeid + ", type=" + type + ", isdelete=" + isdelete + ", description=" + description + ", icon=" + icon + ", creater=" + creater + ", creatime=" + creatime + "]";
	}

	public Long getComid() {
		return comid;
	}

	public void setComid(Long comid) {
		this.comid = comid;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

}
