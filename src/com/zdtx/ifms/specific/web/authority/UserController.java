package com.zdtx.ifms.specific.web.authority;

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
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.authority.Role;
import com.zdtx.ifms.specific.model.authority.User;
import com.zdtx.ifms.specific.service.authority.RoleManager;
import com.zdtx.ifms.specific.service.authority.UserManager;
import com.zdtx.ifms.specific.vo.authority.UserVo;

/**
 * @author zy
 */

public class UserController extends ReportBase<User> {

	private static final long serialVersionUID = 7096631008042727385L;

	@Autowired
	private UserManager userMgr;
	@Autowired
	private RoleManager roleMgr;

	private User user = new User();
	private UserVo userVo = new UserVo();
	private List<KeyAndValue> roleList = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> orgList = new ArrayList<KeyAndValue>();
	private List<Org> departmentList = new ArrayList<Org>();

	private String visibleStr; // 保存用户可见机构字符串
	private Long userDepart; // 用户所在机构ID
	private Long userRoleID;
	private List<KeyAndValue> coms = new ArrayList<KeyAndValue>();

	public String index() {
		User currentUser = (User) Struts2Util.getSession().getAttribute(
				"mainUser");
		page = userMgr.getBatch(currentUser, page, baseMgr
				.isAllData(currentUser.getUserID()), userVo, Utils
				.keysToArray(baseMgr.getComByAuthority(getCurrentUser()
						.getUserID())), Utils.keysToArray(baseMgr
				.getDepartByAuthority(getCurrentUser().getUserID())));
		return "index";
	}

