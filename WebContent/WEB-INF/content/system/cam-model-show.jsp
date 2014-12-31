<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/system/cam-model.js"></script>
	</head>
	<body>
<custom:navigation father="System Settings" model="Camera Model" operate="Show" homePath="system/cam-model" />	
		<form action="<%=basePath%>system/cam-model!create" name="queryResult" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<input type="hidden" name="modelID" id="modelID" value="${modelID}" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" style="width: 200px">Camera Model:</td>
						<td width="80%" style="text-align: left;" class="valid">
						${modelName}
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="40%">Model Type:</td>
						<td width="60%" style="text-align: left;" class="valid">
						<c:if test="${type=='1' }">ip camera</c:if>
							<c:if test="${type=='2' }">video server</c:if>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:</td>
						<td style="text-align: left;">
							<textarea id="memo" name="memo" readonly="readonly" style="height: 100px;width: 360px;">${memo}</textarea>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>