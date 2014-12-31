package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Add
 * @Description: 自定义标签-新增按钮（带有权限验证）
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_Add extends TagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String path;
	private String method;
	
	@Override
	public int doStartTag() throws JspException {
		if(null != pageContext.getRequest().getAttribute("editble") && ((Long)pageContext.getRequest().getAttribute("editble")) == 0L) {	//不可编辑
			return SKIP_BODY;
		}
		try {
			String bathPath = Utils.getBasePath();
			String url = "<a href=\"" + bathPath + path + "!" + method + "\"><img src=\"" + bathPath
					+ "images/dtree/ico_new.GIF\" border=\"0\" align=\"absmiddle\" title=\"Add\" /> Add</a>";
			pageContext.getOut().print(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}