package com.zdtx.ifms.specific.web.vehicle;
import java.io.PrintWriter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.service.vehicle.FleetListManager;
import com.zdtx.ifms.specific.vo.vehicle.FleetListVO;

/**
 * @ClassName: FleetListController
 * @Description: Vehicle-Fleet List-Controller
 * @author Leon Liu
 * @date 2013-4-26 16:59:24
 * @version V1.0
 */
public class FleetListController extends ReportBase<Org>{

	private static final long serialVersionUID = 9173702565438285389L;
	
	@Autowired
	private FleetListManager manager;
	
	private Org orgObj = new Org();
	private FleetListVO vo = new FleetListVO(); 
	
	public String index() {
		page = manager.getBatch(page, vo);
		return "index";
	}

	public String editNew() {
		return "edit";
	}
	
	public String edit() {
		orgObj = baseMgr.get(Org.class, id);
		return "edit";
	}
	public String show() {
		orgObj = baseMgr.get(Org.class, id);
		return "show";
	}
	
	public String create() {
		PrintWriter out = null;
		try {
			Long fatherID = Long.valueOf(orgObj.getParentID());
			Org father = baseMgr.get(Org.class, fatherID);
			if(null == orgObj.getOrgID()) {	//create
				orgObj.setCreater(getCurrentUser().getUserName());
				orgObj.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
				orgObj.setFlag(3);
				orgObj.setParentID(fatherID);
				orgObj.setParentName(father.getOrgName());
				orgObj.setInLevel(3L);
				orgObj.setIsDelete("F");
				baseMgr.save(orgObj);
			} else {	//edit
				Org org = baseMgr.get(Org.class, orgObj.getOrgID());
				org.setCreater(getCurrentUser().getUserName());
				org.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
				org.setParentID(fatherID);
				org.setParentName(father.getOrgName());
				org.setOrgName(orgObj.getOrgName());
				org.setDescription(orgObj.getDescription());
				baseMgr.save(org);
			}
			out = Struts2Util.getResponse().getWriter();
			out.print("<script language='JavaScript'>alert('Save successfully!');location='" + Struts2Util.getBasePath() + "/vehicle/fleet-list?editble=1&highlight=" + orgObj.getOrgID() + Constants.SUCCESS[1]);
		} catch (Exception e) {
			Struts2Util.printError(e);
		} finally {
			out.close();
		}
		return this.index();
	}
	
	public String destory() {
		Org orgObj = baseMgr.get(Org.class, id);
		orgObj.setCreater(getCurrentUser().getUserName());
		orgObj.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
		orgObj.setIsDelete("T");
		baseMgr.save(orgObj);
		return this.index();
	}
	
	/***
	 * 导出
	 * @return
	 */
	public String doExport(){
		try {
			this.xlsFileName = disposeXlsName("Fleet_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()));
			this.xlsStream = baseMgr.doExportExcel("Fleet", "No., Fleet Name, Department, Description", "orgName, parentName, description", null);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	
	/**
	 * check if the data is duplicate
	 * @return JSON
	 */
	public String  checkDuplicate() {
		if(manager.isDuplicate(orgObj.getOrgID(), orgObj.getParentID(), orgObj.getOrgName())) {
			jsonObject.put("result", "duplicate");
		} else {
			jsonObject.put("result", "pass");
		}
		return "jsonObject";
	}
	
	/**
	 * 验证机构能否被删除
	 * @return JSON
	 */
	public String checkDelete() {
		String res = manager.checkDelFleet(vo.getFleetID());
		if (res != null) {
			jsonObject.put("result", res);
		}
		return "jsonObject";
	}
	
	@Override
	public Org getModel() {
		return this.orgObj;
	}

	public FleetListVO getVo() {
		return vo;
	}

	public void setVo(FleetListVO vo) {
		this.vo = vo;
	}
}