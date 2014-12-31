package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Utils;

/**
 * @ClassName: Tag_Save
 * @Description: 自定义标签-保存按钮（带有权限验证）
 * @author Leon Liu
 * @date 2012-9-17 上午9:55:19
 * @version V1.0
 */
public class Tag_Save extends TagSupport {

	private static final long serialVersionUID = 3553317222160327412L;
	
	private String onclick;
	
	@Override
	public int doStartTag() throws JspException {
		if(null != pageContext.getRequest().getAttribute("editble") && ((Long)pageContext.getRequest().getAttribute("editble")) == 0L) {	//可编辑
			return EVAL_BODY_INCLUDE;
		}
		try {
			String bathPath = Utils.getBasePath();
			String url = "<a href=\"#\" onclick=\"" + onclick +"\"><img src=\"" + bathPath
					+ "images/dtree/ico_save.gif\" border=\"0\" align=\"absmiddle\" title=\"Save\" /> Save</a>";
			pageContext.getOut().print(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
}