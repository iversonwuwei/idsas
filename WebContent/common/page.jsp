<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<input type="hidden" id="currentPage" name="${param.pageName}.currentPage" value="1" />
	<input type="hidden" id="_currentPage" value="${page.currentPage}" />
	<input type="hidden" id="sortColumn" name="${param.pageName}.sortColumn" value="${page.sortColumn}" />
	<input type="hidden" id="sortNum" name="${param.pageName}.sortNum" value="${page.sortNum}" />
	<input type="hidden" id="pkDesc" name="${param.pageName}.pkDesc" />
	<input type="hidden" id="sortChinese" name="${param.pageName}.sortChinese" />
	<input type="hidden" id="_sortChinese" value="${page.sortChinese}" />
	<input type="hidden" id="pkName" name="${param.pageName}.pkName" />
	<input type="hidden" name="${param.pageName}.linkSize" value="${param.linkCount}" />

<script type="text/JavaScript">
$(function(){
	$("input[type='button']").css("cursor","pointer");
	$("input[class='cannot']").attr("disabled",true);
});
	/**
	 *
	 * @param
	 * @return
	 */
	requery = function(pageNo) {
		document.getElementById("currentPage").value = pageNo;
		document.getElementById("sortChinese").value = document.getElementById("_sortChinese").value;
		document.${param.formName}.submit();
	}

	/**
	 *
	 * @param
	 * @return
	 */
	showPageSizeTextAndHideSelect = function() {
		document.getElementById("pageSizeSpan").style.display = "";
		document.getElementById("pageSizeSelect").style.display = "none";
	}

	/**
	 * @param
	 * @return
	 */
	hidePageSizeTextAndShowSelect = function() {
		document.getElementById("pageSizeSpan").style.display = "none";
		document.getElementById("pageSizeSelect").style.display = "";
	}

	/**
	 *
	 * @param
	 * @return
	 */
	resort = function(sortColumn, sortNum, sortChinese, pkName) {
		document.getElementById("sortColumn").value = sortColumn;
		document.getElementById("sortNum").value = sortNum;
		document.getElementById("sortChinese").value = sortChinese || false;
		//document.getElementById("pkDesc").value = pkDesc || false;
		document.getElementById("pkName").value = pkName || "id";
		document.getElementById("currentPage").value = document.getElementById("_currentPage").value;
		document.${param.formName}.submit();
	};

	/**
	 *
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	operItem = function(operAction, isHint, hint) {

		if (isHint) {
			if (confirm(hint)) {
				document.${param.formName}.action = operAction;
				document.getElementById("currentPage").value = document.getElementById("_currentPage").value;
				document.${param.formName}.submit();
			}
		} else {
			document.${param.formName}.action = operAction;
			document.getElementById("currentPage").value = document.getElementById("_currentPage").value;
			document.${param.formName}.submit();
		}
	};
</script>
<div class="page">
	<div class="page_n">Total&nbsp;<s:property value="page.getTotalPage()" />&nbsp;Pages&nbsp;${page.totalCount}&nbsp;Records,
		<select id="pageSizeSelect" name="${param.pageName}.pageSize" onchange="document.${param.formName}.submit();" onblur="showPageSizeTextAndHideSelect();" >
			<s:iterator value="page.getResultperpage()" var="ps">
				<option value="${ps}" <s:if test="%{#ps == page.pageSize}">selected="selected"</s:if>>${ps}</option>
			</s:iterator>
		</select>
		Records per Page, 
			<s:if test="page.hasPrevious()">
				<input type="button" name="toBegin" value="First" onclick="requery(1);" />
			<s:if test="page.hasPreBatch()">
				<input type="button" name="toPreviousTen" value="<< 10" onclick="requery(<s:property value="page.getPreBatch()"/>);" />
			</s:if>
				<input type="button" name="toPrevious" value="Prev" onclick="requery(<s:property value="page.getPrevious()"/>);" />
			</s:if>
			<s:else>
				<!-- 无功能的按钮 -->
				<input type="button" value="First" class="cannot"/>
				<input type="button" value="Prev" class="cannot"/>
			</s:else>
		<s:iterator value="page.links" var="link">
			<s:if test="%{#link == page.currentPage}">
				<span style="color: red; font-weight: bolder; font-size: 15px;">${link}</span>
			</s:if>
			<s:else>
				<a href="#${link}" onclick="requery(${link});">${link}</a>
			</s:else>
		</s:iterator>
		<s:if test="page.hasNext()">
			<input type="button" name="toNext" value="Next" onclick="requery(<s:property value="page.getNext()"/>);" />
			<s:if test="page.hasNextBatch()">
				<input type="button" name="toNextTen" value=">> 10" onclick="requery(<s:property value="page.getNextBatch()"/>);" />
			</s:if>
			<input type="button" name="toEnd" value="Last" onclick="requery(${page.totalPage});" />
		</s:if>
		<s:else>
			<!-- 无功能的按钮 -->
			<input type="button" value="Next" class="cannot"/>
			<input type="button" value="Last" class="cannot"/>
		</s:else>
	</div>
</div>