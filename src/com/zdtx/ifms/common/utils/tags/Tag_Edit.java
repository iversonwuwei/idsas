package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Edit
 * @Description: 自定义标签-进入编辑页按钮（带有权限验证）
 * @author Leon Liu
 * @date 2012-10-25 上午10:05:42
 * @version V1.0
 */
public class Tag_Edit extends TagSupport {

	private static final long serialVersionUID = 2786122271648346365L;
	private String path;
	private String id;
	private String method;

	@Override
	public int doStartTag() throws JspException {
		try {
			String bathPath = Utils.getBasePath();
			String url = "";
			if(null != pageContext.getRequest().getAttribute("editble") && ((Long)pageContext.getRequest().getAttribute("editble")) == 0L) {	//可编辑
				url = "<img src=\"" + bathPath + "images/dtree/ico_edit_h.gif\" title=\"Can't edit\" />";
			} else {
				if(null == method) {	//空即为默认值
					method = "edit";
				}
				url = "<a href='" + bathPath + path + "/" + id + "/" + method + "?editble=1'><img src=\"" + bathPath
						+ "images/dtree/ico_edit.gif\" title=\"Edit\" /></a>";
			}
			pageContext.getOut().print(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}