/*
 * @(#)WelcomeController.java  Nov 24, 2010
 * Copyright(c) 2010, Wiflg Goth. All rights reserved.
 */
package com.zdtx.ifms.common.web.layout;

import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.authority.User;

/**
 *
 * @author Wiflg Goth
 * @since Nov 24, 2010
 */
public class WelcomeController extends URLSupport<Object>{

	private static final long serialVersionUID = 8518716427051029913L;
	private String cardid;
	public String index() {
		return "index";
	}

	public String main() {
		return "main";
	}

	public String top() {
		return "top";
	}

	public String hide() {
		return "hide";
	}

	public String bottom() {
		return "bottom";
	}

	public User getModel() {
		return null;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	
}