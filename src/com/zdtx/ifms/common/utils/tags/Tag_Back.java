package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Back
 * @Description: 自定义标签-返回上一个历史页按钮（带有权限验证）
 * @author Leon Liu
 * @date 2012-10-25 上午10:03:09
 * @version V1.0
 */
public class Tag_Back extends TagSupport {

	private static final long serialVersionUID = 2786122271648346365L;

	@Override
	public int doStartTag() throws JspException {
		if(null != pageContext.getRequest().getAttribute("editble") && ((Long)pageContext.getRequest().getAttribute("editble")) == 0L) {	//可编辑
			return SKIP_BODY;
		}
		try {
			String basePath = Utils.getBasePath();
			String url = "<a href=\"javascript:window.history.back();\"><img src=\"" + basePath
					+ "images/dtree/icon_back.jpg\" alt=\"Back\" /></a>";
			pageContext.getOut().print(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}