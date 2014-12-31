<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/system/devicelist.js"></script>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/common/input-box.js"></script>
	<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_5.js" type="text/javascript"></script>
	</head>
	<body>
		<custom:navigation father="Organization Management" model="Device" operate="Show" homePath="system/device-list" />
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="30%">Device Name:</td>
					<td width="70%">${o_devicename}</td>
				</tr>
				<tr>
					<td class="text_bg">Department:</td>
					<td>${deptname}</td>
				</tr>
				<tr>
					<td class="text_bg">Device Type:</td>
					<td>
						<c:if test="${o_unittype == '1'}">Vehicle</c:if>
						<c:if test="${o_unittype == '2'}">Police</c:if>
					</td>
				</tr>
					<tr>
					<td class="text_bg">Vehicle/Police:</td>
					<td>${o_busname}</td>
				</tr>
				<tr>
					<td class="text_bg">Channels:</td>
					<td>${videoname}</td>
				</tr>
			</table>
		</div> 
	</body>
</html>