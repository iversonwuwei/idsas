package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Search
 * @Description: 自定义标签-搜索按钮（带有权限验证）
 * @author Leon Liu
 * @date 2012-9-18 下午4:12:21
 * @version V1.0
 */
public class Tag_Search extends TagSupport {

	private static final long serialVersionUID = 2985149838952928499L;
	private String onclick;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			String bathPath = Utils.getBasePath();
			String content = "<div align=\"center\"><a title='search' href=\"#\" onclick=\"" + onclick +"\"><img src=\"" + bathPath
					+ "images/button_search.jpg\" /></a></div>";
			pageContext.getOut().print(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
}