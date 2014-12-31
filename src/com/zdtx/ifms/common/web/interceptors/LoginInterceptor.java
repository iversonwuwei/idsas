package com.zdtx.ifms.common.web.interceptors;

import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.specific.model.authority.User;

/**
 * 通用类
 * 项目请求跳转拦截器，监听session是否合法
 * @author Leon Liu
 * @since 2013-2-25 9:24:45
 */
public class LoginInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -3324460287195886852L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		HttpSession session = Struts2Util.getSession();
		Object user = session.getAttribute("mainUser");
		if (null == user) {
			return "login";
		}
		User u = (User) user;
		if (!"F".equals(u.getIsDelete())) {
			session.setAttribute("mainUser", null);
			return "login";
		}
		
//		if(invocation.getAction().getClass() != FileUploadController.class) {
//			@SuppressWarnings("unchecked")
//			URLSupport<Object> obj = (URLSupport<Object>)invocation.getAction();
//			obj.getName();
//			obj.getPass();
//			obj.getFeats();
//		}
		return invocation.invoke();
	}
}