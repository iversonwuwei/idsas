package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.zdtx.ifms.common.utils.Page;

/**
 * @ClassName: Tag_Sort
 * @Description: 自定义标签-表头排序
 * @author Leon Liu
 * @date 2012-9-18 下午4:26:52
 * @version V1.0
 */

public class Tag_Sort extends BodyTagSupport {

	private static final long serialVersionUID = -6127170728028934239L;
	
	private Page<?> page;
	private String property;// 对应属性名
	private Boolean chinese;// 中文排序
	private String width;// TD宽度
	private String side;	// 起始
	private Integer rowspan;	// 行
	private Integer colspan;	// 列

	@Override
	public int doStartTag() throws JspException {
		/** 中文排序 **/
		String isChinese = "";
		if (null != chinese) {// 中文项非必填项,顾避免空指针做此判断
			if (chinese) {
				isChinese = ",true";
			}
		}
		rowspan = (null == rowspan ? 1 : rowspan);
		colspan = (null == colspan ? 1 : colspan);
		/** 事件部分 **/
		String click = "onclick=\"resort('" + property + "', 1 " + isChinese + ");\"";
		if(colspan == 1) {
			if (null != page.getSortColumn() && page.getSortColumn().equals(property)) {// 初始的SortColumn为空,这里做空判断
				click = "onclick=\"resort('" + property + "', " + (page.getSortNum() + 1) + ");\" class=\"tableSortHeader"
						+ page.getSortNum() % 3 + "\"";
			}
		}
		/** TD标签 **/
		String td = "";
		if(side != null && side.equals("new")) {
			td += "</tr><tr class='Reprot_Head'>";
		}
		if(colspan != 1) {
			td += "<td width='" + (null == width ? 0 : width) + "' colspan='" + colspan + "' rowspan='" + rowspan + "'>";
		} else {
			td += "<td width='" + (null == width ? 0 : width) + "' style='cursor:pointer' " + click + "  colspan='" + colspan + "' rowspan='" + rowspan + "'>";
		}
		try {
			pageContext.getOut().print(td);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			String td = "</td>";
			pageContext.getOut().print(td);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public void setPage(Page<?> page) {
		this.page = page;
	}
	
	public void setProperty(String property) {
		this.property = property;
	}
	
	public void setChinese(Boolean chinese) {
		this.chinese = chinese;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}

	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}
}