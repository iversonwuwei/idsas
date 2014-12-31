package com.zdtx.ifms.common.web;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.RandomImage;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.authority.User;
import com.zdtx.ifms.specific.service.authority.UserManager;

/**
 *
 * @author Wiflg Goth
 * @since Jan 7, 2011
 */

@Result(name = "img", type = "stream", params = { "contentType", "image/jpeg", "inputName", "randomCode" })
public class LoginController extends ActionSupport {

	private static final long serialVersionUID = 3257375703284238178L;

	private final Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	private BaseManager baseMgr;
	@Autowired
	private UserManager userMgr;

	private String username;
	private String password;
	private String render;
	private String versionCode;
	private String id;
	private boolean checkedRender;

	private ByteArrayInputStream randomCode;
	
	/**
	 * Get current project.
	 *
	 * @return
	 */
	public String index() {
		//读取版本号
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		versionCode = p.getProperty("version");
		return "index";
	}

	/**
	 * Login in this project.
	 *
	 * @return
	 */
	public String logon() {
		HttpSession session = Struts2Util.getSession();
		session.setMaxInactiveInterval(30*60);
		HttpServletRequest request = Struts2Util.getRequest();
		if (!request.getMethod().equals("POST") && !this.checkedRender) {
			Struts2Util.renderHtml("<center><p style='margin-top: 200px; font-size: 150px; color: red; font-weight: bolder;'>FORBIDDEN</p>"
					+ "<p style='font-size: 80px; color: blue; font-weight: bolder;'>请不要随意尝试, 谢谢."
					+ "<a href='/login' style='font-size: 80px; color: green; font-weight: bolder;'>登录</a></p></center>");
			session.setAttribute("mainUser", null);
			return null;
		}
		User currentUser = userMgr.checkLogin(username, password);
		if (null == currentUser) {
			Struts2Util.renderText("error");
			return null;
		}
		String ipAddr = "";
		try {
			ipAddr = Struts2Util.getIpAddr(request);
		} catch (Exception e) {
			logger.error("获取IP异常", e);
		}
		session.setAttribute("mainUser", currentUser);
		session.setAttribute("ipAddr", ipAddr);
		session.setAttribute("userCom", Utils.keysToArray(baseMgr.getComByAuthority(currentUser.getUserID())));
		session.setAttribute("userComStr", Utils.keysToString(baseMgr.getComByAuthority(currentUser.getUserID())));
		session.setAttribute("userDepartment", Utils.keysToArray(baseMgr.getDepartByAuthority(currentUser.getUserID())));
		session.setAttribute("userDepartmentStr", Utils.keysToString(baseMgr.getDepartByAuthority(currentUser.getUserID())));
		session.setAttribute("userFleet", Utils.keysToArray(baseMgr.getFleetByAuthority(currentUser.getUserID())));
		session.setAttribute("userFleetStr", Utils.keysToString(baseMgr.getFleetByAuthority(currentUser.getUserID())));
		session.setAttribute("userVehicle", Utils.listToMap(baseMgr.getVehicleNameByAuthority(currentUser.getUserID())));
		session.setAttribute("userFeat", baseMgr.getRoleTree(currentUser.getUserRole().getRoleID()));
		session.setAttribute("rightNow", DateUtil.formatDate(new Date()));
		Struts2Util.renderText("logon");
		return null;
	}

	/**
	 * Check login random.
	 *
	 * @return
	 */
	public String checkRandom() {
		HttpSession session = Struts2Util.getSession();
		if (render.equalsIgnoreCase((String) session.getAttribute("randomCode"))) {
			this.checkedRender = true;
			Struts2Util.renderText("true");
			return null;
		}
		Struts2Util.renderText("error");
		return null;
	}

	/**
	 * @param 验证码
	 * @return
	 */
	public String code() {
		try {
			RandomImage randomImage = RandomImage.Instance();
			randomCode = randomImage.getImage();
			randomCode.available();
			Struts2Util.getSession().setAttribute("randomCode", randomImage.getCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "img";
	}

	/**
	 * Log out.
	 *
	 * @return
	 */
	public String logout() {
		HttpSession session = Struts2Util.getSession();
		session.invalidate();
		return "index";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRender() {
		return render;
	}

	public void setRender(String render) {
		this.render = render;
	}

	public boolean isCheckedRender() {
		return checkedRender;
	}

	public void setCheckedRender(boolean checkedRender) {
		this.checkedRender = checkedRender;
	}

	public ByteArrayInputStream getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(ByteArrayInputStream randomCode) {
		this.randomCode = randomCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}