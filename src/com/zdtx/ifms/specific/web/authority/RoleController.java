package com.zdtx.ifms.specific.web.authority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Feat;
import com.zdtx.ifms.specific.model.authority.Role;
import com.zdtx.ifms.specific.service.authority.RoleManager;
import com.zdtx.ifms.specific.vo.authority.UserVo;

/**
 * @Description: 权限管理-角色管理-控制层
 * @author Leon Liu
 * @since 2013-9-23 10:39:15
 */
public class RoleController extends ReportBase<Role> {

	private static final long serialVersionUID = -2821336860134282589L;
	
	@Autowired
	private RoleManager roleMgr;
	
	private Role role = new Role();
	private UserVo userVo = new UserVo();
	private List<Feat> featList = new ArrayList<Feat>();// 功能树
	private String feat_Add_Str;// 权限字符串
	private List<KeyAndValue> coms=new ArrayList<KeyAndValue>();

	/**
	 * Go to the main page
	 * @return main page
	 */
	public String index() {
		page = roleMgr.getBatch(page, userVo,getCurrentUser().getUserRole().getAflag(),Utils.keysToArray(baseMgr.getComByAuthority(getCurrentUser().getUserID())));
		return "index";
	}
	
	/***
	 * 导出
	 * @return
	 */
	public String doExport() {
		try {
			this.xlsFileName = disposeXlsName("Role_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()));
			this.xlsStream = baseMgr.doExportExcel("Role", "No., Role Name, Company, Description",
					"roleName, comname, description", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	
	/**
	 * Go to create new role data
	 * @return edit page
	 */
	public String editNew() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		featList = baseMgr.getRoleTree(getCurrentUser().getUserRole().getRoleID());
		return "edit";
	}

	/**
	 * 修改角色
	 * 
	 * @return
	 */
	public String edit() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		featList = baseMgr.getRoleTree(getCurrentUser().getUserRole().getRoleID());
		role = baseMgr.get(Role.class, id);
		feat_Add_Str = roleMgr.getFeatString(role.getFeats());
		return "edit";
	}
	
	/**
	 * Go to the show page
	 */
	public String show() {
		role = baseMgr.get(Role.class, id);
		featList = roleMgr.getFeatByList(role.getFeats());
		return "show";
	}
	
	/**
	 * save the role data
	 * @return success:main page;
	 * 					fail:current page
	 */
	public String create() {
		try {
			roleMgr.saveOrUpdateRole(feat_Add_Str, role, getCurrentUser());		// 设置保存日期 操作人id，name 保存到role表中
			Utils.printSuccess("authority/role", role.getRoleID());
		} catch (Exception e) {
			Utils.printError(e);
			e.printStackTrace();
		} 
		return this.index();
	}

	/**
	 * delete the role
	 * @return main page
	 */
	public String delete() {
		Role deleteRole = baseMgr.get(Role.class, id);
		deleteRole.setIsDelete("T");
		baseMgr.save(deleteRole);
		return this.index();
	}
	
	/**
	 * Check if the role's name is duplicate
	 * @return JSON
	 */
	public String checkDuplicate() {
		if (roleMgr.checkDuplicate(userVo.getRoleID(), userVo.getRoleName(),id,role.getAflag())) {
			jsonObject.put("result", "REP");	// 用户名重复
		} else {
			jsonObject.put("result", "OK");	// 用户名重复
		}
		return "jsonObject";
	}

	@Override
	public Role getModel() {
		return role;
	}
	
	public UserVo getUserVo() {
		return userVo;
	}
	
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	public List<Feat> getFeatList() {
		return featList;
	}

	public void setFeatList(List<Feat> featList) {
		this.featList = featList;
	}

	public String getFeat_Add_Str() {
		return feat_Add_Str;
	}

	public void setFeat_Add_Str(String featAddStr) {
		feat_Add_Str = featAddStr;
	}

	public List<KeyAndValue> getComs() {
		return coms;
	}

	public void setComs(List<KeyAndValue> coms) {
		this.coms = coms;
	}
	
}