package com.zdtx.ifms.common.utils.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @ClassName: Tag_Table
 * @Description: 自定义标签-表格
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_Table extends BodyTagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String id;
	private String width;
	
	@Override
	public int doStartTag() throws JspException {
		id = (id == null ? "maintable" : id);
		try {
			StringBuilder content = new StringBuilder();
			content.append("<div class='main'>");
			content.append("<table id='" + id + "'");
			if(width != null) {
				content.append(" style='width:" + width + ";'");
			}
			content.append(" cellspacing='0' cellpadding='0' class='zhzd'>");
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
			.append("</div>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}