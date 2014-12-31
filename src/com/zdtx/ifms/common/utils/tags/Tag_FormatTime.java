package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @ClassName: Tag_FormatTime
 * @Description: 自定义标签-格式化时间（秒数-》XX:XX:XX）
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_FormatTime extends TagSupport {

	private static final long serialVersionUID = -580535682571624143L;
	private String value;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			if(value == null || value.equals("")) {
				pageContext.getOut().print("00:00:00");
			} else {
				Long sec = Long.valueOf(value.replace(".00", "").replace(".0", ""));	//秒数
				Long h = sec/3600L;	//时
				Long m = (sec%3600L)/60L;	//分
				Long s = sec%60L;	//秒
				String hh = h < 10 ? ("0" + h) : String.valueOf(h);	//补位
				String mm = m < 10 ? ("0" + m) : String.valueOf(m);
				String ss = s < 10 ? ("0" + s) : String.valueOf(s);
				pageContext.getOut().print(hh + ":" + mm + ":" + ss);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setValue(String value) {
		this.value = value;
	}
}