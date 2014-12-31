package com.zdtx.ifms.specific.model.analy;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "t_core_faultcode")
public class FaultDict implements Serializable{

	private static final long serialVersionUID = 2831908543267937910L;
	
	private Long faultcodeid;
	private String vehicletype;
	private String code;
	private String chnscope;
	private String engscope;
	private String chndefinition;
	private String engdefinition;
	private String chncontext;
	private String engcontext;
	private String creater;
	private String creatime;
	private String isdelete;
	
	@Id
	@SequenceGenerator(name = "s_core_faultcode", sequenceName = "s_core_faultcode", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_core_faultcode")
	public Long getFaultcodeid() {
		return faultcodeid;
	}
	public void setFaultcodeid(Long faultcodeid) {
		this.faultcodeid = faultcodeid;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getChnscope() {
		return chnscope;
	}
	public void setChnscope(String chnscope) {
		this.chnscope = chnscope;
	}
	public String getEngscope() {
		return engscope;
	}
	public void setEngscope(String engscope) {
		this.engscope = engscope;
	}
	public String getChndefinition() {
		return chndefinition;
	}
	public void setChndefinition(String chndefinition) {
		this.chndefinition = chndefinition;
	}
	public String getEngdefinition() {
		return engdefinition;
	}
	public void setEngdefinition(String engdefinition) {
		this.engdefinition = engdefinition;
	}
	public String getChncontext() {
		return chncontext;
	}
	public void setChncontext(String chncontext) {
		this.chncontext = chncontext;
	}
	public String getEngcontext() {
		return engcontext;
	}
	public void setEngcontext(String engcontext) {
		this.engcontext = engcontext;
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

}
