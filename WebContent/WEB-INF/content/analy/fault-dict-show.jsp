<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	</head>
	<body>
		<custom:navigation father="Fault Analyzer" model="Fault Dictionary" operate="Show" homePath="analy/fault-dict" />
		<form action="<%=basePath%>analy/fault-dict!create" name="queryResult" method="post">
			<input type="hidden" name="faultcodeid" id="faultcodeid" value="${counseling}" /> 
			<input type="hidden" name="isdelete" id="isdelete" value="${isdelete}" /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">Code:</td>
						<td width="82%" style="text-align: left;">
						${code}
						</td>
					</tr>
					<tr>
						<td class="text_bg">Scope:</td>
						<td style="text-align: left;" >
						${engscope}
						</td>
					</tr>
					<tr>
						<td class="text_bg">Definition:</td>
						<td style="text-align: left;">
						<textarea rows="2" cols="1" style="width: 75%" name="engdefinition" id="engdefinition" readonly="readonly" >${engdefinition}</textarea>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Context:</td>
						<td style="text-align: left;">
						<textarea rows="2" cols="1" style="width: 75%;height:50px " name="engcontext" id="engcontext" readonly="readonly">${engcontext}</textarea>
						</td>
					</tr>
					 
				</table>
			</div>
		</form>
	</body>
</html>