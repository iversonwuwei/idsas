package com.zdtx.ifms.common.utils.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @ClassName: Tag_SearchBody
 * @Description: 自定义标签-搜索区
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_SearchBody extends BodyTagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String row;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder content = new StringBuilder();
			if(row == null || "1".equals(row)) {	//不同行数设置不同样式
				content.append("<div class='search' style='height: 40px;'><div class='search_g'>");
			} else if("2".equals(row)) {
				content.append("<div class='search' style='height: 60px;'><div class='search_g'>");
			} else if("3".equals(row)) {
				content.append("<div class='search' style='height: 80px;'><div class='search_g'>");
			}
			content.append("<table cellspacing='0' cellpadding='0'>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			StringBuilder content = new StringBuilder();
			content.append("</table>")
			.append("</div>").append("</div>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	@Override
	public int doAfterBody() throws JspException {
		return SKIP_PAGE;
	}

	public void setRow(String row) {
		this.row = row;
	}
}