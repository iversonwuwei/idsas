package com.zdtx.ifms.specific.web.query;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.JSTree;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.query.DriverTree;
import com.zdtx.ifms.specific.service.query.DriverTreeManager;

public class DriverTreeController extends URLSupport<DriverTree>{

	private static final long serialVersionUID = 9173702565438285389L;

	@Autowired
	private DriverTreeManager dritreMgr;
	
	// 树数据集合
	private List<DriverTree> list = new ArrayList<DriverTree>();
	private String search_string = "";
	
	/**
	 * 生成树
	 * @return JSON
	 */
	public String getTree() {
		List<JSTree> jsonList = new ArrayList<JSTree>();
		try {
			list = dritreMgr.getTreeList(Long.parseLong(Struts2Util.getParameter("id")));
			for (DriverTree t : list) {
				JSTree node = new JSTree();
				Map<String, String> attr = new HashMap<String, String>();
				attr.put("id", t.getDriverid().toString());
				attr.put("sort", t.getDrivername());
				// data为节点名称
				node.setData(t.getDrivername());
				// 将属性集合放入一开始遍历新建的node里
				node.setAttr(attr);
				// 根节点
				if (-1L == t.getDepartmentid()) {
					attr.put("rel", "root");
				} 
				if (null != t.getDrivernumber() && !"".equals(t.getDrivernumber())) {
					// 这里默认打开三级
					// 根据业务不同,这里可以自行更改
					// 设置其state属性为空字符串意味着其为末节点
					attr.put("rel", "last");
					node.setState("");
				}
				
				jsonList.add(node);
			}
			PrintWriter out = null;
			try {
				out = Utils.getResponse().getWriter();
				JSONArray json = JSONArray.fromObject(jsonList);
				out.print(json.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(null != out){
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @title 搜索功能
	 * @return
	 */
	public String search() {
		PrintWriter out = null; 
		try {
			out=Utils.getResponse().getWriter();
			JSONArray json = JSONArray.fromObject(dritreMgr.search(search_string));
			out.print(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != out){
				out.close();
			}
		}
		return null;
	}
	
	public String getSearch_string() {
		return search_string;
	}

	public void setSearch_string(String search_string) {
		this.search_string = search_string;
	}

	@Override
	public DriverTree getModel() {
		return null;
	}
}
