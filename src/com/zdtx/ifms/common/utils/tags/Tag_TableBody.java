package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.zdtx.ifms.common.utils.EntityUtil;
import com.zdtx.ifms.common.utils.Page;

/**
 * @ClassName: Tag_TableBody
 * @Description: 自定义标签-表格体
 * @author Leon Liu
 * @date 2012-9-17 上午9:39:28
 * @version V1.0
 */

public class Tag_TableBody extends BodyTagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	private Page<?> page;
	private Iterator<?> iterator;
	private String id;
	private String index;
	private String var;
	private int counter;
    private Object obj;
    private String oid;
	
	@Override
	public int doStartTag() throws JspException {
		if(index == null) {		//默认值
			index = "i";
		}
		if(page != null) {  
			iterator = page.getResult().iterator();   
			//存在下一个值
			if(iterator.hasNext()){  	
				counter = 0;	//先置0再加否则会一直加
				obj = iterator.next();		//取下一个值
				pageContext.setAttribute(var,  obj);  //赋给参数var，对象体
				if (index != null && index.length() > 0) {  
					pageContext.setAttribute(index, new Integer(counter));  //赋给参数index，循环次数
					counter ++;  
				}
				try {
					if(!obj.getClass().isArray()) {	//数据对像无关联关系
						oid = EntityUtil.getValueByField(obj, id);	//取数据对象主键
					} else {	//有关联关系，数据维数组形式，数据本体为数组最后一个元素
						Object[] objArr = (Object[])obj;
						oid = EntityUtil.getValueByField(objArr[objArr.length - 1], id);	//取数据对象主键
					}
					//写入样式，此处实际为第一行
					StringBuilder content = new StringBuilder();
					content.append("<tr class=\"Reprot_td\" id=\"tr" + oid + "\">");
					//序号列填值
					Long seq = Long.valueOf(counter) + (page.getCurrentPage() - 1) * page.getPageSize();	
					content.append("<td><label>" + seq  + "</label></td>");	
					pageContext.getOut().print(content.toString());	//在页面打印
				} catch (Exception e) {
					e.printStackTrace();
				}
				return EVAL_BODY_AGAIN;
			} else {  
				return SKIP_BODY;  
			}
		} else {
			return SKIP_BODY;
		}
	}

	@Override
	public int doAfterBody() throws JspException {
		try {
			if(bodyContent != null) {
				//先打印标签体内的内容
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
				bodyContent.clearBody();
			}
			//结束内容
			pageContext.getOut().print("</tr>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(page != null) {  
			// 检验迭代器是否有下一个值  
			if(iterator.hasNext()) {  
				obj = iterator.next();	//取数据对象
				pageContext.setAttribute(var, obj);//赋给参数var，对象体
				if (index != null && index.length() > 0) {  
					pageContext.setAttribute(index, new Integer(counter));  //赋给参数index，循环次数
					counter++;  
		        }
				try {
					if(!obj.getClass().isArray()) {	//数据对像无关联关系
						oid = EntityUtil.getValueByField(obj, id);	//取数据对象主键
					} else {	//有关联关系，数据维数组形式，数据本体为数组最后一个元素
						Object[] objArr = (Object[])obj;
						oid = EntityUtil.getValueByField(objArr[objArr.length - 1], id);
					}
					//写入样式，此处实际从第二行开始
					StringBuilder content = new StringBuilder();
					content.append("<tr class=\"Reprot_td\" id=\"tr" + oid + "\">");
					//序号列填值
					Long seq = Long.valueOf(counter) + (page.getCurrentPage() - 1) * page.getPageSize();
					content.append("<td><label>" + seq  + "</label></td>");
					pageContext.getOut().print(content.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
		        return EVAL_BODY_AGAIN;  
			} else {
				return SKIP_BODY;  
			} 
		} else {
				return SKIP_BODY;
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPage(Page<?> page) {
		this.page = page;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	public void setVar(String var) {  
	      this.var = var;  
	}
}