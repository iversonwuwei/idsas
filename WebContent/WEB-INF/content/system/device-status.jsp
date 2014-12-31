<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
<head>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/system/devicestatus.js"></script>
	</head>
	<body>
		<custom:navigation father="System Settings" model="Device Status" addPath="system/device-status" addMethod="add" />
		<form name="queryResult" action="<%=basePath%>system/device-status!index" method="post">
			<custom:searchBody row="1">
				<custom:searchItem title="Name" side="first">
					<input type="text" name="deVo.name" id="deVo.name" value="${deVo.name}" maxlength="25" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="statusname" page="${page}" width="40%">Name</custom:sort>
					<custom:sort property="memo" page="${page}" width="40%">Memo</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="statusid" index="i" var="u">
					<custom:tableItem>${u.statusname}</custom:tableItem>
					<custom:tableItem>${u.memo}</custom:tableItem>
					<custom:tableItem>
						<custom:edit path="system/device-status" id="${u.statusid}"/>
						<custom:del path="system/device-status" id="${u.statusid}" jsmethod="del"/>
					</custom:tableItem>
				</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'"/>
				<s:param name="formName" value="'queryResult'"/>
				<s:param name="linkCount" value="5"/>
		    </s:include>
		</form>
	</body>
</html>