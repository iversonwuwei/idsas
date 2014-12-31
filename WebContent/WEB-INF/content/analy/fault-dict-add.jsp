<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/analy/fault.js"></script>
	</head>
	<body>
		<custom:navigation father="Fault Analyzer" model="Fault Dictionary" operate="Add" saveMethod="sub1();" homePath="analy/fault-dict" />
		<form action="<%=basePath%>analy/fault-dict!create" name="queryResult" method="post">
			<input type="hidden" name="faultcodeid" id="faultcodeid" value="${counseling}" /> 
			<input type="hidden" name="isdelete" id="isdelete" value="F" /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">Code:</td>
						<td width="82%" style="text-align: left;" class="valid">
						<input type="text" name="code" id="code" value="${code}"  maxlength="10" class="5,1,10" /> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Scope:</td>
						<td style="text-align: left;" class="valid">
						<input type="text" name="engscope" id="engscope" value="${engscope}"  maxlength="30" class="5,1,30" /> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Definition:</td>
						<td style="text-align: left;">
						<textarea rows="2" cols="1" style="width: 75%" name="engdefinition" id="engdefinition" maxlength="100" >${engdefinition}</textarea>
						</td>
					</tr>

					<tr>
						<td class="text_bg">Context:</td>
						<td style="text-align: left;">
						<textarea rows="2" cols="1" style="width: 75%;height:50px " name="engcontext" id="engcontext" maxlength="500" >${engcontext}</textarea>
						</td>
					</tr>

				</table>
			</div>
		</form>
	</body>
</html>