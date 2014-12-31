package com.zdtx.ifms.common.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.specific.model.authority.User;

/**
 * The basic URL support.
 * 
 * @author Leon Liu
 * @since 2013-3-6 10:23:25
 */
@ParentPackage("mainten-default")
@Scope("prototype")
@InterceptorRefs({
	@InterceptorRef(value="params", params={"excludeParams",".*\\u0023.*, .*_new_value.*"}), 	//参数拦截，过滤掉带有指定内容的参数（\u0023为#号）
	@InterceptorRef("mainLogin"), 	//自定义拦截器，验证登陆状态
	@InterceptorRef(value="fileUpload", params={"allowedTypes","application/octet-stream, application/vnd.ms-excel","maximumSize","5000000"}), 	//文件上传拦截器，限制文件大小5M
	@InterceptorRef("restDefaultStack")	//默认拦截器，必须
})
@Results({
	@Result(name = "jsonObject", type = "json", params = { "root", "jsonObject"}),	//使用struts2-json插件封装JSON类型的返回方式，数据对象为jsonObject对象
	@Result(name = "jsonArray", type = "json", params = { "root", "jsonArray"}),	//使用struts2-json插件封装JSON类型的返回方式，数据对象为jsonArray对象
	@Result(name = "login", type = "redirectAction", params = { "actionName", "login" })	//返回登录页
})
public abstract class URLSupport<T> extends ActionSupport implements ModelDriven<T> {

	private static final long serialVersionUID = 258222055630891609L;

	protected Logger log = Logger.getLogger(getClass());

	@Autowired
	protected BaseManager baseMgr;

	protected Long id;
	
	protected Long editble;

	protected Page<T> page = new Page<T>(Constants.PAGE_SIZE);

	protected Long highlight;

	public Page<T> getPage() {
		return page;
	}

	protected JSONObject jsonObject = new JSONObject();
	
	protected JSONArray jsonArray = new JSONArray();
	
//	public String uName;
//	
//	public String uPass;
//	
//	//功能菜单，用于top.jsp中导航的显示
//	public List<Feat> roleFeatList;

//	public List<Feat> getRoleFeatList() {
//		return  roleFeatList;
//	}
//
//	public void setRoleFeatList(List<Feat> roleFeatList) {
//		this.roleFeatList = roleFeatList;
//	}
//
//	public String getuName() {
//		return uName;
//	}
//	
//	public void setuName(String uName) {
//		this.uName = uName;
//	}
//	
//	public String getuPass() {
//		return uPass;
//	}
//
//	public void setuPass(String uPass) {
//		this.uPass = uPass;
//	}

	/**
	 * /movies => method="index"
	 * 
	 * @return
	 */
	public String index() {
		return "index";
	}

	/**
	 * /movies/Thrillers => method="show", id="Thrillers"
	 * 
	 * @return
	 */
	public String show() {
		return "show";
	}

	/**
	 * /movies/new => method="editNew"
	 * 
	 * @return
	 */
	public String editNew() {
		return "edit";
	}

	/*-
	 * /movies/Thrillers;edit => method="edit", id="Thrillers" 
	 * /movies/Thrillers/edit => method="edit", id="Thrillers"
	 * 
	 * @return
	 */
	public String edit() {
		return "edit";
	}

	/**
	 * /movies/Thrillers => method="destroy", id="Thrillers"
	 * 
	 * @return
	 */
	public String destroy() {
		baseMgr.delete(this.getModel().getClass(), id);
		return this.index();
	}

	/*-
	 * The method name to call for a POST request with no id parameter. 
	 * Defaults to 'create'.
	 * 
	 * @return
	 */
	public String create() throws IOException {
		baseMgr.save(this.getModel());
		return this.index();
	}

	public User getCurrentUser() throws NullPointerException {
		HttpSession session = Struts2Util.getSession();
		User user = (User)session.getAttribute("mainUser");
		return user;
	}
	
//	public void getName() {
//		this.uName = ((User)Struts2Util.getSession().getAttribute("mainUser")).getLoginName();
//	}
//	
//	public void getPass() {
//		this.uPass = ((User)Struts2Util.getSession().getAttribute("mainUser")).getPassword();
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void getFeats() {
//		this.roleFeatList = (List<Feat>)Struts2Util.getSession().getAttribute("userFeat");
//	}
	
	public String setJSON(List<KeyAndValue> list) {
		if(list == null || list.size() == 0) {
			jsonObject = new JSONObject();
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = new JSONArray();
			jsonArray = JSONArray.fromObject(list);
			return "jsonArray";
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEditble() {
		return editble;
	}

	public void setEditble(Long editble) {
		this.editble = editble;
	}

	public Long getHighlight() {
		return highlight;
	}

	public void setHighlight(Long highlight) {
		this.highlight = highlight;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
}