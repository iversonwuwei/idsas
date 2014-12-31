package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.authority.User;

/**
 * @ClassName: Tag_Navigation
 * @Description: 自定义标签-导航区（带有权限验证）
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */
public class Tag_Navigation extends TagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private String father;	//父模块名
	private String model;		//模块名
	private String operate; 		//操作	不填为主页面	edit为编辑页		show为浏览页
	private String addPath;		//新增路径
	private String addMethod;	//新增方法
	private String excelMethod;		//导出excel的JS方法
	private String printPath;		//打印PDF的JS方法
	private String saveMethod;		//保存方法
	private String homePath;			//返回路径
	
	@Override
	public int doStartTag() throws JspException {
		try {
			User user = (User)Struts2Util.getSession().getAttribute("mainUser");
			String bathPath = Utils.getBasePath();
			Object editble = pageContext.getRequest().getAttribute("editble");
			StringBuffer url = new StringBuffer("<div class='welcome'>");
			url.append("<div class=\"user\">");
			url.append("<img src='" + bathPath + "images/temp_34.gif' width='6' height='29' align='absmiddle' />");
			url.append("<img src='" + bathPath + "images/dtree/ico_user.gif' width='16' height='16' align='absmiddle' />");
			url.append("&nbsp;&nbsp;Welcome&nbsp;&nbsp;" + user.getLoginName() + "&nbsp;&nbsp;[" + user.getUserRole().getRoleName() + "]&nbsp;&nbsp;");
			if(null == operate) {		//主页面
				url.append("Current Position&nbsp;:&nbsp;").append(father).append("&nbsp;&gt;&gt;&nbsp;<span style=\"font-weight:bold\">").append(model).append("</span>");
			} else {
				url.append("Current Position&nbsp;:&nbsp;").append(father).append("&nbsp;&gt;&gt;&nbsp;").append(model).append("&nbsp;&gt;&gt;&nbsp;<span style='font-weight:bold'>").append(operate).append("</span>");
			}
			url.append("</div>");
			url.append("<div class=\"czan\" style=\"width:300px;\">");
			url.append("<ul>");
			if(null != addPath) {			//新增按钮
				if(null != editble && ((Long)editble) == 0L) {	//不可编辑
					//do nothing
				} else {
					String url_add = "<a href='" + bathPath + addPath + "!" + addMethod + "?editble=1' style='width: 60px; margin-left: -15px;'>" +
							"<img src='" + bathPath + "images/dtree/ico_add.gif' border='0' align='absmiddle' title='Add' /> Add</a>";
					url.append("<li>" + url_add + "</li>");
				}
			}
			if(null != saveMethod) {	//保存按钮
				if(null != editble && ((Long)editble) == 0L) {	//不可编辑
					//do nothing
				} else {
					String save_url = "<a href='#' onclick='" + saveMethod +"' style='width: 60px; margin-left: -15px;'>" +
							"<img src='" + bathPath + "images/dtree/ico_save.gif' border='0' align='absmiddle' title='Save' /> Save</a>";
					url.append("<li>" + save_url + "</li>");
				}
			}
			if(null != excelMethod) {	//导出excel按钮
				String excel_url = "<a href='#' onclick='" + excelMethod + "' style='width: 70px; margin-left: -20px;'>" +
						"<img src='" + bathPath + "images/dtree/ico_exportall.gif' border='0' align='absmiddle' title='Export' /> Export</a>";
				url.append("<li>" + excel_url + "</li>");
			}
			if(null != printPath) {	//打印PDF按钮
				String print_url = "<a href='" + bathPath + printPath + "' style='width: 70px; margin-left: -20px;'>" +
						"<img src='" + bathPath + "images/dtree/ico_printer.png' border='0' align='absmiddle' title='Print' /> Print</a>";
				url.append("<li>" + print_url + "</li>");
			}
			if(null != homePath) {		//返回按钮
				String url_home = "";
				if(null == editble) {	//无可编辑属性
					url_home = "<a href='" + bathPath + homePath + "' style='width: 60px; margin-left: -20px;'>" +
							"<img src='" + bathPath + "images/dtree/ico_home.gif' border='0' align='absmiddle' title='Back' /> Back</a>";
				} else {	//有可编辑属性
					url_home = "<a href='" + bathPath + homePath + (homePath.contains("?") ?  "&" : "?") + "editble=" + editble + "' style='width: 60px; margin-left: -20px;'>" +		//判断是否有其他属性
							"<img src='" + bathPath + "images/dtree/ico_home.gif' border='0' align='absmiddle' title='Back' /> Back</a>";
				}
				url.append("<li>" + url_home + "</li>");
			}
			pageContext.getOut().print(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			StringBuilder content = new StringBuilder();
			content.append("</ul>");
			content.append("</div>");
			content.append("</div>");
			pageContext.getOut().print(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	public void setFather(String father) {
		this.father = father;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public void setAddPath(String addPath) {
		this.addPath = addPath;
	}

	public void setAddMethod(String addMethod) {
		this.addMethod = addMethod;
	}

	public void setExcelMethod(String excelMethod) {
		this.excelMethod = excelMethod;
	}

	public void setPrintPath(String printPath) {
		this.printPath = printPath;
	}

	public void setSaveMethod(String saveMethod) {
		this.saveMethod = saveMethod;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}
}