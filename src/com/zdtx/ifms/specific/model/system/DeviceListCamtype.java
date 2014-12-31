package com.zdtx.ifms.specific.model.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: DeviceList
 * @Description: 设备状态
 * @author yqs
 * @date 2012-9-5 下午05:17:43
 * @version V1.0
 */

@Entity
@Table(name = "V_CORE_Unite_Camera")
public class DeviceListCamtype implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -130382574152939155L;
	
	private Long o_deviceno;	
	private String o_devicename;	
	private String o_devicedetype;
	private String o_devicedescript;
	private String o_unittype;	
	private Long o_busid;	
	private String o_busname;
//	private Long o_videono;	
//	private String o_videoname;	
	private Integer o_channelcount;
	private String o_lururen;
	private String o_lurushijian;
	private String o_xiugairen;
	private String o_xiugaishijian;
	private String o_flag;
	private Long o_issingleupdate;
	private String o_loginhost;
//	private String type;
	private String serverip;
	private String serverport;
	private String ipstatus;
	private String channel1;
	private String channel2;
	private String channel3;
	private String channel4;
	private String channel5;
	private String channel6;
	private String channel7;
	private String channel8;
	private String deptname;
	private Long deptid;
	
	@Id
	@SequenceGenerator(name = "s_core_device", sequenceName = "s_core_device", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_core_device")
	public Long getO_deviceno() {
		return o_deviceno;
	}
	public void setO_deviceno(Long o_deviceno) {
		this.o_deviceno = o_deviceno;
	}
	public String getO_devicename() {
		return o_devicename;
	}
	public void setO_devicename(String o_devicename) {
		this.o_devicename = o_devicename;
	}
	public String getO_devicedetype() {
		return o_devicedetype;
	}
	public void setO_devicedetype(String o_devicedetype) {
		this.o_devicedetype = o_devicedetype;
	}
	public String getO_devicedescript() {
		return o_devicedescript;
	}
	public void setO_devicedescript(String o_devicedescript) {
		this.o_devicedescript = o_devicedescript;
	}
	public String getO_unittype() {
		return o_unittype;
	}
	public void setO_unittype(String o_unittype) {
		this.o_unittype = o_unittype;
	}
	public Long getO_busid() {
		return o_busid;
	}
	public void setO_busid(Long o_busid) {
		this.o_busid = o_busid;
	}
	public String getO_busname() {
		return o_busname;
	}
	public void setO_busname(String o_busname) {
		this.o_busname = o_busname;
	}
//	public Long getO_videono() {
//		return o_videono;
//	}
//	public void setO_videono(Long o_videono) {
//		this.o_videono = o_videono;
//	}
//	public String getO_videoname() {
//		return o_videoname;
//	}
//	public void setO_videoname(String o_videoname) {
//		this.o_videoname = o_videoname;
//	}
	public Integer getO_channelcount() {
		return o_channelcount;
	}
	public void setO_channelcount(Integer o_channelcount) {
		this.o_channelcount = o_channelcount;
	}
	public String getO_lururen() {
		return o_lururen;
	}
	public void setO_lururen(String o_lururen) {
		this.o_lururen = o_lururen;
	}
	public String getO_lurushijian() {
		return o_lurushijian;
	}
	public void setO_lurushijian(String o_lurushijian) {
		this.o_lurushijian = o_lurushijian;
	}
	public String getO_xiugairen() {
		return o_xiugairen;
	}
	public void setO_xiugairen(String o_xiugairen) {
		this.o_xiugairen = o_xiugairen;
	}
	public String getO_xiugaishijian() {
		return o_xiugaishijian;
	}
	public void setO_xiugaishijian(String o_xiugaishijian) {
		this.o_xiugaishijian = o_xiugaishijian;
	}
	public String getO_flag() {
		return o_flag;
	}
	public void setO_flag(String o_flag) {
		this.o_flag = o_flag;
	}
	public Long getO_issingleupdate() {
		return o_issingleupdate;
	}
	public void setO_issingleupdate(Long o_issingleupdate) {
		this.o_issingleupdate = o_issingleupdate;
	}
	public String getO_loginhost() {
		return o_loginhost;
	}
	public void setO_loginhost(String o_loginhost) {
		this.o_loginhost = o_loginhost;
	}
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
	public String getServerip() {
		return serverip;
	}
	public void setServerip(String serverip) {
		this.serverip = serverip;
	}
	public String getServerport() {
		return serverport;
	}
	public void setServerport(String serverport) {
		this.serverport = serverport;
	}
	public String getIpstatus() {
		return ipstatus;
	}
	public void setIpstatus(String ipstatus) {
		this.ipstatus = ipstatus;
	}
	public String getChannel1() {
		return channel1;
	}
	public void setChannel1(String channel1) {
		this.channel1 = channel1;
	}
	public String getChannel2() {
		return channel2;
	}
	public void setChannel2(String channel2) {
		this.channel2 = channel2;
	}
	public String getChannel3() {
		return channel3;
	}
	public void setChannel3(String channel3) {
		this.channel3 = channel3;
	}
	public String getChannel4() {
		return channel4;
	}
	public void setChannel4(String channel4) {
		this.channel4 = channel4;
	}
	public String getChannel5() {
		return channel5;
	}
	public void setChannel5(String channel5) {
		this.channel5 = channel5;
	}
	public String getChannel6() {
		return channel6;
	}
	public void setChannel6(String channel6) {
		this.channel6 = channel6;
	}
	public String getChannel7() {
		return channel7;
	}
	public void setChannel7(String channel7) {
		this.channel7 = channel7;
	}
	public String getChannel8() {
		return channel8;
	}
	public void setChannel8(String channel8) {
		this.channel8 = channel8;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public Long getDeptid() {
		return deptid;
	}
	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}
	
	
	
}