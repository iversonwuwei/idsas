<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<body>
		<custom:navigation father="System Settings" model="Vehicle Type" operate="Show" homePath="system/vehicle-type" />
		<div class="pop_main">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="30%" class="text_bg">Company:</td>
					<td width="70%">${vehtype.comname}</td>
				</tr>
				<tr>
					<td class="text_bg">Vehicle Type:</td>
					<td>${vehtype.type}</td>
				</tr>
				<tr>
					<td class="text_bg">Description:</td>
					<td height="60px;">
						<textarea style="width:100%; height: 100%" disabled="disabled">${vehtype.description}</textarea>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>