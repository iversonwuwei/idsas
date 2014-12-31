package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.KeyAndValue;

/**
 * JqueryUIselect标签
 * 
 * @author Lonn
 * 
 */
public class Tag_Select extends TagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String id;// 非必填，如若空，则生成的select标签的id和难么相同
	private String name;// 必填
	private String value;// 非必填，默认选中的value
	private String width;// 非必填
	private List<KeyAndValue> list;// keyandvalue的集合

	@Override
	public int doStartTag() throws JspException {
		width = (width == null) ? "120px" : width;
		try {
			StringBuffer select = new StringBuffer();
			select.append("<select id='" + (null == id ? name : id) + "' name='" + name + "' style='width:" + width + ";'><option></option>");
			List<KeyAndValue> _list = (List<KeyAndValue>) list;
			for (KeyAndValue kv : _list) {
				select.append("<option value='" + kv.getKey() + "' " + ((null != value && value.equals(kv.getKey())) ? " selected='selected'" : "") + " >" + kv.getValue() + "</option>");
			}
			select.append("<select/>");
			select.append("<script type='text/javascript'>");
			select.append("$(function(){");
			select.append("$(document.getElementById('" + (null == id ? name : id) + "')).combobox();");
			select.append("$('.ui-autocomplete-input').css('width', '" + width + "');");
			select.append("});");
			select.append("</script>");
			pageContext.getOut().print(select.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setList(List<KeyAndValue> list) {
		this.list = list;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}