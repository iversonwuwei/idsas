package com.zdtx.ifms.common.web.layout;

import java.util.List;

import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.authority.Feat;
import com.zdtx.ifms.specific.model.authority.User;

public class LeftController extends URLSupport<User> {

	private static final long serialVersionUID = 129016968811767603L;

	public List<Feat> roleFeatList;
	
	@SuppressWarnings("unchecked")
	public String index(){
		this.roleFeatList = (List<Feat>)Struts2Util.getSession().getAttribute("userFeat");
		return "index";
	}

	@Override
	public User getModel() {
		return null;
	}

	public List<Feat> getRoleFeatList() {
		return roleFeatList;
	}

	public void setRoleFeatList(List<Feat> roleFeatList) {
		this.roleFeatList = roleFeatList;
	}
}