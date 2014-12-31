<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="System Settings" model="Camera Model" />
		</s:if>
		<s:else>
			<custom:navigation father="System Settings" model="Camera Model" />
		</s:else>
		<form name="queryResult" action="<%=basePath%>system/cam-model!index" method="post">
			<s:hidden name="editble" value="%{editble}"/>
			<%-- <custom:searchBody>
				<custom:searchItem title="Camera Model"  side="first">
					<input type="text" name="modelNameStr" value="${modelNameStr}" />
				</custom:searchItem>
				<custom:searchItem side="last" width="60%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody> --%>
			<custom:table>
				<custom:tableHeader>
					<custom:sort property="modelName" page="${page}" width="30%">Camera Model</custom:sort>
					<custom:sort property="type" page="${page}" width="30%">Model Type</custom:sort>
					<custom:sort property="memo" page="${page}" width="30%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="modelID" index="i" var="v">
					<custom:tableItem>${v.modelName}</custom:tableItem>
					<custom:tableItem>
						<c:if test="${v.type==1}">ip camera</c:if>
						<c:if test="${v.type==2}">video server</c:if>
					</custom:tableItem>
					<custom:tableItem>${v.memo}</custom:tableItem>
						<custom:tableItem>
							<custom:show path="system/cam-model" id="${v.modelID}" />
						</custom:tableItem>
				</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'" />
				<s:param name="formName" value="'queryResult'" />
				<s:param name="linkCount" value="5" />
			</s:include>
		</form>
	</body>
</html>