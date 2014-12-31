<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Department" operate="Show" homePath="vehicle/depart-list" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Department" operate="Show" homePath="vehicle/depart-list" />
		</s:else>
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="30%">Department Name:</td>
					<td width="70%">${orgName}</td>
				</tr>
				<tr>
					<td class="text_bg">Company:</td>
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