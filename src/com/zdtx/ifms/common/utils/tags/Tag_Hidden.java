/**
 * @title 隐藏标签
 * @detail 隐藏标签,以简化代码用
 * @author LiuGuilong
 * @date 2012-4-27
 */
package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class Tag_Hidden extends BodyTagSupport {

	private static final long serialVersionUID = 2786122271648346365L;

	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		BodyContent content = getBodyContent();
		try {
			getPreviousOut().print(content.getString().replaceAll("<input", "<input style=\"display: none;\" "));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doAfterBody() throws JspException {
		return SKIP_PAGE;
	}
}
