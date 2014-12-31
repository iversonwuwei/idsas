package com.zdtx.ifms.specific.web.wmrs;

import com.zdtx.ifms.common.web.URLSupport;

public class RealController extends URLSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -101166068813308757L;
	
	public String index(){
		return "index";
	}
	
	@Override
	public Object getModel() {
		return null;
	}

}
