package com.zdtx.ifms.common.utils.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @ClassName: Tag_SearchItem
 * @Description: 自定义标签-搜索元素
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */

public class Tag_SearchItem extends BodyTagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String title;
	private String width;
	private String side;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder content = new StringBuilder();
			if(width == null) {		//默认宽度
				width = "15%";
			}
			if(side != null && "first".equals(side)) {
				content.append("<tr>");
			}
			if(title != null) {
				if(title.equals("empty") || title.equals("")) {	//用于留空占位
					content.append("<td class='text_right'></td>");
				} else {
					content.append("<td class='text_right' width='10%'><label>").append(title).append(":</label></td>");
				}
			}
			content.append("<td width='" + width + "' style='min-width:130px;'>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			//标签体内容
			StringBuilder content = new StringBuilder();
			content.append("</td>");
			if(side != null && "last".equals(side)) {
				content.append("</tr>");
			}
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setSide(String side) {
		this.side = side;
	}
}