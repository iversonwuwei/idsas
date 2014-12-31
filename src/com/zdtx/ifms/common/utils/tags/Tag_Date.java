package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * @ClassName: Tag_Date
 * @Description: 自定义标签-日期选择
 * @author Leon Liu
 * @date 2012-10-29 上午11:17:26
 * @version V1.0
 */
public class Tag_Date extends TagSupport {

	private static final long serialVersionUID = 8044968673625554781L;
	private String id;
	private String dateName;
	private String dateValue;
	private Boolean future; // 可否选未来日期
	private String dateFmt;// 自定义日期格式
	private Boolean  isShowWeek;// 是否显示周

	@Override
	public int doStartTag() throws JspException {
		try {
			if(null == future) {	//空即为默认值
				future = false;
			}
			String url = "";
			if(!future) {	//默认为不可选未来日期
				url = "<label class=\"input_date\">"
						+ "<input type=\"text\" id=\"" + id + "\" name=\"" + dateName + "\" value=\"" + dateValue +"\" onfocus=\"WdatePicker({isShowWeek:"+(null == isShowWeek ? false : isShowWeek)+",dateFmt:'" + (null==dateFmt ? "yyyy-MM-dd" : dateFmt ) + "', maxDate:'%y-%M-%d', readOnly:true});this.blur();\" />"	//
					+ "</label>";
			} else {	//可选未来日期
				url = "<label class=\"input_date\">"
						+ "<input type=\"text\" id=\"" + id + "\" name=\"" + dateName + "\" value=\"" + dateValue +"\" onfocus=\"WdatePicker({isShowWeek:"+(null == isShowWeek ? false : isShowWeek)+",dateFmt:'" + (null==dateFmt ? "yyyy-MM-dd" : dateFmt ) + "', readOnly:true});this.blur();\" />"	//
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

	public void setDateName(String dateName) {
		this.dateName = dateName;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public void setFuture(Boolean future) {
		this.future = future;
	}

	public String getDateFmt() {
		return dateFmt;
	}

	public void setDateFmt(String dateFmt) {
		this.dateFmt = dateFmt;
	}

	public Boolean getIsShowWeek() {
		return isShowWeek;
	}

	public void setIsShowWeek(Boolean isShowWeek) {
		this.isShowWeek = isShowWeek;
	}
}