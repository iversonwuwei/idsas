<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/system/cam-model.js"></script>
	</head>
	<body>
		<s:if test="%{modelID == null}">
			<custom:navigation father="System Settings" model="Camera Model" operate="Add" saveMethod="doSave();" homePath="system/cam-model" />
		</s:if>
		<s:else>
			<custom:navigation father="System Settings" model="Camera Model" operate="Edit" saveMethod="doSave();" homePath="system/cam-model" />
		</s:else>
		<form action="<%=basePath%>system/cam-model!create" name="queryResult" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<input type="hidden" name="modelID" id="modelID" value="${modelID}" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="40%">Model Name:</td>
						<td width="60%" style="text-align: left;" class="valid">
							<input type="text" id="modelName" name="modelName" maxlength="15" value="${modelName}" class="5,1,15">
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="40%">Model Type:</td>
						<td width="60%" style="text-align: left;" class="valid">
						<s:select list="#{'1':'ip camera','2':'video server' }" theme="simple" name='type'></s:select>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Memo:</td>
						<td style="text-align: left;">
							<textarea id="memo" name="memo" style="height: 100px;width: 360px;">${memo}</textarea>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>