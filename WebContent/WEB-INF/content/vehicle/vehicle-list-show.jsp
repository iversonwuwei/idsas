<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Vehicle" operate="Show" homePath="vehicle/vehicle-list" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Vehicle List" operate="Show" homePath="vehicle/vehicle-list" />
		</s:else>
		<div class="pop_main">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="30%" class="text_bg">Plate Number:</td>
					<td width="70%">${veh.vehiclename}</td>
				</tr>
				<tr>
					<td class="text_bg">Department:</td>
					<td>${veh.deptname}</td>
				</tr>
				<tr>
					<td class="text_bg">Fleet:</td>
					<td>${vehv.fleetname}</td>
				</tr>
				<tr>
					<td class="text_bg">Device:</td>
					<td>${vehv.devicename}</td>
				</tr>
				<tr>
					<td class="text_bg">Vehicle Type:</td>
					<td>${vehv.typename}</td>
				</tr>
				<tr>
					<td class="text_bg">Vehicle Brand:</td>
					<td>${veh.brandname}</td>
				</tr>
				<tr>
					<td class="text_bg">Key Code:</td>
					<td>${veh.keycode}</td>
				</tr>
				<tr>
					<td class="text_bg">Description:</td>
					<td height="60px;">
						<textarea style="width:100%; height: 100%;" disabled="disabled">${veh.description}</textarea>
					</td>
				</tr>
				<tr>
					<td class="text_bg">IP:</td>
					<td>${veh.cctvip}</td>
				</tr>
			</table>
		</div>
	</body>
</html>