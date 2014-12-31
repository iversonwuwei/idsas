/*
 * @(#)Constant.java  Friday, November 26th, 2010
 * Copyright(c) 2010, Wiflg Goth. All rights reserved.
 */
package com.zdtx.ifms.common.utils;

/**
 * 项目常量库
 * @author Leon Liu
 * @since 2013-3-18 9:46:05
 */
public class Constants {
	
	//ERP项目名
	public static String ERP = "temp_erp";
	//保存成功跳转内容
	public static final String[] SUCCESS = { "<script language='JavaScript'>alert('Save successfully!');location='", "'</script>" };
	//保存失败统一跳转内容
	public static final String ERROR = "<script language='JavaScript'>alert('Save failed!');window.history.back();</script>";
	//页面默认数据条数
	public static final int PAGE_SIZE = 25;
	//use bu print PDF
	public static final String CATALINA_HOME = System.getProperty("catalina.home");
	public static final String TEM_SOURCE_DIR_PATH = Constants.class.getResource("/").getPath() + "print/PrintTemplate.xsl";
	public static final String DATA_SOURCE_DIR_PATH = Constants.class.getResource("/").getPath() + "print/PrintData.xml";
	public static final String CONF_SOURCE_DIR_PATH = Constants.class.getResource("/").getPath() + "print/PrintRender.xml";
	public static final String PDF_DESTINATION_DIR_PATH = System.getProperty("catalina.home") + "/pdf_for_print";
}