<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/vehicle/fleet-list.js"></script>
	</head>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Fleet" operate="Show" homePath="vehicle/fleet-list" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Fleet" operate="Show" homePath="vehicle/fleet-list" />
		</s:else>
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="30%">Fleet Name:</td>
					<td width="70%">${orgName}</td>
				</tr>
				<tr>
					<td class="text_bg">Department:</td>
					<td>${parentName}</td>
				</tr>
				<tr>
					<td class="text_bg">Description:</td>
					<td height="60px;">
						<textarea style="width:100%; height: 100%;" disabled="disabled">${description}</textarea>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>