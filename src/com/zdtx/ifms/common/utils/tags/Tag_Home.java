package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Home
 * @Description: 自定义标签-返回模块主页面（带有权限验证）
 * @author Leon Liu
 * @date 2012-9-17 上午10:07:37
 * @version V1.0
 */
public class Tag_Home extends TagSupport {

	private static final long serialVersionUID = -601659163931066080L;
	private String path;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			String bathPath = Utils.getBasePath();
			Object editble = pageContext.getRequest().getAttribute("editble");
			String url = "";
			if(null == editble) {	//无可编辑属性
				url = "<a href=\"" + bathPath + path + "\"><img src=\"" + bathPath
						+ "images/dtree/ico_home.gif\" border=\"0\" align=\"absmiddle\" title=\"Back\" /> Back</a>";
			} else {	//有可编辑属性
				url = "<a href=\"" + bathPath + path
						+ (path.contains("?") ?  "&" : "?")	//判断是否有其他属性
						+ "editble=" + editble + "\"><img src=\"" + bathPath
						+ "images/dtree/ico_home.gif\" border=\"0\" align=\"absmiddle\" title=\"Back\" /> Back</a>";
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
}