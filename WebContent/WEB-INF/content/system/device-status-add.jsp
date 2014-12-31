<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/system/device.js"></script>
	</head>
	<body>
		<custom:navigation father="System Settings" model="Device Status" operate="Add" saveMethod="sub2();" homePath="system/device-status" />
		<form action="<%=basePath%>system/device-status!create" name="queryResult" method="post">
			<input type="hidden" name="isdelete" id="isdelete" value="F" /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="20%">Name:</td>
						<td width="80%" style="text-align: left;" class="valid"><input type="text" id="statusname" name="statusname" maxlength="20" value="${statusname}" class="5,1,10"></td>
					</tr>
					<tr>
						<td class="text_bg">Memo:</td>
						<td style="text-align: left;" class="valid"><input type="text" id="memo" name="memo" maxlength="20" value="${memo}" class="5,1,10"></td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>