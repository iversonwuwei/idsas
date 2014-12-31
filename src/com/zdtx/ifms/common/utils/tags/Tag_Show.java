package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Show
 * @Description: 自定义标签-进入查看页按钮（带有权限验证）
 * @author Leon Liu
 * @date 2012-10-25 上午10:24:05
 * @version V1.0
 */
public class Tag_Show extends TagSupport {

	private static final long serialVersionUID = 2786122271648346365L;
	private String path;
	private String id;
	private String method;

	@Override
	public int doStartTag() throws JspException {
		try {
			String basePath = Utils.getBasePath();
			Object editble = pageContext.getRequest().getAttribute("editble");
			String url = "";
			if(null == method) {	//空即为默认值
				method = "show";
			}
			if(null == editble) {	//无可编辑属性
				url = "<a href=\"" + basePath + path + "/" + id + "/" + method +"\"><img src=\"" + basePath
						+ "images/dtree/ico_show.gif\" title=\"View\"/></a>";
			} else {	//有可编辑属性
				url = "<a href=\"" + basePath + path + "/" + id + "/" + method +"?editble=" + editble + "\"><img src=\"" + basePath
						+ "images/dtree/ico_show.gif\" title=\"View\"/></a>";
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