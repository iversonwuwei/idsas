package com.zdtx.ifms.common.web.layout;

import com.zdtx.ifms.common.web.URLSupport;

public class TopController extends URLSupport<Object> {

	private static final long serialVersionUID = 129016968811767603L;

	public String index(){
		return "index";
	}

	public String main() {
		return "main";
	}
	
	@Override
	public Object getModel() {
		return null;
	}
}