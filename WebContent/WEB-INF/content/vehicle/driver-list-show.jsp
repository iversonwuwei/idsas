<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Driver" operate="Show" homePath="vehicle/driver-list" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Driver List" operate="Show" homePath="vehicle/driver-list" />
		</s:else>
		<div class="pop_main">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="text_bg" width="30%">Name:</td>
					<td width="70%">${driver.drivername}</td>
				</tr>
				<tr>
					<td class="text_bg">Department:</td>
					<td>${deptname}</td>
				</tr>
				<tr>
					<td class="text_bg">Card Number:</td>
					<td>${driver.drivernumber}</td>
				</tr>
				<tr>
					<td class="text_bg">Card type:</td>
					<td>
						<c:if test="${driver.type == null}">Normal</c:if>
						<c:if test="${driver.type == ''}">Normal</c:if>
						<c:if test="${driver.type == '0'}">Normal</c:if>
						<c:if test="${driver.type == '1'}">Temporary</c:if>
					</td>
				</tr>
				<tr>
					<td class="text_bg">Gender:</td>
					<td>
						<c:if test="${driver.gender == 'M'}">Male</c:if>
						<c:if test="${driver.gender == 'F'}">Female</c:if>
					</td>
				</tr>
				<tr>
					<td class="text_bg">Email:</td>
					<td>${driver.email}</td>
				</tr>
				<tr>
					<td class="text_bg">Phone:</td>
					<td>${driver.phone}</td>
				</tr>
				<tr>
					<td class="text_bg">Description:</td>
					<td height="60px;">
						<textarea style="width:100%; height: 100%;" disabled="disabled">${driver.description}</textarea>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>