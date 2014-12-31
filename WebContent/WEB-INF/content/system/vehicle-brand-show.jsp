<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<body>
		<custom:navigation father="System Settings" model="Vehicle Brand" operate="Show" homePath="system/vehicle-brand" />
		<div class="pop_main">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="30%" class="text_bg" >Company:</td>
					<td width="70%">${vehbrand.comname}</td>
				</tr>
				<tr>
					<td class="text_bg">Vehicle Brand:</td>
					<td>${vehbrand.name}</td>
				</tr>
				<tr>
					<td class="text_bg">Description:</td>
					<td height="60px;">
						<textarea style="width:100%; height: 100%" disabled="disabled">${vehbrand.memo}</textarea>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>