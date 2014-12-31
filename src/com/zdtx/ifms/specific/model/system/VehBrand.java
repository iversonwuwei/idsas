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
@Table(name = "T_CORE_VEH_brand")
public class VehBrand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8006749428585411165L;
	private Long id;
	private String name;
	private String isdelete;
	private String memo;
	private String creater;
	private String creatime;
	private Long comid;
	private String comname;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_VEH_BRAND")
	@SequenceGenerator(name = "S_CORE_VEH_BRAND", sequenceName = "S_CORE_VEH_BRAND", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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



	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
