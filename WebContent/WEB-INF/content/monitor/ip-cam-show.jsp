<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/ip-cam.js"></script>
	</head>
	<body>
		<custom:navigation father="Organization Management" model="Camera" operate="Show" homePath="monitor/ip-cam" />
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="30%">Camera Name:</td>
					<td width="70%">${cameraName}</td>
				</tr>
				<tr>
					<td class="text_bg">Department:</td>
					<td>${deptname}</td>
				</tr>
				<tr>
					<td class="text_bg">Camera Model:</td>
					<td>${modelID.modelName}</td>
				</tr>
				<tr>
				<s:if test="authLevel == 0">
					<tr>
						<td class="text_bg">Camera Login Username:</td>
						<td>${adminName}</td>
					</tr>
					<tr>
						<td class="text_bg">Camera Login Password:</td>
						<td>${adminPass}</td>
					</tr>
				</s:if>
				<s:if test="%{cameraID != null}">
					<tr>
						<td class="text_bg">IP:</td>
						<td>${ipAddress}</td>
					</tr>
				</s:if>
			</table>
		</div>
	</body>
</html>