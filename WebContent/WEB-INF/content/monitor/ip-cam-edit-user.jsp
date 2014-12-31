<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/ip-cam.js"></script>
	</head>
	<body>
		<custom:navigation father="Organization Management" model="Camera" operate="User Edit" homePath="monitor/ip-cam" saveMethod="doSubUser();"/>
		<form name="queryResult" action="<%=basePath%>monitor/ip-cam!modifyUser" method="post">
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="40%">Administrator Name:</td>
						<td width="60%" style="text-align: left;">
							Admin
						</td>
					</tr>
					<tr>
						<td class="text_bg">Administrator Password:</td>
						<td style="text-align: left;">
							<input type="text" id="adminPass" name="camUserVO.adminPass" value="${camUserVO.adminPass}" maxlength="15" />
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="40%">Operator Name:</td>
						<td width="60%" style="text-align: left;">
							<input type="text" id="operatorName" name="camUserVO.operatorName" value="${camUserVO.operatorName}" maxlength="15" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Operator Password:</td>
						<td style="text-align: left;">
							<input type="text" id="operatorPass" name="camUserVO.operatorPass" value="${camUserVO.operatorPass}" maxlength="15" />
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="40%">Viewer Name:</td>
						<td width="60%" style="text-align: left;">
							<input type="text" id="viewerName" name="camUserVO.viewerName" value="${camUserVO.viewerName}" maxlength="15" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Viewer Password:</td>
						<td style="text-align: left;">
							<input type="text" id="viewerPass" name="camUserVO.viewerPass" value="${camUserVO.viewerPass}"  maxlength="15" />
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>