	/***
	 * 导出
	 * 
	 * @return
	 */
	public String exportDetail() {
		try {
			this.xlsFileName = disposeXlsName("Account_"
					+ DateUtil.formatLongTimeDateWithoutSymbol(new Date()));
			this.xlsStream = userMgr.getExcel("Account");
			// this.xlsStream = baseMgr.doExportExcel("Account",
			// "No., Account, Role, Company, Name, Email, Phone, Description",
			// "loginName, userRole.roleName, userOrg.orgName, userName, e_mail, mobilephone, description",
			// null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}

	/**
	 * 跳转到添加用户界面
	 * 
	 * @return 添加页面
	 */
	public String editNew() {
		coms = baseMgr.getComByAuthority(getCurrentUser().getUserID());
		User currentUser = (User) Struts2Util.getSession().getAttribute(
				"mainUser");
		roleList = roleMgr.getRoleList(Utils.keysToArray(baseMgr
				.getComByAuthority(getCurrentUser().getUserID())), currentUser
				.getUserRole().getAflag().equals("0"));
		departmentList = userMgr.getOrgList(currentUser,
				baseMgr.isAllData(currentUser.getUserID()));
		return "edit";
	}

	/**
	 * 用户修改
	 * 
	 * @return 修改页面
	 */
	public String edit() {
		try {
			coms = baseMgr.getComByAuthority(getCurrentUser().getUserID());
			User currentUser = (User) Struts2Util.getSession().getAttribute("mainUser");
			roleList = roleMgr.getRoleList(Utils.keysToArray(baseMgr.getComByAuthority(getCurrentUser().getUserID())),
					currentUser.getUserRole().getAflag().equals("0"));
			departmentList = userMgr.getOrgList(currentUser, baseMgr.isAllData(currentUser.getUserID()));
			user = baseMgr.get(User.class, id);
			if (user.getUserOrg() != null) {
				userDepart = user.getUserOrg().getOrgID();
			}
			userRoleID = user.getUserRole().getRoleID();
			visibleStr = userMgr.getOrgStr(user.getOrgs());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "edit";
	}

	public String comc() {
		Org o = baseMgr.get(Org.class, id);
		Struts2Util.renderText("" + o.getParentID());
		return null;
	}

	public String show() {
		User currentUser = (User) Struts2Util.getSession().getAttribute(
				"mainUser");
		roleList = roleMgr.getRoleList(Utils.keysToArray(baseMgr
				.getComByAuthority(getCurrentUser().getUserID())), currentUser
				.getUserRole().getAflag().equals("0"));
		user = baseMgr.get(User.class, id);
		departmentList = userMgr.getOrgList(user,
				baseMgr.isAllData(user.getUserID()));
		visibleStr = userMgr.getOrgStr(user.getOrgs());
		return "show";
	}

	public String srole() {
		Role r = baseMgr.get(Role.class, id);
		Struts2Util.renderText(r.getAflag());

		return null;
	}

	public String checkrc() {
		Role r = baseMgr.get(Role.class, id);

		if (r.getAflag().equals("0")) {
			Struts2Util.renderText("true");
		} else {
			if (user.getUserID() == null) {
				Struts2Util.renderText("false");
			} else {
				if (user.getUserID().equals(r.getComid())) {
					Struts2Util.renderText("true");
				} else {
					Struts2Util.renderText("false");
				}
			}
		}
		return null;
	}

	/**
	 * 用户信息保存
	 * 
	 * @return
	 */
	public String create() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			Role userRole = baseMgr.get(Role.class, userRoleID);
			user.setUserRole(userRole);
			userMgr.saveUser(userDepart, visibleStr, getCurrentUser(), user);
			out.print(Constants.SUCCESS[0] + "user!index?highlight=" + user.getUserID() + "&editble=1'</script>");
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return this.index();
	}

	/**
	 * 用户信息删除
	 * 
	 * @return
	 */
	public String delete() {
		User deleteUser = baseMgr.get(User.class, id);
		deleteUser.setIsDelete("T");
		baseMgr.save(deleteUser);
		return this.index();
	}

	/**
	 * 验证用户登录名
	 * 
	 * @return
	 * @throws IOException
	 */
	public String check() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			String names = Struts2Util.getParameter("names");// 从前台获得的登陆账号
			// String userID = Struts2Util.getParameter("userID");// 从前台获得的用户ID
			String code = Struts2Util.getParameter("code");// 从前台获得的卡号
			String userName = Struts2Util.getParameter("userName");// 用户名称

			Long userID = -1l;
			if (!"".equals(Struts2Util.getParameter("userID"))) {
				userID = Long.parseLong(Struts2Util.getParameter("userID"));
			}
			p.print(userMgr.checkLoginName(names, code, userName, userID));
			/*
			 * if (userID == null || userID == "") { if
			 * (userMgr.checkAdd(names)) { p.print("names");// 用户名重复 } else { if
			 * (userMgr.checkAddCode(code)) { p.print("code");// 用户名重复 } else {
			 * p.print("OK");// 用户名不重复 } } } else { if (userMgr.checkEdit(names,
			 * userID)) { p.print("names");// 用户名重复 } else { if
			 * (userMgr.checkEditCode(code, userID)) { p.print("code");// 用户名重复
			 * } else { p.print("OK");// 用户名不重复 } } }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}

	/**
	 * @param 修改密码
	 * @return
	 */
	public String change() {
		user = getCurrentUser();
		return "change";
	}

	/**
	 * @param 保存修改密码
	 * @return
	 * @throws IOException
	 */
	public String saveChange() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		String id = Struts2Util.getParameter("userID");
		String newPassword = Struts2Util.getParameter("newPassword");
		String password = Struts2Util.getParameter("password");
		String msg = userMgr.changePsd(id, password, newPassword);
		p.print(msg);
		p.close();
		return null;
	}

	/**
	 * 重置密码
	 * 
	 * @return
	 * @throws IOException
	 */
	public String resetPassword() throws IOException {
		try {
			PrintWriter p = Struts2Util.getResponse().getWriter();
			String id = Struts2Util.getParameter("userID");
			String msg = userMgr.changePsd(id, "resetbeilv", "123");
			p.print(msg);
			p.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过子节点ID获得父节点ID
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getParentID() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			String orgid = Struts2Util.getParameter("departmentID");// 从前台获得的子节点ID
			p.print(userMgr.getFatherOrgID(orgid));// 返回父节点ID
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}

	@Override
	public User getModel() {
		return user;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	public List<KeyAndValue> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<KeyAndValue> roleList) {
		this.roleList = roleList;
	}

	public List<KeyAndValue> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<KeyAndValue> orgList) {
		this.orgList = orgList;
	}

	public List<Org> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Org> departmentList) {
		this.departmentList = departmentList;
	}

	public Long getUserDepart() {
		return userDepart;
	}

	public void setUserDepart(Long userDepart) {
		this.userDepart = userDepart;
	}

	public String getVisibleStr() {
		return visibleStr;
	}

	public void setVisibleStr(String visibleStr) {
		this.visibleStr = visibleStr;
	}

	public List<KeyAndValue> getComs() {
		return coms;
	}

	public void setComs(List<KeyAndValue> coms) {
		this.coms = coms;
	}

	public Long getUserRoleID() {
		return userRoleID;
	}

	public void setUserRoleID(Long userRoleID) {
		this.userRoleID = userRoleID;
	}

}