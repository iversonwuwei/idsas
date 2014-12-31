package com.zdtx.ifms.common.utils.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @ClassName: Tag_MainTable
 * @Description: 自定义标签-表格头
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_TableHeader extends BodyTagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private Boolean operate;
	private Integer row;
	private String width;

	@Override
	public int doStartTag() throws JspException {
		try {
			row = (row == null ? 1 : row);
			width = (width == null ? "3%" : width);
			StringBuilder content = new StringBuilder();
			content.append("<thead>")
							.append("<tr class='Reprot_Head'>")
								.append("<td width='" + width + "' rowspan='" + row + "'><label>No.</label></td>");
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
			Object editble = pageContext.getRequest().getAttribute("editble");
			Long e = (Long)editble;
			Boolean flagOper;
			if(operate == null) {	//operate is null
				if(e == null || e == 0L) {
					flagOper = false;
				} else {
					flagOper = true;
				}
			} else if(operate) {
				flagOper = true;
			} else {
				flagOper = false;
			}
			if(row == null) {
				row = 1;
			}
			if(flagOper) {
				content.append("<td width='7%' rowspan='" + row + "'><label>Modify</label></td>");
			}
			content.append("</tr>")
					.append("</thead>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public void setOperate(Boolean operate) {
		this.operate = operate;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}