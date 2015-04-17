/*
 * @(#)JSTree.java  May 9, 2011
 * Copyright(c) 2011, Wiflg Goth. All rights reserved.
 */
package com.zdtx.ifms.common.utils;

import java.util.Map;
import java.util.Set;

/**
 * 控制Tree的打开和关闭 - 新架构中利用javascript实现
 **/

/**
 * 
 * @author Wiflg Goth
 * @since May 9, 2011
 */
public class JSTree {

	public static final String OPEN_STATE = "open";
	public static final String CLOSED_STATE = "closed";

	private String data;    
	private Set<String> children;  //
	private Map<String, String> attr;
	private String state = CLOSED_STATE;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Set<String> getChildren() {
		return children;
	}

	public void setChildren(Set<String> children) {
		this.children = children;
	}

	public Map<String, String> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}