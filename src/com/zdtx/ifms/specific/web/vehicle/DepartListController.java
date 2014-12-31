package com.zdtx.ifms.specific.web.vehicle;
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
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.service.vehicle.DepartListManager;
import com.zdtx.ifms.specific.vo.vehicle.DepartListVO;

/**
 * @ClassName: DepartListController
 * @Description: Vehicle-Department List-Controller
 * @author Leon Liu
 * @date 2013-4-27 9:15:33
 * @version V1.0
 */
public class DepartListController extends ReportBase<Org>{

	private static final long serialVersionUID = 9173702565438285389L;
	
	@Autowired
	private DepartListManager manager;
	
	private Org orgObj = new Org();
	private DepartListVO vo = new DepartListVO(); 
	private List<KeyAndValue> coms=new ArrayList<KeyAndValue>();
	
	public String index() {
		page = manager.getBatch(page, vo);
		return "index";
	}
	
	public String editNew() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		return "edit";
	}
	
	public String edit() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
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
			Org father = baseMgr.get(Org.class, orgObj.getParentID());	//所有机构只有一级，父节点为ID=1的总部
			if(null == orgObj.getOrgID()) {	//create
				orgObj.setCreater(getCurrentUser().getUserName());
				orgObj.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
				orgObj.setFlag(2);
				orgObj.setIsDelete("F");
			//	orgObj.setParentID(1L);	//写定父级节点
				orgObj.setParentName(father.getOrgName());
				orgObj.setInLevel(2L);
				baseMgr.save(orgObj);
			} else {	//edit
				Org org = baseMgr.get(Org.class, orgObj.getOrgID());
				org.setCreater(getCurrentUser().getUserName());
				org.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
				org.setOrgName(orgObj.getOrgName());
				org.setDescription(orgObj.getDescription());
				org.setParentName(father.getOrgName());
				org.setParentID(orgObj.getParentID());	
				baseMgr.save(org);
			}
			out = Struts2Util.getResponse().getWriter();
			out.print("<script language='JavaScript'>alert('Save successfully!\\nNew permissions will added after relogin!');location='" + Struts2Util.getBasePath() + "/vehicle/depart-list?editble=1&highlight=" + orgObj.getOrgID() + Constants.SUCCESS[1]);
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
			this.xlsFileName = disposeXlsName("Department_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()));
			this.xlsStream = baseMgr.doExportExcel("Department", "No., Department Name, Company, Description", "orgName, parentName, description", null);
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
	 * 验证公司能否被删除
	 * @return JSON
	 */
	public String checkDelete() {
		String res = manager.checkDelDepartment(vo.getDepartID());
		if (res != null) {
			jsonObject.put("result", res);
		}
		return "jsonObject";
	}
	
	@Override
	public Org getModel() {
		return this.orgObj;
	}

	public DepartListVO getVo() {
		return vo;
	}

	public void setVo(DepartListVO vo) {
		this.vo = vo;
	}

	public List<KeyAndValue> getComs() {
		return coms;
	}

	public void setComs(List<KeyAndValue> coms) {
		this.coms = coms;
	}
}