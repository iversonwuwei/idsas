package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @ClassName: Tag_MonthDoubleDate
 * @Description: 自定义标签-月份段选择
 * @author Leon Liu
 * @date 2012-10-8 下午1:21:54
 * @version V1.0
 */
public class Tag_MonthDoubleDate extends TagSupport {

	private static final long serialVersionUID = 8044968673625554781L;
	private String id;
	private String beginName;
	private String endName;
	private String beginValue;
	private String endValue;
	private Boolean future;		//可否选未来日期

	@Override
	public int doStartTag() throws JspException {
		try {
			if(null == future) {	//空即为默认值
				future = false;
			}
			String url = "";
			if(!future) {	//默认为不可选未来日期
				url = "<label class=\"input_date_l\">"
						+ "<input type=\"text\" id=\"" + id + "_begin_date\" name=\"" + beginName + "\" value=\"" + beginValue +"\" onfocus=\"WdatePicker({dateFmt:'yyyy-MM', maxDate:&quot;#F{$dp.$D('" + id + "_end_date')||'%y-%M'}&quot;, readOnly:true});this.blur();\" />"	//
					+ "</label>"
					+ "<label class=\"input_date_r\">"
						+ "<input type=\"text\" id=\"" + id + "_end_date\" name=\"" + endName + "\" value=\"" + endValue +"\" onfocus=\"WdatePicker({dateFmt:'yyyy-MM', minDate:&quot;#F{$dp.$D('" + id + "_begin_date')}&quot;, maxDate:'%y-%M', readOnly:true});this.blur();\"/>"
					+ "</label>";
			} else {	//可选未来日期
				url = "<label class=\"input_date_l\">"
						+ "<input type=\"text\" id=\"" + id + "_begin_date\" name=\"" + beginName + "\" value=\"" + beginValue +"\" onfocus=\"WdatePicker({dateFmt:'yyyy-MM', maxDate:&quot;#F{$dp.$D('" + id + "_end_date')}&quot;, readOnly:true});this.blur();\" />"	//
					+ "</label>"
					+ "<label class=\"input_date_r\">"
						+ "<input type=\"text\" id=\"" + id + "_end_date\" name=\"" + endName + "\" value=\"" + endValue +"\" onfocus=\"WdatePicker({dateFmt:'yyyy-MM', minDate:&quot;#F{$dp.$D('" + id + "_begin_date')}&quot;, readOnly:true});this.blur();\"/>"
					+ "</label>";
			}
			pageContext.getOut().print(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setBeginName(String beginName) {
		this.beginName = beginName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}

	public void setBeginValue(String beginValue) {
		this.beginValue = beginValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}
	
	public void setFuture(Boolean future) {
		this.future = future;
	}
}