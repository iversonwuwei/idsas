<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Company" operate="Show" homePath="vehicle/com" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Company" operate="Show" homePath="vehicle/com" />
		</s:else>
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="30%">Company Name:</td>
					<td width="70%">${orgName}</td>
				</tr>
				<tr>
					<td class="text_bg">Schedule Option:</td>
					<td>
						<c:if test="${lineno == '0'}">Non-RFID</c:if>
						<c:if test="${lineno == '1'}">RFID</c:if>
					</td>
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