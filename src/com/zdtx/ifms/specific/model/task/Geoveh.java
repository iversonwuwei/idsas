package com.zdtx.ifms.specific.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 电子围栏表
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
@Entity
@Table(name = "V_TREE_GEO_VEH")
public class Geoveh implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long tree_id; // V_TREE_GEO_VEH
	private String tree_name;
	private Long parentid;
	private Long tree_level;
	private Long vehicleid;
	private String licenseplate;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_ifms_geofencing")
	@SequenceGenerator(name = "s_ifms_geofencing", sequenceName = "s_ifms_geofencing", allocationSize = 1)
	public Long getTree_id() {
		return tree_id;
	}

	public void setTree_id(Long tree_id) {
		this.tree_id = tree_id;
	}

	public String getTree_name() {
		return tree_name;
	}

	public void setTree_name(String tree_name) {
		this.tree_name = tree_name;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public Long getTree_level() {
		return tree_level;
	}

	public void setTree_level(Long tree_level) {
		this.tree_level = tree_level;
	}

	public Long getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}

}
