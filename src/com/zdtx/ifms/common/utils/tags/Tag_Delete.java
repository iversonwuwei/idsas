package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Delete
 * @Description: 自定义标签-删除按钮（带有权限验证）
 * @author Leon Liu
 * @date 2012-10-25 上午10:00:57
 * @version V1.0
 */
public class Tag_Delete extends TagSupport {

	private static final long serialVersionUID = 2786122271648346365L;
	private String path;
	private String id;
	private String method;
	private String jsmethod;

	@Override
	public int doStartTag() throws JspException {
		try {
			String basePath = Utils.getBasePath();
			String url = "";
			if(null != pageContext.getRequest().getAttribute("editble") && ((Long)pageContext.getRequest().getAttribute("editble")) == 0L) {	//不可编辑
				url = "<img src=\"" + basePath + "images/dtree/ico_delete_h.gif\" title=\"Can't Delete\"/>";
			} else {
				if(null == method) {	//空即为默认值
					method = "delete";
				}
				if(null==jsmethod){
					url = "<a href='#' onclick=\"if(confirm('Be sure you want to delete this data?'))location='" + basePath  + path + "/" + id
							+ "/" + method +"?editble=1'\"><img src='" + basePath + "images/dtree/ico_delete.gif' title='Delete'/></a>";
				}else{
					url = "<a href='#' onclick=\"" + jsmethod + "(" + id + ")\"><img src='" + basePath + "images/dtree/ico_delete.gif' title='Delete'/></a>";
				}
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

	public void setJsmethod(String jsmethod) {
		this.jsmethod = jsmethod;
	}
}