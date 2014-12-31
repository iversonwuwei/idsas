<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<link type="text/css" href="<%=basePath%>scripts/common/dtree/dtree.css" rel="stylesheet" />
		<script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/input-dtree.js"></script>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/authority/user.js"></script>
	</head>
	<body>
		<c:if test="${editble == 1}">
			<custom:navigation father="User Management" model="Account" operate="Show" homePath="authority/user" />
		</c:if>
		<c:if test="${editble == 0}">
			<custom:navigation father="User Query" model="Account" operate="Show" homePath="authority/user" />
		</c:if>
		<input type="hidden" name="user.userID" id="userID" value="${user.userID}" /> 
		<input type="hidden" name="user.password" id="password" value="${user.password}" /> 
		<input type="hidden" name="orgStr" id="orgstr" value="${orgStr}" />
		<input type="hidden" name="data_Str" id="datastr" value="${data_Str}" />
		<input type="hidden" name="editble" value="${editble}" />
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="30%">Account:</td>
					<td width="70%">${loginName}</td>
				</tr>
				<tr>
					<td class="text_bg">Role:</td>
					<td>${userRole.roleName}</td>
				</tr>
				<tr>
					<td class="text_bg">Company:</td>
					<td style="text-align: left;">${userOrg.orgName}</td>
				</tr>
				<tr>
					<td class="text_bg">Name:</td>
					<td>${userName}</td>
				</tr>
				<tr>
					<td class="text_bg">Email:</td>
					<td>${e_mail}</td>
				</tr>
				<tr>
					<td class="text_bg">Phone:</td>
					<td>${mobilephone}</td>
				</tr>
				<tr>
					<td class="text_bg">Description:</td>
					<td height="60px;">
						<textarea style="width:100%; height: 100%" disabled="disabled">${description}</textarea>
					</td>
				</tr>
				<tr>
					<td class="text_bg">Visible:</td>
					<td style="text-align: left;">
						<s:include value="/common/myTree.jsp">
							<s:param name="treeName" value="'departmentTree'" />
							<s:param name="treeType" value="'dtree'" />
							<s:param name="propertys" value="'orgID, orgName, parentID, n, n, url'" />
							<s:param name="dataList" value="%{departmentList}" />
							<s:param name="dataStr" value="%{dataStr}" />
						</s:include>
					</td>
				</tr>
				<%-- <tr>
					<td class="text_bg" >User Code:</td>
					<td>${userCode}</td>
				</tr>
				<tr>
					<td class="text_bg">Mail Password:</td>
					<td style="text-align: left;"  class="valid">${user.mailpassword}</td>
					<td class="text_bg">Education:</td>
					<td style="text-align: left;">${user.education}</td>
				</tr>
				<tr>
					<td class="text_bg">School:</td>
					<td style="text-align: left;"  class="valid">${user.school}</td>
					<td class="text_bg">Gender:</td>
					<td style="text-align: left;">
						<c:if test="${user.gender == 'M'}">Male</c:if>
						<c:if test="${user.gender == 'F'}">Female</c:if>
					</td>
				</tr> --%>
			</table>
		</div>
	</body>
</html>