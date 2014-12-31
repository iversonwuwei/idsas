package com.zdtx.ifms.common.utils.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sf.json.JSONObject;

/**
 * @ClassName: Tag_TableBody
 * @Description: 自定义标签-表格体元素
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_TableItem extends BodyTagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private Object list;
	private String style;// 添加样式

	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder content = new StringBuilder();
			content.append("<td " + (null == style ? "" : "style=\"" + style + "\"") + "><label>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			StringBuilder content = new StringBuilder();
			if (list == null) {
				content.append((bodyContent.getString())); // 输入标签内容
			} else {
				JSONObject json = JSONObject.fromObject(list);
				content.append(String.valueOf(json.get(bodyContent.getString()))); // 输入转译后的内容
			}
			content.append("</label></td>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public void setList(Object list) {
		this.list = list;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}