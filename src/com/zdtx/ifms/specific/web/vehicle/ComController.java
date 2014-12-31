package com.zdtx.ifms.specific.web.vehicle;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.service.vehicle.DepartListManager;
import com.zdtx.ifms.specific.vo.vehicle.DepartListVO;

public class ComController extends ReportBase<Org> {

	private static final long serialVersionUID = -147361110435793015L;

	@Autowired
	private DepartListManager manager;

	private Org orgObj = new Org();
	private DepartListVO vo = new DepartListVO();

	public String index() {
		page = manager.getoBatch(page, vo);
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
			Org father = baseMgr.get(Org.class, 1L); // 所有机构只有一级，父节点为ID=1的总部
			if (null == orgObj.getOrgID()) { // create
				orgObj.setCreater(getCurrentUser().getUserName());
				orgObj.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
				orgObj.setFlag(2);
				orgObj.setIsDelete("F");
				orgObj.setParentID(1L); // 写定父级节点
				orgObj.setParentName(father.getOrgName());
				orgObj.setInLevel(1L);
				baseMgr.save(orgObj);
			} else { // edit
				Org org = baseMgr.get(Org.class, orgObj.getOrgID());
				org.setCreater(getCurrentUser().getUserName());
				org.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
				org.setOrgName(orgObj.getOrgName());
				org.setDescription(orgObj.getDescription());
				org.setLineno(orgObj.getLineno());
				baseMgr.save(org);
			}
			out = Struts2Util.getResponse().getWriter();
			out.print("<script language='JavaScript'>alert('Save successfully! \\nNew permissions will added after relogin!');location='" + Struts2Util.getBasePath() + "/vehicle/com?editble=1&highlight=" + orgObj.getOrgID() + Constants.SUCCESS[1]);
		} catch (Exception e) {
			Struts2Util.printError(e);
		} finally {
			out.close();
		}
		return null;
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
	 * 
	 * @return
	 */
	public String doExport() {
		try {
			Map<String, String> convert = new HashMap<String, String>();
			convert.put("0", "Non-RFID");
			convert.put("1", "RFID");
			this.xlsFileName = disposeXlsName("Company_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()));
			this.xlsStream = baseMgr.doExportExcel("Company",
					"No., Company Name, Schedule Option[cv], Description",
					"orgName, lineno, description", convert);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	
	/**
	 * check if the data is duplicate
	 * 
	 * @return JSON
	 */
	public String checkDuplicate() {
		if (manager.isDuplicate(orgObj.getOrgID(), orgObj.getParentID(),
				orgObj.getOrgName())) {
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
		if (!manager.hasSubOrg(vo.getDepartID())) {
			jsonObject.put("result", "OK");
		} else {
			jsonObject.put("result", "NO");
		}
		return "jsonObject";
	}

	@Override
	public Org getModel() {
		return orgObj;
	}
	
	public DepartListVO getVo() {
		return vo;
	}

	public void setVo(DepartListVO vo) {
		this.vo = vo;
	}
}