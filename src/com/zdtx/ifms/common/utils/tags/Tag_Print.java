package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Print
 * @Description: 自定义标签-打印
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_Print extends TagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String path;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			String bathPath = Utils.getBasePath();
			String url = "<a href=\"" + bathPath + path + "\"><img src=\"" + bathPath + "images/dtree/ico_printer.png\" border=\"0\" align=\"absmiddle\" title=\"Print\" /> Print</a>";
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