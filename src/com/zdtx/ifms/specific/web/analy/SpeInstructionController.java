package com.zdtx.ifms.specific.web.analy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.analy.FaultDict;
import com.zdtx.ifms.specific.model.analy.SpeInstruction;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.service.analy.FaultDictManager;
import com.zdtx.ifms.specific.service.analy.SpeInstructionManager;
import com.zdtx.ifms.specific.vo.profile.CounsellVo;

public class SpeInstructionController extends ReportBase<SpeInstruction> {
	
	private static final long serialVersionUID = -5353801042915202249L;

	@Autowired
	private SpeInstructionManager fdMgr;
	
	@Autowired
	private FaultDictManager fMgr;
	
 	private List<KeyAndValue> driList = new ArrayList<KeyAndValue>();

 	private SpeInstruction fd = new SpeInstruction();
 	
	private CounsellVo csVo = new CounsellVo();
	
	private List<KeyAndValue> listfa;
	
    private List<KeyAndValue> list;
	
	private String vehname;
	
	
	private Long vehicleid;
	
	private Long faid;
    
	public String index(){
		listfa = fMgr. getDitList();
		page = fdMgr.getBatch(page, csVo);
		return "index";
	}
	public String check() throws IOException{
		 List<SpeInstruction>  list2= fdMgr.getSpeInstruction(fd.getSpecialid(), vehicleid,fd.getCode());
		 if(null != list2 && list2.size()>0){
			 JSONArray json = JSONArray.fromObject(false);
			 Utils.getResponse().getWriter().print(json.toString());
		 } else {
			 JSONArray json = JSONArray.fromObject(true);
			 Utils.getResponse().getWriter().print(json.toString());
		 }
		 return null;
		
	}
	public String delete(){
		SpeInstruction sp = baseMgr.get(SpeInstruction.class, id);
		sp.setSuccess("T");
		sp.setCreatime(DateUtil.formatLongTimeDate(new Date()));
		return this.index();
	}
	public String edit(){
	 
		listfa = fMgr. getDitList();
		fd = baseMgr.get(SpeInstruction.class, id);
		return "edit";
	}
	public String show(){
		fd = baseMgr.get(SpeInstruction.class, id);
		return "show";
	}
	public String add(){
		
		 
		listfa = fMgr.getDitList1();
		return "edit";
	}
	public String recover(){
		fd = baseMgr.get(SpeInstruction.class, id);
//		String str = SocketUtil.duanyouStr(String.valueOf((new Date()).getTime()).substring(0, 10), getCurrentUser().getLoginName(), fd.getVehiclename(), "2");
		String cuttingStr = SocketUtil.setCuttingStr(String.valueOf((new Date()).getTime()).substring(0, 10), getCurrentUser().getLoginName(), fd.getVehiclename(), "2");
		boolean b = SocketUtil.sendSocket(cuttingStr);	//发送断油断电指令
		if(b == true){
			fd.setSuccess("L");
			baseMgr.save(fd);
			fd.setSpecialid(null);
			fd.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			fd.setCreater(getCurrentUser().getUserName());
			List<FaultDict>  list = fMgr.getBatch();
			if(null != list && list.size()>0){
				fd.setCode(list.get(0).getCode());
				fd.setEngdefinition(list.get(0).getEngdefinition());
			}
			fd.setSuccess("K");
			baseMgr.save(fd);
		} else {
			fd.setSuccess("F");
			baseMgr.save(fd);
			fd.setSpecialid(null);
			fd.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			fd.setCreater(getCurrentUser().getUserName());
			List<FaultDict>  list = fMgr.getBatch();
			if(null != list && list.size()>0){
				fd.setCode(list.get(0).getCode());
				fd.setEngdefinition(list.get(0).getEngdefinition());
			}
			fd.setSuccess("P");
			baseMgr.save(fd);
		}
		
		return this.index();
	}
	public String create() throws IOException{
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			fd.setCreater(getCurrentUser().getUserName());
			fd.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			Vehcile f= baseMgr.get(Vehcile.class, vehicleid);
			fd.setVehiclename(f.getVehiclename());
			fd.setLicenseplate(f.getLicenseplate());
			fd.setVehicleid(vehicleid);
			String cuttingStr = SocketUtil.setCuttingStr(String.valueOf((new Date()).getTime()).substring(0, 10), getCurrentUser().getLoginName(), fd.getVehiclename(), "1");
//			String str = SocketUtil.duanyouStr(String.valueOf((new Date()).getTime()).substring(0, 10), getCurrentUser().getLoginName(), f.getVehiclename(), "1");
			boolean b = SocketUtil.sendSocket(cuttingStr);
			if(b == true){
				fd.setSuccess("F");
			} else {
				fd.setSuccess("T");
			}
			baseMgr.save(fd);
			out.print(Constants.SUCCESS[0]+"spe-instruction!index?highlight=" + fd.getSpecialid()
					+ "'</script>");
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return this.index();
	}
 
 
 
	@Override
	public SpeInstruction getModel() {
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
	public SpeInstruction getFd() {
		return fd;
	}
	public void setFd(SpeInstruction fd) {
		this.fd = fd;
	}
	public List<KeyAndValue> getList() {
		return list;
	}
	public void setList(List<KeyAndValue> list) {
		this.list = list;
	}
	public String getVehname() {
		return vehname;
	}
	public void setVehname(String vehname) {
		this.vehname = vehname;
	}
 
	public List<KeyAndValue> getListfa() {
		return listfa;
	}
	public void setListfa(List<KeyAndValue> listfa) {
		this.listfa = listfa;
	}
	public Long getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}
	public Long getFaid() {
		return faid;
	}
	public void setFaid(Long faid) {
		this.faid = faid;
	}
	
	
	
}
