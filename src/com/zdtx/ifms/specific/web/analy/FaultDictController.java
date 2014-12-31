package com.zdtx.ifms.specific.web.analy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.analy.FaultDict;
import com.zdtx.ifms.specific.service.analy.FaultDictManager;
import com.zdtx.ifms.specific.vo.profile.CounsellVo;

public class FaultDictController extends ReportBase<FaultDict> {
	
	private static final long serialVersionUID = -5353801042915202249L;

	@Autowired
	private FaultDictManager fdMgr;
	
 	private List<KeyAndValue> driList = new ArrayList<KeyAndValue>();

 	private FaultDict fd = new FaultDict();
	private CounsellVo csVo = new CounsellVo();
	
	public String index(){
		//driList = vtMgr.getVehicleTypeList();
		page = fdMgr.getBatch(page, csVo);
		return "index";
	}
	public String edit(){
		//driList = vtMgr.getVehicleTypeList();
		fd = baseMgr.get(FaultDict.class, id);
		return "edit";
	}
	public String show(){
		//driList = vtMgr.getVehicleTypeList();
		fd = baseMgr.get(FaultDict.class, id);
		return "show";
	}
	public String add(){
		//driList = vtMgr.getVehicleTypeList();
		return "add";
	}
	public String create() throws IOException{
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			fd.setCreater(getCurrentUser().getUserName());
			fd.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			baseMgr.save(fd);
			out.print(Constants.SUCCESS[0]+"fault-dict!index?highlight=" + fd.getFaultcodeid()
					+ "'</script>");
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return this.index();
	}
	public String delete(){
		fd = baseMgr.get(FaultDict.class, id);
		fd.setIsdelete("T");
		fd.setCreater(getCurrentUser().getUserName());
		fd.setCreatime(DateUtil.formatLongTimeDate(new Date()));
		baseMgr.save(fd);
		return this.index();
	}
	public String checkEdit() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			String names = Struts2Util.getParameter("names");// 从前台获得的登陆账号
			String ids = Struts2Util.getParameter("ids");// 从前台获得的用户ID
			if (fdMgr.checkEdit(ids,names)) {
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
			if (fdMgr.checkAdd(names)) {
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
	public FaultDict getModel() {
		return fd;
	}
	public List<KeyAndValue> getDriList() {
		return driList;
	}
	public void setDriList(List<KeyAndValue> driList) {
		this.driList = driList;
	}
 
	public CounsellVo getCsVo() {
		return csVo;
	}
	public void setCsVo(CounsellVo csVo) {
		this.csVo = csVo;
	}
	
}
