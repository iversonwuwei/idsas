<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="u" uri="/WEB-INF/tags/custom-tags.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<u:hide>
	<input id="currentPage" name="${param.pageName}.currentPage" value="${fn:split(param.page,',')[0]}" />     <%--当前页--%>
	<input id="_currentPage" value="${fn:split(param.page,',')[0]}" />						  <%--默认当前页--%>
	<input id="sortColumn" name="${param.pageName}.sortColumn" value="${fn:split(param.page,',')[1]}" /> <%--排序字段--%>
	<input id="sortNum" name="${param.pageName}.sortNum" value="${fn:split(param.page,',')[2]}" />      <%--排序方式--%>
	<input id="pkDesc" name="${param.pageName}.pkDesc" />
	<input id="sortChinese" name="${param.pageName}.sortChinese" />
	<input id="_sortChinese" value="${fn:split(param.page,',')[3]}" />
	<input id="pkName" name="${param.pageName}.pkName" />
	<input name="${param.pageName}.linkSize" value="${param.linkCount}" />    <%--具体页码显示数量--%>
</u:hide>

<script type="text/JavaScript">
$(function(){
	$("input[type='button']").css("cursor","pointer");
	$("input[class='cannot']").attr("disabled",true);
	
	function ps1(){}
	function ps2(){}
	function ps3(){}
	function ps4(){}   //为了让俩个page页面并存的时候  有不同名字的对象 
	ps1.prototype = {"pstr_${param.pageName}":"${param.page}"};  
	var p1=  new ps1();							//param.page得到了传进来的page的tostring
	ps2.prototype = {"strs_${param.pageName}": p1.pstr_${param.pageName}.split(",")};
	var p2=  new ps2();							 //切割tostring,得到各个值
	ps3.prototype = {"sss_${param.pageName}": (p2.strs_${param.pageName})[6].split(":")};	
	var p3=  new ps3();							//每页显示数量的数据集
	ps4.prototype = {"s2_${param.pageName}":  (p2.strs_${param.pageName})[15].split(":")};
	var p4=  new ps4();							//具体页的链接的数据集
	var pstr="${param.page}";
	var strs=pstr.split(",");
	var sss=strs[6].split(":");   //获得每页显示数量的数据集  赋给相应位置
	var s2=strs[15].split(":");	  //获得具体页的链接的数据集  赋给相应位置
	$("#pageSizeSelect_"+"${param.pageName}").html("");
	for(var i=0;i<p3.sss_${param.pageName}.length;i++){   //循环加载到对应的select里
		if((p3.sss_${param.pageName})[i]==(p2.strs_${param.pageName})[16]){
			$("#pageSizeSelect_"+"${param.pageName}").append("<option value='"+(p3.sss_${param.pageName})[i]+"' selected='selected'>"+(p3.sss_${param.pageName})[i]+"</option>");
		}else{
			$("#pageSizeSelect_"+"${param.pageName}").append("<option value='"+(p3.sss_${param.pageName})[i]+"'>"+(p3.sss_${param.pageName})[i]+"</option>");
		}
	}
	$("#linkspan_"+"${param.pageName}").html("");			//循环得到具体页的链接的数据集 
	for(var i=0;i<p4.s2_${param.pageName}.length;i++){
		if((p4.s2_${param.pageName})[i]==(p2.strs_${param.pageName})[0]){ //当前页变红,取消链接
			$("#linkspan_"+"${param.pageName}").append("<span style='color: red; font-weight: bolder; font-size: 15px;'>"+p4.s2_${param.pageName}[i]+"</span>&nbsp;&nbsp;");
		}else{
			$("#linkspan_"+"${param.pageName}").append("<a href='#' onclick='requery("+p4.s2_${param.pageName}[i]+",\"${param.pageName}\");'>"+p4.s2_${param.pageName}[i]+"</a>&nbsp;&nbsp;");
		}
	}
});
	/**
	 *具体页码 
	 * @param  pm是传进来的page的类名  如'newpage'
	 * @return
	 */
	requery = function(pageNo,pm) {                           //点具体页时调用
		//document.getElementById("currentPage").value = pageNo;
		document.getElementsByName(pm+".currentPage")[0].value= pageNo;   //把点击的页数赋给对应的字段
		document.getElementById("sortChinese").value = document.getElementById("_sortChinese").value;
		document.${param.formName}.submit();
	}

	/**
	 * 每页显示数量的下拉框的样式改变
	 * @param
	 * @return
	 */
	showPageSizeTextAndHideSelect = function() {			
		document.getElementById("pageSizeSpan").style.display = "";
		document.getElementById("pageSizeSelect").style.display = "none";
	}

	/**每页显示数量的下拉框的样式改变
	 * @param
	 * @return
	 */
	hidePageSizeTextAndShowSelect = function() {
		document.getElementById("pageSizeSpan").style.display = "none";
		document.getElementById("pageSizeSelect").style.display = "";
	}

	/**
	 *  排序方法
	 * @param pm是传进来的page的类名  如'newpage'
	 * @return
	 */
	resort = function(sortColumn, sortNum, pm,sortChinese, pkName) {
		document.getElementById("sortColumn").value = sortColumn;
		document.getElementById("sortNum").value = sortNum;
		document.getElementById("sortChinese").value = sortChinese || false;
		document.getElementById("pkName").value = pkName || "id";
		document.getElementById("currentPage").value = document.getElementById("_currentPage").value;
		document.${param.formName}.submit();
	};

	/**
	 * 删除方法  可保留当前页码
	 * @param  删除所对应的action名
	 * @param  是否弹出提示	
	 * @param  提示信息
	 * @return
	 */
	operItem = function(operAction, isHint, hint) {

		if (isHint) {
			if (confirm(hint)) {
				document.${param.formName}.action = operAction;
				document.${param.formName}.submit();
			}
		} else {
			document.${param.formName}.action = operAction;
			document.${param.formName}.submit();
		}
	};
