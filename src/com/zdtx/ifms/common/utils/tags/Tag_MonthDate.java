package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * @ClassName: Tag_Date
 * @Description: 自定义标签-月份选择
 * @author Leon Liu
 * @date 2012-10-29 上午11:17:26
 * @version V1.0
 */
public class Tag_MonthDate extends TagSupport {

	private static final long serialVersionUID = 8044968673625554781L;
	private String id;
	private String dateName;
	private String dateValue;

	@Override
	public int doStartTag() throws JspException {
		try {
			 
			 String url = "<label class=\"input_date\">"
						+ "<input type=\"text\" id=\"" + id + "\" name=\"" + dateName + "\" value=\"" + dateValue +"\" onfocus=\"WdatePicker({isShowClear : false,dateFmt:'yyyy-MM', maxDate : '%y-%M',readOnly:true});this.blur();\" />"	//
					+ "</label>";
			pageContext.getOut().print(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDateName(String dateName) {
		this.dateName = dateName;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}
 
}