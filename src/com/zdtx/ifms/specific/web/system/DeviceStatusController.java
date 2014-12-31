package com.zdtx.ifms.specific.web.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.system.DeviceStatus;
import com.zdtx.ifms.specific.service.system.DeviceStatusManager;
import com.zdtx.ifms.specific.vo.system.DeviceVo;

public class DeviceStatusController extends ReportBase<DeviceStatus> {
	
	private static final long serialVersionUID = 2891176724007296752L;
	@Autowired
	private DeviceStatusManager dsMgr;
	
	private DeviceStatus ds = new DeviceStatus();
	private DeviceVo deVo = new DeviceVo();
	
	public String index(){
		page = dsMgr.getBatch(page, deVo);
		return "index";
	}
	public String edit(){
		ds = baseMgr.get(DeviceStatus.class, id);
		return "edit";
	}
	public String add(){
		return "add";
	}
	public String create() throws IOException{
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			ds.setCreater(getCurrentUser().getUserName());
			ds.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			baseMgr.save(ds);
			out.print(Constants.SUCCESS[0]+"device-status!index?highlight=" + ds.getStatusid()
					+ "'</script>");
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return this.index();
	}
	public String checkDel() throws IOException{
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			if (dsMgr.checkDel(id)) {
				p.print("true");// 用户名重复
			}else{
				p.print("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}
	public String delete(){
		ds = baseMgr.get(DeviceStatus.class, id);
		ds.setIsdelete("T");
		ds.setCreater(getCurrentUser().getUserName());
		ds.setCreatime(DateUtil.formatLongTimeDate(new Date()));
		baseMgr.save(ds);
		return this.index();
	}
	public String checkEdit() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			String names = Struts2Util.getParameter("names");// 从前台获得的登陆账号
			String ids = Struts2Util.getParameter("ids");// 从前台获得的用户ID
			if (dsMgr.checkEdit(ids,names)) {
				p.print("true");// 用户名重复
			}else{
				p.print("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}
	public String checkAdd() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			String names = Struts2Util.getParameter("names");// 从前台获得的登陆账号
			if (dsMgr.checkAdd(names)) {
				p.print("true");// 用户名重复
			}else{
				p.print("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}
	@Override
	public DeviceStatus getModel() {
		return ds;
	}
	public DeviceVo getDeVo() {
		return deVo;
	}
	public void setDeVo(DeviceVo deVo) {
		this.deVo = deVo;
	}
}
