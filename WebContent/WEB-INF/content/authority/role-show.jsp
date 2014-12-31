<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/common/dtree/dtree.css" />
		<script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/input-dtree.js"></script>
	</head>
	<body>
	    <s:if test="editble==1">
			<custom:navigation father="User Management" model="Role" operate="Show" homePath="authority/role" />
		</s:if>
		<s:else>
			<custom:navigation father="User Query" model="Role" operate="Show" homePath="authority/role" />
		</s:else>
		<div class="pop_main">
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
				<tr>
					<td class="text_bg" width="30%">Role Name:</td>
					<td width="70%">${roleName}</td>
				</tr>
				<tr>
					<td class="text_bg">Role type:</td>
					<td>
						<c:if test="${aflag == '0'}">Super Administrator</c:if>
						<c:if test="${aflag == '1'}">Administrator</c:if>
						<c:if test="${aflag == '2'}">Other</c:if>
					</td>
				</tr>
				<tr>
					<td class="text_bg">Company:</td>
					<td>${comname}</td>
				</tr>
				<%--
				
				<tr>
					<td class="text_bg">Camera Permissions:</td>
					<td>
						<s:if test="inSystem == 0">Administrator</s:if>
						<s:elseif test="inSystem == 1">Operator</s:elseif>
						<s:elseif test="inSystem == 2">Viewer</s:elseif>
					</td>
				</tr>
				 --%>
				<tr>
					<td class="text_bg">Description:</td>
					<td height="60px;">
						<textarea style="width:100%; height: 100%" disabled="disabled">${description}</textarea>
					</td>
				</tr>
				<tr>
					<td class="text_bg">Features:</td>
					<td>
						<s:include value="/common/myTree.jsp">
							<s:param name="treeName" value="'featTree'" />
							<s:param name="treeType" value="'dtree'" />
							<s:param name="propertys" value="'featID, featName, fatherID, n, n, url'" />
							<s:param name="dataList" value="%{featList}" />
							<s:param name="dataStr" value="%{feat_Add_Str}" />
						</s:include>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>