</script>
<div class="page">
	<div class="page_n">共${fn:split(param.page,",")[4]}页${fn:split(param.page,",")[5]}条记录， 每页
		<select id="pageSizeSelect_${param.pageName}" name="${param.pageName}.pageSize" onchange="document.${param.formName}.submit();" onblur="showPageSizeTextAndHideSelect();" />
		条，
		<label>
			<c:if test="${fn:split(param.page,',')[7]}">
				<input type="button" name="toBegin" value="首页" onclick="requery(1,'${param.pageName}');" />
			<c:if test="${fn:split(param.page,',')[8]}">
				<input type="button" name="toPreviousTen" value="上十页" onclick="requery(${fn:split(param.page,',')[9]},'${param.pageName}');" />
			</c:if>
				<input type="button" name="toPrevious" value="上一页" onclick="requery(${fn:split(param.page,',')[10]},'${param.pageName}');" />
			</c:if>
			<c:if test="${!fn:split(param.page,',')[7]}">
				<!-- 无功能的按钮 -->
				<input type="button" value="首页" class="cannot"/>
				<input type="button" value="上一页" class="cannot"/>
			</c:if>
		</label>
		<span id="linkspan_${param.pageName}"></span>
		<c:if test="${fn:split(param.page,',')[11]}">
			<input type="button" name="toNext" value="下一页" onclick="requery(${fn:split(param.page,',')[13]},'${param.pageName}');" />
			<c:if test="${fn:split(param.page,',')[12]}">
				<input type="button" name="toNextTen" value="下十页" onclick="requery(${fn:split(param.page,',')[14]},'${param.pageName}');" />
			</c:if>
			<input type="button" name="toEnd" value="末页" onclick="requery(${fn:split(param.page,',')[4]},'${param.pageName}');" />
		</c:if>
		<c:if test="${!fn:split(param.page,',')[11]}">
			<!-- 无功能的按钮 -->
			<input type="button" value="下一页" class="cannot"/>
			<input type="button" value="末 页" class="cannot"/>
		</c:if>
	</div>
</div>