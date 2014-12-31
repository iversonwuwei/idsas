<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Police" operate="Show" homePath="vehicle/police" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Police" operate="Show" homePath="vehicle/police" />
		</s:else>
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="33%">Police Name:</td>
					<td>${policeName}</td>
				</tr>
				<tr>
					<td class="text_bg">Department:</td>
					<td>${deptName}</td>
				</tr>
				<tr>
					<td class="text_bg">Fleet:</td>
					<td>${fleetName}</td>
				</tr>
				<tr>
					<td class="text_bg">Device:</td>
					<td>${deviceName}</td>
				</tr>
				<tr>
					<td class="text_bg">Gender:</td>
					<td>
						<s:if test="%{gender == 1}">male</s:if>
						<s:elseif test="%{gender == 2}">female</s:elseif>
						<s:else>hidden</s:else>
					</td>
				</tr>
				<tr>
					<td class="text_bg">Email:</td>
					<td>${email}</td>
				</tr>
				<tr>
					<td class="text_bg">Phone:</td>
					<td>${phone}</td>
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