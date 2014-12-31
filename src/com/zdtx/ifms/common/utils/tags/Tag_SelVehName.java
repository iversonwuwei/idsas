package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @ClassName: Tag_SelVeh
 * @Description: 自定义标签-级联菜单（车辆级）
 * @author Leon Liu
 * @date 2013-3-29 上午10:49:28
 * @version V1.0
 */
public class Tag_SelVehName extends TagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String name;
	private String value;
	private String width;
	
	@Override
	public int doStartTag() throws JspException {
		width = (width == null) ? "120" : width;
		try {
			String content = "<select id='select_vehicle_name'>" +
													"<option value='-1'></option>" +
											  "</select>";
			content += "<input id='v_select_vehicle_name' type='hidden' name='" + name + "' value='" + value + "'/>";
			content += "<script>" + 
										"var select_vehicle_name = dhtmlXComboFromSelect_cascade('select_vehicle_name', " + width + ");" +
										"select_vehicle_name.enableFilteringMode(true);" +
										"cascadeSel_getVehName();" +
								  "</script>";
			pageContext.getOut().print(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}